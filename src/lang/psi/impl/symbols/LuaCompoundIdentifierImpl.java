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
import com.intellij.openapi.application.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.util.*;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.*;
import com.intellij.psi.scope.*;
import com.intellij.psi.stubs.*;
import com.intellij.util.*;
import com.sylvanaar.idea.Lua.lang.parser.*;
import com.sylvanaar.idea.Lua.lang.psi.*;
import com.sylvanaar.idea.Lua.lang.psi.expressions.*;
import com.sylvanaar.idea.Lua.lang.psi.impl.*;
import com.sylvanaar.idea.Lua.lang.psi.impl.expressions.*;
import com.sylvanaar.idea.Lua.lang.psi.lists.*;
import com.sylvanaar.idea.Lua.lang.psi.statements.*;
import com.sylvanaar.idea.Lua.lang.psi.stubs.*;
import com.sylvanaar.idea.Lua.lang.psi.stubs.api.*;
import com.sylvanaar.idea.Lua.lang.psi.symbols.*;
import com.sylvanaar.idea.Lua.lang.psi.types.*;
import com.sylvanaar.idea.Lua.lang.psi.util.*;
import com.sylvanaar.idea.Lua.lang.psi.visitor.*;
import org.jetbrains.annotations.*;

import java.lang.ref.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jon S Akhtar
 * Date: 1/20/11
 * Time: 3:44 AM
 */
public class LuaCompoundIdentifierImpl extends LuaStubElementBase<LuaCompoundIdentifierStub>
        implements LuaCompoundIdentifier {

    public LuaCompoundIdentifierImpl(ASTNode node) {
        super(node);
    }

    @Override
    public PsiElement getParent() {
         return SharedImplUtil.getParent(getNode());
    }

    public LuaCompoundIdentifierImpl(LuaCompoundIdentifierStub stub) {
        this(stub, LuaElementTypes.GETTABLE);
    }
    public LuaCompoundIdentifierImpl(LuaCompoundIdentifierStub stub, IStubElementType type) {
        super(stub, type);
        myType = LuaStubUtils.GetStubOrPrimativeType(stub, this);
    }

    /** Defined Value Implementation **/
    SoftReference<LuaExpression> definedValue = null;
    @Override
    public LuaExpression getAssignedValue() {
        return definedValue == null ? null : definedValue.get();
    }

    @Override
    public void setAssignedValue(LuaExpression value) {
        definedValue = new SoftReference<LuaExpression>(value);
        setLuaType(value.getLuaType());
    }
    /** Defined Value Implementation **/

    @Override
    public void accept(LuaElementVisitor visitor) {
        visitor.visitCompoundIdentifier(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof LuaElementVisitor) {
            ((LuaElementVisitor) visitor).visitCompoundIdentifier(this);
        } else {
            visitor.visitElement(this);
        }
    }

    @Nullable
    public LuaExpression getRightSymbol() {
        LuaExpression[] e = findChildrenByClass(LuaExpression.class);
        return e.length>1?e[1]:null;
    }

    @Nullable
    public LuaExpression getLeftSymbol() {
        LuaExpression[] e = findChildrenByClass(LuaExpression.class);
        return e.length>0?e[0]:null;
    }

//    @Override
//    public int getStartOffsetInParent() {
//        return getLeftSymbol().getTextLength();
//    }

    private String asString(LuaExpression e) {
//        Object eval = e.evaluate();
//        if (eval == null) return "{"+e.getText()+"}";
//
//        return eval.toString();

        return e.getText();
    }
    
    @Nullable
    @Override
    public String toString() {
        try {
        return "GetTable: " +  asString(getLeftSymbol()) + getOperator() + asString(getRightSymbol()) + (getOperator() == "[" ? "]" : "");
        } catch (Throwable t) { return "err"; }
    }

    @Override
    public String getOperator() {
        try {
        return findChildByType(LuaElementTypes.TABLE_ACCESS).getText();
        } catch (Throwable t) { return "err"; }
    }

    @Override
    public LuaCompoundIdentifier getEnclosingIdentifier() {
        LuaPsiElement e = (LuaPsiElement) getParentByStub();
        if (e instanceof LuaCompoundIdentifier)
            return (LuaCompoundIdentifier) e;

        final PsiElement parent = getParent();

        final PsiReference reference = parent instanceof PsiReference ? (PsiReference) parent : null;

        if (reference == null) return null;

        if (parent.getParent() instanceof LuaCompoundIdentifier)
            return (LuaCompoundIdentifier) parent.getParent();

        return this;
    }

    @Override
    public boolean isCompoundDeclaration() {
        final LuaCompoundIdentifierStub stub = getStub();
        if (stub != null)
            return stub.isGlobalDeclaration();

        PsiElement e = getParent().getParent();
        return e instanceof LuaIdentifierList || e instanceof LuaFunctionDefinition;
    }


    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor,
                                       @NotNull ResolveState state, PsiElement lastParent,
                                       @NotNull PsiElement place) {
        //LuaPsiUtils.processChildDeclarations(this, processor, state, lastParent, place);
        if (isCompoundDeclaration()) {
            if (!processor.execute(this,state)) return false;
//            if (!processor.execute(getRightSymbol(), state)) return false;
        }

        return true;
    }

    @Override
    public PsiElement getScopeIdentifier() {
        PsiElement child = getFirstChild();

        if (child instanceof LuaCompoundReferenceElementImpl)
            child = ((LuaCompoundReferenceElementImpl) child).getElement();

        if (child instanceof LuaCompoundIdentifier)
            return ((LuaCompoundIdentifier) child).getScopeIdentifier();

        if (child instanceof LuaReferenceElement)
            return ((LuaReferenceElement) child).getElement();

        return null;
    }

    @Override
    public boolean isSameKind(LuaSymbol symbol) {
        return symbol instanceof LuaCompoundIdentifier || symbol instanceof LuaDeclarationExpression;
    }

