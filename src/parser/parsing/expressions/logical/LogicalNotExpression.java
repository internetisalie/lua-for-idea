package com.sylvanaar.idea.Lua.parser.parsing.expressions.logical;//package com.sylvanaar.idea.Lua.parser.parsing.expressions.logical;
//
//import com.sylvanaar.idea.Lua.lexer.LuaTokenTypes;
//import com.sylvanaar.idea.Lua.parser.util.LuaPsiBuilder;
//import com.sylvanaar.idea.Lua.parser.LuaElementTypes;
//import com.sylvanaar.idea.Lua.parser.parsing.expressions.AssignmentExpression;
//import com.intellij.psi.tree.IElementType;
//import com.intellij.lang.PsiBuilder;
//
///**
// * @author jay
// * @time 16.12.2007 21:00:36
// */
//public class LogicalNotExpression implements LuaTokenTypes {
//
//	public static IElementType parse(LuaPsiBuilder builder) {
//		PsiBuilder.Marker marker = builder.mark();
//		if (builder.compareAndEat(opNOT)) {
//			parse(builder);
//			marker.done(LuaElementTypes.UNARY_EXPRESSION);
//			return LuaElementTypes.UNARY_EXPRESSION;
//		} else {
//			marker.drop();
//			IElementType result = AssignmentExpression.parseWithoutPriority(builder);
//			if (result == LuaElementTypes.EMPTY_INPUT) {
//				result = InstanceofExpression.parse(builder);
//			}
//			return result;
//		}
//	}
//}
