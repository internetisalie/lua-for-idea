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
import com.sylvanaar.idea.Lua.parser.LuaPsiBuilder;
import com.sylvanaar.idea.Lua.parser.parsing.functions.Function;

/**
 * Created by IntelliJ IDEA.
 * User: Jon S Akhtar
 * Date: Apr 21, 2010
 * Time: 6:22:56 PM
 */
public class LocalScoped implements LuaTokenTypes {

    public static IElementType parse(LuaPsiBuilder builder) {
        PsiBuilder.Marker statement = builder.mark();
		if (!builder.compareAndEat(LOCAL)) {
			statement.drop();
			return LuaElementTypes.EMPTY_INPUT;
		}

        if (builder.getTokenType() == FUNCTION)
            Function.parse(builder);
        else
            AssignmentStatement.parse(builder);

        statement.done(LuaElementTypes.LOCAL_SCOPED);

        return LuaElementTypes.LOCAL_SCOPED;
    }
    
}
