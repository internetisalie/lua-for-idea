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
import com.sylvanaar.idea.Lua.parser.parsing.Block;
import com.sylvanaar.idea.Lua.parser.util.LuaPsiBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 01.11.2007
 */
public class DoBlockStatement implements LuaTokenTypes {

	//		'do' block 'end'
	public static IElementType parse(LuaPsiBuilder builder) {
		PsiBuilder.Marker statement = builder.mark();
		if (!builder.compareAndEat(DO)) {
			statement.drop();
			return LuaElementTypes.EMPTY_INPUT;
		}

        Block.parse(builder);

		builder.match(END);
        
		statement.done(LuaElementTypes.BLOCK);

        return LuaElementTypes.BLOCK;
	}
}
