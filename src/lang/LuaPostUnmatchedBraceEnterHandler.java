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

package com.sylvanaar.idea.Lua.lang;

import com.intellij.codeInsight.CodeInsightSettings;
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettingsManager;
import com.sylvanaar.idea.Lua.LuaFileType;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaBlock;
import org.jetbrains.annotations.NotNull;

import static com.sylvanaar.idea.Lua.lang.lexer.LuaTokenTypes.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jon S Akhtar
 * Date: Oct 1, 2010
 * Time: 8:04:29 PM
 */
public class LuaPostUnmatchedBraceEnterHandler implements EnterHandlerDelegate {
    @Override
    public Result preprocessEnter(PsiFile file, Editor editor, Ref<Integer> caretOffset,
                                  Ref<Integer> caretAdvance, DataContext dataContext,
                                  EditorActionHandler originalHandler) {
        String text = editor.getDocument().getText();
        if (StringUtil.isEmpty(text)) {
            return Result.Continue;
        }

        final int caret = editor.getCaretModel().getOffset();
        final EditorHighlighter highlighter = ((EditorEx) editor).getHighlighter();
        if (caret >= 1 && caret < text.length() && CodeInsightSettings.getInstance().SMART_INDENT_ON_ENTER) {
            HighlighterIterator iterator = highlighter.createIterator(caret);
            iterator.retreat();
            while (!iterator.atEnd() && WS == iterator.getTokenType()) {
                iterator.retreat();
            }


            iterator = highlighter.createIterator(editor.getCaretModel().getOffset());
            while (!iterator.atEnd() && WS == iterator.getTokenType()) {
                iterator.advance();
            }
            if (!iterator.atEnd() && RCURLY == iterator.getTokenType()) {
                PsiDocumentManager.getInstance(file.getProject()).commitDocument(editor.getDocument());
                final PsiElement element = file.findElementAt(iterator.getStart());
                if (element != null &&
                        element.getNode().getElementType() == RCURLY &&
                        element.getParent() instanceof LuaBlock &&
                        text.length() > caret) {
                    return Result.DefaultForceIndent;
                }
            }
//      if (afterArrow) {
//        return Result.Stop;
//      }
        }

        if (handleEnter(editor, dataContext, file.getProject(), originalHandler, file)) {
            return Result.Stop;
        }
        return Result.Continue;
    }

    protected static boolean handleEnter(Editor editor,
                                         DataContext dataContext,
                                         @NotNull Project project,
                                         EditorActionHandler originalHandler, PsiFile file) {
        if (isReadOnly(editor)) {
            return false;
        }
        int caretOffset = editor.getCaretModel().getOffset();
        if (caretOffset < 1) return false;

        if (handleBetweenSquareBraces(editor, caretOffset, dataContext, project, originalHandler)) {
            return true;
        }

        return false;
    }

    private static boolean handleBetweenSquareBraces(Editor editor,
                                                     int caret,
                                                     DataContext context,
                                                     Project project,
                                                     EditorActionHandler originalHandler) {
        String text = editor.getDocument().getText();
        if (text == null || text.length() == 0) return false;
        final EditorHighlighter highlighter = ((EditorEx) editor).getHighlighter();
        if (caret < 1 || caret > text.length() - 1) {
            return false;
        }
        HighlighterIterator iterator = highlighter.createIterator(caret - 1);
        if (LBRACK == iterator.getTokenType()) {
            if (text.length() > caret) {
                iterator = highlighter.createIterator(caret);
                if (RBRACK == iterator.getTokenType()) {
                    originalHandler.execute(editor, context);
                    originalHandler.execute(editor, context);
                    editor.getCaretModel().moveCaretRelatively(0, -1, false, false, true);
                    insertSpacesByContinuationIndent(editor, project);
                    return true;
                }
            }
        }
        return false;
    }

    public static void insertSpacesByContinuationIndent(Editor editor, Project project) {
        int indentSize = CodeStyleSettingsManager.getSettings(project).getContinuationIndentSize(LuaFileType.LUA_FILE_TYPE);
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < indentSize; i++) {
            buffer.append(" ");
        }
        EditorModificationUtil.insertStringAtCaret(editor, buffer.toString());
    }

    public static boolean isReadOnly(@NotNull final Editor editor) {
        if (editor.isViewer()) {
            return true;
        }
        Document document = editor.getDocument();
        return !document.isWritable();
    }
}



