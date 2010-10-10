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
package com.sylvanaar.idea.Lua.editor.inspections.validity;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.SuppressIntentionAction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.sylvanaar.idea.Lua.editor.inspections.BaseInspectionVisitor;
import com.sylvanaar.idea.Lua.editor.inspections.inspections.AbstractInspection;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaBlock;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaLastStatementElement;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaStatementElement;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class LuaUnreachableStatementInspection extends AbstractInspection {

    @Nls
    @NotNull
    public String getGroupDisplayName() {
        return VALIDITY_ISSUES;
    }

    @Nls
    @NotNull
    public String getDisplayName() {
        return "Unreachable_Statement";
    }

    @NotNull
    @Override
    public String getShortName() {
        return "Unreachable Statement";
    }

    @Nullable
    protected String buildErrorString(Object... args) {
        return "Unreachable statement #loc";

    }

    public boolean isEnabledByDefault() {
        return true;
    }



    @Override
    public SuppressIntentionAction[] getSuppressActions(@Nullable PsiElement element) {
        return new SuppressIntentionAction[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

//    @Override
//    protected void check(LuaControlFlowOwner owner, ProblemsHolder problemsHolder) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }

      @NotNull
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {
     //   return new MyVisitor(holder,isOnTheFly);
    //}
    return new BaseInspectionVisitor(holder,isOnTheFly) {
//        MyVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {
//            //To change body of created methods use File | Settings | File Templates.
//        }


//        public void visitClosure(LuaFunctionDefinitionStatement closure) {
//            //     super.visitClosure(closure);
//            LuaStatementElement[] statements = closure.getBlock().getStatements();
//            for (int i = 0; i < statements.length - 1; i++) {
//                checkPair(statements[i], statements[i + 1]);
//            }
//        }

        public void visitBlock(LuaBlock block) {

            LuaStatementElement[] statements = block.getStatements();
            for (int i = 0; i < statements.length - 1; i++) {
                checkPair(statements[i], statements[i + 1]);
            }
        };

        private void checkPair(LuaStatementElement prev, LuaStatementElement statement) {
            if (prev instanceof LuaLastStatementElement) {
                registerError(statement);
            }
        };
    };
      }}
