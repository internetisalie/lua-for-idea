/*
 * Copyright 2003-2009 Dave Griffith, Bas Leijdekkers
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
package com.sylvanaar.idea.Lua.intentions.utils;


import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.sylvanaar.idea.Lua.lang.lexer.LuaTokenTypes;
import com.sylvanaar.idea.Lua.lang.psi.LuaPsiElement;
import com.sylvanaar.idea.Lua.lang.psi.LuaPsiType;
import com.sylvanaar.idea.Lua.lang.psi.expressions.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


public class ParenthesesUtils{

    private ParenthesesUtils(){}

    public static final int PARENTHESIZED_PRECEDENCE = 0;
    public static final int LITERAL_PRECEDENCE = 0;
    public static final int METHOD_CALL_PRECEDENCE = 1;
    public static final int POSTFIX_PRECEDENCE = 2;
    public static final int PREFIX_PRECEDENCE = 3;
    public static final int TYPE_CAST_PRECEDENCE = 4;
    public static final int CONDITIONAL_PRECEDENCE = 4;
    public static final int MULTIPLICATIVE_PRECEDENCE = 5;
    public static final int ADDITIVE_PRECEDENCE = 6;
    public static final int SHIFT_PRECEDENCE = 7;
    public static final int RELATIONAL_PRECEDENCE = 8;
    public static final int EQUALITY_PRECEDENCE = 9;
    public static final int BINARY_AND_PRECEDENCE = 10;
    public static final int BINARY_XOR_PRECEDENCE = 11;
    public static final int BINARY_OR_PRECEDENCE = 12;
    public static final int AND_PRECEDENCE = 13;
    public static final int OR_PRECEDENCE = 14;

    public static final int ASSIGNMENT_PRECEDENCE = 16;
    public static final int NUM_PRECEDENCES = 17;

    private static final Map<IElementType, Integer> s_binaryOperatorPrecedence =
            new HashMap<IElementType, Integer>(NUM_PRECEDENCES);

    static {
        s_binaryOperatorPrecedence.put(LuaTokenTypes.PLUS, ADDITIVE_PRECEDENCE);
        s_binaryOperatorPrecedence.put(LuaTokenTypes.MINUS, ADDITIVE_PRECEDENCE);
        s_binaryOperatorPrecedence.put(LuaTokenTypes.MULT, MULTIPLICATIVE_PRECEDENCE);
        s_binaryOperatorPrecedence.put(LuaTokenTypes.DIV, MULTIPLICATIVE_PRECEDENCE);
        s_binaryOperatorPrecedence.put(LuaTokenTypes.MOD, MULTIPLICATIVE_PRECEDENCE);
        s_binaryOperatorPrecedence.put(LuaTokenTypes.AND, BINARY_AND_PRECEDENCE);
        s_binaryOperatorPrecedence.put(LuaTokenTypes.OR, BINARY_OR_PRECEDENCE);
        s_binaryOperatorPrecedence.put(LuaTokenTypes.GT, RELATIONAL_PRECEDENCE);
        s_binaryOperatorPrecedence.put(LuaTokenTypes.GE, RELATIONAL_PRECEDENCE);
        s_binaryOperatorPrecedence.put(LuaTokenTypes.LT, RELATIONAL_PRECEDENCE);
        s_binaryOperatorPrecedence.put(LuaTokenTypes.LE, RELATIONAL_PRECEDENCE);
        s_binaryOperatorPrecedence.put(LuaTokenTypes.EQ, EQUALITY_PRECEDENCE);
        s_binaryOperatorPrecedence.put(LuaTokenTypes.NE, EQUALITY_PRECEDENCE);
    }

    @Nullable
    public static LuaExpression stripParentheses(
            @Nullable LuaExpression expression){
        while(expression instanceof LuaParenthesizedExpression){
            final LuaParenthesizedExpression parenthesizedExpression =
                    (LuaParenthesizedExpression)expression;
            expression = parenthesizedExpression.getEnclosedExpression();
        }
        return expression;
    }

    public static boolean isCommutativeBinaryOperator(
            @NotNull IElementType token) {
        return !(token.equals(LuaTokenTypes.MINUS) ||
                token.equals(LuaTokenTypes.DIV) ||
                token.equals(LuaTokenTypes.MOD));
    }

    public static int getPrecedence(LuaExpression expression){
        if(
           expression instanceof LuaLiteralExpression)




            return LITERAL_PRECEDENCE;
        
        if(expression instanceof LuaReferenceExpression){
            final LuaReferenceExpression referenceExpression =
                    (LuaReferenceExpression)expression;

                return LITERAL_PRECEDENCE;
        }
//        if(expression instanceof LuaMethodCallExpression ||
//                expression instanceof LuaNewExpression){
//            return METHOD_CALL_PRECEDENCE;
//        }
//        if(expression instanceof LuaTypeCastExpression){
//            return TYPE_CAST_PRECEDENCE;
//        }
//        if(expression instanceof LuaPrefixExpression){
//            return PREFIX_PRECEDENCE;
//        }
//        if(expression instanceof LuaPostfixExpression){
//            return POSTFIX_PRECEDENCE;
//        }
        if(expression instanceof LuaBinaryExpression){
            final LuaBinaryExpression binaryExpression =
                    (LuaBinaryExpression)expression;
            final IElementType sign =
                    binaryExpression.getOperationTokenType();
            return getPrecedenceForBinaryOperator(sign);
        }
        if(expression instanceof LuaConditionalExpression){
            return CONDITIONAL_PRECEDENCE;
        }
        if(expression instanceof LuaParenthesizedExpression){
            return PARENTHESIZED_PRECEDENCE;
        }
        return -1;
    }

    public static int getPrecedenceForBinaryOperator(@NotNull LuaTokenTypes sign){
        final IElementType tokenType = (IElementType) sign;
        return getPrecedenceForBinaryOperator(tokenType);
    }

    public static int getPrecedenceForBinaryOperator(IElementType operator) {
        final Integer precedence = s_binaryOperatorPrecedence.get(operator);
        return precedence.intValue();
    }

    public static void removeParentheses(@NotNull LuaExpression expression,
                                         boolean ignoreClarifyingParentheses)
            throws IncorrectOperationException {
        if (expression == null ) return;
        if(expression instanceof LuaReferenceExpression){
            final LuaReferenceExpression referenceExpression =
                    (LuaReferenceExpression)expression;
            removeParensFromReferenceExpression(referenceExpression,
                    ignoreClarifyingParentheses);
        }
        if(expression instanceof LuaBinaryExpression){
            final LuaBinaryExpression binaryExpression =
                    (LuaBinaryExpression)expression;
            removeParensFromBinaryExpression(binaryExpression,
                    ignoreClarifyingParentheses);
        }
        if(expression instanceof LuaConditionalExpression){
            final LuaConditionalExpression conditionalExpression =
                    (LuaConditionalExpression)expression;
            removeParensFromConditionalExpression(conditionalExpression,
                    ignoreClarifyingParentheses);
        }
        if(expression instanceof LuaParenthesizedExpression){
            final LuaParenthesizedExpression parenthesizedExpression =
                    (LuaParenthesizedExpression)expression;
            removeParensFromParenthesizedExpression(
                    parenthesizedExpression, ignoreClarifyingParentheses);
        }
        if(expression.getFirstChild() instanceof LuaExpression){
            removeParentheses((LuaExpression) expression.getFirstChild(), ignoreClarifyingParentheses);
        }
    }

    private static void removeParensFromReferenceExpression(
            @NotNull LuaReferenceExpression referenceExpression,
            boolean ignoreClarifyingParentheses)
            throws IncorrectOperationException {
        final LuaExpression qualifier = null;
        if(qualifier != null){
            removeParentheses(qualifier, ignoreClarifyingParentheses);
        }
    }

    private static void removeParensFromParenthesizedExpression(
            @NotNull LuaParenthesizedExpression parenthesizedExpression,
            boolean ignoreClarifyingParentheses)
            throws IncorrectOperationException {
        final LuaExpression body = parenthesizedExpression.getEnclosedExpression();
        if (body == null) {
            parenthesizedExpression.delete();
            return;
        }
        final LuaPsiElement parent = (LuaPsiElement) parenthesizedExpression.getParent();
        if(!(parent instanceof LuaExpression) ||
                parent instanceof LuaParenthesizedExpression){
            final LuaExpression newExpression =
                    (LuaExpression) parenthesizedExpression.replace(body);
            removeParentheses(newExpression, ignoreClarifyingParentheses);
            return;
        }
        final LuaExpression parentExpression = (LuaExpression) parent;
        final int parentPrecedence = getPrecedence(parentExpression);
        final int childPrecedence = getPrecedence(body);
        if(parentPrecedence < childPrecedence){
            final LuaPsiElement bodyParent = (LuaPsiElement) body.getParent();
            final LuaParenthesizedExpression newParenthesizedExpression =
                    (LuaParenthesizedExpression)
                            parenthesizedExpression.replace(bodyParent);
            final LuaExpression expression =
                    newParenthesizedExpression.getEnclosedExpression();
            if (expression != null) {
                removeParentheses(expression, ignoreClarifyingParentheses);
            }
        } else if(parentPrecedence == childPrecedence){
            if(parentExpression instanceof LuaBinaryExpression &&
               body instanceof LuaBinaryExpression){
                final LuaBinaryExpression parentBinaryExpression =
                        (LuaBinaryExpression)parentExpression;
                final IElementType parentOperator =
                        parentBinaryExpression.getOperationTokenType();
                final LuaBinaryExpression bodyBinaryExpression =
                        (LuaBinaryExpression)body;
                final IElementType bodyOperator =
                        bodyBinaryExpression.getOperationTokenType();
                final LuaPsiType parentType = parentBinaryExpression.getType();
                final LuaPsiType bodyType = body.getType();
                if(parentType != null && parentType.equals(bodyType) &&
                        parentOperator.equals(bodyOperator))
  {
                    final LuaExpression rhs =
                            parentBinaryExpression.getRightOperand();
                    if (!PsiTreeUtil.isAncestor(rhs, body, true) ||
                            isCommutativeBinaryOperator(bodyOperator)) {
                        // use addAfter() + delete() instead of replace() to
                        // workaround automatic insertion of parentheses by Lua
                  final LuaExpression newExpression = (LuaExpression)
                                parent.addAfter(body, parenthesizedExpression);
                        parenthesizedExpression.delete();
                        removeParentheses(newExpression,
                                ignoreClarifyingParentheses);
                        return; }
//                    }
//                }
                if (ignoreClarifyingParentheses) {
                    if (parentOperator.equals(bodyOperator)) {
                        removeParentheses(body, ignoreClarifyingParentheses);
                    }
                } else {
                    final LuaExpression newExpression = (LuaExpression)
                            parenthesizedExpression.replace(body);
                    removeParentheses(newExpression,
                            ignoreClarifyingParentheses);
                }
            } else{
                final LuaExpression newExpression =
                        (LuaExpression) parenthesizedExpression.replace(body);
                removeParentheses(newExpression, ignoreClarifyingParentheses);
            }
        } else {
            if (ignoreClarifyingParentheses &&
                    parent instanceof LuaBinaryExpression &&
                    (body instanceof LuaBinaryExpression /*||
                            body instanceof LuaInstanceOfExpression*/)) {
                removeParentheses(body, ignoreClarifyingParentheses);
            } else {
                final LuaExpression newExpression =
                        (LuaExpression) parenthesizedExpression.replace(body);
                removeParentheses(newExpression, ignoreClarifyingParentheses);
            }
        }
    } }

    private static void removeParensFromConditionalExpression(
            @NotNull LuaConditionalExpression conditionalExpression,
            boolean ignoreClarifyingParentheses)
            throws IncorrectOperationException {
        final LuaExpression condition = conditionalExpression.getCondition();
        removeParentheses(condition, ignoreClarifyingParentheses);
//        final LuaExpression thenBranch =
//                conditionalExpression.getThenExpression();
//        if (thenBranch != null) {
//            removeParentheses(thenBranch, ignoreClarifyingParentheses);
//        }
//        final LuaExpression elseBranch =
//                conditionalExpression.getElseExpression();
//        if (elseBranch != null) {
//            removeParentheses(elseBranch, ignoreClarifyingParentheses);
//        }
    }



    private static void removeParensFromBinaryExpression(
            @NotNull LuaBinaryExpression binaryExpression,
            boolean ignoreClarifyingParentheses)
            throws IncorrectOperationException {
        final LuaExpression lhs = binaryExpression.getLeftOperand();
        removeParentheses(lhs, ignoreClarifyingParentheses);
        final LuaExpression rhs = binaryExpression.getRightOperand();
        if (rhs != null) {
            removeParentheses(rhs, ignoreClarifyingParentheses);
        }
    }

