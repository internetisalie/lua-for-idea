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


/**
 * @author ilyas
 */
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
//
//    public Result preprocessEnter(PsiFile file,
//                                  Editor editor,
//                                  Ref<Integer> caretOffset,
//                                  Ref<Integer> caretAdvance,
//                                  DataContext dataContext,
//                                  EditorActionHandler originalHandler) {
//        String text = editor.getDocument().getText();
//        if (StringUtil.isEmpty(text)) {
//            return Result.Continue;
//        }
//
//        final int caret = editor.getCaretModel().getOffset();
//        final EditorHighlighter highlighter = ((EditorEx) editor).getHighlighter();
//        if (caret >= 1 && caret < text.length() && CodeInsightSettings.getInstance().SMART_INDENT_ON_ENTER) {
//            HighlighterIterator iterator = highlighter.createIterator(caret);
//            iterator.retreat();
//            while (!iterator.atEnd() && WS == iterator.getTokenType()) {
//                iterator.retreat();
//            }
////      boolean afterArrow = !iterator.atEnd() && iterator.getTokenType() == mCLOSABLE_BLOCK_OP;
////      if (afterArrow) {
////        originalHandler.execute(editor, dataContext);
////        PsiDocumentManager.getInstance(file.getProject()).commitDocument(editor.getDocument());
////        CodeStyleManager.getInstance(file.getProject()).adjustLineIndent(file, editor.getCaretModel().getOffset());
////      }
//
//            iterator = highlighter.createIterator(editor.getCaretModel().getOffset());
//            while (!iterator.atEnd() && WS == iterator.getTokenType()) {
//                iterator.advance();
//            }
//            if (!iterator.atEnd() && RCURLY == iterator.getTokenType()) {
//                PsiDocumentManager.getInstance(file.getProject()).commitDocument(editor.getDocument());
//                final PsiElement element = file.findElementAt(iterator.getStart());
//                if (element != null &&
//                        element.getNode().getElementType() == RCURLY &&
//                        element.getParent() instanceof LuaBlock &&
//                        text.length() > caret) {
//                    return Result.DefaultForceIndent;
//                }
//            }
////      if (afterArrow) {
////        return Result.Stop;
////      }
//        }
//
//        if (handleEnter(editor, dataContext, file.getProject(), originalHandler, file)) {
//            return Result.Stop;
//        }
//        return Result.Continue;
//    }
//
//    protected static boolean handleEnter(Editor editor,
//                                         DataContext dataContext,
//                                         @NotNull Project project,
//                                         EditorActionHandler originalHandler, PsiFile file) {
//        if (isReadOnly(editor)) {
//            return false;
//        }
//        int caretOffset = editor.getCaretModel().getOffset();
//        if (caretOffset < 1) return false;
//
//        if (handleBetweenSquareBraces(editor, caretOffset, dataContext, project, originalHandler)) {
//            return true;
//        }
//
//        return false;
//    }
//
//    private static boolean handleBetweenSquareBraces(Editor editor,
//                                                     int caret,
//                                                     DataContext context,
//                                                     Project project,
//                                                     EditorActionHandler originalHandler) {
//        String text = editor.getDocument().getText();
//        if (text == null || text.length() == 0) return false;
//        final EditorHighlighter highlighter = ((EditorEx) editor).getHighlighter();
//        if (caret < 1 || caret > text.length() - 1) {
//            return false;
//        }
//        HighlighterIterator iterator = highlighter.createIterator(caret - 1);
//        if (LBRACK == iterator.getTokenType()) {
//            if (text.length() > caret) {
//                iterator = highlighter.createIterator(caret);
//                if (RBRACK == iterator.getTokenType()) {
//                    originalHandler.execute(editor, context);
//                    originalHandler.execute(editor, context);
//                    editor.getCaretModel().moveCaretRelatively(0, -1, false, false, true);
//                    insertSpacesByContinuationIndent(editor, project);
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    public static void insertSpacesByContinuationIndent(Editor editor, Project project) {
//        int indentSize = CodeStyleSettingsManager.getSettings(project).getContinuationIndentSize(LuaFileType.LUA_FILE_TYPE);
//        StringBuffer buffer = new StringBuffer();
//        for (int i = 0; i < indentSize; i++) {
//            buffer.append(" ");
//        }
//        EditorModificationUtil.insertStringAtCaret(editor, buffer.toString());
//    }
//
//    public static boolean isReadOnly(@NotNull final Editor editor) {
//        if (editor.isViewer()) {
//            return true;
//        }
//        Document document = editor.getDocument();
//        return !document.isWritable();
//    }}

