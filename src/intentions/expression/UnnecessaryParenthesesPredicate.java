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
package com.sylvanaar.idea.Lua.intentions.expression;

import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.sylvanaar.idea.Lua.intentions.base.ErrorUtil;
import com.sylvanaar.idea.Lua.intentions.base.PsiElementPredicate;
import com.sylvanaar.idea.Lua.intentions.utils.ParenthesesUtils;
import com.sylvanaar.idea.Lua.lang.psi.LuaPsiElement;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaBinaryExpression;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaExpression;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaParenthesizedExpression;

class UnnecessaryParenthesesPredicate implements PsiElementPredicate {

    public boolean satisfiedBy(PsiElement element){
        if(!(element instanceof LuaParenthesizedExpression)){
            return false;
        }
        if(ErrorUtil.containsError(element)){
            return false;
        }
        final LuaParenthesizedExpression expression =
                (LuaParenthesizedExpression) element;
        final LuaPsiElement parent = (LuaPsiElement) expression.getParent();

        final LuaExpression body = expression.getEnclosedExpression();
        if(body instanceof LuaParenthesizedExpression){
            return true;
        }
//        if(!(parent instanceof LuaExpression)){
//            return true;
//        }
        final int parentPrecedence =
                ParenthesesUtils.getPrecedence((LuaExpression)parent);
        final int childPrecedence = ParenthesesUtils.getPrecedence((LuaExpression) body);
        if(parentPrecedence > childPrecedence){
            return true;
        } else if(parentPrecedence == childPrecedence){
            if(parent instanceof LuaBinaryExpression &&
                    body instanceof LuaBinaryExpression){
                final LuaBinaryExpression binaryExpression =
                        (LuaBinaryExpression) parent;
                final LuaPsiElement parentSign =
                        binaryExpression.getOperator();
                final IElementType parentOperator = parentSign.getNode().getElementType();
                final LuaPsiElement childSign =
                        ((LuaBinaryExpression) body).getOperator();
                final IElementType childOperator = childSign.getNode().getElementType();
                if(!parentOperator.equals(childOperator)){
                    return false;
                }
//                final LuaPsiType parentType = binaryExpression.getType();
//                final LuaPsiType bodyType = body.getType();
//                return parentType != null && parentType.equals(bodyType);

                return false;
            } else{
                return false;
            }
        } else{
            return false;
        }
    }
}