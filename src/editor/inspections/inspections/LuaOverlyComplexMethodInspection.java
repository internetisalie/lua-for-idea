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
package com.sylvanaar.idea.Lua.editor.inspections.inspections;


import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.sylvanaar.idea.Lua.lang.psi.visitor.LuaElementVisitor;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaBlockStatement;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaFunctionDefinitionStatement;
import org.jetbrains.annotations.NotNull;

public class LuaOverlyComplexMethodInspection extends LuaMethodMetricInspection {

  @NotNull
  public String getDisplayName() {
    return "Overly complex method";
  }


  protected int getDefaultLimit() {
    return 10;
  }

  protected String getConfigurationLabel() {
    return "Method complexity limit:";
  }

  public String buildErrorString(Object... args) {
    return "Method '#ref' is overly complex ( cyclomatic complexity =" + args[0] + '>' + args[1] + ')';
  }

  public LuaElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    return new LuaElementVisitor() {
         public void visitFunctionDef(LuaFunctionDefinitionStatement func) {
              super.visitFunctionDef(func);
              final int limit = getLimit();
              final CyclomaticComplexityVisitor visitor = new CyclomaticComplexityVisitor();
              final LuaBlockStatement body = func.getBlock();
              if (body == null) {
                return;
              }
              body.accept(visitor);
              final int complexity = visitor.getComplexity();
              if (complexity <= limit) {
                return;
              }
              holder.registerProblem(func, buildErrorString(complexity, limit), LocalQuickFix.EMPTY_ARRAY);
         }
    };
  }
//
//  private class Visitor extends BaseInspectionVisitor {
//    public void visitMethod(LuaFunctionDefinitionStatement func) {
//      super.visitFunctionDef(func);
//      final int limit = getLimit();
//      final CyclomaticComplexityVisitor visitor = new CyclomaticComplexityVisitor();
//      final LuaBlock body = func.getBlock();
//      if (body == null) {
//        return;
//      }
//      body.accept(visitor);
//      final int complexity = visitor.getComplexity();
//      if (complexity <= limit) {
//        return;
//      }
//      registerMethodError(grMethod, complexity, limit);
//    }
//  }
}