//    private static void removeParensFromPostfixExpression(
//            @NotNull LuaPostfixExpression postfixExpression,
//            boolean ignoreClarifyingParentheses)
//            throws IncorrectOperationException {
//        final LuaExpression operand = postfixExpression.getOperand();
//        removeParentheses(operand, ignoreClarifyingParentheses);
//    }
//
//    private static void removeParensFromPrefixExpression(
//            @NotNull LuaPrefixExpression prefixExpression,
//            boolean ignoreClarifyingParentheses)
//            throws IncorrectOperationException {
//        final LuaExpression operand = prefixExpression.getOperand();
//        if (operand != null) {
//            removeParentheses(operand, ignoreClarifyingParentheses);
//        }
//    }

//    private static void removeParensFromArrayAccessExpression(
//            @NotNull LuaArrayAccessExpression arrayAccessExpression,
//            boolean ignoreClarifyingParentheses)
//            throws IncorrectOperationException {
//        final LuaExpression arrayExpression =
//                arrayAccessExpression.getArrayExpression();
//        removeParentheses(arrayExpression, ignoreClarifyingParentheses);
//        final LuaExpression indexExpression =
//                arrayAccessExpression.getIndexExpression();
//        if (indexExpression != null) {
//            removeParentheses(indexExpression, ignoreClarifyingParentheses);
//        }
//    }

