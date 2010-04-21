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

package com.sylvanaar.idea.Lua.parser.util;

import com.intellij.psi.tree.IElementType;

/**
 * @author jay
 */
public interface ParserPart {

	/**
	 * Parses some part of BNF tree
	 *
	 * @param builder standard builder to mark AST nodes
	 * @return type of expression that was parsed
	 */
	public IElementType parse(LuaPsiBuilder builder);

}
