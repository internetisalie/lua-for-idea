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
//package com.sylvanaar.idea.Lua.lang.psi.impl.expressions;
//
//import com.intellij.lang.ASTNode;
//import com.intellij.openapi.project.Project;
//import com.intellij.openapi.util.Comparing;
//import com.intellij.openapi.util.Computable;
//import com.intellij.openapi.util.TextRange;
//import com.intellij.openapi.util.text.StringUtil;
//import com.intellij.psi.*;
//import com.intellij.psi.impl.source.resolve.ResolveCache;
//import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
//import com.intellij.psi.impl.source.resolve.reference.impl.PsiMultiReference;
//import com.intellij.psi.tree.IElementType;
//import com.intellij.psi.util.PropertyUtil;
//import com.intellij.psi.util.PsiTreeUtil;
//import com.intellij.psi.util.TypeConversionUtil;
//import com.intellij.util.ArrayUtil;
//import com.intellij.util.Consumer;
//import com.intellij.util.Function;
//import com.intellij.util.IncorrectOperationException;
//import com.sylvanaar.idea.Lua.lang.lexer.LuaTokenTypes;
//import com.sylvanaar.idea.Lua.lang.parser.LuaElementTypes;
//import com.sylvanaar.idea.Lua.lang.psi.visitor.LuaElementVisitor;
//import com.sylvanaar.idea.Lua.lang.psi.LuaPsiFile;
//import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaExpression;
//import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaReferenceExpression;
//import com.sylvanaar.idea.Lua.lang.psi.impl.LuaReferenceElementImpl;
//import com.sylvanaar.idea.Lua.lang.psi.impl.PsiUtil;
//import com.sylvanaar.idea.Lua.lang.psi.statements.LuaAssignmentStatement;
//import org.jetbrains.annotations.NonNls;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.EnumSet;
//import java.util.List;
//
///**
// * @author ilyas
// */
//public class LuaReferenceExpressionImpl extends LuaReferenceElementImpl implements LuaReferenceExpression {
//  public LuaReferenceExpressionImpl(@NotNull ASTNode node) {
//    super(node);
//  }
//
//  public void accept(LuaElementVisitor visitor) {
//    visitor.visitReferenceExpression(this);
//  }
//
//  @Nullable
//  public PsiElement getReferenceNameElement() {
//    final ASTNode lastChild = getNode().getLastChildNode();
//    if (lastChild == null) return null;
//    for (IElementType elementType : LuaElementTypes.REFERENCE_NAMES.getTypes()) {
//      if (lastChild.getElementType() == elementType) return lastChild.getPsi();
//    }
//
//    return null;
//  }
//
//  @NotNull
//  public PsiReference getReference() {
//    PsiReference[] otherReferences = ReferenceProvidersRegistry.getReferencesFromProviders(this, LuaReferenceExpression.class);
//    PsiReference[] thisReference = {this};
//    return new PsiMultiReference(otherReferences.length == 0 ? thisReference : ArrayUtil.mergeArrays(thisReference, otherReferences, PsiReference.class), this);
//  }
//
//  @Nullable
//  public PsiElement getQualifier() {
//    return getQualifierExpression();
//  }
//
//  public String getReferenceName() {
//    PsiElement nameElement = getReferenceNameElement();
//    if (nameElement != null) {
//      if (nameElement.getNode().getElementType() == LuaElementTypes.mSTRING_LITERAL ||
//          nameElement.getNode().getElementType() == LuaElementTypes.mGSTRING_LITERAL) {
//        return GrStringUtil.removeQuotes(nameElement.getText());
//      }
//
//      return nameElement.getText();
//    }
//    return null;
//  }
//
//  public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
//    final PsiElement resolved = resolve();
//    if (resolved instanceof PsiMethod) {
//      final PsiMethod method = (PsiMethod) resolved;
//      final String oldName = getReferenceName();
//      if (!method.getName().equals(oldName)) { //was property reference to accessor
//        if (GroovyPropertyUtils.isSimplePropertyAccessor(method)) {
//          final String newPropertyName = PropertyUtil.getPropertyName(newElementName);
//          if (newPropertyName != null) {
//            return doHandleElementRename(newPropertyName);
//          } else {
//            //todo encapsulate fields:)
//          }
//        }
//      }
//    } else if (resolved instanceof LuaField && ((LuaField) resolved).isProperty()) {
//      final GrField field = (GrField) resolved;
//      final String oldName = getReferenceName();
//      if (oldName != null && !oldName.equals(field.getName())) { //was accessor reference to property
//        if (oldName.startsWith("get")) {
//          return doHandleElementRename("get" + StringUtil.capitalize(newElementName));
//        } else if (oldName.startsWith("set")) {
//          return doHandleElementRename("set" + StringUtil.capitalize(newElementName));
//        }
//      }
//    }
//
//    return doHandleElementRename(newElementName);
//  }
//
//  @Override
//  protected PsiElement bindWithQualifiedRef(String qName) {
//    final GrTypeArgumentList list = getTypeArgumentList();
//    final String typeArgs = (list != null) ? list.getText() : "";
//    final String text = qName + typeArgs;
//    GrReferenceExpression qualifiedRef = GroovyPsiElementFactory.getInstance(getProject()).createReferenceExpressionFromText(text);
//    getNode().getTreeParent().replaceChild(getNode(), qualifiedRef.getNode());
//    PsiUtil.shortenReference(qualifiedRef);
//    return qualifiedRef;
//  }
//
//  private PsiElement doHandleElementRename(String newElementName) throws IncorrectOperationException {
//    if (!PsiUtil.isValidReferenceName(newElementName)) {
//      PsiElement element = GroovyPsiElementFactory.getInstance(getProject()).createStringLiteral(newElementName);
//      getReferenceNameElement().replace(element);
//      return this;
//    }
//
//    return super.handleElementRename(newElementName);
//  }
//
//  public int getTextOffset() {
//    PsiElement parent = getParent();
//    TextRange range = getTextRange();
//    if (!(parent instanceof LuaAssignmentStatement) || !this.equals(((LuaAssignmentStatement) parent).getLeftExprs())) {
//      return range.getEndOffset(); //need this as a hack against TargetElementUtil
//    }
//
//    return range.getStartOffset();
//  }
//
//  public String toString() {
//    return "Reference expression";
//  }
//
//  @Nullable
//  public PsiElement resolve() {
//    ResolveResult[] results = getManager().getResolveCache().resolveWithCaching(this, RESOLVER, true, false);
//    return results.length == 1 ? results[0].getElement() : null;
//  }
//
//  private static final OurResolver RESOLVER = new OurResolver();
//
//  private static final OurTypesCalculator TYPES_CALCULATOR = new OurTypesCalculator();
//
//  public PsiType getNominalType() {
//    return LuaPsiManager.getInstance(getProject()).getTypeInferenceHelper().doWithInferenceDisabled(new Computable<PsiType>() {
//      @Nullable
//      public PsiType compute() {
//        return getNominalTypeImpl();
//      }
//    });
//  }
//
//  @Nullable
//  private PsiType getNominalTypeImpl() {
//    IElementType dotType = getDotTokenType();
//
//    final GroovyResolveResult resolveResult = advancedResolve();
//    PsiElement resolved = resolveResult.getElement();
//    if (dotType == GroovyTokenTypes.mMEMBER_POINTER) {
//      if (resolved instanceof PsiMethod) {
//        return GrClosureType.create((PsiMethod) resolved, resolveResult.getSubstitutor());
//      }
//      return JavaPsiFacade.getInstance(getProject()).getElementFactory().createTypeByFQClassName(GrClosableBlock.GROOVY_LANG_CLOSURE, getResolveScope());
//    }
//    PsiType result = null;
//    JavaPsiFacade facade = JavaPsiFacade.getInstance(getProject());
//    if (resolved == null && !"class".equals(getReferenceName())) {
//      resolved = getReference().resolve();
//    }
//    if (resolved instanceof PsiClass) {
//      if (getParent() instanceof GrReferenceExpression) {
//        result = facade.getElementFactory().createType((PsiClass) resolved);
//      } else {
//        PsiClass javaLangClass = facade.findClass(CommonClassNames.JAVA_LANG_CLASS, getResolveScope());
//        if (javaLangClass != null) {
//          PsiSubstitutor substitutor = PsiSubstitutor.EMPTY;
//          final PsiTypeParameter[] typeParameters = javaLangClass.getTypeParameters();
//          if (typeParameters.length == 1) {
//            substitutor = substitutor.put(typeParameters[0], facade.getElementFactory().createType((PsiClass) resolved));
//          }
//          result = facade.getElementFactory().createType(javaLangClass, substitutor);
//        }
//      }
//    } else if (resolved instanceof GrVariableBase) {
//      result = ((GrVariableBase) resolved).getDeclaredType();
//    } else if (resolved instanceof PsiVariable) {
//      result = ((PsiVariable) resolved).getType();
//    } else
//    if (resolved instanceof PsiMethod && !GroovyPsiManager.isTypeBeingInferred(resolved)) {
//      if (dotType == GroovyTokenTypes.mMEMBER_POINTER) {
//        return facade.getElementFactory().createTypeByFQClassName(GrClosableBlock.GROOVY_LANG_CLOSURE, getResolveScope());
//      }
//      PsiMethod method = (PsiMethod) resolved;
//      if (PropertyUtil.isSimplePropertySetter(method) && !method.getName().equals(getReferenceName())) {
//        result = method.getParameterList().getParameters()[0].getType();
//      } else {
//        PsiClass containingClass = method.getContainingClass();
//        if (containingClass != null && CommonClassNames.JAVA_LANG_OBJECT.equals(containingClass.getQualifiedName()) &&
//                "getClass".equals(method.getName())) {
//          result = getTypeForObjectGetClass(facade, method);
//        } else {
//          if (method instanceof GrAccessorMethod) {
//            result = ((GrAccessorMethod) method).getReturnTypeGroovy();
//          } else {
//            result = method.getReturnType();
//          }
//        }
//
//      }
//    } else if (resolved instanceof GrReferenceExpression) {
//      PsiElement parent = resolved.getParent();
//      if (parent instanceof GrAssignmentExpression) {
//        GrAssignmentExpression assignment = (GrAssignmentExpression) parent;
//        if (resolved.equals(assignment.getLValue())) {
//          GrExpression rValue = assignment.getRValue();
//          if (rValue != null) {
//            PsiType rType = rValue.getType();
//            if (rType != null) result = rType;
//          }
//        }
//      }
//    } else if (resolved == null) {
//      if ("class".equals(getReferenceName())) {
//        return JavaPsiFacade.getInstance(getProject()).getElementFactory().createTypeByFQClassName("java.lang.Class",
//                getResolveScope());
//      }
//
//      GrExpression qualifier = getQualifierExpression();
//      if (qualifier != null) {
//        PsiType qType = qualifier.getType();
//        if (qType instanceof PsiClassType) {
//          PsiClassType.ClassResolveResult qResult = ((PsiClassType) qType).resolveGenerics();
//          PsiClass clazz = qResult.getElement();
//          if (clazz != null) {
//            PsiClass mapClass = facade.findClass(CommonClassNames.JAVA_UTIL_MAP, getResolveScope());
//            if (mapClass != null && mapClass.getTypeParameters().length == 2) {
//              PsiSubstitutor substitutor = TypeConversionUtil.getClassSubstitutor(mapClass, clazz, qResult.getSubstitutor());
//              if (substitutor != null) {
//                return substitutor.substitute(mapClass.getTypeParameters()[1]);
//              }
//            }
//          }
//        }
//      }
//    }
//
//    if (result != null) {
//      result = resolveResult.getSubstitutor().substitute(result);
//      result = TypesUtil.boxPrimitiveType(result, getManager(), getResolveScope());
//    }
//    if (dotType != GroovyTokenTypes.mSPREAD_DOT) {
//      return result;
//    } else {
//      return ResolveUtil.getListTypeForSpreadOperator(this, result);
//    }
//  }
//
//  @Nullable
//  private PsiType getTypeForObjectGetClass(JavaPsiFacade facade, PsiMethod method) {
//    PsiType type = method.getReturnType();
//    if (type instanceof PsiClassType) {
//      PsiClass clazz = ((PsiClassType) type).resolve();
//      if (clazz != null &&
//              "java.lang.Class".equals(clazz.getQualifiedName())) {
//        PsiTypeParameter[] typeParameters = clazz.getTypeParameters();
//        if (typeParameters.length == 1) {
//          PsiClass qualifierClass = null;
//          GrExpression qualifier = getQualifierExpression();
//          if (qualifier != null) {
//            PsiType qualifierType = qualifier.getType();
//            if (qualifierType instanceof PsiClassType) {
//              qualifierClass = ((PsiClassType) qualifierType).resolve();
//            }
//          } else {
//            PsiNamedElement context = PsiTreeUtil.getParentOfType(this, PsiClass.class, GroovyFile.class);
//            if (context instanceof PsiClass) qualifierClass = (PsiClass) context;
//            else if (context instanceof GroovyFile) qualifierClass = ((GroovyFile) context).getScriptClass();
//          }
//
//          PsiSubstitutor substitutor = PsiSubstitutor.EMPTY;
//          if (qualifierClass != null) {
//            PsiType t = facade.getElementFactory().createType(qualifierClass);
//            substitutor = substitutor.put(typeParameters[0], t);
//          }
//          return facade.getElementFactory().createType(clazz, substitutor);
//        }
//      }
//    }
//    return type;
//  }
//
//  private static final class OurTypesCalculator implements Function<GrReferenceExpressionImpl, PsiType> {
//    @Nullable
//    public PsiType fun(GrReferenceExpressionImpl refExpr) {
//      final PsiType inferred = GroovyPsiManager.getInstance(refExpr.getProject()).getTypeInferenceHelper().getInferredType(refExpr);
//      final PsiType nominal = refExpr.getNominalTypeImpl();
//      if (inferred == null || PsiType.NULL.equals(inferred)) {
//        if (nominal == null) {
//          /*inside nested closure we could still try to infer from variable initializer.
//          * Not sound, but makes sense*/
//          final PsiElement resolved = refExpr.resolve();
//          if (resolved instanceof GrVariableBase) return ((GrVariableBase) resolved).getTypeGroovy();
//        }
//
//        return nominal;
//      }
//
//      if (nominal == null) return inferred;
//      if (!TypeConversionUtil.isAssignable(nominal, inferred, false)) {
//        final PsiElement resolved = refExpr.resolve();
//        if (resolved instanceof GrVariable && ((GrVariable) resolved).getTypeElementGroovy() != null) {
//          return nominal; //see GRVY-487
//        }
//      }
//      return inferred;
//    }
//  }
//
//  public PsiType getType() {
//    return GroovyPsiManager.getInstance(getProject()).getType(this, TYPES_CALCULATOR);
//  }
//
//  public GrExpression replaceWithExpression(@NotNull GrExpression newExpr, boolean removeUnnecessaryParentheses) {
//    return PsiImplUtil.replaceExpression(this, newExpr, removeUnnecessaryParentheses);
//  }
//
//  public String getName() {
//    return getReferenceName();
//  }
//
//  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
//    PsiElement nameElement = getReferenceNameElement();
//    ASTNode node = nameElement.getNode();
//    ASTNode newNameNode = GroovyPsiElementFactory.getInstance(getProject()).createReferenceNameFromText(name).getNode();
//    assert newNameNode != null && node != null;
//    node.getTreeParent().replaceChild(node, newNameNode);
//
//    return this;
//  }
//
//  private static class OurResolver implements ResolveCache.PolyVariantResolver<GrReferenceExpressionImpl> {
//    public GroovyResolveResult[] resolve(GrReferenceExpressionImpl refExpr, boolean incompleteCode) {
//      String name = refExpr.getReferenceName();
//      if (name == null) return GroovyResolveResult.EMPTY_ARRAY;
//
//      Kind kind = refExpr.getKind();
//      if (incompleteCode) {
//        ResolverProcessor processor = CompletionProcessor.createRefSameNameProcessor(refExpr, name);
//        resolveImpl(refExpr, processor);
//        GroovyResolveResult[] propertyCandidates = processor.getCandidates();
//        if (propertyCandidates.length > 0) return propertyCandidates;
//      }
//
//      if (kind == Kind.METHOD_OR_PROPERTY) {
//        final PsiType[] argTypes = PsiUtil.getArgumentTypes(refExpr, false);
//        PsiType thisType = getThisType(refExpr);
//
//        MethodResolverProcessor methodResolver =
//          new MethodResolverProcessor(name, refExpr, false, thisType, argTypes, refExpr.getTypeArguments());
//        resolveImpl(refExpr, methodResolver);
//        if (methodResolver.hasApplicableCandidates()) return methodResolver.getCandidates();
//
//        PropertyResolverProcessor propertyResolver = new PropertyResolverProcessor(name, refExpr);
//        resolveImpl(refExpr, propertyResolver);
//        if (propertyResolver.hasCandidates()) return propertyResolver.getCandidates();
//
//        final String[] names = GroovyPropertyUtils.suggestGettersName(name);
//        List<GroovyResolveResult> list = new ArrayList<GroovyResolveResult>();
//        for (String getterName : names) {
//          AccessorResolverProcessor getterResolver = new AccessorResolverProcessor(getterName, refExpr, true);
//          resolveImpl(refExpr, getterResolver);
//          list.addAll(Arrays.asList(getterResolver.getCandidates()));
//        }
//        if (list.size() > 0) return list.toArray(new GroovyResolveResult[list.size()]);
//        return methodResolver.getCandidates();
//      }
//      else if (kind == Kind.TYPE_OR_PROPERTY) {
//        ResolverProcessor processor = new PropertyResolverProcessor(name, refExpr);
//        resolveImpl(refExpr, processor);
//        final GroovyResolveResult[] fieldCandidates = processor.getCandidates();
//
//        //if reference expression is in class we need to return field instead of accessor method
//        for (GroovyResolveResult candidate : fieldCandidates) {
//          final PsiElement element = candidate.getElement();
//          if (element instanceof PsiField) {
//            final PsiClass containingClass = ((PsiField)element).getContainingClass();
//            if (containingClass != null && PsiTreeUtil.isAncestor(containingClass, refExpr, true)) return fieldCandidates;
//          } else {
//            return fieldCandidates;
//          }
//        }
//
//        final boolean isLValue = PsiUtil.isLValue(refExpr);
//        String[] names;
//        names = isLValue ? GroovyPropertyUtils.suggestSettersName(name) : GroovyPropertyUtils.suggestGettersName(name);
//        List<GroovyResolveResult> accessorResults = new ArrayList<GroovyResolveResult>();
//        for (String getterName : names) {
//          AccessorResolverProcessor accessorResolver = new AccessorResolverProcessor(getterName, refExpr, !isLValue);
//          resolveImpl(refExpr, accessorResolver);
//          final GroovyResolveResult[] candidates = accessorResolver.getCandidates(); //can be only one candidate
//          if (candidates.length == 1 && candidates[0].isStaticsOK()) {
//            return candidates;
//          }
//          else {
//            accessorResults.addAll(Arrays.asList(candidates));
//          }
//        }
//        if (fieldCandidates.length > 0) return fieldCandidates;
//        if (accessorResults.size() > 0) return new GroovyResolveResult[]{accessorResults.get(0)};
//
//        EnumSet<ClassHint.ResolveKind> kinds = refExpr.getParent() instanceof GrReferenceExpression
//                                               ? EnumSet.of(ClassHint.ResolveKind.CLASS, ClassHint.ResolveKind.PACKAGE)
//                                               : EnumSet.of(ClassHint.ResolveKind.CLASS);
//        ResolverProcessor classProcessor = new ClassResolverProcessor(refExpr.getReferenceName(), refExpr, kinds);
//        resolveImpl(refExpr, classProcessor);
//        return classProcessor.getCandidates();
//      }
//
//      return GroovyResolveResult.EMPTY_ARRAY;
//    }
//
//
//    private static void resolveImpl(GrReferenceExpressionImpl refExpr, ResolverProcessor processor) {
//      GrExpression qualifier = refExpr.getQualifierExpression();
//      if (qualifier == null) {
//        ResolveUtil.treeWalkUp(refExpr, processor, true);
//        if (!processor.hasCandidates()) {
//          qualifier = PsiImplUtil.getRuntimeQualifier(refExpr);
//          if (qualifier != null) {
//            processQualifier(refExpr, processor, qualifier);
//          }
//        }
//      } else {
//        if (refExpr.getDotTokenType() != GroovyTokenTypes.mSPREAD_DOT) {
//          processQualifier(refExpr, processor, qualifier);
//        } else {
//          processQualifierForSpreadDot(refExpr, processor, qualifier);
//        }
//      }
//    }
//
//    private static void processQualifierForSpreadDot(GrReferenceExpressionImpl refExpr, ResolverProcessor processor, GrExpression qualifier) {
//      PsiType qualifierType = qualifier.getType();
//      if (qualifierType instanceof PsiClassType) {
//        PsiClassType.ClassResolveResult result = ((PsiClassType) qualifierType).resolveGenerics();
//        PsiClass clazz = result.getElement();
//        if (clazz != null) {
//          PsiClass listClass = ResolveUtil.findListClass(refExpr.getManager(), refExpr.getResolveScope());
//          if (listClass != null && listClass.getTypeParameters().length == 1) {
//            PsiSubstitutor substitutor = TypeConversionUtil.getClassSubstitutor(listClass, clazz, result.getSubstitutor());
//            if (substitutor != null) {
//              PsiType componentType = substitutor.substitute(listClass.getTypeParameters()[0]);
//              if (componentType != null) {
//                processClassQualifierType(refExpr, processor, componentType);
//              }
//            }
//          }
//        }
//      } else if (qualifierType instanceof PsiArrayType) {
//        processClassQualifierType(refExpr, processor, ((PsiArrayType) qualifierType).getComponentType());
//      }
//    }
//
//    private static void processQualifier(GrReferenceExpressionImpl refExpr, ResolverProcessor processor, GrExpression qualifier) {
//      PsiType qualifierType = qualifier.getType();
//      if (qualifierType == null) {
//        if (qualifier instanceof GrReferenceExpression) {
//          PsiElement resolved = ((GrReferenceExpression) qualifier).resolve();
//          if (resolved instanceof PsiPackage) {
//            if (!resolved.processDeclarations(processor, ResolveState.initial(), null, refExpr)) //noinspection UnnecessaryReturnStatement
//              return;
//          }
//          else {
//            qualifierType = JavaPsiFacade.getInstance(refExpr.getProject()).getElementFactory()
//              .createTypeByFQClassName(CommonClassNames.JAVA_LANG_OBJECT, refExpr.getResolveScope());
//            processClassQualifierType(refExpr, processor, qualifierType);
//          }
//        }
//      } else {
//        if (qualifierType instanceof PsiIntersectionType) {
//          for (PsiType conjunct : ((PsiIntersectionType) qualifierType).getConjuncts()) {
//            processClassQualifierType(refExpr, processor, conjunct);
//          }
//        } else {
//          processClassQualifierType(refExpr, processor, qualifierType);
//          if (qualifier instanceof GrReferenceExpression) {
//            PsiElement resolved = ((GrReferenceExpression) qualifier).resolve();
//            if (resolved instanceof PsiClass) { //omitted .class
//              PsiClass javaLangClass = PsiUtil.getJavaLangClass(resolved, refExpr.getResolveScope());
//              if (javaLangClass != null) {
//                ResolveState state = ResolveState.initial();
//                PsiTypeParameter[] typeParameters = javaLangClass.getTypeParameters();
//                PsiSubstitutor substitutor = state.get(PsiSubstitutor.KEY);
//                if (substitutor == null) substitutor = PsiSubstitutor.EMPTY;
//                if (typeParameters.length == 1) {
//                  substitutor = substitutor.put(typeParameters[0], qualifierType);
//                  state = state.put(PsiSubstitutor.KEY, substitutor);
//                }
//                if (!javaLangClass.processDeclarations(processor, state, null, refExpr)) return;
//                PsiType javaLangClassType = JavaPsiFacade.getInstance(refExpr.getProject()).getElementFactory().createType(javaLangClass, substitutor);
//                ResolveUtil.processNonCodeMethods(javaLangClassType, processor, refExpr.getProject(), refExpr, false);
//              }
//            }
//          }
//        }
//      }
//    }
//
//    private static void processClassQualifierType(GrReferenceExpressionImpl refExpr, ResolverProcessor processor, PsiType qualifierType) {
//      Project project = refExpr.getProject();
//      if (qualifierType instanceof PsiClassType) {
//        PsiClassType.ClassResolveResult qualifierResult = ((PsiClassType) qualifierType).resolveGenerics();
//        PsiClass qualifierClass = qualifierResult.getElement();
//        if (qualifierClass != null) {
//          if (!qualifierClass.processDeclarations(processor,
//                  ResolveState.initial().put(PsiSubstitutor.KEY, qualifierResult.getSubstitutor()), null, refExpr))
//            return;
//        }
//        if (!ResolveUtil.processCategoryMembers(refExpr, processor)) return;
//      } else if (qualifierType instanceof PsiArrayType) {
//        final GrTypeDefinition arrayClass = GroovyPsiManager.getInstance(project).getArrayClass();
//        if (!arrayClass.processDeclarations(processor, ResolveState.initial(), null, refExpr)) return;
//      } else if (qualifierType instanceof PsiIntersectionType) {
//        for (PsiType conjunct : ((PsiIntersectionType) qualifierType).getConjuncts()) {
//          processClassQualifierType(refExpr, processor, conjunct);
//        }
//        return;
//      }
//
//      ResolveUtil.processNonCodeMethods(qualifierType, processor, project, refExpr, false);
//    }
//  }
//
//  private static PsiType getThisType(GrReferenceExpression refExpr) {
//    GrExpression qualifier = refExpr.getQualifierExpression();
//    if (qualifier != null) {
//      PsiType qType = qualifier.getType();
//      if (qType != null) return qType;
//    }
//
//    return TypesUtil.getJavaLangObject(refExpr);
//  }
//
//
//  enum Kind {
//    TYPE_OR_PROPERTY,
//    METHOD_OR_PROPERTY
//  }
//
//  Kind getKind() {
//    if (getDotTokenType() == GroovyTokenTypes.mMEMBER_POINTER) return Kind.METHOD_OR_PROPERTY;
//
//    PsiElement parent = getParent();
//    if (parent instanceof GrMethodCallExpression || parent instanceof GrApplicationStatement) {
//      return Kind.METHOD_OR_PROPERTY;
//    }
//
//    return Kind.TYPE_OR_PROPERTY;
//  }
//
//  @Nullable
//  public String getCanonicalText() {
//    return null;
//  }
//
//  public boolean isReferenceTo(PsiElement element) {
//    if (element instanceof PsiMethod && GroovyPropertyUtils.isSimplePropertyAccessor((PsiMethod) element)) {
//      final PsiElement target = resolve();
//      if (element instanceof GrAccessorMethod && getManager().areElementsEquivalent(((GrAccessorMethod)element).getProperty(), target)) {
//        return false;
//      }
//
//      return getManager().areElementsEquivalent(element, target);
//    }
//
//    if (element instanceof GrField && ((GrField) element).isProperty()) {
//      final PsiElement target = resolve();
//      if (getManager().areElementsEquivalent(element, target)) {
//        return true;
//      }
//
//      for (final GrAccessorMethod getter : ((GrField)element).getGetters()) {
//        if (getManager().areElementsEquivalent(getter, target)) {
//          return true;
//        }
//      }
//      return getManager().areElementsEquivalent(((GrField)element).getSetter(), target);
//    }
//
//    if (element instanceof PsiNamedElement && Comparing.equal(((PsiNamedElement) element).getName(), getReferenceName())) {
//      return getManager().areElementsEquivalent(element, resolve());
//    }
//    return false;
//  }
//
//  @NotNull
//  public Object[] getVariants() {
//    return ArrayUtil.EMPTY_OBJECT_ARRAY;
//  }
//
//
//  public boolean isSoft() {
//    return false;
//  }
//
//  public GrExpression getQualifierExpression() {
//    return findChildByClass(GrExpression.class);
//  }
//
//  public boolean isQualified() {
//    return getQualifierExpression() != null;
//  }
//
//  @Nullable
//  public PsiElement getDotToken() {
//    return findChildByType(GroovyTokenTypes.DOTS);
//  }
//
//  public void replaceDotToken(PsiElement newDot) {
//    if (newDot == null) return;
//    if (!GroovyTokenTypes.DOTS.contains(newDot.getNode().getElementType())) return;
//    final PsiElement oldDot = getDotToken();
//    if (oldDot == null) return;
//
//    getNode().replaceChild(oldDot.getNode(), newDot.getNode());
//  }
//
//  @Nullable
//  public IElementType getDotTokenType() {
//    PsiElement dot = getDotToken();
//    return dot == null ? null : dot.getNode().getElementType();
//  }
//
//  public GroovyResolveResult advancedResolve() {
//    ResolveResult[] results = getManager().getResolveCache().resolveWithCaching(this, RESOLVER, false, false);
//    return results.length == 1 ? (GroovyResolveResult) results[0] : GroovyResolveResult.EMPTY_RESULT;
//  }
//
//  @NotNull
//  public GroovyResolveResult[] multiResolve(boolean incomplete) {  //incomplete means we do not take arguments into consideration
//    return (GroovyResolveResult[]) getManager().getResolveCache().resolveWithCaching(this, RESOLVER, false, incomplete);
//  }
//
//  public void processVariants(Consumer<Object> consumer) {
//    CompleteReferenceExpression.processVariants(consumer, this);
//  }
//
//  @NotNull
//  public GroovyResolveResult[] getSameNameVariants() {
//    return RESOLVER.resolve(this, true);
//  }
//
//
//
//    public void setQualifierExpression(LuaReferenceExpression newQualifier) {
//    final LuaExpression oldQualifier = getQualifierExpression();
//    final ASTNode node = getNode();
//    final PsiElement refNameElement = getReferenceNameElement();
//    if (newQualifier == null) {
//      if (oldQualifier != null) {
//        if (refNameElement != null) {
//          node.removeRange(node.getFirstChildNode(), refNameElement.getNode());
//        }
//      }
//    } else {
//      if (oldQualifier != null) {
//        node.replaceChild(oldQualifier.getNode(), newQualifier.getNode());
//      } else {
//        if (refNameElement != null) {
//          node.addChild(newQualifier.getNode(), refNameElement.getNode());
//          node.addLeaf(LuaTokenTypes.DOT, ".", refNameElement.getNode());
//        }
//      }
//    }
//
//  }
//
//  public LuaReferenceExpression bindToElementViaStaticImport(@NotNull PsiClass qualifierClass) {
//    if (getQualifier() != null) {
//      throw new IncorrectOperationException("Reference has qualifier");
//    }
//
//    if (StringUtil.isEmpty(getReferenceName())) {
//      throw new IncorrectOperationException("Reference has empty name");
//    }
//    final PsiFile file = getContainingFile();
//    if (file instanceof LuaPsiFile) {
//      final GrImportStatement statement = GroovyPsiElementFactory.getInstance(getProject())
//        .createImportStatementFromText("import static " + qualifierClass.getQualifiedName() + "." + getReferenceName());
//      ((GroovyFile)file).addImport(statement);
//    }
//    return this;
//  }
//}