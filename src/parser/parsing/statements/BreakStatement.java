package com.sylvanaar.idea.Lua.parser.parsing.statements;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.sylvanaar.idea.Lua.lexer.LuaTokenTypes;
import com.sylvanaar.idea.Lua.parser.LuaElementTypes;
import com.sylvanaar.idea.Lua.parser.util.LuaPsiBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 03.11.2007
 */
public class BreakStatement implements LuaTokenTypes {

	//	kwBREAK ';'
	//	| kwBREAK expr ';'
	public static IElementType parse(LuaPsiBuilder builder) {
		if (!builder.compare(BREAK)) {
			return LuaElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker statement = builder.mark();
		builder.advanceLexer();
//		if (!builder.compareAndEat(opSEMICOLON)) {
//			Expression.parse(builder);
//			builder.match(opSEMICOLON);
//		}
		statement.done(LuaElementTypes.BREAK);
		return LuaElementTypes.BREAK;
	}
}
