package com.sylvanaar.idea.Lua.parser.parsing.expressions.math;//package com.sylvanaar.idea.Lua.parser.parsing.expressions.math;
//
//import com.sylvanaar.idea.Lua.lexer.LuaTokenTypes;
//import com.sylvanaar.idea.Lua.parser.util.LuaPsiBuilder;
//import com.sylvanaar.idea.Lua.parser.util.LuaParserErrors;
//import com.sylvanaar.idea.Lua.parser.LuaElementTypes;
//import com.sylvanaar.idea.Lua.parser.parsing.expressions.AssignmentExpression;
//import com.intellij.psi.tree.IElementType;
//import com.intellij.psi.tree.TokenSet;
//import com.intellij.lang.PsiBuilder;
//
///**
// * @author jay
// * @time 16.12.2007 20:43:48
// */
//public class AdditiveExpression implements LuaTokenTypes {
//
//	private static TokenSet ADDITIVE_OPERATORS = TokenSet.create(
//		PLUS, MINUS, CONCAT
//	);
//
//	public static IElementType parse(LuaPsiBuilder builder) {
//		PsiBuilder.Marker marker = builder.mark();
//		IElementType result = MultiplicativeExpression.parse(builder);
//		if (result != LuaElementTypes.EMPTY_INPUT && builder.compareAndEat(ADDITIVE_OPERATORS)) {
//			result = AssignmentExpression.parseWithoutPriority(builder);
//			if (result == LuaElementTypes.EMPTY_INPUT) {
//				result = MultiplicativeExpression.parse(builder);
//			}
//			if (result == LuaElementTypes.EMPTY_INPUT) {
//				builder.error(LuaParserErrors.expected("expression"));
//			}
//			PsiBuilder.Marker newMarker = marker.precede();
//			marker.done(LuaElementTypes.ADDITIVE_EXPRESSION);
//			result = LuaElementTypes.ADDITIVE_EXPRESSION;
//			if (builder.compareAndEat(ADDITIVE_OPERATORS)) {
//				subParse(builder, newMarker);
//			} else {
//				newMarker.drop();
//			}
//		} else {
//			marker.drop();
//		}
//		return result;
//	}
//
//	private static IElementType subParse(LuaPsiBuilder builder, PsiBuilder.Marker marker) {
//		IElementType result = AssignmentExpression.parseWithoutPriority(builder);
//		if (result == LuaElementTypes.EMPTY_INPUT) {
//			result = MultiplicativeExpression.parse(builder);
//		}
//		if (result == LuaElementTypes.EMPTY_INPUT) {
//			builder.error(LuaParserErrors.expected("expression"));
//		}
//		PsiBuilder.Marker newMarker = marker.precede();
//		marker.done(LuaElementTypes.ADDITIVE_EXPRESSION);
//		if (builder.compareAndEat(ADDITIVE_OPERATORS)) {
//			subParse(builder, newMarker);
//		} else {
//			newMarker.drop();
//		}
//		return LuaElementTypes.ADDITIVE_EXPRESSION;
//	}
//
//}
