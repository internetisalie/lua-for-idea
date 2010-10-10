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
package com.sylvanaar.idea.Lua.lang.smartEnter.processors;

import com.intellij.codeInsight.editorActions.smartEnter.EnterProcessor;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.psi.PsiElement;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaBlock;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaStatementElement;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaWhileStatement;


/**
 * User: Dmitry.Krasilschikov
 * Date: 05.08.2008
 */
public class LuaPlainEnterProcessor implements EnterProcessor {
  public boolean doEnter(Editor editor, PsiElement psiElement, boolean isModified) {
    LuaBlock block = getControlStatementBlock(editor.getCaretModel().getOffset(), psiElement);

    if (block != null) {
      PsiElement firstElement = block.getFirstChild().getNextSibling();

      editor.getCaretModel().moveToOffset(firstElement != null ?
              firstElement.getTextRange().getStartOffset() - 1 :
              block.getTextRange().getEndOffset());
    }

    getEnterHandler().execute(editor, ((EditorEx) editor).getDataContext());
    return true;
  }

  private EditorActionHandler getEnterHandler() {
    EditorActionHandler enterHandler = EditorActionManager.getInstance().getActionHandler(
            IdeActions.ACTION_EDITOR_START_NEW_LINE
    );
    return enterHandler;
  }

  private LuaBlock getControlStatementBlock(int caret, PsiElement element) {
    LuaStatementElement body = null;
//    if (element instanceof GrIfStatement) {
//      body = ((GrIfStatement) element).getThenBranch();
//      if (caret > body.getTextRange().getEndOffset()) {
//        body = ((GrIfStatement) element).getElseBranch();
//      }
//    } else

    if (element instanceof LuaWhileStatement) {
      body = ((LuaWhileStatement) element).getBody();
    }

//    else if (element instanceof LuaNumericForStatement) {
//      body = ((LuaNumericForStatement) element).getBody();
//    }

    return body instanceof LuaBlock ? (LuaBlock) body :null;
  }
}
