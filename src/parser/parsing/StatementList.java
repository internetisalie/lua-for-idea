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

package com.sylvanaar.idea.Lua.parser.parsing;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.sylvanaar.idea.Lua.parser.LuaElementTypes;
import com.sylvanaar.idea.Lua.parser.LuaPsiBuilder;
import com.sylvanaar.idea.Lua.parser.util.LuaParserErrors;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 12.10.2007
 * Time: 10:31:27
 */
public class StatementList {

	//	statement_list:

	public static void parse(LuaPsiBuilder builder, IElementType ... endDelimeters) {
		parse(builder, TokenSet.create(endDelimeters));
	}

	private static void parse(LuaPsiBuilder builder, TokenSet whereToStop) {
		PsiBuilder.Marker statementList = builder.mark();

		if (!whereToStop.contains(builder.getTokenType())) {
			int previous;
			while (true) {
				previous = builder.getCurrentOffset();
				if (parseStatement(builder) == LuaElementTypes.EMPTY_INPUT) {
					builder.error("Unexpected token: " + builder.getTokenType());
					builder.advanceLexer();
				}

				if (builder.eof() || whereToStop.contains(builder.getTokenType())) {
					break;
				}
				if (previous == builder.getCurrentOffset()) {
					builder.error(LuaParserErrors.unexpected(builder.getTokenType()));
					builder.advanceLexer();
				}
			}
		}

		statementList.done(LuaElementTypes.STATEMENT_LIST);
	}

    private static IElementType parseStatement(LuaPsiBuilder builder) {
        return Statement.parse(builder);  //To change body of created methods use File | Settings | File Templates.
    }

}
