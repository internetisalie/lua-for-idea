/*
 * Copyright 2000-2009 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sylvanaar.idea.Lua.editor.inspections.inspections.unusedDef;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.SuppressIntentionAction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Processor;
import com.sylvanaar.idea.Lua.editor.inspections.LuaInspectionBundle;
import com.sylvanaar.idea.Lua.editor.inspections.LuaLocalInspectionBase;
import com.sylvanaar.idea.Lua.lang.psi.LuaControlFlowOwner;
import com.sylvanaar.idea.Lua.lang.psi.LuaPsiElement;
import com.sylvanaar.idea.Lua.lang.psi.LuaPsiFileBase;
import com.sylvanaar.idea.Lua.lang.psi.controlFlow.Instruction;
import com.sylvanaar.idea.Lua.lang.psi.controlFlow.ReadWriteVariableInstruction;
import com.sylvanaar.idea.Lua.lang.psi.dataFlow.DFAEngine;
import com.sylvanaar.idea.Lua.lang.psi.dataFlow.reachingDefs.ReachingDefinitionsDfaInstance;
import com.sylvanaar.idea.Lua.lang.psi.dataFlow.reachingDefs.ReachingDefinitionsSemilattice;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaReferenceExpression;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaTableConstructor;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaVariable;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaAssignmentStatement;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaFunctionDefinitionStatement;
import gnu.trove.TIntHashSet;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntProcedure;
import gnu.trove.TObjectProcedure;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 & @author ven
 */
public class UnusedDefInspection extends LuaLocalInspectionBase {
  @Nls
  @NotNull
  public String getGroupDisplayName() {
    return LuaInspectionBundle.message("lua.dfa.issues");
  }

  @Nls
  @NotNull
  public String getDisplayName() {
    return LuaInspectionBundle.message("unused.assignment");
  }

  @NonNls
  @NotNull
  public String getShortName() {
    return "LuaUnusedAssignment";
  }


  protected void check(final LuaControlFlowOwner owner, final ProblemsHolder problemsHolder) {
    final Instruction[] flow = owner.getControlFlow();
    final ReachingDefinitionsDfaInstance dfaInstance = new ReachingDefinitionsDfaInstance(flow);
    final ReachingDefinitionsSemilattice lattice = new ReachingDefinitionsSemilattice();
    final DFAEngine<TIntObjectHashMap<TIntHashSet>> engine = new DFAEngine<TIntObjectHashMap<TIntHashSet>>(flow, dfaInstance, lattice);
    final ArrayList<TIntObjectHashMap<TIntHashSet>> dfaResult = engine.performDFA();
    final TIntHashSet unusedDefs = new TIntHashSet();
    for (Instruction instruction : flow) {
      if (instruction instanceof ReadWriteVariableInstruction && ((ReadWriteVariableInstruction) instruction).isWrite()) {
        unusedDefs.add(instruction.num());
      }
    }

    for (int i = 0; i < dfaResult.size(); i++) {
      final Instruction instruction = flow[i];
      if (instruction instanceof ReadWriteVariableInstruction) {
        final ReadWriteVariableInstruction varInsn = (ReadWriteVariableInstruction) instruction;
        if (!varInsn.isWrite()) {
          final String varName = varInsn.getVariableName();
          TIntObjectHashMap<TIntHashSet> e = dfaResult.get(i);
          e.forEachValue(new TObjectProcedure<TIntHashSet>() {
            public boolean execute(TIntHashSet reaching) {
              reaching.forEach(new TIntProcedure() {
                public boolean execute(int defNum) {
                  final String defName = ((ReadWriteVariableInstruction) flow[defNum]).getVariableName();
                  if (varName.equals(defName)) {
                    unusedDefs.remove(defNum);
                  }
                  return true;
                }
              });
              return true;
            }
          });
        }
      }
    }

    unusedDefs.forEach(new TIntProcedure() {
      public boolean execute(int num) {
        final ReadWriteVariableInstruction instruction = (ReadWriteVariableInstruction) flow[num];
        final PsiElement element = instruction.getElement();
        if (isLocalAssignment(element) && isUsedInToplevelFlowOnly(element)) {
          if (element instanceof LuaReferenceExpression) {
            PsiElement parent = element.getParent();
            PsiElement toHighlight = null;
            if (parent instanceof LuaAssignmentStatement) {
              toHighlight = ((LuaAssignmentStatement) parent).getLeftExprs();
            }
            if (toHighlight == null) toHighlight = element;
            problemsHolder.registerProblem(toHighlight, LuaInspectionBundle.message("unused.assignment.tooltip"), ProblemHighlightType.LIKE_UNUSED_SYMBOL);
          } else if (element instanceof LuaVariable) {
            problemsHolder.registerProblem(((LuaVariable) element).getNameIdentifierLua(), LuaInspectionBundle.message("unused.assignment.tooltip"), ProblemHighlightType.LIKE_UNUSED_SYMBOL);
          }
        }
        return true;
      }
    });
  }

  private boolean isUsedInToplevelFlowOnly(PsiElement element) {
    LuaVariable var = null;
    if (element instanceof LuaVariable) {
      var = (LuaVariable) element;
    } else if (element instanceof LuaReferenceExpression) {
      final PsiElement resolved = ((LuaReferenceExpression) element).resolve();
      if (resolved instanceof LuaVariable) var = (LuaVariable) resolved;
    }

    if (var != null) {
      final LuaPsiElement scope = getScope(var);
      assert scope != null;

      return ReferencesSearch.search(var, new LocalSearchScope(scope)).forEach(new Processor<PsiReference>() {
        public boolean process(PsiReference ref) {
          return getScope(ref.getElement()) == scope;
        }
      });
    }

    return true;
  }

  private LuaPsiElement getScope(PsiElement var) {
    return PsiTreeUtil.getParentOfType(var, LuaFunctionDefinitionStatement.class, LuaFunctionDefinitionStatement.class, LuaTableConstructor.class, LuaPsiFileBase.class);
  }

  private boolean isLocalAssignment(PsiElement element) {
    if (element instanceof LuaVariable) {
      return isLocalVariable((LuaVariable) element, false);
    } else if (element instanceof LuaReferenceExpression) {
      final PsiElement resolved = ((LuaReferenceExpression) element).resolve();
      return resolved instanceof LuaVariable && isLocalVariable((LuaVariable) resolved, true);
    }

    return false;
  }

  private boolean isLocalVariable(LuaVariable var, boolean parametersAllowed) {
//    if (var instanceof GrField) return false;
//    else if (var instanceof GrParameter && !parametersAllowed) return false;

    return var.isLocal();
  }

  public boolean isEnabledByDefault() {
    return true;
  }

    @Override
    public SuppressIntentionAction[] getSuppressActions(@Nullable PsiElement element) {
        return new SuppressIntentionAction[0];  //To change body of implemented methods use File | Settings | File Templates.
    }
}
