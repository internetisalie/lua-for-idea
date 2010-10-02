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

import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.InheritanceUtil;
import com.intellij.psi.util.TypeConversionUtil;
import com.intellij.util.containers.HashMap;
import com.sylvanaar.idea.Lua.lang.psi.LuaPsiElement;
import com.sylvanaar.idea.Lua.lang.psi.LuaPsiType;
import com.sylvanaar.idea.Lua.lang.psi.LuaResolveResult;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaBinaryExpression;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaExpression;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaTuple;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaUnaryExpression;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TObjectIntHashMap;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static com.intellij.psi.CommonClassNames.*;
import static com.sylvanaar.idea.Lua.lang.lexer.LuaTokenTypes.*;


/**
 * @author ven
 */
public class TypesUtil {
  @NonNls
  public static final Map<String, PsiType> ourQNameToUnboxed = new HashMap<String, PsiType>();

  private TypesUtil() {
  }

  @Nullable
  public static PsiType getNumericResultType(LuaBinaryExpression binaryExpression, LuaPsiType lType) {
    final LuaExpression rop = binaryExpression.getRightOperand();
    LuaPsiType rType = rop == null ? null : rop.getLuaType();
    if (lType == null || rType == null) return null;
    String lCanonical = lType.getCanonicalText();
    String rCanonical = rType.getCanonicalText();
    if (TYPE_TO_RANK.containsKey(lCanonical) && TYPE_TO_RANK.containsKey(rCanonical)) {
      int lRank = TYPE_TO_RANK.get(lCanonical);
      int rRank = TYPE_TO_RANK.get(rCanonical);
      int resultRank = Math.max(lRank, rRank);
      String qName = RANK_TO_TYPE.get(resultRank);
      GlobalSearchScope scope = binaryExpression.getResolveScope();
      if (qName == null) return null;
      return JavaPsiFacade.getInstance(binaryExpression.getProject()).getElementFactory().createTypeByFQClassName(qName, scope);
    }

//    final PsiType type = getOverloadedOperatorType(lType, binaryExpression.getOperationTokenType(), binaryExpression, new PsiType[]{rType});
//    if (type != null) {
//      return type;
//    }

//    if (rType.equalsToText(LuaStringUtil.GROOVY_LANG_GSTRING)) {
//      PsiType gstringType = JavaPsiFacade.getInstance(binaryExpression.getProject()).getElementFactory()
//        .createTypeByFQClassName(LuaStringUtil.GROOVY_LANG_GSTRING, binaryExpression.getResolveScope());
//      return getOverloadedOperatorType(lType, binaryExpression.getOperationTokenType(), binaryExpression, new PsiType[]{gstringType});
//    }
    return null;
  }

  @Nullable
  public static PsiType getOverloadedOperatorType(PsiType thisType,
                                                  IElementType tokenType,
                                                  LuaPsiElement place,
                                                  PsiType[] argumentTypes) {
    return getOverloadedOperatorType(thisType, ourOperationsToOperatorNames.get(tokenType), place, argumentTypes);
  }

  @Nullable
  public static PsiType getOverloadedOperatorType(PsiType thisType, String operatorName, LuaPsiElement place, PsiType[] argumentTypes) {
    final LuaResolveResult[] candidates = getOverloadedOperatorCandidates(thisType, operatorName, place, argumentTypes);
    if (candidates.length == 1) {
      final PsiElement element = candidates[0].getElement();
      if (element instanceof PsiMethod) {
        return candidates[0].getSubstitutor().substitute(((PsiMethod)element).getReturnType());
      }
    }
    return null;
  }

