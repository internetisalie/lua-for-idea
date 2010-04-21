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

package com.sylvanaar.idea.Lua.parser.parsing.functions;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.sylvanaar.idea.Lua.parser.LuaElementTypes;
import com.sylvanaar.idea.Lua.parser.parsing.StatementList;
import com.sylvanaar.idea.Lua.parser.util.LuaPsiBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 12.10.2007
 * Time: 11:44:29
 */
public class Function implements LuaElementTypes {


    public static IElementType parse(LuaPsiBuilder builder) {
        PsiBuilder.Marker funcBlock = builder.mark();

        PsiBuilder.Marker funcStmt = builder.mark();
        builder.compareAndEat(FUNCTION);


        PsiBuilder.Marker funcName = builder.mark();

        int pos = builder.getCurrentOffset();

        while (builder.compareAndEat(FUNCTION_IDENTIFIER_SET))
            ;

        boolean anon = false;
        if (builder.compare(LPAREN)) {
            anon = builder.getCurrentOffset() <= pos;

            if (anon) {
                funcName.drop();
            } else {
                funcName.done(FUNCTION_IDENTIFIER);
            }
        }

        builder.match(LPAREN, "expected (");

        pos = builder.getCurrentOffset();
        PsiBuilder.Marker mark = builder.mark();

        while (builder.compareAndEat(TokenSet.create(NAME, COMMA, ELLIPSIS)))
            ;

        if (builder.getCurrentOffset() > pos)
            mark.done(LuaElementTypes.PARAMETERS);
        else
            mark.drop();

        builder.match(RPAREN, "expected )");

        funcStmt.done(anon ? ANON_FUNCTION_DEFINITION : FUNCTION_DEFINITION);

        StatementList.parse(builder, END);

        builder.match(END);

        funcBlock.done(anon?ANON_FUNCTION_BLOCK:FUNCTION_BLOCK);
        return anon ? ANON_FUNCTION_BLOCK : FUNCTION_BLOCK;
    }
}

