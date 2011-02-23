/*
 * Copyright 2011 Jon S Akhtar (Sylvanaar)
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
package com.sylvanaar.idea.Lua.editor.smartEnter.fixers;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.sylvanaar.idea.Lua.editor.smartEnter.*;
import com.sylvanaar.idea.Lua.lang.lexer.LuaTokenTypes;

/**
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Sep 5, 2003
 * Time: 6:32:22 PM
 * To change this template use Options | File Templates.
 */
public class StringLiteralFixer implements LuaFixer {
  public void apply(Editor editor, LuaSmartEnterProcessor processor, PsiElement psiElement)
      throws IncorrectOperationException {
    if (psiElement instanceof ASTWrapperPsiElement) {
        char firstChar = psiElement.getText().charAt(0);
      if (psiElement.getNode().getElementType() == LuaTokenTypes.STRING &&
          !StringUtil.endsWithChar(psiElement.getText(), firstChar)) {
        editor.getDocument().insertString(psiElement.getTextRange().getEndOffset(), ""+firstChar);
      }
    }
  }
}
