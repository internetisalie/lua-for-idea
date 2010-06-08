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

package com.sylvanaar.idea.Lua.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.sylvanaar.idea.Lua.parser.LuaElementTypes;
import com.sylvanaar.idea.Lua.psi.LuaIdentifier;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

/**
 * Created by IntelliJ IDEA.
 * User: Jon S Akhtar
 * Date: Apr 11, 2010
 * Time: 2:33:37 PM
 */
public class LuaIdentifierImpl extends LuaPsiElementImpl implements LuaIdentifier {
    boolean global = false;
    boolean local = false;
    
    public LuaIdentifierImpl(ASTNode node) {
        super(node);

        global = node.getElementType() == LuaElementTypes.GLOBAL_NAME;
        local = node.getElementType() == LuaElementTypes.LOCAL_NAME;
    }

    @Nullable
    @NonNls
    public String getName() {
      return getText();
    }

    @Override
    public String toString() {
        return super.toString() + " (" + getText() + ")";
    }

    @Override
    public PsiElement setName(@NonNls String name) throws IncorrectOperationException {
        return null;
    }

    @Override
    public boolean isGlobal() {
        return global;
    }

    @Override
    public boolean isLocal() {
        return local;
    }
}
