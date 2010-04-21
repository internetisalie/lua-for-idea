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

package com.sylvanaar.idea.Lua.parser.parsing.statements;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.sylvanaar.idea.Lua.lexer.LuaTokenTypes;
import com.sylvanaar.idea.Lua.parser.LuaElementTypes;
import com.sylvanaar.idea.Lua.parser.parsing.calls.Variable;
import com.sylvanaar.idea.Lua.parser.util.ListParsingHelper;
import com.sylvanaar.idea.Lua.parser.util.LuaPsiBuilder;
import com.sylvanaar.idea.Lua.parser.util.ParserPart;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 04.11.2007
 */
public class LocalStatement implements LuaTokenTypes {

	//	kwGLOBAL global_var_list ';'
	public static IElementType parse(LuaPsiBuilder builder) {
		if (!builder.compare(LOCAL)) {
			return LuaElementTypes.EMPTY_INPUT;
		}
		PsiBuilder.Marker statement = builder.mark();
		parseLocallVarList(builder);

		statement.done(LuaElementTypes.LOCAL);
		return LuaElementTypes.LOCAL;
	}

	//	global_var_list:
	//		global_var_list ',' global_var
	//		| global_var
	//	;
	//
	//	global_var:
	//		VARIABLE
	//		| '$' variable //read
	//		| '$' '{' expr '}'
	//	;
	private static void parseLocallVarList(LuaPsiBuilder builder) {
		ParserPart localVariable = new ParserPart() {
			public IElementType parse(LuaPsiBuilder builder) {

				PsiBuilder.Marker variable = builder.mark();
				Variable.parse(builder);
				variable.done(LuaElementTypes.IDENTIFIER_EXPR);
				return LuaElementTypes.IDENTIFIER_EXPR;
			}
		};
		ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder,
			localVariable.parse(builder),
			localVariable,
			false);
	}
}
