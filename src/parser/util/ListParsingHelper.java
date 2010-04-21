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

package com.sylvanaar.idea.Lua.parser.util;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.sylvanaar.idea.Lua.lexer.LuaTokenTypes;
import com.sylvanaar.idea.Lua.parser.LuaElementTypes;

/**
 * @author markov
 * @date 15.10.2007
 */
public class ListParsingHelper implements LuaTokenTypes {

	/**
	 * Parses comma delimited list of expressions
	 *
	 * @param builder	 Current builder wrapper
	 * @param result	 Result of parsing first element
	 * @param parser	 method, used in parsing each single expression in list
	 * @param eatFollowComma If true, the following comma will be eaten
	 * @return number of expressions in list
	 */
	public static int parseCommaDelimitedExpressionWithLeadExpr(final LuaPsiBuilder builder,
								    final IElementType result,
								    final ParserPart parser,
								    final boolean eatFollowComma) {
		if (result == LuaElementTypes.EMPTY_INPUT) {
			if (builder.compare(COMMA)) {
				builder.error(LuaParserErrors.EXPRESSION_EXPECTED_MESSAGE);
			} else {
				return 0;
			}
		}
		int count = 1;
		PsiBuilder.Marker beforeLastCommaMarker = builder.mark();
		while (builder.compareAndEat(COMMA)) {
			if (parser.parse(builder) != LuaElementTypes.EMPTY_INPUT) {
				count++;
				beforeLastCommaMarker.drop();
				beforeLastCommaMarker = builder.mark();
			} else {
				if (builder.compare(COMMA)) {
					builder.error(LuaParserErrors.EXPRESSION_EXPECTED_MESSAGE);
				} else {
					if (!eatFollowComma) {
						builder.error(LuaParserErrors.EXPRESSION_EXPECTED_MESSAGE);
					}
				}
			}
		}
		if (!eatFollowComma) {
			beforeLastCommaMarker.rollbackTo();
		} else {
			beforeLastCommaMarker.drop();
		}
		return count;

	}

}
