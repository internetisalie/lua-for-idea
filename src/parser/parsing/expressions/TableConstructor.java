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

package com.sylvanaar.idea.Lua.parser.parsing.expressions;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.sylvanaar.idea.Lua.parser.LuaElementTypes;
import com.sylvanaar.idea.Lua.parser.LuaPsiBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: Jon S Akhtar
 * Date: Apr 21, 2010
 * Time: 6:34:03 PM
 */
public class TableConstructor implements LuaElementTypes {

    /*
tableconstructor : '{' (fieldlist)? '}';

fieldlist : field (fieldsep field)* (fieldsep)?;

field : '[' exp ']' '=' exp | NAME '=' exp | exp;

fieldsep : ',' | ';';
    
     */

    static TokenSet FIELD_SEPS = TokenSet.create(COMMA, SEMI);
   

    public static IElementType parse(LuaPsiBuilder builder) {
        builder.match(LCURLY);

        if (!builder.compare(RCURLY))
            parseFieldList(builder);

        builder.match(RCURLY);
        
        return TABLE_CONSTRUCTOR;
    }

    private static void parseFieldList(LuaPsiBuilder builder) {
            parseField(builder);
            
            if (builder.compare(FIELD_SEPS))
                builder.advanceLexer();

            if (!builder.compare(RCURLY)) {
                parseFieldList(builder);
            }
    }

    private static void parseField(LuaPsiBuilder builder) {
        PsiBuilder.Marker field = builder.mark();
        if (builder.compareAndEat(LBRACK)) {
            Expression.parse(builder);
            builder.match(RBRACK);

            builder.match(ASSIGN);

            Expression.parse(builder);
        } else {
            builder.match(NAME);

            builder.match(ASSIGN);
        }
        field.done(FIELD);
    }
}
