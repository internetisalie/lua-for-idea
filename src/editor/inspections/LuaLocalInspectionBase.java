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

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import com.sylvanaar.idea.Lua.lang.psi.LuaControlFlowOwner;
import com.sylvanaar.idea.Lua.lang.psi.LuaPsiFileBase;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaBlock;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaFunctionDefinitionStatement;
import com.sylvanaar.idea.Lua.lang.psi.visitor.LuaElementVisitor;
import com.sylvanaar.idea.Lua.lang.psi.visitor.LuaPsiElementVisitor;
import org.jetbrains.annotations.NotNull;


/**
 * @author ven
 */
public abstract class LuaLocalInspectionBase extends LuaSuppressableInspectionTool {
  @NotNull
   @Override
   public String[] getGroupPath() {
     return new String[]{"Lua", getGroupDisplayName()};
   }


  @NotNull
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder problemsHolder, boolean isOnTheFly) {
    return new LuaPsiElementVisitor(new LuaElementVisitor() {

      public void visitFunctionDef(LuaFunctionDefinitionStatement method) {
        final LuaBlock block = method.getBlock();
        if (block != null) {
          check(block, problemsHolder);
        }
      }

      public void visitFile(LuaPsiFileBase file) {
        check(file, problemsHolder);
      }
    });
  }

  protected abstract void check(LuaControlFlowOwner owner, ProblemsHolder problemsHolder);
}
