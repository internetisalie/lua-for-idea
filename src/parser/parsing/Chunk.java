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

import com.sylvanaar.idea.Lua.lexer.LuaTokenTypes;
import com.sylvanaar.idea.Lua.parser.LuaPsiBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: jon
 * Date: Apr 3, 2010
 * Time: 3:53:37 AM
 */

/*
    chunk : (stat (';')?)* (laststat (';')?)?;
 */
public class Chunk  implements LuaTokenTypes {

	public static void parse(LuaPsiBuilder builder) {
		while (builder.compare(LuaTokenTypes.COMMENT_SET)) {
			builder.advanceLexer();
		}
		StatementList.parse(builder, RETURN, BREAK, END);

        LastStatement.parse(builder);
	}

}
