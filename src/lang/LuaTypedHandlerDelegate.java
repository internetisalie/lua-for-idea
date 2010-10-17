/*
 * Copyright 2000-2009 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sylvanaar.idea.Lua.lang;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;

public class LuaTypedHandlerDelegate extends TypedHandlerDelegate {


  /**
   * Called after the specified character typed by the user has been inserted in the editor.
   *
   * @param c
   * @param project
   * @param editor
   * @param file
   */
  public Result charTyped(char c, final Project project, final Editor editor, final PsiFile file) {
    if (c == '}')
     CodeStyleManager.getInstance(file.getProject()).reformatRange(file, editor.getCaretModel().getVisualLineStart(), editor.getCaretModel().getVisualLineEnd());

    return Result.CONTINUE;
  }

    
}