//    private static void removeParensFromTypeCastExpression(
//            @NotNull LuaTypeCastExpression typeCastExpression,
//            boolean ignoreClarifyingParentheses)
//            throws IncorrectOperationException {
//        final LuaExpression operand = typeCastExpression.getOperand();
//        if (operand != null) {
//            removeParentheses(operand, ignoreClarifyingParentheses);
//        }
//    }

//    private static void removeParensFromArrayInitializerExpression(
//            @NotNull LuaArrayInitializerExpression arrayInitializerExpression,
//            boolean ignoreClarifyingParentheses)
//            throws IncorrectOperationException {
//        final LuaExpression[] initializers =
//                arrayInitializerExpression.getInitializers();
//        for (final LuaExpression initializer : initializers) {
//            removeParentheses(initializer, ignoreClarifyingParentheses);
//        }
//    }
//
//    private static void removeParensFromAssignmentExpression(
//            @NotNull LuaAssignmentExpression assignment,
//            boolean ignoreClarifyingParentheses)
//            throws IncorrectOperationException {
//        final LuaExpression lhs = assignment.getLExpression();
//        final LuaExpression rhs = assignment.getRExpression();
//        removeParentheses(lhs, ignoreClarifyingParentheses);
//        if (rhs != null) {
//            removeParentheses(rhs, ignoreClarifyingParentheses);
//        }
//    }

