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

package com.sylvanaar.idea.Lua.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.sylvanaar.idea.Lua.lang.psi.LuaSyntaxLevel;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaIdentifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jon S Akhtar
 * Date: Oct 16, 2010
 * Time: 9:27:37 PM
 */
public class LuaSyntaxLevelImpl extends LuaPsiElementImpl implements LuaSyntaxLevel {    
    public LuaSyntaxLevelImpl(ASTNode node) {
        super(node);
    }

    List<LuaIdentifier> declarations = new ArrayList<LuaIdentifier>();

    @Override
    public LuaIdentifier[] getDeclarations() {
        return declarations.toArray(new LuaIdentifier[declarations.size()]);
    }

    @Override
    public void addDeclaration(LuaIdentifier identifier) {
        declarations.add(identifier);
    }
}
