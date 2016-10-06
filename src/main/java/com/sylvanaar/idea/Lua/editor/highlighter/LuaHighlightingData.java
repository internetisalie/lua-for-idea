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

package com.sylvanaar.idea.Lua.editor.highlighter;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import org.jetbrains.annotations.NonNls;

import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.*;


/**
 * Created by IntelliJ IDEA.
 * User: jon
 * Date: Apr 3, 2010
 * Time: 1:55:00 AM
 */
public class LuaHighlightingData {
    @NonNls
    static final String KEYWORD_ID            = "LUA_KEYWORD";
    @NonNls
    static final String COMMENT_ID            = "LUA_COMMENT";
    @NonNls
    static final String LONGCOMMENT_ID        = "LUA_LONGCOMMENT";
    @NonNls
    static final String NUMBER_ID             = "LUA_NUMBER";
    @NonNls
    static final String STRING_ID             = "LUA_STRING";
    @NonNls
    static final String LONGSTRING_ID         = "LUA_LONGSTRING";
    @NonNls
    static final String LONGSTRING_BRACES_ID  = "LUA_LONGSTRING_BRACES";
    @NonNls
    static final String LONGCOMMENT_BRACES_ID = "LUA_LONGCOMMENT_BRACES";
    @NonNls
    static final String BRACES_ID             = "LUA_BRACES";
    @NonNls
    static final String PARENTHS_ID           = "LUA_PARENTHS";
    @NonNls
    static final String BRACKETS_ID           = "LUA_BRACKETS";
    @NonNls
    static final String BAD_CHARACTER_ID      = "LUA_BAD_CHARACTER";
    @NonNls
    static final String COMMA_ID              = "LUA_COMMA";
    @NonNls
    static final String SEMICOLON_ID          = "LUA_SEMICOLON";
    @NonNls
    static final String DEFINED_CONSTANTS_ID  = "LUA_DEFINED_CONSTANTS";
    @NonNls
    static final String LOCAL_VAR_ID          = "LUA_LOCAL_VAR";
    @NonNls
    static final String GLOBAL_VAR_ID         = "LUA_GLOBAL_VAR";
    @NonNls
    static final String FIELD_ID              = "LUA_FIELD";
    @NonNls
    static final String TAIL_CALL_ID          = "LUA_TAIL_CALL";
    @NonNls
    static final String PARAMETER_ID          = "LUA_PARAMETER";
    @NonNls
    static final String UPVAL_ID              = "LUA_UPVAL";

    @NonNls
    static final String OPERATORS_ID    = "LUA_OPERATORS";
    @NonNls
    static final String LUADOC_ID       = "LUA_LUADOC";
    @NonNls
    static final String LUADOC_TAG_ID   = "LUA_LUADOC_TAG";
    @NonNls
    static final String LUADOC_VALUE_ID = "LUA_LUADOC_VALUE";

    public static final TextAttributesKey LUADOC       =
            TextAttributesKey.createTextAttributesKey(LUADOC_ID, DOC_COMMENT);
    public static       TextAttributesKey LUADOC_TAG   =
            TextAttributesKey.createTextAttributesKey(LUADOC_TAG_ID, DOC_COMMENT_TAG);
    public static       TextAttributesKey LUADOC_VALUE =
            TextAttributesKey.createTextAttributesKey(LUADOC_VALUE_ID, DOC_COMMENT);

    public static final TextAttributesKey LOCAL_VAR  =
            TextAttributesKey.createTextAttributesKey(LOCAL_VAR_ID, LOCAL_VARIABLE);
    public static final TextAttributesKey UPVAL      = TextAttributesKey.createTextAttributesKey(UPVAL_ID, LOCAL_VAR);
    public static final TextAttributesKey PARAMETER  =
            TextAttributesKey.createTextAttributesKey(PARAMETER_ID, DefaultLanguageHighlighterColors.PARAMETER);
    public static final TextAttributesKey GLOBAL_VAR =
            TextAttributesKey.createTextAttributesKey(GLOBAL_VAR_ID, GLOBAL_VARIABLE);
    public static final TextAttributesKey FIELD      =
            TextAttributesKey.createTextAttributesKey(FIELD_ID, STATIC_FIELD);

    public static final TextAttributesKey TAIL_CALL =
            TextAttributesKey.createTextAttributesKey(TAIL_CALL_ID, HighlighterColors.TEXT);

    public static final TextAttributesKey KEYWORD =
            TextAttributesKey.createTextAttributesKey(KEYWORD_ID, DefaultLanguageHighlighterColors.KEYWORD);

    public static final TextAttributesKey COMMENT            =
            TextAttributesKey.createTextAttributesKey(COMMENT_ID, DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey LONGCOMMENT        =
            TextAttributesKey.createTextAttributesKey(LONGCOMMENT_ID, BLOCK_COMMENT);
    public static final TextAttributesKey LONGCOMMENT_BRACES = TextAttributesKey
            .createTextAttributesKey(LONGCOMMENT_BRACES_ID, BLOCK_COMMENT);

    public static final TextAttributesKey NUMBER =
            TextAttributesKey.createTextAttributesKey(NUMBER_ID, DefaultLanguageHighlighterColors.NUMBER);

    public static final TextAttributesKey STRING =
            TextAttributesKey.createTextAttributesKey(STRING_ID, DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey LONGSTRING        =
            TextAttributesKey.createTextAttributesKey(LONGSTRING_ID, DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey LONGSTRING_BRACES =
            TextAttributesKey.createTextAttributesKey(LONGSTRING_BRACES_ID, DefaultLanguageHighlighterColors.STRING);

    public static final TextAttributesKey BRACKETS      =
            TextAttributesKey.createTextAttributesKey(BRACKETS_ID, DefaultLanguageHighlighterColors.BRACKETS);
    public static final TextAttributesKey BRACES        =
            TextAttributesKey.createTextAttributesKey(BRACES_ID, DefaultLanguageHighlighterColors.BRACES);
    public static final TextAttributesKey PARENTHESES   =
            TextAttributesKey.createTextAttributesKey(PARENTHS_ID, DefaultLanguageHighlighterColors.PARENTHESES);
    public static final TextAttributesKey BAD_CHARACTER =
            TextAttributesKey.createTextAttributesKey(BAD_CHARACTER_ID, HighlighterColors.BAD_CHARACTER);
    public static final TextAttributesKey OPERATORS     =
            TextAttributesKey.createTextAttributesKey(OPERATORS_ID, DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey COMMA         =
            TextAttributesKey.createTextAttributesKey(COMMA_ID, DefaultLanguageHighlighterColors.COMMA);

    public static final TextAttributesKey SEMI =
            TextAttributesKey.createTextAttributesKey(SEMICOLON_ID, DefaultLanguageHighlighterColors.SEMICOLON);

    public static final TextAttributesKey DEFINED_CONSTANTS =
            TextAttributesKey.createTextAttributesKey(DEFINED_CONSTANTS_ID, CONSTANT);
}
