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
package com.sylvanaar.idea.Lua.lang.surroundWith.surrounders.surroundersImpl.blocks.open;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.sylvanaar.idea.Lua.lang.psi.LuaPsiElement;
import com.sylvanaar.idea.Lua.lang.psi.LuaPsiElementFactory;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaConditionalExpression;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaWhileStatement;
import com.sylvanaar.idea.Lua.lang.surroundWith.surrounders.LuaManyStatementsSurrounder;
import org.jetbrains.annotations.NotNull;



public class LuaWithWhileSurrounder extends LuaManyStatementsSurrounder {
  protected LuaPsiElement doSurroundElements(PsiElement[] elements) throws IncorrectOperationException {
    LuaPsiElementFactory factory = LuaPsiElementFactory.getInstance(elements[0].getProject());
    LuaWhileStatement whileStatement = (LuaWhileStatement) factory.createStatementFromText("while a do end");
    addStatements(whileStatement.getBlock(), elements);
    return whileStatement;
  }

  protected TextRange getSurroundSelectionRange(LuaPsiElement element) {
    assert element instanceof LuaWhileStatement;
    LuaConditionalExpression condition = ((LuaWhileStatement) element).getCondition();

    int endOffset = element.getTextRange().getEndOffset();
    if (condition != null) {
      endOffset = condition.getTextRange().getStartOffset();
      condition.getParent().getNode().removeChild(condition.getNode());
    }
    return new TextRange(endOffset, endOffset);
  }

  public boolean isApplicable(@NotNull PsiElement[] elements) {

    return true;
  }

  public String getTemplateDescription() {
    return "while <cond> do <block> end";
  }
}
