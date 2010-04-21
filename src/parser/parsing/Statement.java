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

package com.sylvanaar.idea.Lua.parser.parsing;

import com.intellij.psi.tree.IElementType;
import com.sylvanaar.idea.Lua.lexer.LuaTokenTypes;
import com.sylvanaar.idea.Lua.parser.LuaElementTypes;
import com.sylvanaar.idea.Lua.parser.parsing.expressions.Expression;
import com.sylvanaar.idea.Lua.parser.parsing.functions.Function;
import com.sylvanaar.idea.Lua.parser.parsing.statements.*;
import com.sylvanaar.idea.Lua.parser.util.LuaPsiBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 12.10.2007
 * Time: 11:44:18
 */
public class Statement implements LuaTokenTypes {

/*
stat :  varlist1 '=' explist1 |
	functioncall |
	'do' block 'end' |
	'while' exp 'do' block 'end' |
	'repeat' block 'until' exp |
	'if' exp 'then' block ('elseif' exp 'then' block)* ('else' block)? 'end' |
	'for' NAME '=' exp ',' exp (',' exp)? 'do' block 'end' |
	'for' namelist 'in' explist1 'do' block 'end' |
	'function' funcname funcbody |
	'local' 'function' NAME funcbody |
	'local' namelist ('=' explist1)? ;
 */
	public static IElementType parse(LuaPsiBuilder builder) {

		IElementType result = parseStatementByKeyword(builder);

		if (result == LuaElementTypes.EMPTY_INPUT) {
			result = Expression.parse(builder);
			if (result != LuaElementTypes.EMPTY_INPUT) {
				builder.compareAndEat(SEMI);
			}
		}
		return result;
	}

	private static IElementType parseStatementByKeyword(LuaPsiBuilder builder) {
		if (!builder.compare(LuaElementTypes.STATEMENT_FIRST_TOKENS)) {
			return LuaElementTypes.EMPTY_INPUT;
		}
		if (builder.compare(IF))
			return IfStatement.parse(builder);
		if (builder.compare(WHILE))
			return WhileStatement.parse(builder);
		if (builder.compare(DO))
			return DoBlockStatement.parse(builder);
		if (builder.compare(FOR))
			return ForStatement.parse(builder);
		if (builder.compare(CONTINUE))
			return ContinueStatement.parse(builder);
		if (builder.compare(NAME))
			return AssignmentStatement.parse(builder);
        if (builder.compare(FUNCTION))
            return Function.parse(builder);

        return LuaElementTypes.EMPTY_INPUT;
	}
}
