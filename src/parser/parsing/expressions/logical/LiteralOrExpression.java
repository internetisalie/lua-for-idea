package com.sylvanaar.idea.Lua.parser.parsing.expressions.logical;//package com.sylvanaar.idea.Lua.parser.parsing.expressions.logical;
//
//import com.sylvanaar.idea.Lua.lexer.LuaTokenTypes;
//import com.sylvanaar.idea.Lua.parser.LuaPsiBuilder;
//import com.sylvanaar.idea.Lua.parser.util.LuaParserErrors;
//import com.sylvanaar.idea.Lua.parser.LuaElementTypes;
//import com.intellij.psi.tree.IElementType;
//import com.intellij.lang.PsiBuilder;
//
///**
// * Created by IntelliJ IDEA.
// * User: markov
// * Date: 14.12.2007
// */
//public class LiteralOrExpression implements LuaTokenTypes {
//
//	public static IElementType parse(LuaPsiBuilder builder) {
//		PsiBuilder.Marker marker = builder.mark();
//		IElementType result = LiteralXorExpression.parse(builder);
//		if (result != LuaElementTypes.EMPTY_INPUT) {
//			if (builder.compareAndEat(opLIT_OR)) {
//				result = LiteralXorExpression.parse(builder);
//				if (result == LuaElementTypes.EMPTY_INPUT) {
//					builder.error(LuaParserErrors.expected("expression"));
//				}
//				PsiBuilder.Marker newMarker = marker.precede();
//				marker.done(LuaElementTypes.LITERAL_LOGICAL_EXPRESSION);
//				result = LuaElementTypes.LITERAL_LOGICAL_EXPRESSION;
//				if (builder.compareAndEat(opLIT_OR)) {
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
//		if (LiteralXorExpression.parse(builder) == LuaElementTypes.EMPTY_INPUT) {
//			builder.error(LuaParserErrors.expected("expression"));
//		}
//		PsiBuilder.Marker newMarker = marker.precede();
//		marker.done(LuaElementTypes.LITERAL_LOGICAL_EXPRESSION);
//		if (builder.compareAndEat(opLIT_OR)) {
//			subParse(builder, newMarker);
//		} else {
//			newMarker.drop();
//		}
//		return LuaElementTypes.LITERAL_LOGICAL_EXPRESSION;
//	}
//
//}
