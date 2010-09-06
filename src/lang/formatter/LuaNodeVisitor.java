/*
* Copyright 2000-2005 JetBrains s.r.o.
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
package com.sylvanaar.idea.Lua.lang.formatter;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import com.sylvanaar.idea.Lua.lang.parser.LuaElementTypes;

public abstract class LuaNodeVisitor {
    public final void visit(ASTNode node) {
        final IElementType type = node.getElementType();
        if (type == LuaElementTypes.FILE) {
            visitFile(node);
        } else if (type == LuaElementTypes.FUNCTION_BLOCK) {
            visitFunctionDeclaration(node);
        } else if (type == LuaElementTypes.PARAMETER_LIST) {
            visitParameterList(node);
        } else if (type == LuaElementTypes.VARIABLE) {
            visitVariable(node);
        } else if (type == LuaElementTypes.PARAMETER) {
            visitParameter(node);
//        } else if (type == LuaElementTypes.ARGUMENT_LIST) {
//            visitArgumentList(node);
        } else if (type == LuaElementTypes.BLOCK) {
            visitBlock(node);
//        } else if (type == LuaElementTypes.EMPTY_STATEMENT) {
//            visitEmptyStatement(node);
//        } else if (type == LuaElementTypes.IF_STATEMENT) {
//            visitIfStatement(node);
//        } else if (type == LuaElementTypes.BREAK_STATEMENT) {
//            visitBreakStatement(node);
//        } else if (type == LuaElementTypes.WITH_STATEMENT) {
//            visitWithStatement(node);
        } else if (type == LuaElementTypes.RETURN_STATEMENT) {
            visitReturnStatement(node);
        } else if (type == LuaElementTypes.WHILE_BLOCK) {
            visitWhileStatement(node);
//        } else if (type == LuaElementTypes.DOWHILE_STATEMENT) {
//            visitDoWhileStatement(node);
        } else if (type == LuaElementTypes.NUMERIC_FOR_BLOCK) {
            visitForStatement(node);
        } else if (type == LuaElementTypes.GENERIC_FOR_BLOCK) {
            visitForInStatement(node);
        } else if (type == LuaElementTypes.LITERAL_EXPRESSION) {
            visitLiteralExpression(node);
//        } else if (type == LuaElementTypes.REFERENCE_EXPRESSION) {
//            visitReferenceExpression(node);
//        } else if (type == LuaElementTypes.PARENTHESIZED_EXPRESSION) {
//            visitParenthesizedExpression(node);
//        } else if (type == LuaElementTypes.ARRAY_LITERAL_EXPRESSION) {
//            visitArrayLiteralExpression(node);
//        } else if (type == LuaElementTypes.OBJECT_LITERAL_EXPRESSION) {
//            visitObjectLiteralExpression(node);
//        } else if (type == LuaElementTypes.PROPERTY) {
//            visitProperty(node);
//        } else if (type == LuaElementTypes.BINARY_EXPRESSION) {
//            visitBinaryExpression(node);
//        } else if (type == LuaElementTypes.ASSIGNMENT_EXPRESSION) {
//            visitAssignmentExpression(node);
//        } else if (type == LuaElementTypes.COMMA_EXPRESSION) {
//            visitCommaExpression(node);
//        } else if (type == LuaElementTypes.CONDITIONAL_EXPRESSION) {
//            visitConditionalExpression(node);
//        } else if (type == LuaElementTypes.POSTFIX_EXPRESSION) {
//            visitPostfixExpression(node);
//        } else if (type == LuaElementTypes.PREFIX_EXPRESSION) {
//            visitPrefixExpression(node);
//        } else if (type == LuaElementTypes.FUNCTION_EXPRESSION) {
//            visitFunctionExpression(node);
//        } else if (type == LuaElementTypes.NEW_EXPRESSION) {
//            visitNewExpression(node);
//        } else if (type == LuaElementTypes.INDEXED_PROPERTY_ACCESS_EXPRESSION) {
//            visitIndexedPropertyAccessExpression(node);
        } else if (type == LuaElementTypes.FUNCTION_CALL_EXPR) {
            visitCallExpression(node);
        } else {
            visitElement(node);
        }
    }

    public void visitFile(final ASTNode node) {
        visitElement(node);
    }

    public void visitCallExpression(final ASTNode node) {
        visitExpression(node);
    }

    public void visitIndexedPropertyAccessExpression(final ASTNode node) {
        visitExpression(node);
    }

    public void visitNewExpression(final ASTNode node) {
        visitExpression(node);
    }

    public void visitFunctionExpression(final ASTNode node) {
        visitExpression(node);
    }

    public void visitPrefixExpression(final ASTNode node) {
        visitExpression(node);
    }

    public void visitPostfixExpression(final ASTNode node) {
        visitExpression(node);
    }

    public void visitConditionalExpression(final ASTNode node) {
        visitExpression(node);
    }

    public void visitCommaExpression(final ASTNode node) {
        visitBinaryExpression(node);
    }

    public void visitAssignmentExpression(final ASTNode node) {
        visitBinaryExpression(node);
    }

    public void visitBinaryExpression(final ASTNode node) {
        visitExpression(node);
    }

    public void visitProperty(final ASTNode node) {
        visitElement(node);
    }

    public void visitObjectLiteralExpression(final ASTNode node) {
        visitExpression(node);
    }

    public void visitArrayLiteralExpression(final ASTNode node) {
        visitExpression(node);
    }

    public void visitParenthesizedExpression(final ASTNode node) {
        visitExpression(node);
    }

    public void visitReferenceExpression(final ASTNode node) {
        visitExpression(node);
    }

    public void visitLiteralExpression(final ASTNode node) {
        visitExpression(node);
    }

    public void visitThisExpression(final ASTNode node) {
        visitExpression(node);
    }

    public void visitForInStatement(final ASTNode node) {
        visitStatement(node);
    }

    public void visitForStatement(final ASTNode node) {
        visitStatement(node);
    }

    public void visitDoWhileStatement(final ASTNode node) {
        visitStatement(node);
    }

    public void visitWhileStatement(final ASTNode node) {
        visitStatement(node);
    }

    public void visitCaseClause(final ASTNode node) {
        visitElement(node);
    }

    public void visitSwitchStatement(final ASTNode node) {
        visitStatement(node);
    }

    public void visitCatchBlock(final ASTNode node) {
        visitElement(node);
    }

    public void visitTryStatement(final ASTNode node) {
        visitStatement(node);
    }

    public void visitThrowStatement(final ASTNode node) {
        visitStatement(node);
    }

    public void visitReturnStatement(final ASTNode node) {
        visitStatement(node);
    }

    public void visitWithStatement(final ASTNode node) {
        visitStatement(node);
    }

    public void visitBreakStatement(final ASTNode node) {
        visitStatement(node);
    }

    public void visitContinueStatement(final ASTNode node) {
        visitStatement(node);
    }

    public void visitIfStatement(final ASTNode node) {
        visitStatement(node);
    }

    public void visitEmptyStatement(final ASTNode node) {
        visitStatement(node);
    }

    public void visitVarStatement(final ASTNode node) {
        visitStatement(node);
    }

    public void visitExpressionStatement(final ASTNode node) {
        visitStatement(node);
    }

    public void visitLabeledStatement(final ASTNode node) {
        visitStatement(node);
    }

    public void visitBlock(final ASTNode node) {
        visitStatement(node);
    }

    public void visitArgumentList(final ASTNode node) {
        visitElement(node);
    }

    public void visitParameter(final ASTNode node) {
        visitVariable(node);
    }

    public void visitVariable(final ASTNode node) {
        visitElement(node);
    }

    public void visitParameterList(final ASTNode node) {
        visitElement(node);
    }

    public void visitElement(final ASTNode node) {

    }

    public void visitSourceElement(final ASTNode node) {
        visitElement(node);
    }

    public void visitFunctionDeclaration(final ASTNode node) {
        visitSourceElement(node);
    }

    public void visitStatement(final ASTNode node) {
        visitSourceElement(node);
    }

    public void visitExpression(final ASTNode node) {
        visitElement(node);
    }
}


