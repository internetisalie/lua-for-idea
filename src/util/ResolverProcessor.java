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

package com.sylvanaar.idea.Lua.util;

import com.intellij.openapi.util.Key;
import com.intellij.psi.*;
import com.intellij.psi.scope.ElementClassHint;
import com.intellij.psi.scope.NameHint;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.sylvanaar.idea.Lua.lang.psi.LuaPsiElement;
import com.sylvanaar.idea.Lua.lang.psi.LuaResolveResult;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * @author ven
 */
public class ResolverProcessor implements PsiScopeProcessor, NameHint, ElementClassHint {
  protected String myName;
//  private final EnumSet<ResolveKind> myResolveTargetKinds;
  private final Set<String> myProcessedClasses = new HashSet<String>();
  protected PsiElement myPlace;
  private
  @NotNull final PsiType[] myTypeArguments;

  protected Set<LuaResolveResult> myCandidates = new LinkedHashSet<LuaResolveResult>();

  public LuaPsiElement getCurrentFileResolveContext() {
    return myCurrentFileResolveContext;
  }

  public void setCurrentFileResolveContext(LuaPsiElement currentFileResolveContext) {
    myCurrentFileResolveContext = currentFileResolveContext;
  }

  protected LuaPsiElement myCurrentFileResolveContext;

  protected ResolverProcessor(String name,                               PsiElement place,
                              @NotNull PsiType[] typeArguments) {
    myName = name;

    myPlace = place;
    myTypeArguments = typeArguments;
  }

  public boolean execute(PsiElement element, ResolveState state) {

      final PsiNamedElement namedElement = (PsiNamedElement) element;
      PsiSubstitutor substitutor = state.get(PsiSubstitutor.KEY);
      if (substitutor == null) substitutor = PsiSubstitutor.EMPTY;

      if (myTypeArguments.length > 0 && namedElement instanceof PsiClass) {
        substitutor = substitutor.putAll((PsiClass) namedElement, myTypeArguments);
      }

      if (namedElement instanceof PsiClass) {
        final PsiClass aClass = (PsiClass)namedElement;
        final String fqn = aClass.getQualifiedName();
        if (!myProcessedClasses.contains(fqn)) {
          myProcessedClasses.add(fqn);
        } else {
          return true;
        }

      myCandidates.add(new LuaResolveResult(){

          @Override
          public boolean isAccessible() {
              return true;  //To change body of implemented methods use File | Settings | File Templates.
          }

          @Override
          public boolean isStaticsOK() {
              return true;  //To change body of implemented methods use File | Settings | File Templates.
          }

          @Override
          public LuaPsiElement getCurrentFileResolveContext() {
              return myCurrentFileResolveContext;  //To change body of implemented methods use File | Settings | File Templates.
          }

          @Override
          public PsiSubstitutor getSubstitutor() {
              return PsiSubstitutor.EMPTY;  //To change body of implemented methods use File | Settings | File Templates.
          }

          @Override
          public PsiElement getElement() {
              return namedElement;  //To change body of implemented methods use File | Settings | File Templates.
          }

          @Override
          public boolean isValidResult() {
              return true;  //To change body of implemented methods use File | Settings | File Templates.
          }
      });

      return true;
    }

    return true;
  }



  @NotNull
  public LuaResolveResult[] getCandidates() {
    return myCandidates.toArray(new LuaResolveResult[myCandidates.size()]);
  }

  @SuppressWarnings({"unchecked"})
  public <T> T getHint(Key<T> hintKey) {
//    if (NameHint.KEY == hintKey && myName != null) {
//      return (T) this;
//    } else if (ClassHint.KEY == hintKey) {
//      return (T) this;
//    } else if (ElementClassHint.KEY == hintKey) {
//      return (T) this;
//    }

    return null;
  }

  public void handleEvent(Event event, Object associated) {
  }

  public String getName() {
    return myName;
  }



  public boolean shouldProcess(DeclaractionKind kind) {
return true;
  }

  public boolean hasCandidates() {
    return myCandidates.size() > 0;
  }

//  private static ResolveKind getResolveKind(PsiElement element) {
//    if (element instanceof PsiVariable) return PROPERTY;
//    if (element instanceof GrReferenceExpression) return PROPERTY;
//
//    else if (element instanceof PsiMethod) return METHOD;
//
//    else if (element instanceof PsiPackage) return PACKAGE;
//
//    return CLASS;
//  }

  public String getName(ResolveState state) {
    //todo[DIANA] implement me!
    return myName;
  }
}
