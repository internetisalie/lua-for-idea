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

package com.sylvanaar.idea.Lua.lang.psi.visitor;

import com.intellij.openapi.progress.ProgressManager;
import com.intellij.psi.PsiElementVisitor;
import com.sylvanaar.idea.Lua.lang.psi.LuaPsiElement;
import com.sylvanaar.idea.Lua.lang.psi.LuaPsiFile;
import com.sylvanaar.idea.Lua.lang.psi.expressions.*;
import com.sylvanaar.idea.Lua.lang.psi.impl.LuaPsiKeywordImpl;
import com.sylvanaar.idea.Lua.lang.psi.impl.LuaPsiTokenImpl;
import com.sylvanaar.idea.Lua.lang.psi.statements.*;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Jon S Akhtar
 * Date: Jun 12, 2010
 * Time: 7:39:03 AM
 */

public class LuaElementVisitor extends PsiElementVisitor {
    public LuaElementVisitor(LuaElementVisitor e) {}
    public LuaElementVisitor() {}

    public void visitElement(LuaPsiElement element)
    {
        ProgressManager.checkCanceled();
    }

    public void visitFile(LuaPsiFile e) {
        visitElement(e);
    }

    public void visitClosure(LuaFunctionDefinitionStatement closure) {
        visitStatement(closure);
    }
    public void visitBlock(LuaBlock e) {
        visitBlock(e);
    }
    public void visitExpression(LuaExpression e) {
        visitElement(e);
    }
    public void visitBreakStatement(LuaBreakStatement e) {
        visitStatement(e);
    }
    public void visitVariable(LuaVariable e) {
        visitExpression(e);
    }

  public void visitParenthesizedExpression(LuaParenthesizedExpression expression) {
      visitExpression(expression);
  }

  public void visitUnaryExpression(LuaUnaryExpression expression) {
      visitExpression(expression);
  }

    public void visitFunctionDef(LuaFunctionDefinitionStatement e) {
        visitElement(e);
    }

    public void visitAssignment(LuaAssignmentStatement e) {
        visitElement(e);
    }

    public void visitIdentifier(LuaIdentifier e) {
        visitElement(e);
    }

    public void visitStatement(LuaStatementElement e) {
        visitElement(e);
    }

    public void visitNumericForStatement(LuaNumericForStatement e) {
        visitStatement(e);
    }


       
    public void visitGenericForStatement(LuaGenericForStatement e) {
        visitStatement(e);
    }

     public void visitIfThenStatement(@NotNull LuaIfThenStatement e) {
        visitStatement(e);
    }

    public void visitWhileStatement(LuaWhileStatement e) {
        visitStatement(e);
    }
    //
//    public void visitReferenceExpression(LuaReferenceExpressionImpl e) {
//       visitElement(e);
//    }

    public void visitReferenceExpression(LuaReferenceExpression e) {
        visitElement(e);        
    }

    public void visitKeyword(LuaPsiKeywordImpl e) {
        visitElement(e);
    }

    public void visitLuaToken(LuaPsiTokenImpl e) {
        visitElement(e);
    }
}




