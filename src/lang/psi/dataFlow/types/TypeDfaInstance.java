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
package com.sylvanaar.idea.Lua.lang.psi.dataFlow.types;

import com.intellij.openapi.util.Computable;
import com.intellij.psi.*;
import com.intellij.psi.util.TypeConversionUtil;
import com.intellij.util.containers.HashMap;
import com.sylvanaar.idea.Lua.lang.psi.controlFlow.Instruction;
import com.sylvanaar.idea.Lua.lang.psi.controlFlow.ReadWriteVariableInstruction;
import com.sylvanaar.idea.Lua.lang.psi.dataFlow.DfaInstance;
import com.sylvanaar.idea.Lua.lang.psi.expressions.*;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaAssignmentStatement;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author ven
 */
public class TypeDfaInstance implements DfaInstance<Map<String, PsiType>> {
//  public void fun(Map<String, PsiType> map, Instruction instruction) {
//    if (instruction instanceof ReadWriteVariableInstruction && ((ReadWriteVariableInstruction) instruction).isWrite()) {
//      final PsiElement element = instruction.getElement();
//      if (element != null) {
//        final Computable<PsiType> computation = getInitializerTypeComputation(element);
//        if (computation != null) {
//          final TypeInferenceHelper helper = LuaPsiManager.getInstance(element.getProject()).getTypeInferenceHelper();
//
//          final PsiType type = helper.doInference(computation, map);
//
//          map.put(((ReadWriteVariableInstruction) instruction).getVariableName(), type);
//        }
//      }
//    }
////    if (instruction instanceof AssertionInstruction) {
////      final AssertionInstruction assertionInstruction = (AssertionInstruction)instruction;
////      final PsiElement element = assertionInstruction.getElement();
////      if (element instanceof LuaInstanceOfExpression && !assertionInstruction.isNegate()) {
////        final LuaExpression operand = ((LuaInstanceOfExpression)element).getOperand();
////        final LuaTypeElement typeElement = ((LuaInstanceOfExpression)element).getTypeElement();
////        if (typeElement != null) {
////          map.put(operand.getText(), typeElement.getType());
//        //}
//     // }
//    }
//  }
//
//  private Computable<PsiType> getInitializerTypeComputation(PsiElement element) {
//    LuaExpression initializer = null;
//    if (element instanceof LuaReferenceExpression && ((LuaReferenceExpression) element).getQualifierExpression() == null) {
//      final PsiElement parent = element.getParent();
//      if (parent instanceof LuaAssignmentStatement) {
//        initializer = ((LuaAssignmentStatement)parent).getRValue();
//      } else if (parent instanceof LuaTuple) {
//        LuaTuple list = (LuaTuple)parent;
//        if (list.getParent() instanceof LuaAssignmentStatement) {
//          LuaAssignmentStatement assignment = (LuaAssignmentStatement) list.getParent(); //multiple assignment
//          int idx = -1;
//          LuaExpression[] initializers = list.getInitializers();
//          for (int i = 0; i < initializers.length; i++) {
//            if (element == initializers[i]) {
//              idx = i;
//              break;
//            }
//          }
//          if (idx >= 0) {
//            final LuaExpression rValue = assignment.getRValue();
//            if (rValue != null) {
//              return getMultipleAssignmentTypeComputation(rValue, idx);
//            }
//          }
//        }
//      }
//    } else if (element instanceof LuaVariable && !(element instanceof LuaParameter)) {
//      initializer = ((LuaVariable) element).getInitializerLua();
//    }
//
//    final LuaExpression initializer1 = initializer;
//    return initializer == null ? null : new Computable<PsiType>() {
//      public PsiType compute() {
//        return initializer1.getType();
//      }
//    };
//  }
//
//  private Computable<PsiType> getMultipleAssignmentTypeComputation(final LuaExpression rValue, final int idx) {
//    return new Computable<PsiType>() {
//      public PsiType compute() {
//        PsiType rType = rValue.getType();
//        if (rType == null) return null;
//        if (rType instanceof LuaTuple) {
//          PsiType[] componentTypes = ((LuaTuple) rType).getComponentTypes();
//          if (idx < componentTypes.length) return componentTypes[idx];
//        } else if (rType instanceof PsiClassType) {
//          PsiClassType.ClassResolveResult result = ((PsiClassType) rType).resolveGenerics();
//          PsiClass clazz = result.getElement();
//          if (clazz != null) {
//            PsiClass listClass = JavaPsiFacade.getInstance(rValue.getProject()).findClass("java.util.List", rValue.getResolveScope());
//            if (listClass != null && listClass.getTypeParameters().length == 1) {
//              PsiSubstitutor substitutor = TypeConversionUtil.getClassSubstitutor(listClass, clazz, result.getSubstitutor());
//              if (substitutor != null) {
//                return substitutor.substitute(listClass.getTypeParameters()[0]);
//              }
//            }
//          }
//        }
     //   return null;
    //  }
  //  };
//  }

    @Override
    public void fun(Map<String, PsiType> stringPsiTypeMap, Instruction instruction) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
  public Map<String, PsiType> initial() {
    return new HashMap<String, PsiType>();
  }

  public boolean isForward() {
    return true;
  }
}
