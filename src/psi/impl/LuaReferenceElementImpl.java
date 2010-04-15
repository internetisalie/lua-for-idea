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
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import com.sylvanaar.idea.Lua.lexer.LuaTokenTypes;
import com.sylvanaar.idea.Lua.psi.LuaPsiElementFactory;
import com.sylvanaar.idea.Lua.psi.LuaReferenceElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;



public abstract class LuaReferenceElementImpl extends LuaPsiElementImpl implements LuaReferenceElement {
  public LuaReferenceElementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public PsiReference getReference() {
    return this;
  }

  public String getReferenceName() {
    PsiElement nameElement = getReferenceNameElement();
    if (nameElement != null) {
      return nameElement.getText();
    }
    return null;
  }

  @Nullable
  public PsiElement getReferenceNameElement() {
    return findChildByType(LuaTokenTypes.NAME);
  }

  public PsiElement getElement() {
    return this;
  }

  public TextRange getRangeInElement() {
    final PsiElement refNameElement = getReferenceNameElement();
    if (refNameElement != null) {
      final int offsetInParent = refNameElement.getStartOffsetInParent();
      return new TextRange(offsetInParent, offsetInParent + refNameElement.getTextLength());
    }
    return new TextRange(0, getTextLength());
  }

  public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
    PsiElement nameElement = getReferenceNameElement();
    if (nameElement != null) {
      ASTNode node = nameElement.getNode();
      ASTNode newNameNode = LuaPsiElementFactory.getInstance(getProject()).createReferenceNameFromText(newElementName).getNode();
      assert newNameNode != null && node != null;
      node.getTreeParent().replaceChild(node, newNameNode);
    }

    return this;
  }

  public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
//    if (isReferenceTo(element)) return this;
//
//    if (element instanceof PsiClass) {
//      final String newName = ((PsiClass) element).getName();
//      handleElementRename(newName);
//      if (isReferenceTo(element)) return this;
//
////      final GroovyFileBase file = (GroovyFileBase) getContainingFile();
////      final PsiClass clazz = (PsiClass) element;
////      final String qName = clazz.getQualifiedName();
////      if (qName != null) {
////        if (mayInsertImport()) {
////          final GrImportStatement added = file.addImportForClass(clazz);
////          if (!bindsCorrectly(element)) {
////            file.removeImport(added);
////            return bindWithQualifiedRef(qName);
////          }
////
////          return this;
////        } else {
////          return bindWithQualifiedRef(qName);
////        }
////      }
//    } else if (element instanceof PsiMember) {
//      PsiMember member = (PsiMember)element;
//      if (!isPhysical()) {
//        // don't qualify reference: the isReferenceTo() check fails anyway, whether we have a static import for this member or not
//        return this;
//      }
//      final PsiClass psiClass = member.getContainingClass();
//      if (psiClass == null) throw new IncorrectOperationException();
//
//      String qName = psiClass.getQualifiedName() + "." + member.getName();
//      return bindWithQualifiedRef(qName);
//    }
//    else if (element instanceof PsiPackage) {
//      final String qName = ((PsiPackage) element).getQualifiedName();
//      return bindWithQualifiedRef(qName);
//    }
//
    throw new IncorrectOperationException("Cannot bind to:" + element + " of class " + element.getClass());
  }

  private PsiElement bindWithQualifiedRef(String qName) {
//    final GrTypeArgumentList list = getTypeArgumentList();
//    final String typeArgs = (list != null) ? list.getText() : "";
//    final String text = qName + typeArgs;
//    final GrCodeReferenceElement qualifiedRef = GroovyPsiElementFactory.getInstance(getProject()).createTypeOrPackageReference(text);
//    getNode().getTreeParent().replaceChild(getNode(), qualifiedRef.getNode());
//    PsiUtil.shortenReference(qualifiedRef);
//    return qualifiedRef;
      return null;
  }

  protected boolean bindsCorrectly(PsiElement element) {
    return isReferenceTo(element);
  }
}
