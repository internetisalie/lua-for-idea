package com.sylvanaar.idea.Lua.parser.parsing.expressions;//package com.sylvanaar.idea.Lua.parser.parsing.expressions;
//
//import com.sylvanaar.idea.Lua.parser.util.LuaPsiBuilder;
//import com.sylvanaar.idea.Lua.parser.util.ParserPart;
//import com.sylvanaar.idea.Lua.parser.util.LuaParserErrors;
//import com.sylvanaar.idea.Lua.parser.util.ListParsingHelper;
//import com.sylvanaar.idea.Lua.parser.LuaElementTypes;
//import com.sylvanaar.idea.Lua.lexer.LuaTokenTypes;
//import com.intellij.psi.tree.IElementType;
//import com.intellij.lang.PsiBuilder;
//
///**
// * @author markov
// * @date 17.10.2007
// */
//public class StaticArrayPairList {
//
//	//	static_array_pair_list:
//	//		/* empty */
//	//		| non_empty_static_array_pair_list possible_comma
//	//	;
//	//
//	//	possible_comma:
//	//		/* empty */
//	//		| ','
//	//	;
//	//
//	//	non_empty_static_array_pair_list:
//	//		non_empty_static_array_pair_list ',' static_scalar opHASH_ARRAY static_scalar
//	//		| non_empty_static_array_pair_list ',' static_scalar
//	//		| static_scalar opHASH_ARRAY static_scalar
//	//		| static_scalar
//	//	;
//	public static IElementType parse(LuaPsiBuilder builder) {
//		ParserPart parser = new ParserPart() {
//
//			public IElementType parse(LuaPsiBuilder builder) {
//				PsiBuilder.Marker staticArrayPair = builder.mark();
//				IElementType result = StaticScalar.parse(builder);
//				if (result != LuaElementTypes.EMPTY_INPUT) {
//					if (builder.compare(LuaTokenTypes.opHASH_ARRAY)) {
//						staticArrayPair.done(LuaElementTypes.ARRAY_KEY);
//						staticArrayPair = staticArrayPair.precede();
//						builder.advanceLexer();
//						if (StaticScalar.parse(builder) == LuaElementTypes.EMPTY_INPUT) {
//							builder.error(LuaParserErrors.expected("static value"));
//						}
//					}
//					staticArrayPair.done(LuaElementTypes.ARRAY_VALUE);
//				} else {
//					staticArrayPair.rollbackTo();
//				}
//				return result;
//			}
//		};
//
//		IElementType result = parser.parse(builder);
//		if (result != LuaElementTypes.EMPTY_INPUT) {
//			ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder,
//				result,
//				parser,
//				true);
//		}
//		return result;
//	}
//}
