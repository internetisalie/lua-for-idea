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
//
//package com.sylvanaar.idea.Lua.lang.resolve;
//
//import com.intellij.openapi.project.Project;
//import com.intellij.openapi.util.Pair;
//import com.intellij.psi.*;
//import com.intellij.psi.scope.JavaScopeProcessorEvent;
//import com.intellij.psi.scope.NameHint;
//import com.intellij.psi.scope.PsiScopeProcessor;
//import com.intellij.psi.search.GlobalSearchScope;
//import com.intellij.psi.util.MethodSignature;
//import com.intellij.psi.util.MethodSignatureUtil;
//import com.intellij.psi.util.TypeConversionUtil;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//import org.jetbrains.plugins.groovy.annotator.intentions.dynamic.DynamicManager;
//import org.jetbrains.plugins.groovy.dsl.GroovyDslFileIndex;
//import org.jetbrains.plugins.groovy.lang.psi.GroovyFile;
//import org.jetbrains.plugins.groovy.lang.psi.GroovyFileBase;
//import org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElement;
//import org.jetbrains.plugins.groovy.lang.psi.api.GroovyResolveResult;
//import org.jetbrains.plugins.groovy.lang.psi.api.statements.*;
//import org.jetbrains.plugins.groovy.lang.psi.api.statements.arguments.GrArgumentList;
//import org.jetbrains.plugins.groovy.lang.psi.api.statements.blocks.GrClosableBlock;
//import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrExpression;
//import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression;
//import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.path.GrMethodCallExpression;
//import org.jetbrains.plugins.groovy.lang.psi.api.statements.typedef.GrTypeDefinition;
//import org.jetbrains.plugins.groovy.lang.psi.api.statements.typedef.members.GrGdkMethod;
//import org.jetbrains.plugins.groovy.lang.psi.api.statements.typedef.members.GrMember;
//import org.jetbrains.plugins.groovy.lang.psi.impl.GroovyPsiManager;
//import org.jetbrains.plugins.groovy.lang.resolve.processors.ClassResolverProcessor;
//import org.jetbrains.plugins.groovy.lang.resolve.processors.PropertyResolverProcessor;
//import org.jetbrains.plugins.groovy.lang.resolve.processors.ResolverProcessor;
//
//import java.util.*;
//
///**
// * @author ven
// */
//@SuppressWarnings({"StringBufferReplaceableByString"})
//public class ResolveUtil {
//  public static final PsiScopeProcessor.Event DECLARATION_SCOPE_PASSED = new PsiScopeProcessor.Event() {
//  };
//
//  private ResolveUtil() {
//  }
//
//  public static boolean treeWalkUp(PsiElement place, PsiScopeProcessor processor, boolean processNonCodeMethods) {
//    PsiElement lastParent = null;
//    PsiElement run = place;
//
//    final Project project = place.getProject();
//    PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);
//
//    while (run != null) {
//      if (!run.processDeclarations(processor, ResolveState.initial(), lastParent, place)) return false;
//      if (processNonCodeMethods) {
//        if (run instanceof GrTypeDefinition) {
//          processNonCodeMethods(factory.createType(((GrTypeDefinition)run)), processor, project, place, false);
//        }
//        else if ((run instanceof LuaFileBase) && ((LuaFileBase)run).isScript()) {
//          final PsiClass psiClass = ((GroovyFileBase)run).getScriptClass();
//          if (psiClass != null) {
//            processNonCodeMethods(factory.createType(psiClass), processor, project, place, false);
//          }
//        }
//      }
//      lastParent = run;
//      run = run.getContext();
//      processor.handleEvent(JavaScopeProcessorEvent.CHANGE_LEVEL, null);
//    }
//
//    return true;
//  }
//
//  public static boolean processChildren(PsiElement element,
//                                        PsiScopeProcessor processor,
//                                        ResolveState substitutor,
//                                        PsiElement lastParent,
//                                        PsiElement place) {
//    PsiElement run = lastParent == null ? element.getLastChild() : lastParent.getPrevSibling();
//    while (run != null) {
//      if (!run.processDeclarations(processor, substitutor, null, place)) return false;
//      run = run.getPrevSibling();
//    }
//
//    return true;
//  }
//
//  public static boolean processElement(PsiScopeProcessor processor, PsiNamedElement namedElement) {
//    NameHint nameHint = processor.getHint(NameHint.KEY);
//    //todo [DIANA] look more carefully
//    String name = nameHint == null ? null : nameHint.getName(ResolveState.initial());
//    if (name == null || name.equals(namedElement.getName())) {
//      return processor.execute(namedElement, ResolveState.initial());
//    }
//
//    return true;
//  }
//
//  public static boolean processNonCodeMethods(PsiType type,
//                                              PsiScopeProcessor processor,
//                                              Project project,
//                                              PsiElement place,
//                                              boolean forCompletion) {
//    return processNonCodeMethods(type, processor, project, new HashSet<String>(), place, forCompletion);
//  }
//
//  private static boolean processNonCodeMethods(PsiType type,
//                                               PsiScopeProcessor processor,
//                                               Project project,
//                                               Set<String> visited,
//                                               PsiElement place,
//                                               boolean forCompletion) {
//    String qName = rawCanonicalText(type);
//
//    if (qName != null) {
//      if (!visited.add(qName)) {
//        return true;
//      }
//
//      for (PsiMethod defaultMethod : LuaPsiManager.getInstance(project).getDefaultMethods(qName)) {
//        if (!processElement(processor, defaultMethod)) return false;
//      }
//
//      for (PsiMethod method : DynamicManager.getInstance(project).getMethods(qName)) {
//        if (!processElement(processor, method)) return false;
//      }
//
//      for (PsiVariable var : DynamicManager.getInstance(project).getProperties(qName)) {
//        if (!processElement(processor, var)) return false;
//      }
//
//      for (NonCodeMembersProcessor membersProcessor : NonCodeMembersProcessor.EP_NAME.getExtensions()) {
//        if (!membersProcessor.processNonCodeMembers(type, processor, place, forCompletion)) return false;
//      }
//
//      if (visited.size() == 1) {
//        if (type instanceof PsiArrayType) {
//          //implicit super types
//          PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
//          PsiClassType t = factory.createTypeByFQClassName("java.lang.Object", GlobalSearchScope.allScope(project));
//          if (!processNonCodeMethods(t, processor, project, visited, place, forCompletion)) return false;
//          t = factory.createTypeByFQClassName("java.lang.Comparable", GlobalSearchScope.allScope(project));
//          if (!processNonCodeMethods(t, processor, project, visited, place, forCompletion)) return false;
//          t = factory.createTypeByFQClassName("java.io.Serializable", GlobalSearchScope.allScope(project));
//          if (!processNonCodeMethods(t, processor, project, visited, place, forCompletion)) return false;
//        } else if (type instanceof PsiClassType) {
//          PsiClass psiClass = ((PsiClassType)type).resolve();
//          if (psiClass != null) {
//            if (!GroovyDslFileIndex.processExecutors(psiClass, place, processor)) {
//              return false;
//            }
//          }
//        }
//
//      }
//      for (PsiType superType : type.getSuperTypes()) {
//        if (!processNonCodeMethods(TypeConversionUtil.erasure(superType), processor, project, visited, place, forCompletion)) {
//          return false;
//        }
//      }
//    }
//
//    return true;
//  }
//
//  @Nullable
//  private static String rawCanonicalText(PsiType type) {
//    final String result = type.getCanonicalText();
//    if (result == null) return null;
//    final int i = result.indexOf('<');
//    if (i > 0) return result.substring(0, i);
//    return result;
//  }
//
//  @Nullable
//  public static PsiType getListTypeForSpreadOperator(GrReferenceExpression refExpr, PsiType componentType) {
//    PsiClass clazz = findListClass(refExpr.getManager(), refExpr.getResolveScope());
//    if (clazz != null) {
//      PsiTypeParameter[] typeParameters = clazz.getTypeParameters();
//      if (typeParameters.length == 1) {
//        PsiSubstitutor substitutor = PsiSubstitutor.EMPTY.put(typeParameters[0], componentType);
//        return JavaPsiFacade.getInstance(refExpr.getProject()).getElementFactory().createType(clazz, substitutor);
//      }
//    }
//
//    return null;
//  }
//
//  @Nullable
//  public static PsiClass findListClass(PsiManager manager, GlobalSearchScope resolveScope) {
//    return JavaPsiFacade.getInstance(manager.getProject()).findClass("java.util.List", resolveScope);
//  }
//
//  public static GroovyPsiElement resolveProperty(GroovyPsiElement place, String name) {
//    PropertyResolverProcessor processor = new PropertyResolverProcessor(name, place);
//    return resolveExistingElement(place, processor, GrVariable.class, GrReferenceExpression.class);
//  }
//
//  @Nullable
//  public static PsiClass resolveClass(GroovyPsiElement place, String name) {
//    ClassResolverProcessor processor = new ClassResolverProcessor(name, place);
//    return resolveExistingElement(place, processor, PsiClass.class);
//  }
//
//  @Nullable
//  public static <T> T resolveExistingElement(GroovyPsiElement place, ResolverProcessor processor, Class<? extends T>... classes) {
//    treeWalkUp(place, processor, true);
//    final GroovyResolveResult[] candidates = processor.getCandidates();
//    for (GroovyResolveResult candidate : candidates) {
//      final PsiElement element = candidate.getElement();
//      if (element == place) continue;
//      for (Class<? extends T> clazz : classes) {
//        if (clazz.isInstance(element)) return (T)element;
//      }
//    }
//
//    return null;
//  }
//
//  @NotNull
//  public static Pair<GrStatement, GrLabeledStatement> resolveLabelTargets(@Nullable String labelName,
//                                                                          @Nullable PsiElement element,
//                                                                          boolean isBreak) {
//    if (element == null) return new Pair<GrStatement, GrLabeledStatement>(null, null);
//
//    if (labelName == null) {
//      do {
//        element = element.getParent();
//        if (element == null || element instanceof GrClosableBlock || element instanceof GrMember || element instanceof GroovyFile) {
//          return new Pair<GrStatement, GrLabeledStatement>(null, null);
//        }
//      }
//      while (!(element instanceof GrLoopStatement) && !(isBreak && element instanceof GrSwitchStatement));
//      return new Pair<GrStatement, GrLabeledStatement>(((GrStatement)element), null);
//    }
//
//    GrStatement statement = null;
//    do {
//      PsiElement last = element;
//      element = element.getParent();
//      if (element == null || element instanceof GrMember || element instanceof GroovyFile) break;
//      if (element instanceof GrStatement && !(element instanceof GrClosableBlock)) {
//        statement = (GrStatement)element;
//      }
//      PsiElement sibling = element;
//      while (sibling != null) {
//        final GrLabeledStatement labelStatement = findLabelStatementIn(sibling, last, labelName);
//        if (labelStatement != null) {
//          return new Pair<GrStatement, GrLabeledStatement>(statement, labelStatement);
//        }
//        sibling = sibling.getPrevSibling();
//      }
//      if (element instanceof GrClosableBlock) break;
//    }
//    while (true);
//    return new Pair<GrStatement, GrLabeledStatement>(null, null);
//  }
//
//  private static boolean isApplicableLabelStatement(PsiElement element, String labelName) {
//    return ((element instanceof GrLabeledStatement && labelName.equals(((GrLabeledStatement)element).getLabelName())));
//  }
//
//  @Nullable
//  private static GrLabeledStatement findLabelStatementIn(PsiElement element, PsiElement lastChild, String labelName) {
//    if (isApplicableLabelStatement(element, labelName)) {
//      return (GrLabeledStatement)element;
//    }
//    for (PsiElement child = element.getFirstChild(); child != null && child != lastChild; child = child.getNextSibling()) {
//      final GrLabeledStatement statement = findLabelStatementIn(child, child, labelName);
//      if (statement != null) return statement;
//    }
//    return null;
//  }
//
//  @Nullable
//  public static GrLabeledStatement resolveLabeledStatement(@Nullable String labelName, @Nullable PsiElement element, boolean isBreak) {
//    return resolveLabelTargets(labelName, element, isBreak).second;
//  }
//
//  @Nullable
//  public static GrStatement resolveLabelTargetStatement(@Nullable String labelName, @Nullable PsiElement element, boolean isBreak) {
//    return resolveLabelTargets(labelName, element, isBreak).first;
//  }
//
//  public static boolean processCategoryMembers(PsiElement place, ResolverProcessor processor) {
//    PsiElement prev = null;
//    while (place != null) {
//      if (place instanceof GrMember) break;
//
//      if (place instanceof GrMethodCallExpression) {
//        final GrMethodCallExpression call = (GrMethodCallExpression)place;
//        final GrExpression invoked = call.getInvokedExpression();
//        if (invoked instanceof GrReferenceExpression && "use".equals(((GrReferenceExpression)invoked).getReferenceName())) {
//          final GrClosableBlock[] closures = call.getClosureArguments();
//          if (closures.length == 1 && closures[0].equals(prev)) {
//            if (useCategoryClass(call)) {
//              final GrArgumentList argList = call.getArgumentList();
//              if (argList != null) {
//                final GrExpression[] args = argList.getExpressionArguments();
//                if (args.length == 1 && args[0] instanceof GrReferenceExpression) {
//                  final PsiElement resolved = ((GrReferenceExpression)args[0]).resolve();
//                  if (resolved instanceof PsiClass) {
//                    try {
//                      processor.setCurrentFileResolveContext(call);
//                      if (!resolved.processDeclarations(processor, ResolveState.initial(), null, place)) return false;
//                    }
//                    finally {
//                      processor.setCurrentFileResolveContext(null);
//                    }
//                  }
//                }
//              }
//            }
//          }
//        }
//      }
//
//      prev = place;
//      place = place.getContext();
//    }
//
//    return true;
//  }
//
//  private static boolean useCategoryClass(GrMethodCallExpression call) {
//
//    final PsiMethod resolved = call.resolveMethod();
//    if (resolved instanceof GrGdkMethod) {
//      final PsiElementFactory factory = JavaPsiFacade.getInstance(call.getProject()).getElementFactory();
//      final GlobalSearchScope scope = call.getResolveScope();
//      final PsiType[] parametersType =
//        {factory.createTypeByFQClassName("java.lang.Class", scope), factory.createTypeByFQClassName("groovy.lang.Closure", scope)};
//      final MethodSignature pattern =
//        MethodSignatureUtil.createMethodSignature("use", parametersType, PsiTypeParameter.EMPTY_ARRAY, PsiSubstitutor.EMPTY);
//      return resolved.getSignature(PsiSubstitutor.EMPTY).equals(pattern);
//    }
//
//    return false;
//  }
//
//  public static PsiElement[] mapToElements(GroovyResolveResult[] candidates) {
//    PsiElement[] elements = new PsiElement[candidates.length];
//    for (int i = 0; i < elements.length; i++) {
//      elements[i] = candidates[i].getElement();
//    }
//
//    return elements;
//  }
//
//  public static GroovyResolveResult[] filterSameSignatureCandidates(Collection<GroovyResolveResult> candidates, int argumentCount) {
//    GroovyResolveResult[] array = candidates.toArray(new GroovyResolveResult[candidates.size()]);
//    if (array.length == 1) return array;
//
//    List<GroovyResolveResult> result = new ArrayList<GroovyResolveResult>();
//    result.add(array[0]);
//
//    Outer:
//    for (int i = 1; i < array.length; i++) {
//      PsiElement currentElement = array[i].getElement();
//      if (currentElement instanceof PsiMethod) {
//        PsiMethod currentMethod = (PsiMethod)currentElement;
//        for (Iterator<GroovyResolveResult> iterator = result.iterator(); iterator.hasNext();) {
//          final GroovyResolveResult otherResolveResult = iterator.next();
//          PsiElement element = otherResolveResult.getElement();
//          if (element instanceof PsiMethod) {
//            PsiMethod method = (PsiMethod)element;
//            if (dominated(currentMethod, array[i].getSubstitutor(), method, otherResolveResult.getSubstitutor(), argumentCount)) {
//              continue Outer;
//            }
//            else if (dominated(method, otherResolveResult.getSubstitutor(), currentMethod, array[i].getSubstitutor(), argumentCount)) {
//              iterator.remove();
//            }
//          }
//        }
//      }
//
//      result.add(array[i]);
//    }
//
//    return result.toArray(new GroovyResolveResult[result.size()]);
//  }
//
//  public static boolean dominated(PsiMethod method1,
//                                  PsiSubstitutor substitutor1,
//                                  PsiMethod method2,
//                                  PsiSubstitutor substitutor2,
//                                  int argumentCount) {  //method1 has more general parameter types then method2
//    if (!method1.getName().equals(method2.getName())) return false;
//
//    PsiParameter[] params1 = method1.getParameterList().getParameters();
//    PsiParameter[] params2 = method2.getParameterList().getParameters();
//    if (argumentCount != params1.length && argumentCount == params2.length) return true;
//
//    if (params1.length != params2.length) return false;
//
//    for (int i = 0; i < params2.length; i++) {
//      PsiType type1 = substitutor1.substitute(params1[i].getType());
//      PsiType type2 = substitutor2.substitute(params2[i].getType());
//      if (!type1.equals(type2)) return false;
//    }
//
//    return true;
//  }
//}