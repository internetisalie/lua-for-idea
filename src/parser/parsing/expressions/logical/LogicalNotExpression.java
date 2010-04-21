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

package com.sylvanaar.idea.Lua.parser.parsing.expressions.logical;//package com.sylvanaar.idea.Lua.parser.parsing.expressions.logical;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.sylvanaar.idea.Lua.lexer.LuaTokenTypes;
import com.sylvanaar.idea.Lua.parser.LuaElementTypes;

import com.sylvanaar.idea.Lua.parser.util.LuaPsiBuilder;

/**
 * @author jay
 * @time 16.12.2007 21:00:36
 */
public class LogicalNotExpression implements LuaTokenTypes {

	public static IElementType parse(LuaPsiBuilder builder) {
		PsiBuilder.Marker marker = builder.mark();
		if (builder.compareAndEat(NOT)) {
			parse(builder);
			marker.done(LuaElementTypes.UNARY_EXPRESSION);
			return LuaElementTypes.UNARY_EXPRESSION;
		} else {
			marker.drop();
			IElementType result = null;//AssignmentExpression.parseWithoutPriority(builder);

			return LuaElementTypes.EMPTY_INPUT;
		}
	}
}
