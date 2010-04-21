package com.sylvanaar.idea.Lua.parser.parsing.expressions.math;//package com.sylvanaar.idea.Lua.parser.parsing.expressions.math;
//
//import com.sylvanaar.idea.Lua.lexer.LuaTokenTypes;
//import com.sylvanaar.idea.Lua.parser.util.LuaPsiBuilder;
//import com.sylvanaar.idea.Lua.parser.util.LuaParserErrors;
//import com.sylvanaar.idea.Lua.parser.LuaElementTypes;
//import com.sylvanaar.idea.Lua.parser.parsing.expressions.logical.LogicalNotExpression;
//import com.sylvanaar.idea.Lua.parser.parsing.expressions.AssignmentExpression;
//import com.intellij.psi.tree.IElementType;
//import com.intellij.psi.tree.TokenSet;
//import com.intellij.lang.PsiBuilder;
//
///**
// * @author jay
// * @time 16.12.2007 20:50:23
// */
//public class MultiplicativeExpression implements LuaTokenTypes {
//
//	private static TokenSet MULTIPLICATIVE_OPERATORS = TokenSet.create(
//		DIV, MULT
//	);
//
//	public static IElementType parse(LuaPsiBuilder builder) {
//		PsiBuilder.Marker marker = builder.mark();
//		IElementType result = LogicalNotExpression.parse(builder);
//		if (result != LuaElementTypes.EMPTY_INPUT && builder.compareAndEat(MULTIPLICATIVE_OPERATORS)) {
//			result = AssignmentExpression.parseWithoutPriority(builder);
//			if (result == LuaElementTypes.EMPTY_INPUT) {
//				result = LogicalNotExpression.parse(builder);
//			}
//			if (result == LuaElementTypes.EMPTY_INPUT) {
//				builder.error(LuaParserErrors.expected("expression"));
//			}
//			PsiBuilder.Marker newMarker = marker.precede();
//			marker.done(LuaElementTypes.MULTIPLICATIVE_EXPRESSION);
//			result = LuaElementTypes.MULTIPLICATIVE_EXPRESSION;
//			if (builder.compareAndEat(MULTIPLICATIVE_OPERATORS)) {
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
//			result = LogicalNotExpression.parse(builder);
//		}
//		if (result == LuaElementTypes.EMPTY_INPUT) {
//			builder.error(LuaParserErrors.expected("expression"));
//		}
//		PsiBuilder.Marker newMarker = marker.precede();
//		marker.done(LuaElementTypes.MULTIPLICATIVE_EXPRESSION);
//		if (builder.compareAndEat(MULTIPLICATIVE_OPERATORS)) {
//			subParse(builder, newMarker);
//		} else {
//			newMarker.drop();
//		}
//		return LuaElementTypes.MULTIPLICATIVE_EXPRESSION;
//	}
//}
