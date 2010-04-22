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

package com.sylvanaar.idea.Lua.parser.parsing.statements;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.sylvanaar.idea.Lua.lexer.LuaTokenTypes;
import com.sylvanaar.idea.Lua.parser.LuaElementTypes;
import com.sylvanaar.idea.Lua.parser.LuaPsiBuilder;
import com.sylvanaar.idea.Lua.parser.parsing.StatementList;
import com.sylvanaar.idea.Lua.parser.parsing.calls.Variable;
import com.sylvanaar.idea.Lua.parser.util.ListParsingHelper;
import com.sylvanaar.idea.Lua.parser.util.ParserPart;

import static com.sylvanaar.idea.Lua.parser.LuaElementTypes.GENERIC_FOR_BLOCK;
import static com.sylvanaar.idea.Lua.parser.LuaElementTypes.NUMERIC_FOR_BLOCK;


/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 01.11.2007
 */
public class ForStatement implements LuaTokenTypes {

	//		'for' NAME '=' exp ',' exp (',' exp)? 'do' block 'end' |
	//      'for' namelist 'in' explist1 'do' block 'end

	public static IElementType parse(LuaPsiBuilder builder) {
		PsiBuilder.Marker statement = builder.mark();
        IElementType forType = null;
        
		if (!builder.compareAndEat(FOR)) {
			statement.drop();
			return LuaElementTypes.FOR;
		}

        while (!builder.compareAndEat(DO)) {
            if (builder.compare(IN))  {                
                forType = GENERIC_FOR_BLOCK;
                // todo parse generic for
            }

            builder.advanceLexer();
        }
        if (forType == null) {
            forType = NUMERIC_FOR_BLOCK;
            // Todo parse numeric for
        }



        StatementList.parse(builder, END);

        builder.match(END);

		
		statement.done(forType);
		return forType;
	}

	private static void parseForExpression(LuaPsiBuilder builder) {
		ParserPart parserPart = new ParserPart() {
			public IElementType parse(LuaPsiBuilder builder) {
				return Variable.parse(builder);
			}
		};
		ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(
			builder,
			parserPart.parse(builder),
			parserPart,
			false
		);
	}
}
