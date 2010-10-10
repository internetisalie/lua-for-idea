/*
 * Copyright 2010 Jon S Akhtar (Sylvanaar)
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *  
 *   http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.sylvanaar.idea.Lua.editor.inspections;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.PsiWhiteSpace;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaVariable;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaStatementElement;
import com.sylvanaar.idea.Lua.lang.psi.visitor.LuaRecursiveElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BaseInspectionVisitor extends LuaRecursiveElementVisitor {
  private BaseInspection inspection = null;
  private ProblemsHolder problemsHolder = null;
  private boolean onTheFly = false;
  private final List<ProblemDescriptor> errors = null;

    public BaseInspectionVisitor(ProblemsHolder holder, boolean onTheFly) {
        this.problemsHolder = holder;
        this.onTheFly = onTheFly;
    }

    public void setInspection(BaseInspection inspection) {
    this.inspection = inspection;
  }

  public void setProblemsHolder(ProblemsHolder problemsHolder) {
    this.problemsHolder = problemsHolder;
  }

  public void setOnTheFly(boolean onTheFly) {
    this.onTheFly = onTheFly;
  }

  protected void registerStatementError(LuaStatementElement statement, Object... args) {
    final PsiElement statementToken = statement.getFirstChild();
    registerError(statementToken, args);
  }

//  protected void registerClassError(LuaTypeDefinition aClass, Object... args) {
//    final PsiElement statementToken = aClass.getNameIdentifierLua();
//    registerError(statementToken, args);
//  }


  protected void registerVariableError(LuaVariable variable) {
    final PsiElement nameIdentifier = variable.getNameIdentifierLua();
    registerError(nameIdentifier);
  }

  protected void registerModifierError(String modifier,
                                       PsiModifierListOwner parameter) {
    final PsiModifierList modifiers = parameter.getModifierList();
    if (modifiers == null) {
      return;
    }
    final PsiElement[] children = modifiers.getChildren();
    for (final PsiElement child : children) {
      final String text = child.getText();
      if (modifier.equals(text)) {
        registerError(child);
      }
    }
  }

  protected void registerError(PsiElement location) {
    if (location == null) {
      return;
    }
    final LocalQuickFix[] fix = createFixes(location);
    final String description = inspection.buildErrorString(location);
    registerError(location, description, fix);
  }

//  protected void registerMethodError(LuaMethod method, Object... args) {
//    if (method == null) {
//      return;
//    }
//    final LocalQuickFix[] fix = createFixes(method);
//    final String description = inspection.buildErrorString(args);
//    registerError(method.getNameIdentifierLua(), description, fix);
//  }
//
//  protected void registerVariableError(LuaVariable variable, Object... args) {
//    if (variable == null) {
//      return;
//    }
//    final LocalQuickFix[] fix = createFixes(variable);
//    final String description = inspection.buildErrorString(args);
//    registerError(variable.getNode().getText(), description, fix);
//  }
//
//  protected void registerMethodCallError(LuaMethodCallExpression method, Object... args) {
//    if (method == null) {
//      return;
//    }
//    final LocalQuickFix[] fix = createFixes(method);
//    final String description = inspection.buildErrorString(args);
//    registerError(((LuaReferenceExpression) method.getInvokedExpression()).getReferenceNameElement(), description, fix);
//  }

  private void registerError(@NotNull PsiElement location, String description,
                             LocalQuickFix[] fixes) {
    problemsHolder.registerProblem(location,
        description,
        ProblemHighlightType.GENERIC_ERROR_OR_WARNING, fixes);
  }

  protected void registerError(@NotNull PsiElement location, Object... args) {
    final LocalQuickFix[] fix = createFixes(location);
    final String description = inspection.buildErrorString(args);
    registerError(location, description, fix);
  }

  @Nullable
  private LocalQuickFix[] createFixes(PsiElement location) {
    if (!onTheFly &&
        inspection.buildQuickFixesOnlyForOnTheFlyErrors()) {
      return null;
    }
    final LuaFix[] fixes = inspection.buildFixes(location);
    if (fixes != null) {
      return fixes;
    }
    final LuaFix fix = inspection.buildFix(location);
    if (fix == null) {
      return null;
    }
    return new LuaFix[]{fix};
  }

  @Nullable
  public ProblemDescriptor[] getErrors() {
    if (errors == null) {
      return null;
    } else {
      final int numErrors = errors.size();
      return errors.toArray(new ProblemDescriptor[numErrors]);
    }
  }

  public void visitWhiteSpace(PsiWhiteSpace space) {
    // none of our inspections need to do anything with white space,
    // so this is a performance optimization
  }
}
