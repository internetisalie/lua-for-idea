///*
// * Copyright 2011 Jon S Akhtar (Sylvanaar)
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.sylvanaar.idea.Lua.editor;
//
//import com.intellij.codeInsight.CodeInsightSettings;
//import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
//import com.intellij.openapi.editor.Editor;
//import com.intellij.openapi.editor.EditorModificationUtil;
//import com.intellij.openapi.editor.ex.EditorEx;
//import com.intellij.openapi.editor.highlighter.EditorHighlighter;
//import com.intellij.openapi.editor.highlighter.HighlighterIterator;
//import com.intellij.openapi.fileTypes.FileType;
//import com.intellij.openapi.project.Project;
//import com.intellij.psi.PsiFile;
//import com.intellij.psi.tree.IElementType;
//import com.intellij.psi.tree.TokenSet;
//import com.sylvanaar.idea.Lua.LuaFileType;
//import com.sylvanaar.idea.Lua.lang.lexer.LuaTokenTypes;
//import com.sylvanaar.idea.Lua.util.TextUtil;
//
//import static com.intellij.codeInsight.editorActions.TypedHandlerDelegate.Result.*;
//
///**
// * Created by IntelliJ IDEA.
// * User: Jon S Akhtar
// * Date: 1/30/11
// * Time: 6:45 AM
// */
//    public class LuaTypedHandlerDelegate extends TypedHandlerDelegate
//        implements LuaTokenTypes
//    {
//
//        public LuaTypedHandlerDelegate()
//        {
//        }
//
//        public com.intellij.codeInsight.editorActions.TypedHandlerDelegate.Result beforeCharTyped(char charTyped, Project project, Editor editor, PsiFile file, FileType fileType)
//        {
//            if(file.getLanguage() != LuaFileType.LUA_LANGUAGE|| editor.isViewer() || !editor.getDocument().isWritable())
//                return com.intellij.codeInsight.editorActions.TypedHandlerDelegate.Result.CONTINUE;
//            if(editor.getSelectionModel().hasBlockSelection())
//                return com.intellij.codeInsight.editorActions.TypedHandlerDelegate.Result.CONTINUE;
//            String text = editor.getDocument().getText();
//            CodeInsightSettings settings = CodeInsightSettings.getInstance();
//            if(settings.AUTOINSERT_PAIR_QUOTE && (TextUtil.isQuote(charTyped)))
//                return handleQuoteTyped(editor, charTyped, text);
//            if(settings.AUTOINSERT_PAIR_BRACKET && TextUtil.isOpenBrace(charTyped))
//                return handleOpenBrace(editor, charTyped, text);
//            if(TextUtil.isCloseBrace(charTyped))
//                return handleCloseBrace(editor, charTyped, text);
//              return com.intellij.codeInsight.editorActions.TypedHandlerDelegate.Result.CONTINUE;
//        }
//
//        public com.intellij.codeInsight.editorActions.TypedHandlerDelegate.Result charTyped(char c, Project project, Editor editor, PsiFile file)
//        {
//            if(file.getLanguage() != LuaFileType.LUA_LANGUAGE || editor.getSelectionModel().hasBlockSelection())
//                return com.intellij.codeInsight.editorActions.TypedHandlerDelegate.Result.CONTINUE;
//            EditorHighlighter highlighter = ((EditorEx)editor).getHighlighter();
//            HighlighterIterator iterator = highlighter.createIterator(editor.getCaretModel().getOffset() - 1);
//            IElementType tokenType = iterator.atEnd() ? null : iterator.getTokenType();
//            CodeInsightSettings settings = CodeInsightSettings.getInstance();
//            String text = editor.getDocument().getText();
//            if(settings.AUTOINSERT_PAIR_BRACKET &&
//                    (TokenBNF.tSTRINGS_BEGINNINGS.contains(tokenType) ||
//                            TokenBNF.tWORDS_BEGINNINGS.contains(tokenType)) &&
//                    iterator.getEnd() == iterator.getStart() + 3 &&
//                    text.charAt(iterator.getEnd() - 1) == c)
//            {
//                EditorModificationUtil.insertStringAtCaret(editor, String.valueOf(new char[] {
//                    c != '<' ? c : '>'
//                }));
//                editor.getCaretModel().moveCaretRelatively(-1, 0, false, false, true);
//                return STOP;
//            } else
//            {
//                return com.intellij.codeInsight.editorActions.TypedHandlerDelegate.Result.CONTINUE;
//            }
//        }
//
//        private static com.intellij.codeInsight.editorActions.TypedHandlerDelegate.Result handleCloseBrace(Editor editor, char closeBrace, String text)
//        {
//            int caret = editor.getCaretModel().getOffset();
//            if(caret >= text.length())
//                return com.intellij.codeInsight.editorActions.TypedHandlerDelegate.Result.CONTINUE;
//            TokenSet openBraceTypes = null;
//            IElementType closeBraceType = null;
//            if(closeBrace == ')')
//            {
//                openBraceTypes = TokenBNF.LPARENS;
//                closeBraceType = tRPAREN;
//            }
//            if(closeBrace == '}')
//            {
//                openBraceTypes = TokenBNF.tLBRACES;
//                closeBraceType = tRBRACE;
//            }
//            if(closeBrace == ']')
//            {
//                openBraceTypes = TokenBNF.tLBRACKS;
//                closeBraceType = tRBRACK;
//            }
//            if(!$assertionsDisabled && openBraceTypes == null)
//                throw new AssertionError();
//            EditorHighlighter highlighter = ((EditorEx)editor).getHighlighter();
//            IElementType type = highlighter.createIterator(caret).getTokenType();
//            if(TokenBNF.tCOMMENTS.contains(type) || type != tSTRING_DEND && type != tSTRING_END && type != tREGEXP_END && type != tWORDS_END && (TokenBNF.tSTRING_TOKENS.contains(type) || TokenBNF.tREGEXP_TOKENS.contains(type) || TokenBNF.tWORDS_TOKENS.contains(type)))
//            {
//                EditorModificationUtil.insertStringAtCaret(editor, String.valueOf(closeBrace));
//                return STOP;
//            }
//            if(closeBrace == text.charAt(caret) && getBraceBalance(editor, openBraceTypes, closeBraceType) <= 0)
//            {
//                editor.getCaretModel().moveCaretRelatively(1, 0, false, false, true);
//                return STOP;
//            } else
//            {
//                return com.intellij.codeInsight.editorActions.TypedHandlerDelegate.Result.CONTINUE;
//            }
//        }
//
//        private static com.intellij.codeInsight.editorActions.TypedHandlerDelegate.Result handleOpenBrace(Editor editor, char openBrace, String text)
//        {
//            char closeBrace = TextUtil.getCloseDelim(openBrace);
//            if(text.length() == 0)
//            {
//                EditorModificationUtil.insertStringAtCaret(editor, String.valueOf(new char[] {
//                    openBrace, closeBrace
//                }));
//                editor.getCaretModel().moveCaretRelatively(-1, 0, false, false, true);
//                return STOP;
//            }
//            EditorModificationUtil.insertStringAtCaret(editor, String.valueOf(new char[] {
//                openBrace
//            }));
//            EditorHighlighter highlighter = ((EditorEx)editor).getHighlighter();
//            int caret = editor.getCaretModel().getOffset();
//            HighlighterIterator iterator = highlighter.createIterator(caret - 1);
//            IElementType tokenType = iterator.atEnd() ? null : iterator.getTokenType();
//            iterator.advance();
//            IElementType nextTokenType = iterator.atEnd() ? null : iterator.getTokenType();
//            if(tINTEGER == tokenType)
//                return STOP;
//            if(tSTRING_DBEG == tokenType || TokenBNF.tSTRINGS_BEGINNINGS.contains(tokenType) || TokenBNF.tWORDS_BEGINNINGS.contains(tokenType) || TokenBNF.tREGEXP_BEGINNINGS.contains(tokenType) || (nextTokenType == null || nextTokenType.getLanguage() != RubyLanguage.INSTANCE || INSERT_QUOTE_OR_BRACE_BEFORE.contains(nextTokenType)) && !TokenBNF.tCOMMENTS.contains(tokenType) && !TokenBNF.tSTRING_TOKENS.contains(tokenType) && !TokenBNF.tREGEXP_TOKENS.contains(tokenType) && !TokenBNF.tWORDS_TOKENS.contains(tokenType))
//            {
//                EditorModificationUtil.insertStringAtCaret(editor, String.valueOf(new char[] {
//                    closeBrace
//                }));
//                editor.getCaretModel().moveCaretRelatively(-1, 0, false, false, true);
//            }
//            return STOP;
//        }
//
//        private static com.intellij.codeInsight.editorActions.TypedHandlerDelegate.Result handleQuoteTyped(Editor editor, char quote, String text)
//        {
//            if(text.length() == 0)
//            {
//                EditorModificationUtil.insertStringAtCaret(editor, String.valueOf(new char[] {
//                    quote, quote
//                }));
//                editor.getCaretModel().moveCaretRelatively(-1, 0, false, false, true);
//                return STOP;
//            }
//            EditorHighlighter highlighter = ((EditorEx)editor).getHighlighter();
//            int caret = editor.getCaretModel().getOffset();
//            HighlighterIterator iterator = highlighter.createIterator(caret);
//            IElementType tokenType = iterator.atEnd() ? null : iterator.getTokenType();
//            int lineNumber = tokenType == null ? -1 : editor.getDocument().getLineNumber(caret);
//            if(tokenType == WS && caret > 0)
//            {
//                iterator.retreat();
//                IElementType type = iterator.getTokenType();
//                int prevLineNumber;
//                if(lineNumber == -1 || type == null || (prevLineNumber = editor.getDocument().getLineNumber(iterator.getStart())) == -1 || lineNumber != prevLineNumber)
//                {
//                    EditorModificationUtil.insertStringAtCaret(editor, String.valueOf(new char[] {
//                        quote, quote
//                    }));
//                    editor.getCaretModel().moveCaretRelatively(-1, 0, false, false, true);
//                    return STOP;
//                }
//                if(type == WRONG || COMMENT_SET.contains(type))
//                    return com.intellij.codeInsight.editorActions.TypedHandlerDelegate.Result.CONTINUE;
//                iterator.advance();
//            }
//            if(tokenType != tSTRING_DEND && tokenType != tREGEXP_END && BNF.tSTRING_TOKENS.contains(tokenType))
//            {
//                if(caret >= text.length() || text.charAt(caret) != quote)
//                    EditorModificationUtil.insertStringAtCaret(editor, String.valueOf(quote));
//                else
//                    editor.getCaretModel().moveCaretRelatively(1, 0, false, false, true);
//                return STOP;
//            }
//            if(iterator.atEnd() || caret == text.length() || caret < text.length() && text.charAt(caret) != quote && (tokenType.getLanguage() != RubyLanguage.INSTANCE || INSERT_QUOTE_OR_BRACE_BEFORE.contains(tokenType)))
//            {
//                EditorModificationUtil.insertStringAtCaret(editor, String.valueOf(new char[] {
//                    quote, quote
//                }));
//                editor.getCaretModel().moveCaretRelatively(-1, 0, false, false, true);
//                return STOP;
//            } else
//            {
//                return com.intellij.codeInsight.editorActions.TypedHandlerDelegate.Result.CONTINUE;
//            }
//        }
//
//        private static int getBraceBalance(Editor editor, TokenSet openBraces, IElementType closeBrace)
//        {
//            if(editor == null)
//                throw new IllegalArgumentException("Argument 0 for @NotNull parameter of org/jetbrains/plugins/ruby/" +
//    "ruby/actions/editor/handlers/RubyTypedHandler.getBraceBalance mu" +
//    "st not be null"
//    );
//            EditorHighlighter highlighter = ((EditorEx)editor).getHighlighter();
//            HighlighterIterator iterator = highlighter.createIterator(0);
//            int balance = 0;
//            for(; !iterator.atEnd(); iterator.advance())
//            {
//                IElementType type = iterator.getTokenType();
//                if(openBraces.contains(type))
//                    balance++;
//                if(closeBrace == type)
//                    balance--;
//            }
//
//            return balance;
//        }
//
//        private static final TokenSet INSERT_QUOTE_OR_BRACE_BEFORE;
//        static final boolean $assertionsDisabled = !org/jetbrains/plugins/ruby/ruby/actions/editor/handlers/RubyTypedHandler.desiredAssertionStatus();
//
//        static
//        {
//            INSERT_QUOTE_OR_BRACE_BEFORE = TokenSet.orSet(new TokenSet[] {
//                BNF.tBINARY_OPS, TokenSet.create(new IElementType[]{
//                    tDOT, tDOT2, tDOT3, tCOMMA, tCOLON, tCOLON2, tCOLON3, tQUESTION, tEXCLAMATION, tWHITE_SPACE,
//                    tEOL, tRPAREN, tRBRACE, tRBRACK, tSTRING_DEND
//            })
//            });
//        }
//    }
//
//}
//
//
//
