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
// * Date: 15.12.2007
// */
//public class AndExpression implements LuaTokenTypes {
//
//	public static IElementType parse(LuaPsiBuilder builder) {
//		PsiBuilder.Marker marker = builder.mark();
//		IElementType result = PrintExpression.parse(builder);
//		if (result != LuaElementTypes.EMPTY_INPUT) {
//			if (builder.compareAndEat(opAND)) {
//				result = AssignmentExpression.parseWithoutPriority(builder);
//				if (result == LuaElementTypes.EMPTY_INPUT) {
//					result = PrintExpression.parse(builder);
//				}
//				if (result == LuaElementTypes.EMPTY_INPUT) {
//					builder.error(LuaParserErrors.expected("expression"));
//				}
//				PsiBuilder.Marker newMarker = marker.precede();
//				marker.done(LuaElementTypes.LOGICAL_EXPRESSION);
//				result = LuaElementTypes.LOGICAL_EXPRESSION;
//				if (builder.compareAndEat(opAND)) {
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
//		IElementType result = AssignmentExpression.parseWithoutPriority(builder);
//		if (result == LuaElementTypes.EMPTY_INPUT) {
//			result = PrintExpression.parse(builder);
//		}
//		if (result == LuaElementTypes.EMPTY_INPUT) {
//			builder.error(LuaParserErrors.expected("expression"));
//		}
//		PsiBuilder.Marker newMarker = marker.precede();
//		marker.done(LuaElementTypes.LOGICAL_EXPRESSION);
//		if (builder.compareAndEat(opAND)) {
//			subParse(builder, newMarker);
//		} else {
//			newMarker.drop();
//		}
//		return LuaElementTypes.LOGICAL_EXPRESSION;
//	}
//}
