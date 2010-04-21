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

package com.sylvanaar.idea.Lua.parser.parsing.expressions.comparison;

import com.sylvanaar.idea.Lua.lexer.LuaTokenTypes;
import com.sylvanaar.idea.Lua.parser.util.LuaPsiBuilder;
import com.sylvanaar.idea.Lua.parser.util.LuaParserErrors;
import com.sylvanaar.idea.Lua.parser.LuaElementTypes;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 15.12.2007
 */
public class EqualityExpression implements LuaTokenTypes {

	private static TokenSet EQUALITY_OPERATORS = TokenSet.create(
		EQ, NE
	);

	public static IElementType parse(LuaPsiBuilder builder) {
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = RelationalExpression.parse(builder);
		if (result != LuaElementTypes.EMPTY_INPUT && builder.compareAndEat(EQUALITY_OPERATORS)) {
//			result = AssignmentExpression.parseWithoutPriority(builder);
//			if (result == LuaElementTypes.EMPTY_INPUT) {
				result = RelationalExpression.parse(builder);
//			}
			if (result == LuaElementTypes.EMPTY_INPUT) {
				builder.error(LuaParserErrors.expected("expression"));
			}
			marker.done(LuaElementTypes.EQUALITY_EXPRESSION);
			return LuaElementTypes.EQUALITY_EXPRESSION;
		} else {
			marker.drop();
		}
		return result;
	}
}
