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

package com.sylvanaar.idea.Lua.parser.parsing.calls;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.sylvanaar.idea.Lua.lexer.LuaTokenTypes;
import com.sylvanaar.idea.Lua.parser.LuaElementTypes;
import com.sylvanaar.idea.Lua.parser.LuaPsiBuilder;
import com.sylvanaar.idea.Lua.parser.parsing.expressions.Expression;
import com.sylvanaar.idea.Lua.parser.util.ListParsingHelper;
import com.sylvanaar.idea.Lua.parser.util.ParserPart;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 16.11.2007
 */
public class Function implements LuaTokenTypes {

	//	function_call:
	//		IDENTIFIER '(' function_call_parameter_list ')'
	//		| fully_qualified_class_name SCOPE_RESOLUTION IDENTIFIER '(' function_call_parameter_list ')'
	//		| fully_qualified_class_name SCOPE_RESOLUTION variable_without_objects '('
	//			function_call_parameter_list ')'
	//		| variable_without_objects '(' function_call_parameter_list ')'
	//	;
	public static IElementType parse(LuaPsiBuilder builder) {
		PsiBuilder.Marker variable = builder.mark();
		IElementType result = Variable.parseVariableWithoutObjects(builder);
		if (result != LuaElementTypes.EMPTY_INPUT) {
			variable.done(result);
			parseFunctionCallParameterList(builder);
			return LuaElementTypes.FUNCTION_CALL;
		}
		variable.drop();
		if (builder.compare(NAME)) {
			PsiBuilder.Marker rollback = builder.mark();
			builder.advanceLexer();
			if (builder.compare(LPAREN)) {
				rollback.drop();
				parseFunctionCallParameterList(builder);
			} else if (builder.compare(COLON)) {
				rollback.rollbackTo();
			//	ClassReference.parse(builder);
				builder.match(COLON);
				if (builder.compareAndEat(NAME)) {
					if (builder.compare(LPAREN)) {
						parseFunctionCallParameterList(builder);
					} else {
						rollback.rollbackTo();
						return LuaElementTypes.EMPTY_INPUT;
					}
				} else {
					variable = builder.mark();
					result = Variable.parseVariableWithoutObjects(builder);
					variable.done(result);
					parseFunctionCallParameterList(builder);
				}
			} else {
				rollback.rollbackTo();
				return LuaElementTypes.EMPTY_INPUT;
			}
			return LuaElementTypes.FUNCTION_CALL;
		}
		return LuaElementTypes.EMPTY_INPUT;
	}

	//	function_call_parameter_list:
	//		non_empty_function_call_parameter_list
	//		| /* empty */
	//	;
	//
	//	non_empty_function_call_parameter_list:
	//		expr_without_variable
	//		| variable
	//		| '&' variable //write
	//		| non_empty_function_call_parameter_list ',' expr_without_variable
	//		| non_empty_function_call_parameter_list ',' variable
	//		| non_empty_function_call_parameter_list ',' '&' variable //write
	//	;
	public static void parseFunctionCallParameterList(LuaPsiBuilder builder) {
		builder.match(LPAREN);

		ParserPart functionParameter = new ParserPart() {
			public IElementType parse(LuaPsiBuilder builder) {
				return Expression.parse(builder);
			}
		};

		PsiBuilder.Marker paramList = builder.mark();
		ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder,
			functionParameter.parse(builder),
			functionParameter,
			false);
		paramList.done(LuaElementTypes.PARAMETER_LIST);

		builder.match(RPAREN);
	}


}
