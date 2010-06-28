///*
// * Copyright 2010 Jon S Akhtar (Sylvanaar)
// *
// *   Licensed under the Apache License, Version 2.0 (the "License");
// *   you may not use this file except in compliance with the License.
// *   You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// *   Unless required by applicable law or agreed to in writing, software
// *   distributed under the License is distributed on an "AS IS" BASIS,
// *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *   See the License for the specific language governing permissions and
// *   limitations under the License.
// */
//package com.sylvanaar.idea.Lua.lang.resolve.processors;
//
//import com.intellij.psi.PsiElement;
//import com.intellij.psi.PsiMethod;
//import com.intellij.psi.PsiType;
//import com.intellij.psi.ResolveState;
//import org.jetbrains.plugins.groovy.lang.psi.util.GroovyPropertyUtils;
//
//import java.util.EnumSet;
//
///**
// * @author Maxim.Medvedev
// */
//public class AccessorResolverProcessor extends ResolverProcessor {
//  private boolean mySearchForGetter;
//
//  public AccessorResolverProcessor(String name, PsiElement place, boolean searchForGetter) {
//    super(name, EnumSet.of(ResolveKind.METHOD), place, PsiType.EMPTY_ARRAY);
//    mySearchForGetter = searchForGetter;
//  }
//
//  public boolean execute(PsiElement element, ResolveState state) {
//    if (mySearchForGetter) {
//      if (element instanceof PsiMethod && GroovyPropertyUtils.isSimplePropertyGetter((PsiMethod)element)) {
//        return super.execute(element, state);
//      }
//    }
//    else {
//      if (element instanceof PsiMethod && GroovyPropertyUtils.isSimplePropertySetter((PsiMethod)element)) {
//        return super.execute(element, state);
//      }
//    }
//    return true;
//  }
//}