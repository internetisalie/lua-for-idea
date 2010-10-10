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

package com.sylvanaar.idea.Lua.lang.psi.impl.statements;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.sylvanaar.idea.Lua.lang.parser.LuaElementTypes;
import com.sylvanaar.idea.Lua.lang.psi.controlFlow.Instruction;
import com.sylvanaar.idea.Lua.lang.psi.controlFlow.impl.ControlFlowBuilder;
import com.sylvanaar.idea.Lua.lang.psi.impl.LuaPsiElementImpl;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaBlock;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaLastStatementElement;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaStatementElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Jon S Akhtar
 * Date: Jun 12, 2010
 * Time: 10:17:49 PM
 */
public class LuaBlockImpl extends LuaPsiElementImpl implements LuaBlock {
    private Instruction[] myControlFlow = new Instruction[0];

    public LuaBlockImpl(ASTNode node) {
        super(node);
    }


    public LuaStatementElement[] getStatements() {


        return findChildrenByClass(LuaStatementElement.class);
    }

    @Override
    public LuaLastStatementElement getLastStatement() {
        return (LuaLastStatementElement) findChildByType(LuaElementTypes.LAST_STATEMENT);
    }

    @Override
    public void addStatementBefore(LuaStatementElement statement, Object o) {
        // TODO
    }

    public Instruction[] getControlFlow() {
        if (myControlFlow == null) {
            myControlFlow = new ControlFlowBuilder(getProject()).buildControlFlow(this, null, null);
        }

        return myControlFlow;
    }


    public boolean processDeclarations(@NotNull PsiScopeProcessor processor,
                                       @NotNull ResolveState resolveState,
                                       PsiElement lastParent,
                                       @NotNull PsiElement place) {

        if (lastParent != null && lastParent.getParent() == this) {
            final PsiElement[] children = getChildren();
            for (PsiElement child : children) {
                if (child == lastParent) break;
                if (!child.processDeclarations(processor, resolveState, lastParent, place)) return false;
            }
        }

        return true;
    }

//    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
//        if (lastParent != null && lastParent.getParent() == this) {
//
//        }
////        return ResolveUtil.processChildren(this, processor, state, lastParent, place);
//    }

}
