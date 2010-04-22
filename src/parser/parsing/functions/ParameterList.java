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
import com.sylvanaar.idea.Lua.parser.LuaPsiBuilder;
import com.sylvanaar.idea.Lua.parser.util.ListParsingHelper;
import com.sylvanaar.idea.Lua.parser.util.ParserPart;

/**
 * @author markov
 * @date 14.10.2007
 */
public class ParameterList implements LuaTokenTypes {

	public static IElementType parse(LuaPsiBuilder builder) {
		PsiBuilder.Marker parameterList = builder.mark();
		ParserPart parameterParser = new Parameter();
		int result = ListParsingHelper.parseCommaDelimitedExpressionWithLeadExpr(builder, parameterParser.parse(builder), parameterParser, false);
		parameterList.done(LuaElementTypes.PARAMETER_LIST);
		return (result > 0) ? LuaElementTypes.PARAMETER_LIST : LuaElementTypes.EMPTY_INPUT;
	}


	private static class Parameter implements ParserPart {

		public IElementType parse(LuaPsiBuilder builder) {
			PsiBuilder.Marker parameter = builder.mark();

			if (!builder.compareAndEat(NAME)) {
				parameter.rollbackTo();
				return LuaElementTypes.EMPTY_INPUT;
			}
            
			parameter.done(LuaElementTypes.PARAMETER);
			return LuaElementTypes.PARAMETER;
		}
	}

}