//    @Override
//    public PsiReference getReference() {
//        if (getStub() != null) return null;
//        return (PsiReference) getParent();
//    }

//    @Override
//    public int getStartOffsetInParent() {
//        return getTextOffset()-getRightSymbol().getTextOffset();
//    }

    @Override
    public boolean isAssignedTo() {
        // This should return true if this variable is being assigned to in the current statement
        // it will be used for example by the global identifier class to decide if it should resolve
        // as a declaration or not

        PsiElement parent = getParent();
        while (!(parent instanceof LuaStatementElement)) {
            parent = parent.getParent();
        }

        if (parent instanceof LuaAssignmentStatement) {
            LuaAssignmentStatement s = (LuaAssignmentStatement)parent;

            for (LuaSymbol e : s.getLeftExprs().getSymbols())
                if (e == getParent().getParent())
                    return true;
        }
        else if (parent instanceof LuaFunctionDefinitionStatement) {
            LuaFunctionDefinitionStatement s = (LuaFunctionDefinitionStatement)parent;

            if (s.getIdentifier() == getParent().getParent())
                return true;
        }


        return false;
    }

    public boolean isIdentifier(final String name, final Project project) {
        return LuaPsiElementFactory.getInstance(project).createReferenceNameFromText(name) != null;
    }


    NotNullLazyValue<String> name = new NameLazyValue();

    @Override
    public String getDefinedName() {
        final LuaCompoundIdentifierStub stub = getStub();
        if (stub != null) {
            return stub.getName();
        }

        return name.getValue();
    }

   @Override
    public String getName() {
        final LuaCompoundIdentifierStub stub = getStub();
        if (stub != null) {
            return stub.getName();
        }

        return name.getValue();
    }

    LuaType myType = LuaType.ANY;

    @Override
    public void setLuaType(LuaType type) {
        myType = type;

        LuaExpression l = getLeftSymbol();
        if (l == null) return;
        LuaType t = l.getLuaType();

        LuaExpression r = getRightSymbol();

        if (r == null) return;

        Object field = null;
        if (r instanceof LuaFieldIdentifier)
            field = r.getText();
        else if (r instanceof LuaLiteralExpression)
            field = ((LuaLiteralExpression) r).getValue();

        if (t instanceof LuaTable && field != null) {
            ((LuaTable) t).addPossibleElement(field, type);

             r.setLuaType(type);
        }
    }

    @Override
    public PsiElement replaceWithExpression(LuaExpression newExpr, boolean removeUnnecessaryParentheses) {
        return LuaPsiUtils.replaceElement(this, newExpr);
    }


    @NotNull
    @Override
    public LuaType getLuaType() {
        if (myType instanceof StubType)
            myType = ((StubType) myType).get();

        if (myType instanceof LuaTypeSet)
            if (((LuaTypeSet) myType).getTypeSet().size() == 1)
                myType = ((LuaTypeSet) myType).getTypeSet().iterator().next();


        return myType;
    }


    @Override
    public Object evaluate() {
        return null;
    }

    @Override
    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
        return LuaPsiUtils.replaceElement(this, LuaPsiElementFactory.getInstance(getProject()).createIdentifier(name));
    }

    public PsiElement getNameIdentifier() {
        return this;
    }

    private class NameLazyValue extends NotNullLazyValue<String> {
        @NotNull
        @Override
        protected String compute() {
            ApplicationManager.getApplication().assertReadAccessAllowed();

            LuaExpression rhs = getRightSymbol();
            if (rhs instanceof LuaStringLiteralExpressionImpl) {
                String s = (String) ((LuaStringLiteralExpressionImpl) rhs).getValue();
                if (getOperator().equals("[") && isIdentifier(s, getProject())) {

                    final LuaExpression lhs = getLeftSymbol();
                    if (lhs instanceof LuaNamedElement) {
                        return lhs.getName() + "." + s;
                    }
                }
            }

            LuaExpression lhs = getLeftSymbol();

            String text = getText();
            if (lhs == null || !(lhs instanceof LuaSymbol)) return text;

            int leftLen = lhs.getTextLength();
            return ((LuaSymbol) lhs).getName() + text.substring(leftLen);
        }
    }
}
