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

package com.sylvanaar.idea.Lua.parser.parsing.expressions.math;//package com.sylvanaar.idea.Lua.parser.parsing.expressions.math;

import com.sylvanaar.idea.Lua.lexer.LuaTokenTypes;
import com.sylvanaar.idea.Lua.parser.util.LuaPsiBuilder;
import com.sylvanaar.idea.Lua.parser.util.LuaParserErrors;
import com.sylvanaar.idea.Lua.parser.LuaElementTypes;
import com.sylvanaar.idea.Lua.parser.parsing.expressions.logical.LogicalNotExpression;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: jon
 * Date: Apr 3, 2010
 * Time: 3:53:59 AM
 */
public class MultiplicativeExpression implements LuaTokenTypes {

	private static TokenSet MULTIPLICATIVE_OPERATORS = TokenSet.create(
		DIV, MULT
	);

	public static IElementType parse(LuaPsiBuilder builder) {
		PsiBuilder.Marker marker = builder.mark();
		IElementType result = LogicalNotExpression.parse(builder);
		if (result != LuaElementTypes.EMPTY_INPUT && builder.compareAndEat(MULTIPLICATIVE_OPERATORS)) {
//			result = AssignmentExpression.parseWithoutPriority(builder);
//			if (result == LuaElementTypes.EMPTY_INPUT) {
				result = LogicalNotExpression.parse(builder);
//			}
			if (result == LuaElementTypes.EMPTY_INPUT) {
				builder.error(LuaParserErrors.expected("expression"));
			}
			PsiBuilder.Marker newMarker = marker.precede();
			marker.done(LuaElementTypes.MULTIPLICATIVE_EXPRESSION);
			result = LuaElementTypes.MULTIPLICATIVE_EXPRESSION;
			if (builder.compareAndEat(MULTIPLICATIVE_OPERATORS)) {
				subParse(builder, newMarker);
			} else {
				newMarker.drop();
			}
		} else {
			marker.drop();
		}
		return result;
	}

	private static IElementType subParse(LuaPsiBuilder builder, PsiBuilder.Marker marker) {
//		IElementType result = AssignmentExpression.parseWithoutPriority(builder);
//		if (result == LuaElementTypes.EMPTY_INPUT) {
			IElementType result = LogicalNotExpression.parse(builder);
//		}
		if (result == LuaElementTypes.EMPTY_INPUT) {
			builder.error(LuaParserErrors.expected("expression"));
		}
		PsiBuilder.Marker newMarker = marker.precede();
		marker.done(LuaElementTypes.MULTIPLICATIVE_EXPRESSION);
		if (builder.compareAndEat(MULTIPLICATIVE_OPERATORS)) {
			subParse(builder, newMarker);
		} else {
			newMarker.drop();
		}
		return LuaElementTypes.MULTIPLICATIVE_EXPRESSION;
	}
}
