///*
// * Copyright 2000-2009 JetBrains s.r.o.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.sylvanaar.idea.Lua.intentions.control;
//
//import com.intellij.psi.PsiElement;
//import com.intellij.util.IncorrectOperationException;
//import com.sylvanaar.idea.Lua.intentions.base.Intention;
//import com.sylvanaar.idea.Lua.intentions.base.PsiElementPredicate;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.plugins.lua.intentions.base.*;
//import org.jetbrains.plugins.lua.lang.psi.api.statements.GrBlockStatement;
//import org.jetbrains.plugins.lua.lang.psi.api.statements.GrIfStatement;
//import org.jetbrains.plugins.lua.lang.psi.api.statements.GrStatement;
//import org.jetbrains.plugins.lua.lang.psi.api.statements.blocks.GrOpenBlock;
//
//public class MergeElseIfIntention extends Intention {
//
//  @NotNull
//  public PsiElementPredicate getElementPredicate() {
//    return new MergeElseIfPredicate();
//  }
//
//  public void processIntention(@NotNull PsiElement element)
//      throws IncorrectOperationException {
//    final GrIfStatement parentStatement = (GrIfStatement) element;
//    GrBlockStatement elseBlockStatement = (GrBlockStatement) parentStatement.getElseBranch();
//    assert elseBlockStatement != null;
//    final GrOpenBlock elseBranch = elseBlockStatement.getBlock();
//    final GrStatement elseBranchContents = elseBranch.getStatements()[0];
//    IntentionUtils.replaceStatement("if(" + parentStatement.getCondition().getText()+ ")"+ parentStatement.getThenBranch().getText() + "else " + elseBranchContents.getText(), parentStatement);
//  }
//}