  @NotNull
  public static LuaResolveResult[] getOverloadedOperatorCandidates(PsiType thisType,
                                                                      IElementType tokenType,
                                                                      LuaPsiElement place,
                                                                      PsiType[] argumentTypes) {
    return getOverloadedOperatorCandidates(thisType, ourOperationsToOperatorNames.get(tokenType), place, argumentTypes);
  }
  @NotNull
  public static LuaResolveResult[] getOverloadedOperatorCandidates(PsiType thisType,
                                                                      String operatorName,
                                                                      LuaPsiElement place,
                                                                      PsiType[] argumentTypes) {
//    if (operatorName != null) {
//      MethodResolverProcessor processor =
//        new MethodResolverProcessor(operatorName, place, false, thisType, argumentTypes, PsiType.EMPTY_ARRAY);
//      if (thisType instanceof PsiClassType) {
//        final PsiClassType classtype = (PsiClassType)thisType;
//        final PsiClassType.ClassResolveResult resolveResult = classtype.resolveGenerics();
//        final PsiClass lClass = resolveResult.getElement();
//        if (lClass != null) {
//          lClass
//            .processDeclarations(processor, ResolveState.initial().put(PsiSubstitutor.KEY, resolveResult.getSubstitutor()), null, place);
//        }
//      }
//
//    ///  ResolveUtil.processNonCodeMethods(thisType, processor, place.getProject(), place, false);
//      return processor.getCandidates();
//    }
    return LuaResolveResult.EMPTY_ARRAY;
  }

  private static final Map<IElementType, String> ourPrimitiveTypesToClassNames = new HashMap<IElementType, String>();
  private static final String NULL = "null";
  private static final String JAVA_MATH_BIG_DECIMAL = "java.math.BigDecimal";
  private static final String JAVA_MATH_BIG_INTEGER = "java.math.BigInteger";

  static {
    ourPrimitiveTypesToClassNames.put(STRING, JAVA_LANG_STRING);
    ourPrimitiveTypesToClassNames.put(LONGSTRING, JAVA_LANG_STRING);    
    ourPrimitiveTypesToClassNames.put(NUMBER, JAVA_LANG_DOUBLE);
    ourPrimitiveTypesToClassNames.put(FALSE, JAVA_LANG_BOOLEAN);
    ourPrimitiveTypesToClassNames.put(TRUE, JAVA_LANG_BOOLEAN);
    ourPrimitiveTypesToClassNames.put(NIL, NULL);
  }

  private static final Map<IElementType, String> ourOperationsToOperatorNames = new HashMap<IElementType, String>();

  static {
    ourOperationsToOperatorNames.put(PLUS, "plus");
    ourOperationsToOperatorNames.put(MINUS, "minus");
    ourOperationsToOperatorNames.put(AND, "and");
    ourOperationsToOperatorNames.put(OR, "or");

    ourOperationsToOperatorNames.put(DIV, "div");
    ourOperationsToOperatorNames.put(MOD, "mod");
    ourOperationsToOperatorNames.put(MULT, "multiply");


  }

  private static final TObjectIntHashMap<String> TYPE_TO_RANK = new TObjectIntHashMap<String>();

  static {
    TYPE_TO_RANK.put(JAVA_LANG_DOUBLE, 8);
    TYPE_TO_RANK.put(JAVA_LANG_NUMBER, 9);
  }

  static {
    ourQNameToUnboxed.put(JAVA_LANG_BOOLEAN, PsiType.BOOLEAN);
    ourQNameToUnboxed.put(JAVA_LANG_DOUBLE, PsiType.DOUBLE);
  }


  private static final TIntObjectHashMap<String> RANK_TO_TYPE = new TIntObjectHashMap<String>();

  static {
    RANK_TO_TYPE.put(7, JAVA_LANG_DOUBLE);
    RANK_TO_TYPE.put(8, JAVA_LANG_DOUBLE);
    RANK_TO_TYPE.put(9, JAVA_LANG_NUMBER);
  }

  public static boolean isAssignable(PsiType lType, PsiType rType, PsiManager manager, GlobalSearchScope scope) {
    if (isAssignableByMethodCallConversion(lType, rType, manager, scope)){
      return true;
    }
    //all numeric types are assignable
    if (isNumericType(lType)) {
      return isNumericType(rType) || rType.equals(PsiType.NULL);
    }
    else if (lType.equalsToText(JAVA_LANG_STRING)) {
      return true;
    }

    rType = boxPrimitiveType(rType, manager, scope);
    lType = boxPrimitiveType(lType, manager, scope);

    return lType.isAssignableFrom(rType);
  }

