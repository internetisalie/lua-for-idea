package com.sylvanaar.idea.Lua.parser.parsing.expressions;//package com.sylvanaar.idea.Lua.parser.parsing.expressions;
//
//import com.intellij.psi.tree.IElementType;
//import com.intellij.psi.tree.TokenSet;
//import com.intellij.lang.PsiBuilder;
//import com.sylvanaar.idea.Lua.lexer.LuaTokenTypes;
//import com.sylvanaar.idea.Lua.parser.LuaElementTypes;
//import com.sylvanaar.idea.Lua.parser.parsing.classes.StaticClassConstant;
//import com.sylvanaar.idea.Lua.parser.util.LuaPsiBuilder;
//
///**
// * @author markov
// * @date 17.10.2007
// */
//public class StaticScalar implements LuaTokenTypes {
//
//	//	static_scalar: /* compile-time evaluated scalars */
//	//		common_scalar
//	//		| IDENTIFIER
//	//		| opPLUS static_scalar
//	//		| opMINUS static_scalar
//	//		| kwARRAY '(' static_array_pair_list ')'
//	//		| static_class_constant
//	//	;
//	public static IElementType parse(LuaPsiBuilder builder) {
//		PsiBuilder.Marker staticScalar = builder.mark();
//		IElementType result = StaticClassConstant.parse(builder);
//		if (result == LuaElementTypes.EMPTY_INPUT) {
//			if (builder.compareAndEat(opPLUS) || builder.compareAndEat(opMINUS)) {
//				parse(builder);
//				staticScalar.done(LuaElementTypes.STATIC_SCALAR);
//				result = LuaElementTypes.STATIC_SCALAR;
//			} else if (builder.compareAndEat(kwARRAY)) {
//				builder.match(chLPAREN);
//				StaticArrayPairList.parse(builder);
//				builder.match(chRPAREN);
//				staticScalar.done(LuaElementTypes.ARRAY);
//				result = LuaElementTypes.ARRAY;
//			} else if (builder.compareAndEat(IDENTIFIER)) {
//				staticScalar.done(IDENTIFIER);
//				result = LuaElementTypes.CONSTANT;
//			} else {
//				result = parseCommonScalar(builder);
//				if (result != LuaElementTypes.EMPTY_INPUT) {
//					staticScalar.drop();
//				}
//			}
//		}
//		if (result == LuaElementTypes.EMPTY_INPUT) {
//			staticScalar.rollbackTo();
//		}
//		return result;
//	}
//
//	//	common_scalar:
//	//		INTEGER_LITERAL
//	//		| FLOAT_LITERAL
//	//		| STRING_LITERAL
//	//		| CONST_LINE
//	//		| CONST_FILE
//	//		| CONST_CLASS
//	//		| CONST_METHOD
//	//		| CONST_FUNCTION
//	//	;
//	/**
//	 * We can use STRING_LITERAL here because it means
//	 * that the string has no variables in it
//	 *
//	 * @param builder current PsiBuilder wrapper
//	 * @return EMPTY_INPUT on empty input, COMMON_SCALAR on success
//	 */
//	public static IElementType parseCommonScalar(LuaPsiBuilder builder) {
//		if (builder.compare(tsCOMMON_SCALARS)) {
//			PsiBuilder.Marker scalar = builder.mark();
//			IElementType type = builder.getTokenType();
//			builder.advanceLexer();
//			if (TokenSet.create(INTEGER_LITERAL, FLOAT_LITERAL).contains(type)) {
//				scalar.done(LuaElementTypes.NUMBER);
//			} else if (TokenSet.create(STRING_LITERAL, STRING_LITERAL_SINGLE_QUOTE).contains(type)) {
//				scalar.done(LuaElementTypes.STRING);
//			} else {
//				scalar.done(LuaElementTypes.CONSTANT);
//			}
//			return LuaElementTypes.COMMON_SCALAR;
//		}
//		return LuaElementTypes.EMPTY_INPUT;
//	}
//
//}
//
