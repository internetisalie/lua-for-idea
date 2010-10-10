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
package com.sylvanaar.idea.Lua.lang.surroundWith.surrounders;

import com.intellij.lang.ASTNode;
import com.intellij.lang.surroundWith.Surrounder;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.sylvanaar.idea.Lua.lang.parser.LuaElementTypes;
import com.sylvanaar.idea.Lua.lang.psi.LuaPsiElement;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaBlock;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaStatementElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * User: Dmitry.Krasilschikov
 * Date: 22.05.2007
 */
public abstract class LuaManyStatementsSurrounder implements Surrounder {
  public boolean isStatements(@NotNull PsiElement[] elements) {
    for (PsiElement element : elements) {
      if (!(element instanceof LuaStatementElement)) {
        return false;
      }
    }
    return true;
  }

  public boolean isApplicable(@NotNull PsiElement[] elements) {
    return true;
  }

  protected String getListElementsTemplateAsString(PsiElement... elements) {
    StringBuffer result = new StringBuffer();
    for (PsiElement element : elements) {
      result.append(element.getText());
      result.append("\n");
    }
    return result.toString();
  }

  @Nullable
  public TextRange surroundElements(@NotNull Project project, @NotNull Editor editor, @NotNull PsiElement[] elements) throws IncorrectOperationException {
    if (elements.length == 0) return null;

    PsiElement element1 = elements[0];
    final LuaPsiElement newStmt = doSurroundElements(elements);
    assert newStmt != null;

    ASTNode parentNode = element1.getParent().getNode();

    for (int i = 0; i < elements.length; i++) {
      PsiElement element = elements[i];

      if (i == 0) {
        parentNode.replaceChild(element1.getNode(), newStmt.getNode());
      } else {
        if (parentNode != element.getParent().getNode()) return null;

        final int endOffset = element.getTextRange().getEndOffset();
        final PsiElement semicolon = PsiTreeUtil.findElementOfClassAtRange(element.getContainingFile(), endOffset, endOffset + 1, PsiElement.class);
        if (semicolon != null && ";".equals(semicolon.getText())) {
          assert parentNode == semicolon.getParent().getNode();
          parentNode.removeChild(semicolon.getNode());
        }

        if (i < elements.length - 1) {
          final PsiElement newLine = PsiTreeUtil.findElementOfClassAtRange(element.getContainingFile(), endOffset, elements[i + 1].getTextRange().getStartOffset(), PsiElement.class);
          if (newLine != null && LuaElementTypes.NEWLINE.equals(newLine.getNode().getElementType())) {
            assert parentNode == newLine.getParent().getNode();
            parentNode.removeChild(newLine.getNode());
          }
        }

        parentNode.removeChild(element.getNode());
      }
    }

    return getSurroundSelectionRange(newStmt);
  }

  protected void addStatements(LuaBlock block, PsiElement[] elements) throws IncorrectOperationException {
    for (int i = 0; i < elements.length; i++) {
      PsiElement element = elements[i];
      final LuaStatementElement statement = (LuaStatementElement) element;
      block.addStatementBefore(statement, null);
    }
  }

  protected abstract LuaPsiElement doSurroundElements(PsiElement[] elements) throws IncorrectOperationException;

  protected abstract TextRange getSurroundSelectionRange(LuaPsiElement element);
}