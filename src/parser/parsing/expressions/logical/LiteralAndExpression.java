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

package com.sylvanaar.idea.Lua.parser.parsing.expressions.logical;//package com.sylvanaar.idea.Lua.parser.parsing.expressions.logical;
//
//import com.sylvanaar.idea.Lua.lexer.LuaTokenTypes;
//import com.sylvanaar.idea.Lua.parser.LuaPsiBuilder;
//import com.sylvanaar.idea.Lua.parser.util.LuaParserErrors;
//import com.sylvanaar.idea.Lua.parser.LuaElementTypes;
//import com.sylvanaar.idea.Lua.parser.parsing.expressions.AssignmentExpression;
//import com.intellij.psi.tree.IElementType;
//import com.intellij.lang.PsiBuilder;
//
///**
// * Created by IntelliJ IDEA.
// * User: markov
// * Date: 14.12.2007
// */
//public class LiteralAndExpression implements LuaTokenTypes {
//
//	public static IElementType parse(LuaPsiBuilder builder) {
//		PsiBuilder.Marker marker = builder.mark();
//		IElementType result = AssignmentExpression.parse(builder);
//		if (result != LuaElementTypes.EMPTY_INPUT) {
//			if (builder.compareAndEat(AND)) {
//				result = LiteralAndExpression.parse(builder);
//				if (result == LuaElementTypes.EMPTY_INPUT) {
//					builder.error(LuaParserErrors.expected("expression"));
//				}
//				PsiBuilder.Marker newMarker = marker.precede();
//				marker.done(LuaElementTypes.LITERAL_LOGICAL_EXPRESSION);
//				result = LuaElementTypes.LITERAL_LOGICAL_EXPRESSION;
//				if (builder.compareAndEat(AND)) {
//					subParse(builder, newMarker);
//				} else {
//					newMarker.drop();
//				}
//			} else {
//				marker.drop();
//			}
//		} else {
//			marker.drop();
//		}
//		return result;
//	}
//
//	private static IElementType subParse(LuaPsiBuilder builder, PsiBuilder.Marker marker) {
//		if (LiteralAndExpression.parse(builder) == LuaElementTypes.EMPTY_INPUT) {
//			builder.error(LuaParserErrors.expected("expression"));
//		}
//		PsiBuilder.Marker newMarker = marker.precede();
//		marker.done(LuaElementTypes.LITERAL_LOGICAL_EXPRESSION);
//		if (builder.compareAndEat(opLIT_AND)) {
//			subParse(builder, newMarker);
//		} else {
//			newMarker.drop();
//		}
//		return LuaElementTypes.LITERAL_LOGICAL_EXPRESSION;
//	}
//}