  public static boolean isAssignableByMethodCallConversion(PsiType lType, PsiType rType, PsiManager manager, GlobalSearchScope scope) {
    if (lType == null || rType == null) return false;

//    for (LuaTypeConverter converter : LuaTypeConverter.EP_NAME.getExtensions()) {
//      final Boolean result = converter.isConvertible(lType, rType, manager, scope);
//      if (result != null) {
//        return result;
//      }
//    }

    if (rType instanceof LuaTuple) {
      final LuaTuple tuple = (LuaTuple)rType;
      if (tuple.getComponentTypes().length == 0) {
        if (lType instanceof PsiArrayType ||
            InheritanceUtil.isInheritor(lType, JAVA_UTIL_LIST) ||
            InheritanceUtil.isInheritor(lType, JAVA_UTIL_SET)) {
          return true;
        }
      }
    }
//
//    if (rType.equalsToText(LuaStringUtil.GROOVY_LANG_GSTRING)) {
//      final PsiClass javaLangString = JavaPsiFacade.getInstance(manager.getProject()).findClass(JAVA_LANG_STRING, scope);
//      if (javaLangString != null &&
//          isAssignable(lType, JavaPsiFacade.getElementFactory(manager.getProject()).createType(javaLangString), manager, scope)) {
//        return true;
//      }
//    }

    if (isNumericType(lType) && isNumericType(rType)) {
      lType = unboxPrimitiveTypeWrapper(lType);
      if (lType.equalsToText(JAVA_MATH_BIG_DECIMAL)) lType = PsiType.DOUBLE;
      rType = unboxPrimitiveTypeWrapper(rType);
      if (rType.equalsToText(JAVA_MATH_BIG_DECIMAL)) rType = PsiType.DOUBLE;
    }
    else {
      rType = boxPrimitiveType(rType, manager, scope);
      lType = boxPrimitiveType(lType, manager, scope);
    }

    return TypeConversionUtil.isAssignable(lType, rType);

  }

  public static boolean isNumericType(PsiType type) {
    if (type instanceof PsiClassType) {
      return TYPE_TO_RANK.contains(type.getCanonicalText());
    }

    return type instanceof PsiPrimitiveType && TypeConversionUtil.isNumericType(type);
  }

  public static PsiType unboxPrimitiveTypeWraperAndEraseGenerics(PsiType result) {
    return TypeConversionUtil.erasure(unboxPrimitiveTypeWrapper(result));
  }

  public static PsiType unboxPrimitiveTypeWrapper(PsiType type) {
    if (type instanceof PsiClassType) {
      PsiType unboxed = ourQNameToUnboxed.get(type.getCanonicalText());
      if (unboxed != null) type = unboxed;
    }
    return type;
  }

  public static PsiType boxPrimitiveType(PsiType result, PsiManager manager, GlobalSearchScope resolveScope) {
    if (result instanceof PsiPrimitiveType && result != PsiType.VOID) {
      PsiPrimitiveType primitive = (PsiPrimitiveType)result;
      String boxedTypeName = primitive.getBoxedTypeName();
      if (boxedTypeName != null) {
        return JavaPsiFacade.getInstance(manager.getProject()).getElementFactory().createTypeByFQClassName(boxedTypeName, resolveScope);
      }
    }

    return result;
  }

  @Nullable
  public static PsiType getTypeForIncOrDecExpression(LuaUnaryExpression expr) {
//    final LuaExpression op = expr.getOperand();
//    if (op != null) {
//      final LuaPsiType opType = op.getLuaType();
//      if (opType != null) {
//        final PsiType overloaded = getOverloadedOperatorType(opType, expr.getOperationTokenType(), expr, PsiType.EMPTY_ARRAY);
//        if (overloaded != null) {
//          return overloaded;
//        }
////        if (isNumericType(opType)) {
////          return opType;
////        }
//      }
//    }

    return null;
  }

