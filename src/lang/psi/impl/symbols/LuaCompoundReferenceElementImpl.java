/*
 * Copyright 2011 Jon S Akhtar (Sylvanaar)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sylvanaar.idea.Lua.lang.psi.impl.symbols;

import com.intellij.lang.*;
import com.intellij.openapi.util.*;
import com.intellij.openapi.util.text.*;
import com.intellij.psi.*;
import com.intellij.psi.scope.*;
import com.intellij.util.*;
import com.sylvanaar.idea.Lua.lang.parser.*;
import com.sylvanaar.idea.Lua.lang.psi.*;
import com.sylvanaar.idea.Lua.lang.psi.expressions.*;
import com.sylvanaar.idea.Lua.lang.psi.impl.expressions.*;
import com.sylvanaar.idea.Lua.lang.psi.symbols.*;
import com.sylvanaar.idea.Lua.lang.psi.util.*;
import com.sylvanaar.idea.Lua.lang.psi.visitor.*;
import org.jetbrains.annotations.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jon S Akhtar
 * Date: 2/5/11
 * Time: 12:35 PM
 */
public class LuaCompoundReferenceElementImpl extends LuaReferenceElementImpl implements LuaReferenceElement, LuaExpression {

    public LuaCompoundReferenceElementImpl(ASTNode node) {
        super(node);



    }

    @NotNull
    @Override
    public Object[] getVariants() {
        final PsiElement element = getElement();
        assert element instanceof LuaCompoundIdentifier;
        if (((LuaCompoundIdentifier) element).isCompoundDeclaration())
            return ArrayUtil.EMPTY_OBJECT_ARRAY;

        return super.getVariants();
    }

    @Override
    public void accept(LuaElementVisitor visitor) {
        visitor.visitCompoundReference(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof LuaElementVisitor) {
            ((LuaElementVisitor) visitor).visitCompoundReference(this);
        } else {
            visitor.visitElement(this);
        }
    }

    @Override
    public boolean isSameKind(LuaSymbol symbol) {
        return symbol instanceof LuaCompoundIdentifier;// || symbol instanceof LuaFieldIdentifier;
    }

    public PsiElement getNameElement() {
//        return ((LuaCompoundIdentifier)getElement()).getRightSymbol();
        return getElement();
    }

    @Override
    public PsiElement getElement() {
        return  findChildByType(LuaElementTypes.GETTABLE);
    }

    public PsiReference getReference() {
        final LuaExpression rightSymbol = (LuaExpression) getNameElement();
        if (rightSymbol instanceof LuaStringLiteralExpressionImpl) {
            final TextRange textRange =
                    ((LuaStringLiteralExpressionImpl) rightSymbol).getStringContentTextRange();
            if (textRange != null)
                return new PsiReferenceBase.Immediate<PsiElement>(rightSymbol,
                                    textRange.shiftRight(getTextOffset()), rightSymbol);
        }

        return this;
    }
//    public TextRange getRangeInElement() {
//        final PsiElement nameElement = ((LuaCompoundIdentifier)getElement()).getRightSymbol();
//        int nameLen = nameElement != null ? nameElement.getTextLength() : 0;
//
//        final int textOffset = nameElement != null ? nameElement.getTextOffset() : 0;
//
//        return TextRange.from(textOffset - getTextOffset(), nameLen);
//    }

//    @Override
//    @NotNull
//    public TextRange getRangeInElement() {
//        LuaExpression e = ((LuaCompoundIdentifier)getElement()).getRightSymbol();
//        if (e != null)
//            return TextRange.from(e.getTextOffset() - getTextOffset(), e.getTextLength());
//
//        return TextRange.EMPTY_RANGE;
//    }


    @Override
    public String toString() {
        return "Compound Reference: " + getText();
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state,
                                       PsiElement lastParent, @NotNull PsiElement place) {
        return LuaPsiUtils.processChildDeclarations(this, processor, state, lastParent, place);
    }

    @NotNull
    public String getCanonicalText() {
        LuaCompoundIdentifier element = (LuaCompoundIdentifier) getElement();


        final PsiElement scopeIdentifier = element.getScopeIdentifier();
        
        if (scopeIdentifier instanceof LuaGlobal) {
            final String moduleName = ((LuaGlobal) scopeIdentifier).getModuleName();

            if (scopeIdentifier.equals("_M"))
                return moduleName + element.getDefinedName().substring(3);
            if (StringUtil.isNotEmpty(moduleName))
                return moduleName + "." + element.getDefinedName();
        }

        return element.getDefinedName();
    }

    @Override
    public boolean checkSelfReference(PsiElement element) {
        return element instanceof LuaCompoundIdentifier && ((LuaCompoundIdentifier) element).isCompoundDeclaration();
    }
}
