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
import com.sylvanaar.idea.Lua.parser.parsing.Block;
import com.sylvanaar.idea.Lua.parser.parsing.expressions.Expression;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 30.10.2007
 */
public class IfStatement implements LuaTokenTypes {

    // 'if' exp 'then' block ('elseif' exp 'then' block)* ('else' block)? 'end' |
	public static IElementType parse(LuaPsiBuilder builder) {
		PsiBuilder.Marker statement = builder.mark();
		if (!builder.compareAndEat(IF)) {
			statement.drop();
			return LuaElementTypes.EMPTY_INPUT;
		}

		Expression.parse(builder);

        parseThenClause(builder);

        builder.match(END);
        
		statement.done(LuaElementTypes.IF_THEN_BLOCK);
		return LuaElementTypes.IF_THEN_BLOCK;
	}


    private static void parseThenClause(LuaPsiBuilder builder) {
		PsiBuilder.Marker statement = builder.mark();
		if (!builder.compareAndEat(THEN)) {
			statement.drop();
			return;
		}

        Block.parse(builder);

        statement.done(LuaElementTypes.THEN_CLAUSE);
        
        parseElseOrEnd(builder);

		return;
    }

	private static void parseElseOrEnd(LuaPsiBuilder builder) {
		
		if (builder.compare(ELSEIF)) {
			PsiBuilder.Marker elseifClause = builder.mark();
			builder.advanceLexer();

			Expression.parse(builder);

            elseifClause.done(LuaElementTypes.ELSEIF_CLAUSE);

            parseThenClause(builder);

		}
		if (builder.compare(ELSE)) {
			PsiBuilder.Marker elseClause = builder.mark();
			builder.advanceLexer();

			Block.parse(builder);
            
			elseClause.done(LuaElementTypes.ELSE_CLAUSE);
		}
	}


}