//    private static void removeParensFromNewExpression(
//            @NotNull LuaNewExpression newExpression,
//            boolean ignoreClarifyingParentheses)
//            throws IncorrectOperationException {
//        final LuaExpression[] dimensions = newExpression.getArrayDimensions();
//        for (LuaExpression dimension : dimensions) {
//            removeParentheses(dimension, ignoreClarifyingParentheses);
//        }
//        final LuaExpression qualifier = newExpression.getQualifier();
//        if(qualifier != null){
//            removeParentheses(qualifier, ignoreClarifyingParentheses);
//        }
//        final LuaExpression arrayInitializer =
//                newExpression.getArrayInitializer();
//        if(arrayInitializer != null){
//            removeParentheses(arrayInitializer, ignoreClarifyingParentheses);
//        }
//        final LuaExpressionList argumentList = newExpression.getArgumentList();
//        if(argumentList != null){
//            final LuaExpression[] arguments = argumentList.getExpressions();
//            for (LuaExpression argument : arguments) {
//                removeParentheses(argument, ignoreClarifyingParentheses);
//            }
//        }
//    }

//    private static void removeParensFromMethodCallExpression(
//            @NotNull LuaMethodCallExpression methodCallExpression,
//            boolean ignoreClarifyingParentheses)
//            throws IncorrectOperationException {
//        final LuaReferenceExpression target =
//                methodCallExpression.getMethodExpression();
//        final LuaExpressionList argumentList =
//                methodCallExpression.getArgumentList();
//        final LuaExpression[] arguments = argumentList.getExpressions();
//        removeParentheses(target, ignoreClarifyingParentheses);
//        for (final LuaExpression argument : arguments) {
//            removeParentheses(argument, ignoreClarifyingParentheses);
//        }
//    }

    public static boolean areParenthesesNeeded(
            LuaParenthesizedExpression expression,
            boolean ignoreClarifyingParentheses) {
        final LuaPsiElement parent = (LuaPsiElement) expression.getParent();
        final LuaExpression child = expression.getEnclosedExpression();
        return areParenthesesNeeded(child, parent,
                ignoreClarifyingParentheses);
    }

    public static boolean areParenthesesNeeded(
            LuaExpression expression, LuaPsiElement parentExpression,
            boolean ignoreClarifyingParentheses) {
        if (parentExpression instanceof LuaBinaryExpression) {
            final LuaBinaryExpression parentBinaryExpression =
                    (LuaBinaryExpression) parentExpression;
            if (expression instanceof LuaBinaryExpression) {
                final LuaBinaryExpression childBinaryExpression =
                        (LuaBinaryExpression)expression;
                final IElementType childOperator =
                        childBinaryExpression.getOperationTokenType();
                final IElementType parentOperator =
                        parentBinaryExpression.getOperationTokenType();
                if (ignoreClarifyingParentheses &&
                        !childOperator.equals(parentOperator)) {
                    return true;
                }
                final LuaPsiType parentType = parentBinaryExpression.getType();
                if (parentType == null) {
                    return true;
                }
                final LuaPsiType childType = childBinaryExpression.getType();
                if (!parentType.equals(childType)) {
                    return true;
                }
                if (PsiTreeUtil.isAncestor(parentBinaryExpression.getRightOperand(),
                        expression, false)) {
                    if (!isCommutativeBinaryOperator(parentOperator)) {
                        return true;
                    }
                }
                return false;
            } else if (expression instanceof LuaConditionalExpression) {
                if (PsiTreeUtil.isAncestor(parentBinaryExpression.getRightOperand(),
                        expression, false)) {
                    return true;
                }
            }
        }
//        else if (parentExpression instanceof LuaPrefixExpression) {
//            if (expression instanceof LuaBinaryExpression) {
//                return true;
//            }
//        }
        return false;
    }
}