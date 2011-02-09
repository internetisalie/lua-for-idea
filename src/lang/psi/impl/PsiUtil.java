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

package com.sylvanaar.idea.Lua.lang.psi.impl;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.sylvanaar.idea.Lua.lang.lexer.LuaLexer;
import com.sylvanaar.idea.Lua.lang.lexer.LuaTokenTypes;
import org.jetbrains.annotations.Nullable;

/**
 * @author ven
 */
public class PsiUtil {
  public static final Logger LOG = Logger.getInstance("org.jetbrains.plugins.groovy.lang.psi.util.PsiUtil");
 // public static final Key<JavaIdentifier> NAME_IDENTIFIER = new Key<JavaIdentifier>("Java Identifier");

  private PsiUtil() {
  }

    
//
////  @Nullable
////  public static String getQualifiedReferenceText(GrCodeReferenceElement referenceElement) {
////    StringBuilder builder = new StringBuilder();
////    if (!appendName(referenceElement, builder)) return null;
////
////    return builder.toString();
////  }
////
////  private static boolean appendName(GrCodeReferenceElement referenceElement, StringBuilder builder) {
////    String refName = referenceElement.getReferenceName();
////    if (refName == null) return false;
////    GrCodeReferenceElement qualifier = referenceElement.getQualifier();
////    if (qualifier != null) {
////      appendName(qualifier, builder);
////      builder.append(".");
////    }
////
////    builder.append(refName);
////    return true;
////  }
////
////  public static boolean isLValue(GroovyPsiElement element) {
////    if (element instanceof GrExpression) {
////      PsiElement parent = element.getParent();
////      if (parent instanceof GrListOrMap && !((GrListOrMap)parent).isMap()) {
////        return isLValue((GroovyPsiElement)parent);
////      }
////      return parent instanceof GrAssignmentExpression && element.equals(((GrAssignmentExpression)parent).getLValue());
////    }
////    return false;
////  }
//
////  public static boolean isApplicable(@Nullable PsiType[] argumentTypes,
////                                     PsiMethod method,
////                                     PsiSubstitutor substitutor,
////                                     boolean isInUseCategory) {
////    if (argumentTypes == null) return true;
////
////    PsiParameter[] parameters = method.getParameterList().getLuaParameters();
////    if (isInUseCategory && method.hasModifierProperty(PsiModifier.STATIC) && parameters.length > 0) {
////      //do not check first parameter, it is 'this' inside categorized block
////      parameters = ArrayUtil.remove(parameters, 0);
////    }
////
////    PsiType[] parameterTypes = skipOptionalParametersAndSubstitute(argumentTypes.length, parameters, substitutor);
////    if (parameterTypes.length - 1 > argumentTypes.length) return false; //one Map type might represent named arguments
////    if (parameterTypes.length == 0 && argumentTypes.length > 0) return false;
////
////    final GlobalSearchScope scope = method.getResolveScope();
////
////    if (parameterTypes.length - 1 == argumentTypes.length) {
////      final PsiType firstType = parameterTypes[0];
////      final PsiClassType mapType = createMapType(method.getManager(), scope);
////      if (mapType.isAssignableFrom(firstType)) {
////        final PsiType[] trimmed = new PsiType[parameterTypes.length - 1];
////        System.arraycopy(parameterTypes, 1, trimmed, 0, trimmed.length);
////        parameterTypes = trimmed;
////      } else if (!method.isVarArgs()) return false;
////    }
////
////    for (int i = 0; i < argumentTypes.length; i++) {
////      PsiType argType = argumentTypes[i];
////      PsiType parameterTypeToCheck;
////      if (i < parameterTypes.length - 1) {
////        parameterTypeToCheck = parameterTypes[i];
////      } else {
////        PsiType lastParameterType = parameterTypes[parameterTypes.length - 1];
////        if (lastParameterType instanceof PsiArrayType && !(argType instanceof PsiArrayType)) {
////          parameterTypeToCheck = ((PsiArrayType)lastParameterType).getComponentType();
////        } else if (parameterTypes.length == argumentTypes.length) {
////          parameterTypeToCheck = lastParameterType;
////        } else {
////          return false;
////        }
////      }
////
////      if (!TypesUtil.isAssignableByMethodCallConversion(parameterTypeToCheck, argType, method.getManager(), scope)) return false;
////    }
////
////    return true;
////  }
//
//  public static PsiType[] skipOptionalParametersAndSubstitute(int argNum, PsiParameter[] parameters, PsiSubstitutor substitutor) {
//    int diff = parameters.length - argNum;
//    List<PsiType> result = new ArrayList<PsiType>(argNum);
//    for (PsiParameter parameter : parameters) {
//      if (diff > 0 && parameter instanceof LuaParameter) {
//        diff--;
//        continue;
//      }
//
//      result.add(substitutor.substitute(parameter.getType()));
//    }
//
//    return result.toArray(new PsiType[result.size()]);
//  }
////
////  public static PsiClassType createMapType(PsiManager manager, GlobalSearchScope scope) {
////    return JavaPsiFacade.getInstance(manager.getProject()).getElementFactory().createTypeByFQClassName(CommonClassNames.JAVA_UTIL_MAP, scope);
////  }
////
////  @Nullable
////  public static GroovyPsiElement getArgumentsElement(PsiElement methodRef) {
////    PsiElement parent = methodRef.getParent();
////    if (parent instanceof GrMethodCallExpression) {
////      return ((GrMethodCallExpression)parent).getArgumentList();
////    } else if (parent instanceof GrApplicationStatement) {
////      return ((GrApplicationStatement)parent).getArgumentList();
////    } else if (parent instanceof GrNewExpression) {
////      return ((GrNewExpression)parent).getArgumentList();
////    }
////    return null;
////  }
////
////  // Returns arguments types not including Map for named arguments
////  @Nullable
////  public static PsiType[] getArgumentTypes(PsiElement place, boolean forConstructor, boolean nullAsBottom) {
////    PsiElement parent = place.getParent();
////    if (parent instanceof GrCallExpression) {
////      List<PsiType> result = new ArrayList<PsiType>();
////      GrCallExpression call = (GrCallExpression)parent;
////
////      if (!forConstructor) {
////        GrNamedArgument[] namedArgs = call.getNamedArguments();
////        if (namedArgs.length > 0) {
////          result.add(createMapType(place.getManager(), place.getResolveScope()));
////        }
////      }
////
////      GrExpression[] expressions = call.getExpressionArguments();
////      for (GrExpression expression : expressions) {
////        PsiType type = getArgumentType(expression);
////        if (type == null) {
////          result.add(nullAsBottom ? PsiType.NULL : TypesUtil.getJavaLangObject(call));
////        } else {
////          result.add(type);
////        }
////      }
////
////      GrClosableBlock[] closures = call.getClosureArguments();
////      for (GrClosableBlock closure : closures) {
////        PsiType closureType = closure.getType();
////        if (closureType != null) {
////          result.add(closureType);
////        }
////      }
////
////      return result.toArray(new PsiType[result.size()]);
////
////    }
////    else if (parent instanceof GrAnonymousClassDefinition) {
////      final GrAnonymousClassDefinition anonymous = (GrAnonymousClassDefinition)parent;
////      final GrArgumentList argList = anonymous.getArgumentListGroovy();
////      List<PsiType> result = new ArrayList<PsiType>();
////
////      if (!forConstructor) {
////        GrNamedArgument[] namedArgs = argList.getNamedArguments();
////        if (namedArgs.length > 0) {
////          result.add(createMapType(place.getManager(), place.getResolveScope()));
////        }
////      }
////
////      GrExpression[] expressions = argList.getExpressionArguments();
////      for (GrExpression expression : expressions) {
////        PsiType type = getArgumentType(expression);
////        if (type == null) {
////          result.add(nullAsBottom ? PsiType.NULL : TypesUtil.getJavaLangObject(argList));
////        } else {
////          result.add(type);
////        }
////      }
////
////      return result.toArray(new PsiType[result.size()]);
////    }
////    else if (parent instanceof GrApplicationStatement) {
////      final GrApplicationStatement call = (GrApplicationStatement)parent;
////      GrExpression[] args = call.getArguments();
////      final GrArgumentList argList = call.getArgumentList();
////      GrNamedArgument[] namedArgs = argList != null ? argList.getNamedArguments() : GrNamedArgument.EMPTY_ARRAY;
////      final ArrayList<PsiType> result = new ArrayList<PsiType>();
////      if (namedArgs.length > 0) {
////        result.add(createMapType(place.getManager(), place.getResolveScope()));
////      }
////      for (GrExpression arg : args) {
////        PsiType argType = getArgumentType(arg);
////        if (argType == null) {
////          result.add(nullAsBottom ? PsiType.NULL : TypesUtil.getJavaLangObject((GroovyPsiElement)parent));
////        }
////        else {
////          result.add(argType);
////        }
////      }
////      return result.toArray(new PsiType[result.size()]);
////    } else if (parent instanceof GrConstructorInvocation || parent instanceof GrEnumConstant) {
////      final GrArgumentList argList = ((GrCall)parent).getArgumentList();
////      if (argList == null) return PsiType.EMPTY_ARRAY;
////
////      List<PsiType> result = new ArrayList<PsiType>();
////      if (argList.getNamedArguments().length > 0) {
////        result.add(createMapType(place.getManager(), place.getResolveScope()));
////      }
////
////      GrExpression[] expressions = argList.getExpressionArguments();
////      for (GrExpression expression : expressions) {
////        PsiType type = getArgumentType(expression);
////        if (type == null) {
////          result.add(nullAsBottom ? PsiType.NULL : TypesUtil.getJavaLangObject(argList));
////        } else {
////          result.add(type);
////        }
////      }
////
////      return result.toArray(new PsiType[result.size()]);
////    }
////
////    return null;
////  }
////
////  @Nullable
////  private static PsiType getArgumentType(GrExpression expression) {
////    if (expression instanceof GrReferenceExpression) {
////      final PsiElement resolved = ((GrReferenceExpression)expression).resolve();
////      if (resolved instanceof PsiClass) {
////        //this argument is passed as java.lang.Class
////        return JavaPsiFacade.getInstance(resolved.getProject()).getElementFactory()
////          .createTypeByFQClassName(CommonClassNames.JAVA_LANG_CLASS, expression.getResolveScope());
////      }
////    }
////
////    return expression.getType();
////  }
////
////  public static SearchScope restrictScopeToGroovyFiles(final Computable<SearchScope> originalScopeComputation) { //important to compute originalSearchScope in read action!
////    return ApplicationManager.getApplication().runReadAction(new Computable<SearchScope>() {
////      public SearchScope compute() {
////        final SearchScope originalScope = originalScopeComputation.compute();
////        if (originalScope instanceof GlobalSearchScope) {
////          return GlobalSearchScope
////            .getScopeRestrictedByFileTypes((GlobalSearchScope)originalScope, Lus.getGroovyEnabledFileTypes());
////        }
////        return originalScope;
////      }
////    });
////  }
////
////  @Nullable
////  public static PsiClass getJavaLangClass(PsiElement resolved, GlobalSearchScope scope) {
////    return JavaPsiFacade.getInstance(resolved.getProject()).findClass(CommonClassNames.JAVA_LANG_CLASS, scope);
////  }
////
//    public static boolean isValidReferenceName(String text) {
//        final LuaLexer lexer = new LuaLexer();
//        lexer.start(text);
//        return LuaTokenTypes.REFERENCE_NAMES.contains(lexer.getTokenType()) && lexer.getTokenEnd() == text.length();
//    }
////
////  public static void shortenReferences(GroovyPsiElement element) {
////    doShorten(element);
////  }
////
////  private static void doShorten(PsiElement element) {
////    PsiElement child = element.getFirstChild();
////    while (child != null) {
////      if (child instanceof GrCodeReferenceElement) {
////        shortenReference((GrCodeReferenceElement)child);
////      }
////
////      doShorten(child);
////      child = child.getNextSibling();
////    }
////  }
////
////  private static final Key<Boolean> SHORTENING = Key.create("SHORTENING");
////  public static void shortenReference(GrCodeReferenceElement ref) {
////    if (ref.getQualifier() != null &&
////        (PsiTreeUtil.getParentOfType(ref, GrDocMemberReference.class) != null ||
////         PsiTreeUtil.getParentOfType(ref, GrDocComment.class) == null) &&
////         PsiTreeUtil.getParentOfType(ref, GrImportStatement.class) == null &&
////         PsiTreeUtil.getParentOfType(ref, GroovyCodeFragment.class) == null) {
////      final PsiElement resolved = ref.resolve();
////      if (resolved instanceof PsiClass && mayShorten(ref)) {
////        if (ref.getUserData(SHORTENING) != null) {
////          LOG.error("Endless shortening. Ref=" + ref.getText() + "; parent=" + ref.getParent());
////          return;
////        }
////
////        ref.setQualifier(null);
////        ref.putUserData(SHORTENING, Boolean.TRUE);
////        try {
////          ref.bindToElement(resolved);
////        }
////        catch (IncorrectOperationException e) {
////          LOG.error(e);
////        }
////        finally {
////          ref.putUserData(SHORTENING, null);
////        }
////      }
////    }
////  }
////
////  private static boolean mayShorten(@NotNull GrCodeReferenceElement ref) {
////    GrCodeReferenceElement cur = (GrCodeReferenceElement)ref.copy();
////    while (true) {
////      final GrCodeReferenceElement qualifier = cur.getQualifier();
////      if (qualifier == null) {
////        return true;
////      }
////      if (!(qualifier.resolve() instanceof PsiClass)) {
////        final PsiClass correctResolved = (PsiClass)cur.resolve();
////        cur.setQualifier(null);
////        final PsiClass rawResolved = (PsiClass)cur.resolve();
////        return rawResolved == null || cur.getManager().areElementsEquivalent(correctResolved, rawResolved);
////      }
////      cur = qualifier;
////    }
////  }
////
////  @Nullable
////  public static GrTopLevelDefintion findPreviousTopLevelElementByThisElement(PsiElement element) {
////    PsiElement parent = element.getParent();
////
////    while (parent != null && !(parent instanceof GrTopLevelDefintion)) {
////      parent = parent.getParent();
////    }
////
////    if (parent == null) return null;
////    return ((GrTopLevelDefintion)parent);
////  }
//
////  public static boolean isStaticsOK(PsiModifierListOwner owner, PsiElement place) {
////    if (owner instanceof PsiMember) {
////      if (place instanceof GrReferenceExpression) {
////        GrExpression qualifier = ((GrReferenceExpression)place).getQualifierExpression();
////        if (qualifier != null) {
////          if (qualifier instanceof GrReferenceExpression) {
////            PsiElement qualifierResolved = ((GrReferenceExpression)qualifier).resolve();
////            if (qualifierResolved instanceof PsiClass || qualifierResolved instanceof PsiPackage) { //static context
////              if (owner instanceof PsiClass) {
////                return true;
////              }
////              //members from java.lang.Class can be invoked without ".class"
////              PsiClass javaLangClass = JavaPsiFacade.getInstance(place.getProject()).findClass("java.lang.Class", place.getResolveScope());
////              if (javaLangClass != null) {
////                PsiClass containingClass = ((PsiMember)owner).getContainingClass();
////                if ((containingClass == null) || //default groovy method
////                    InheritanceUtil.isInheritorOrSelf(javaLangClass, containingClass, true)) {
////                  return true;
////                }
////              }
////              return owner.hasModifierProperty(PsiModifier.STATIC);
////            }
////          }
////
////          //instance context
////          if (owner instanceof PsiClass) {
////            return false;
////          }
////          return CodeInsightSettings.getInstance().SHOW_STATIC_AFTER_INSTANCE || !owner.hasModifierProperty(PsiModifier.STATIC);
////        }
////      }
////    }
////    return true;
////  }
//
////  public static boolean isAccessible(PsiElement place, PsiMember member) {
////
////    if (PsiTreeUtil.getParentOfType(place, GrDocComment.class) != null) return true;
////    if (!member.isPhysical()) {
////      return true;
////    }
////
////    if (place instanceof GrReferenceExpression && ((GrReferenceExpression)place).getQualifierExpression() == null) {
////      if (member.getContainingClass() instanceof GroovyScriptClass) { //calling toplevel script members from the same script file
////        return true;
////      }
////    }
////    return com.intellij.psi.util.PsiUtil.isAccessible(member, place, null);
////  }
//
//  public static void reformatCode(final PsiElement element) {
//    final TextRange textRange = element.getTextRange();
//    try {
//      CodeStyleManager.getInstance(element.getProject())
//        .reformatText(element.getContainingFile(), textRange.getStartOffset(), textRange.getEndOffset());
//    }
//    catch (IncorrectOperationException e) {
//      LOG.error(e);
//    }
//  }
//
////  public static boolean isInStaticContext(GrReferenceExpression refExpression, PsiClass targetClass) {
////    if (refExpression.isQualified()) {
////      GrExpression qualifer = refExpression.getQualifierExpression();
////      if (qualifer instanceof GrReferenceExpression) return ((GrReferenceExpression)qualifer).resolve() instanceof PsiClass;
////    } else {
////      PsiElement run = refExpression;
////      while (run != null && run != targetClass) {
////        if (run instanceof PsiModifierListOwner && ((PsiModifierListOwner)run).hasModifierProperty(PsiModifier.STATIC)) return true;
////        run = run.getParent();
////      }
////    }
////    return false;
////
////  }
//
//  public static Iterable<PsiClass> iterateSupers(final @NotNull PsiClass psiClass, final boolean includeSelf) {
//    return new Iterable<PsiClass>() {
//      public Iterator<PsiClass> iterator() {
//        return new Iterator<PsiClass>() {
//          TIntStack indices = new TIntStack();
//          Stack<PsiClassType[]> superTypesStack = new Stack<PsiClassType[]>();
//          PsiClass current;
//          boolean nextObtained;
//          Set<PsiClass> visited = new HashSet<PsiClass>();
//
//          {
//            if (includeSelf) {
//              current = psiClass;
//              nextObtained = true;
//            } else {
//              current = null;
//              nextObtained = false;
//            }
//
//            pushSuper(psiClass);
//          }
//
//          public boolean hasNext() {
//            nextElement();
//            return current != null;
//          }
//
//          private void nextElement() {
//            if (nextObtained) return;
//
//            nextObtained = true;
//            while (!superTypesStack.empty()) {
//              assert indices.size() > 0;
//
//              int i = indices.pop();
//              PsiClassType[] superTypes = superTypesStack.peek();
//              while (i < superTypes.length) {
//                PsiClass clazz = superTypes[i].resolve();
//                if (clazz != null && !visited.contains(clazz)) {
//                  current = clazz;
//                  visited.add(clazz);
//                  indices.push(i + 1);
//                  pushSuper(clazz);
//                  return;
//                }
//                i++;
//              }
//
//              superTypesStack.pop();
//            }
//
//            current = null;
//          }
//
//          private void pushSuper(PsiClass clazz) {
//            superTypesStack.push(clazz.getSuperTypes());
//            indices.push(0);
//          }
//
//          @NotNull
//          public PsiClass next() {
//            nextElement();
//            nextObtained = false;
//            if (current == null) throw new NoSuchElementException();
//            return current;
//          }
//
//          public void remove() {
//            throw new IllegalStateException("should not be called");
//          }
//        };
//      }
//    };
//  }
//
////  @Nullable
////  public static PsiClass getContextClass(PsiElement context) {
////    GroovyPsiElement parent = PsiTreeUtil.getParentOfType(context, GrTypeDefinition.class, GroovyFileBase.class);
////    if (parent instanceof GrTypeDefinition) {
////      return (PsiClass)parent;
////    } else if (parent instanceof GroovyFileBase) {
////      return ((GroovyFileBase)parent).getScriptClass();
////    }
////    return null;
////  }
////
////  public static boolean mightBeLVlaue(GrExpression expr) {
////    if (expr instanceof GrParenthesizedExpression) return mightBeLVlaue(((GrParenthesizedExpression)expr).getOperand());
////
////    if (expr instanceof GrListOrMap) {
////      GrListOrMap listOrMap = (GrListOrMap)expr;
////      if (listOrMap.isMap()) return false;
////      GrExpression[] initializers = listOrMap.getInitializers();
////      for (GrExpression initializer : initializers) {
////        if (!mightBeLVlaue(initializer)) return false;
////      }
////      return true;
////    }
////    if (expr instanceof GrTupleExpression) return true;
////    return expr instanceof GrReferenceExpression || expr instanceof GrIndexProperty || expr instanceof GrPropertySelection;
////  }
////
////  public static PsiType[] skipOptionalClosureParameters(int argsNum, GrClosureType closureType) {
////    PsiType[] parameterTypes = closureType.getClosureParameterTypes();
////    int diff = parameterTypes.length - argsNum;
////    List<PsiType> result = new ArrayList<PsiType>(argsNum);
////    for (int i = 0; i < parameterTypes.length; i++) {
////      PsiType type = parameterTypes[i];
////      if (diff > 0 && closureType.isOptionalParameter(i)) {
////        diff--;
////        continue;
////      }
////
////      result.add(type);
////    }
////
////    return result.toArray(new PsiType[result.size()]);
////  }
////
////  public static boolean isRawMethodCall(GrMethodCallExpression call) {
////    final GroovyResolveResult[] resolveResults = call.getMethodVariants();
////    if (resolveResults.length == 0) return false;
////    final PsiElement element = resolveResults[0].getElement();
////    if (element instanceof PsiMethod) {
////      PsiType returnType = ((PsiMethod)element).getReturnType();
////      return isRawType(returnType, resolveResults[0].getSubstitutor());
////    }
////    return false;
////  }
////
////  public static boolean isRawFieldAccess(GrReferenceExpression ref) {
////    PsiElement element = null;
////    final GroovyResolveResult[] resolveResults = ref.multiResolve(false);
////    if (resolveResults.length == 0) return false;
////    final GroovyResolveResult resolveResult = resolveResults[0];
////    if (resolveResult != null) {
////      element = resolveResult.getElement();
////    }
////    if (element instanceof PsiField) {
////      return isRawType(((PsiField)element).getType(), resolveResult.getSubstitutor());
////    }
////    else if (element instanceof GrAccessorMethod) {
////      return isRawType(((GrAccessorMethod)element).getReturnType(), resolveResult.getSubstitutor());
////    }
////    return false;
////  }
////
////  private static boolean isRawIndexPropertyAccess(GrIndexProperty expr) {
////    final GrExpression qualifier = expr.getSelectedExpression();
////    final PsiType qualifierType = qualifier.getType();
////    if (qualifierType instanceof PsiClassType) {
////
////      if (InheritanceUtil.isInheritor(qualifierType, CommonClassNames.JAVA_UTIL_LIST)) {
////        return com.intellij.psi.util.PsiUtil.extractIterableTypeParameter(qualifierType, false) == null;
////      }
////
////      if (InheritanceUtil.isInheritor(qualifierType, CommonClassNames.JAVA_UTIL_MAP)) {
////        return com.intellij.psi.util.PsiUtil.substituteTypeParameter(qualifierType, CommonClassNames.JAVA_UTIL_MAP, 1, false) == null;
////      }
////      PsiClassType classType = (PsiClassType)qualifierType;
////      final PsiClassType.ClassResolveResult resolveResult = classType.resolveGenerics();
////      GrExpression[] arguments = expr.getArgumentList().getExpressionArguments();
////      PsiType[] argTypes = new PsiType[arguments.length];
////      for (int i = 0; i < arguments.length; i++) {
////        PsiType argType = arguments[i].getType();
////        if (argType == null) argType = TypesUtil.getJavaLangObject(expr);
////        argTypes[i] = argType;
////      }
////
////      MethodResolverProcessor processor = new MethodResolverProcessor("getAt", expr, false, qualifierType, argTypes, PsiType.EMPTY_ARRAY);
////
////      final PsiClass qClass = resolveResult.getElement();
////      if (qClass != null) {
////        qClass.processDeclarations(processor, ResolveState.initial().put(PsiSubstitutor.KEY, PsiSubstitutor.EMPTY), null, expr);
////      }
////
////      ResolveUtil.processNonCodeMethods(qualifierType, processor, expr.getProject(), expr, false);
////      final GroovyResolveResult[] candidates = processor.getCandidates();
////      PsiType type = null;
////      if (candidates.length == 1) {
////        final PsiElement element = candidates[0].getElement();
////        if (element instanceof PsiMethod) {
////          type = ((PsiMethod)element).getReturnType();
////        }
////      }
////      return isRawType(type, resolveResult.getSubstitutor());
////    }
////    return false;
////  }
//
////  public static boolean isRawClassMemberAccess(GrExpression expr) {
////    while (expr instanceof GrParenthesizedExpression) {
////      expr = ((GrParenthesizedExpression)expr).getOperand();
////    }
////
////    if (expr instanceof GrMethodCallExpression) {
////      return isRawMethodCall((GrMethodCallExpression)expr);
////    }
////    if (expr instanceof GrReferenceExpression) {
////      return isRawFieldAccess((GrReferenceExpression)expr);
////    }
////    if (expr instanceof GrIndexProperty) {
////      return isRawIndexPropertyAccess((GrIndexProperty)expr);
////    }
////    return false;
////  }
//
//  public static boolean isRawType(PsiType type, PsiSubstitutor substitutor) {
//    if (type instanceof PsiClassType) {
//      final PsiClass returnClass = ((PsiClassType)type).resolve();
//      if (returnClass instanceof PsiTypeParameter) {
//        final PsiTypeParameter typeParameter = (PsiTypeParameter)returnClass;
//        final PsiType substitutedType = substitutor.substitute(typeParameter);
//        if (substitutedType == null) {
//          return true;
//        }
//      }
//    }
//    return false;
//  }
//
//  public static boolean isNewLine(PsiElement element) {
//    if (element == null) return false;
//    ASTNode node = element.getNode();
//    if (node == null) return false;
//    return node.getElementType() == LuaTokenTypes.NEWLINE;
//  }
//
  @Nullable
  public static PsiElement getPrevNonSpace(final PsiElement elem) {
    PsiElement prevSibling = elem.getPrevSibling();
    while (prevSibling instanceof PsiWhiteSpace) {
      prevSibling = prevSibling.getPrevSibling();
    }
    return prevSibling;
  }
//
//  @Nullable
//  public static PsiElement getNextNonSpace(final PsiElement elem) {
//    PsiElement nextSibling = elem.getNextSibling();
//    while (nextSibling instanceof PsiWhiteSpace) {
//      nextSibling = nextSibling.getNextSibling();
//    }
//    return nextSibling;
//  }
//
////  public static PsiIdentifier getJavaNameIdentifier(GrNamedElement namedElement) {
////    final PsiElement element = namedElement.getNameIdentifierGroovy();
////    JavaIdentifier identifier = element.getUserData(NAME_IDENTIFIER);
////    if (identifier == null) {
////      //noinspection SynchronizationOnLocalVariableOrMethodParameter
////      synchronized (element) {
////        identifier = element.getUserData(NAME_IDENTIFIER);
////        if (identifier != null) {
////          return identifier;
////        }
////
////        identifier = new JavaIdentifier(element.getManager(), element);
////        element.putUserData(NAME_IDENTIFIER, identifier);
////      }
////    }
////    return identifier;
////  }
//
////  @Nullable
////  public static PsiElement findEnclosingStatement(@Nullable PsiElement context) {
////    if (context == null) return null;
////    context = PsiTreeUtil.getParentOfType(context, GrStatement.class, false);
////    while (context != null) {
////      final PsiElement parent = context.getParent();
////      if (parent instanceof GrControlFlowOwner) return context;
////      context = parent;
////    }
////    return null;
////  }
////
////  public static boolean isMethodCall(GrMethodCallExpression call, String methodName) {
////    final GrExpression expression = call.getInvokedExpression();
////    return expression instanceof GrReferenceExpression && methodName.equals(expression.getText().trim());
////  }
//
//  public static boolean hasEnclosingInstanceInScope(PsiClass clazz, PsiElement scope, boolean isSuperClassAccepted) {
//    PsiElement place = scope;
//    while (place != null && place != clazz && !(place instanceof PsiFile)) {
//      if (place instanceof PsiClass) {
//        if (isSuperClassAccepted) {
//          if (InheritanceUtil.isInheritorOrSelf((PsiClass)place, clazz, true)) return true;
//        }
//        else {
//          if (clazz.getManager().areElementsEquivalent(place, clazz)) return true;
//        }
//      }
//      if (place instanceof PsiModifierListOwner && ((PsiModifierListOwner)place).hasModifierProperty(PsiModifier.STATIC)) return false;
//      place = place.getParent();
//    }
//    return place == clazz;
//  }
//
//  @Nullable
//  public static PsiElement skipWhitespaces(@Nullable PsiElement elem, boolean forward) {
//    while (elem != null &&
//           elem.getNode() != null &&
//           LuaElementTypes.WHITE_SPACES_OR_COMMENTS.contains(elem.getNode().getElementType())) {
//      if (forward) {
//        elem = elem.getNextSibling();
//      }
//      else {
//        elem = elem.getPrevSibling();
//      }
//    }
//    return elem;
//  }
}