  public static PsiClassType createType(String fqName, PsiElement context) {
    JavaPsiFacade facade = JavaPsiFacade.getInstance(context.getProject());
    return facade.getElementFactory().createTypeByFQClassName(fqName, context.getResolveScope());
  }

  public static PsiClassType getJavaLangObject(PsiElement context) {
    return PsiType.getJavaLangObject(context.getManager(), context.getResolveScope());
  }

  @Nullable
  public static PsiType getLeastUpperBound(@NotNull PsiType type1, @NotNull PsiType type2, PsiManager manager) {
//    if (type1 instanceof LuaTuple && type2 instanceof LuaTuple) {
//      LuaTuple tuple1 = (LuaTuple)type1;
//      LuaTuple tuple2 = (LuaTuple)type2;
//      LuaPsiType[] components1 = tuple1.getComponentTypes();
//      LuaPsiType[] components2 = tuple2.getComponentTypes();
//      LuaPsiType[] components3 = new PsiType[Math.min(components1.length, components2.length)];
//      for (int i = 0; i < components3.length; i++) {
//        PsiType c1 = components1[i];
//        PsiType c2 = components2[i];
//        if (c1 == null || c2 == null) {
//          components3[i] = null;
//        }
//        else {
//          components3[i] = getLeastUpperBound(c1, c2, manager);
//        }
//      }
//      return new LuaTuple(components3, JavaPsiFacade.getInstance(manager.getProject()),
//                             tuple1.getScope().intersectWith(tuple2.getResolveScope()));
//    }
//    else if (type1 instanceof LuaClosureType && type2 instanceof LuaClosureType) {
//      LuaClosureType clType1 = (LuaClosureType)type1;
//      LuaClosureType clType2 = (LuaClosureType)type2;
//      LuaClosureSignature signature1=clType1.getSignature();
//      LuaClosureSignature signature2=clType2.getSignature();
//
//      LuaClosureParameter[] parameters1 = signature1.getParameters();
//      LuaClosureParameter[] parameters2 = signature2.getParameters();
//
//      if (parameters1.length == parameters2.length) {
//        final LuaClosureSignature signature = LuaClosureSignatureImpl.getLeastUpperBound(signature1, signature2, manager);
//        if (signature != null) {
//          GlobalSearchScope scope = clType1.getResolveScope().intersectWith(clType2.getResolveScope());
//          final LanguageLevel languageLevel = ComparatorUtil.max(clType1.getLanguageLevel(), clType2.getLanguageLevel());
//          return LuaClosureType.create(signature, manager, scope, languageLevel);
//        }
//      }
//    }
//    else if (LuaStringUtil.GROOVY_LANG_GSTRING.equals(type1.getCanonicalText()) &&
//             CommonClassNames.JAVA_LANG_STRING.equals(type2.getInternalCanonicalText())) {
//      return type2;
//    }
//    else if (LuaStringUtil.GROOVY_LANG_GSTRING.equals(type2.getCanonicalText()) &&
//             CommonClassNames.JAVA_LANG_STRING.equals(type1.getInternalCanonicalText())) {
//      return type1;
//    }

    return GenericsUtil.getLeastUpperBound(type1, type2, manager);
  }

  @Nullable
  public static PsiType getPsiType(PsiElement context, IElementType elemType) {
    if (elemType == NIL) {
      return PsiType.NULL;
    }
    final String typeName = getPsiTypeName(elemType);
    if (typeName != null) {
      return JavaPsiFacade.getElementFactory(context.getProject()).createTypeByFQClassName(typeName, context.getResolveScope());
    }
    return null;
  }

  @Nullable
  private static String getPsiTypeName(IElementType elemType) {
    return ourPrimitiveTypesToClassNames.get(elemType);
  }
}
