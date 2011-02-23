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

package com.sylvanaar.idea.Lua.util;

/**
 * Created by IntelliJ IDEA.
 * User: Jon S Akhtar
 * Date: 2/23/11
 * Time: 6:35 AM
 */
public class TextUtil {
    private TextUtil()
    {
    }

    public static boolean isEol(char c)
    {
        return c == '\n' || c == '\r';
    }

    public static boolean isWhiteSpace(char c)
    {
        return c == ' ' || c == '\t' || c == '\f' || c == '\r' || c == '\013';
    }

    public static boolean isWhiteSpaceOrEol(char c)
    {
        return isWhiteSpace(c) || c == '\n';
    }

    public static boolean isQuote(char c)
    {
        return c == '"' || c == '\'';
    }
    public static boolean isOpenBrace(char c)
    {
        return c == '(' || c == '{' || c == '[';
    }

    public static boolean isCloseBrace(char c)
    {
        return c == ')' || c == '}' || c == ']';
    }
}
