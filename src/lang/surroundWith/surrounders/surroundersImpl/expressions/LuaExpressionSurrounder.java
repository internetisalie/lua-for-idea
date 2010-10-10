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
package com.sylvanaar.idea.Lua.lang.surroundWith.surrounders.surroundersImpl.expressions;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaExpression;
import com.sylvanaar.idea.Lua.lang.surroundWith.surrounders.LuaSingleElementSurrounder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: Dmitry.Krasilschikov
 * Date: 22.05.2007
 */
public abstract class LuaExpressionSurrounder extends LuaSingleElementSurrounder {
  protected boolean isApplicable(PsiElement element) {
    return element instanceof LuaExpression;
  }

  @Nullable
  public TextRange surroundElements(@NotNull Project project, @NotNull Editor editor, @NotNull PsiElement[] elements) throws IncorrectOperationException {
    if (elements.length != 1) return null;

    PsiElement element = elements[0];

    return surroundExpression((LuaExpression) element);
  }

  protected abstract TextRange surroundExpression(LuaExpression expression);

  protected void replaceToOldExpression(LuaExpression oldExpr, LuaExpression replacement) {
    oldExpr.replaceWithExpression(replacement, false);
  }
}
