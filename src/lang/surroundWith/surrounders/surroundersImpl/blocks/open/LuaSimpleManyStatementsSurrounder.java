///*
//* Copyright 2010 Jon S Akhtar (Sylvanaar)
//*
//*   Licensed under the Apache License, Version 2.0 (the "License");
//*   you may not use this file except in compliance with the License.
//*   You may obtain a copy of the License at
//*
//*   http://www.apache.org/licenses/LICENSE-2.0
//*
//*   Unless required by applicable law or agreed to in writing, software
//*   distributed under the License is distributed on an "AS IS" BASIS,
//*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//*   See the License for the specific language governing permissions and
//*   limitations under the License.
//*/
//package com.sylvanaar.idea.Lua.lang.surroundWith.surrounders.surroundersImpl.blocks.open;
//
//
//import com.intellij.openapi.util.TextRange;
//import com.intellij.psi.PsiElement;
//import com.intellij.util.IncorrectOperationException;
//import com.sylvanaar.idea.Lua.lang.psi.LuaPsiElement;
//import com.sylvanaar.idea.Lua.lang.psi.LuaPsiElementFactory;
//import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaFunctionCallExpression;
//import com.sylvanaar.idea.Lua.lang.psi.statements.LuaFunctionCallStatement;
//import com.sylvanaar.idea.Lua.lang.surroundWith.surrounders.LuaManyStatementsSurrounder;
//
///**
//* This is a base class for simple "Many Statement" surrounds, such as with() and shouldFail().
//*
//* User: Hamlet D'Arcy
//* Date: Mar 18, 2009
//*/
//public abstract class LuaSimpleManyStatementsSurrounder extends LuaManyStatementsSurrounder {
//  protected final LuaPsiElement doSurroundElements(PsiElement[] elements) throws IncorrectOperationException {
//    LuaPsiElementFactory factory = LuaPsiElementFactory.getInstance(elements[0].getProject());
//    LuaFunctionCallStatement withCall = (LuaFunctionCallStatement) factory.createStatementFromText(getReplacementTokens());
//    addStatements(withCall.getClosureArguments()[0], elements);
//    return withCall;
//  }
//
//  protected abstract String getReplacementTokens();
//
//  protected final TextRange getSurroundSelectionRange(LuaPsiElement element) {
//    assert element instanceof LuaFunctionCallExpression;
//
//    GrMethodCallExpression withCall = (GrMethodCallExpression) element;
//    GrCondition condition = withCall.getExpressionArguments()[0];
//    int endOffset = condition.getTextRange().getStartOffset();
//
//    condition.getParent().getNode().removeChild(condition.getNode());
//
//    return new TextRange(endOffset, endOffset);
//  }
//
//}
