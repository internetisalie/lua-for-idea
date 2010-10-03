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

package com.sylvanaar.idea.Lua.lang.psi.impl.expressions;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.IncorrectOperationException;
import com.sylvanaar.idea.Lua.lang.parser.LuaElementTypes;
import com.sylvanaar.idea.Lua.lang.psi.LuaFunctionDefinition;
import com.sylvanaar.idea.Lua.lang.psi.LuaPsiElement;
import com.sylvanaar.idea.Lua.lang.psi.LuaPsiType;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaExpression;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaIdentifier;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaParameter;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaReferenceExpression;
import com.sylvanaar.idea.Lua.lang.psi.impl.LuaDeclarationImpl;
import org.jetbrains.annotations.NotNull;

import static com.sylvanaar.idea.Lua.lang.psi.LuaPsiType.VOID;


public class LuaParameterImpl extends LuaDeclarationImpl implements LuaPsiElement, LuaParameter {
    public LuaParameterImpl(@NotNull ASTNode node) {
        super(node);
    }

    public String toString() {
        return "Parameter: " + getText();
    }

    @Override
    public LuaFunctionDefinition getDeclaringFunction() {
        return (LuaFunctionDefinition) getNode().getTreeParent().getTreeParent().getPsi();

    }

    @Override
    public boolean isVarArgs() {
        return (getNode().getElementType() == LuaElementTypes.ELLIPSIS);
    }

    @NotNull
    @Override
    public LuaPsiType getLuaType() {
        return VOID;
    }


    @NotNull
    public SearchScope getUseScope() {
//        if (!isPhysical()) {
//            final PsiFile file = getContainingFile();
//            final PsiElement context = file.getContext();
//            if (context != null) return new LocalSearchScope(context);
//            return super.getUseScope();
//        }

        final PsiElement scope = getDeclarationScope();

        return new LocalSearchScope(scope);
    }

    @NotNull
    public PsiElement getDeclarationScope() {
        return getDeclaringFunction();
    }

//    @Override
//    public PsiElement setName(@NotNull @NonNls String s) throws IncorrectOperationException {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }


    @Override
    public LuaIdentifier getNameSymbol() {
        return this;
    }

    @Override
    public String getDefinedName() {
        return getName();
    }

    @Override
    public LuaReferenceExpression getPrimaryIdentifier() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public LuaExpression getInitializer() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PsiElement getElement() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TextRange getRangeInElement() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PsiElement resolve() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getCanonicalText() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isReferenceTo(PsiElement element) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isSoft() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PsiElement getNameIdentifierLua() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
