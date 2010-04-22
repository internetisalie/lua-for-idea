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
 * User: markov
 * Date: 29.10.2007
 */
public class Expression implements LuaElementTypes {
    static TokenSet EXPR_SET = TokenSet.create(NIL, FALSE, TRUE, NUMBER, STRING, ELLIPSIS, NAME, EQ, NE, GT, LT, LE, GE, PLUS, MINUS, MULT, DIV); //todo


    //	 ('nil' | 'false' | 'true' | number | string | '...' | function | prefixexp | tableconstructor | unop exp) (binop exp)* ;

	public static IElementType parse(LuaPsiBuilder builder) {
        PsiBuilder.Marker exp = builder.mark();

        
		//IElementType result; = builder.getTokenType();// parseExpressionWithoutVariable(builder);

        while ( builder.compareAndEat(EXPR_SET)) {
            ;
        }
        exp.done(EXPRESSION);
  
		return LuaElementTypes.EXPRESSION;
	}
}
