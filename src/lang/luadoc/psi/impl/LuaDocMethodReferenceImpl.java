/*
 * Copyright 2000-2009 JetBrains s.r.o.
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

package com.sylvanaar.idea.Lua.lang.luadoc.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Comparing;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PropertyUtil;
import com.intellij.util.IncorrectOperationException;
import com.sylvanaar.idea.Lua.lang.luadoc.psi.api.LuaDocMethodParams;
import com.sylvanaar.idea.Lua.lang.luadoc.psi.api.LuaDocMethodReference;
import com.sylvanaar.idea.Lua.lang.psi.visitor.LuaElementVisitor;
import org.jetbrains.annotations.NotNull;








/**
 * @author ilyas
 */
public class LuaDocMethodReferenceImpl extends LuaDocMemberReferenceImpl implements LuaDocMethodReference {

  public LuaDocMethodReferenceImpl(@NotNull ASTNode node) {
    super(node);
  }

  public String toString() {
    return "LuaDocMethodReference";
  }

  public void accept(LuaElementVisitor visitor) {
    visitor.visitDocMethodReference(this);
  }

  @NotNull
  public LuaDocMethodParams getParameterList() {
    LuaDocMethodParams child = findChildByClass(LuaDocMethodParams.class);
    assert child != null;
    return child;
  }

  @Override
  public PsiElement resolve() {
//    String name = getReferenceName();
//    LuaDocReferenceElement holder = getReferenceHolder();
//    PsiElement resolved;
//    if (holder != null) {
//      LuaReferenceElement referenceElement = holder.getReferenceElement();
//      resolved = referenceElement != null ? referenceElement.resolve() : null;
//    } else {
//      resolved = getEnclosingClass(this);
//    }
//    if (resolved instanceof PsiClass) {
//      PsiType[] parameterTypes = getParameterList().getParameterTypes();
//      PsiType thisType = JavaPsiFacade.getInstance(getProject()).getElementFactory().createType((PsiClass)resolved, PsiSubstitutor.EMPTY);
//
//      MethodResolverProcessor processor = new MethodResolverProcessor(name, this, false, thisType, parameterTypes, PsiType.EMPTY_ARRAY);
//      resolved.processDeclarations(processor, ResolveState.initial(), resolved, this);
//      if (processor.hasApplicableCandidates()) {
//        return processor.getCandidates()[0].getElement();
//      }
//
//      MethodResolverProcessor constructorProcessor =
//        new MethodResolverProcessor(name, this, true, thisType, parameterTypes, PsiType.EMPTY_ARRAY);
//      resolved.processDeclarations(constructorProcessor, ResolveState.initial(), resolved, this);
//      if (constructorProcessor.hasApplicableCandidates()) {
//        return constructorProcessor.getCandidates()[0].getElement();
//      }
//    }
    return null;
  }

  protected ResolveResult[] multiResolveImpl() {
//    String name = getReferenceName();
//    LuaDocReferenceElement holder = getReferenceHolder();
//    PsiElement resolved;
//    if (holder != null) {
//      LuaReferenceElement referenceElement = holder.getReferenceElement();
//      resolved = referenceElement != null ? referenceElement.resolve() : null;
//    } else {
//      resolved = getEnclosingClass(this);
//    }
//    if (resolved instanceof PsiClass) {
//      PsiType[] parameterTypes = getParameterList().getParameterTypes();
//      PsiType thisType = JavaPsiFacade.getInstance(getProject()).getElementFactory().createType((PsiClass) resolved, PsiSubstitutor.EMPTY);
//      MethodResolverProcessor processor = new MethodResolverProcessor(name, this, false, thisType, parameterTypes, PsiType.EMPTY_ARRAY);
//      MethodResolverProcessor constructorProcessor = new MethodResolverProcessor(name, this, true, thisType, parameterTypes, PsiType.EMPTY_ARRAY);
//      resolved.processDeclarations(processor, ResolveState.initial(), resolved, this);
//      resolved.processDeclarations(constructorProcessor, ResolveState.initial(), resolved, this);
//      return ArrayUtil.mergeArrays(processor.getCandidates(), constructorProcessor.getCandidates(), LuaResolveResult.class);
//    }
    return new ResolveResult[0];
  }

  public boolean isReferenceTo(PsiElement element) {
    if (element instanceof PsiNamedElement && Comparing.equal(((PsiNamedElement) element).getName(), getReferenceName())) {
      return getManager().areElementsEquivalent(element, resolve());
    }
    return false;
  }

  public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
    final PsiElement resolved = resolve();
    if (resolved instanceof PsiMethod) {
      final PsiMethod method = (PsiMethod) resolved;
      final String oldName = getReferenceName();
      if (!method.getName().equals(oldName)) { //was property reference to accessor
        if (PropertyUtil.isSimplePropertyAccessor(method)) {
          final String newPropertyName = PropertyUtil.getPropertyName(newElementName);
          if (newPropertyName != null) {
            return super.handleElementRename(newPropertyName);
          }
        }
      }
    }
    return super.handleElementRename(newElementName);
  }

}
