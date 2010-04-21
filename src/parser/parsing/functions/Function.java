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

package com.sylvanaar.idea.Lua.parser.parsing.functions;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.sylvanaar.idea.Lua.lexer.LuaTokenTypes;
import com.sylvanaar.idea.Lua.parser.LuaElementTypes;
import com.sylvanaar.idea.Lua.parser.parsing.Block;
import com.sylvanaar.idea.Lua.parser.util.LuaParserErrors;
import com.sylvanaar.idea.Lua.parser.util.LuaPsiBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 12.10.2007
 * Time: 11:44:29
 */
public class Function implements LuaTokenTypes {

	//	function_declaration_statement:
	//		kwFUNCTION is_reference IDENTIFIER '(' parameter_list ')'
	//			'{' statement_list '}'
	//	;
	public static IElementType parse(LuaPsiBuilder builder) {
		if (!builder.compare(FUNCTION)) {
			return LuaElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker function = builder.mark();
		builder.advanceLexer();
		
		if (!builder.compareAndEat(NAME)) {
            // todo anonymous func
			builder.error(LuaParserErrors.expected("function name"));
		}
		builder.match(LPAREN);
		ParameterList.parse(builder);
		builder.match(RPAREN);

		Block.parse(builder);

		builder.match(END);

        function.done(LuaElementTypes.FUNCTION);
		return LuaElementTypes.FUNCTION;
	}
}
