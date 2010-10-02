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
package com.sylvanaar.idea.Lua.lang.psi.controlFlow.impl;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiConstantEvaluationHelper;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.sylvanaar.idea.Lua.lang.parser.LuaElementTypes;
import com.sylvanaar.idea.Lua.lang.psi.LuaFunctionDefinition;
import com.sylvanaar.idea.Lua.lang.psi.LuaPsiElement;
import com.sylvanaar.idea.Lua.lang.psi.controlFlow.AfterCallInstruction;
import com.sylvanaar.idea.Lua.lang.psi.controlFlow.CallEnvironment;
import com.sylvanaar.idea.Lua.lang.psi.controlFlow.CallInstruction;
import com.sylvanaar.idea.Lua.lang.psi.controlFlow.Instruction;
import com.sylvanaar.idea.Lua.lang.psi.expressions.*;
import com.sylvanaar.idea.Lua.lang.psi.impl.PsiUtil;
import com.sylvanaar.idea.Lua.lang.psi.statements.*;
import com.sylvanaar.idea.Lua.lang.psi.visitor.LuaRecursiveElementVisitor;
import se.krka.kahlua.integration.annotations.LuaMethod;

import java.util.*;

/**
 * @author ven
 */
public class ControlFlowBuilder extends LuaRecursiveElementVisitor {
  private List<InstructionImpl> myInstructions;

  private Stack<InstructionImpl> myProcessingStack;
  private PsiConstantEvaluationHelper myConstantEvaluator;

  public ControlFlowBuilder(Project project) {
    myConstantEvaluator = JavaPsiFacade.getInstance(project).getConstantEvaluationHelper();

  }

  private class ExceptionInfo {
//    LuaCatchClause myClause;
//    List<InstructionImpl> myThrowers = new ArrayList<InstructionImpl>();
//
//    private ExceptionInfo(LuaCatchClause clause) {
//      myClause = clause;
//    }
  }

  private Stack<ExceptionInfo> myCatchedExceptionInfos;
  private InstructionImpl myHead;
  private boolean myNegate;
  private boolean myAssertionsOnly;

  private List<Pair<InstructionImpl, LuaPsiElement>> myPending;
  private LuaPsiElement myStartInScope;
  private LuaPsiElement myEndInScope;

  private boolean myIsInScope;
  private int myInstructionNumber;

  public void visitElement(LuaPsiElement element) {
    if (element == myStartInScope) {
      myIsInScope = true;
    }
    else if (element == myEndInScope) myIsInScope = false;

    if (myIsInScope) {
      super.visitElement(element);
    }
  }

  public void visitOpenBlock(LuaBlock block) {
    final PsiElement parent = block.getParent();
  //  final PsiElement lbrace = block.getLBrace();
//    if (lbrace != null && parent instanceof LuaMethod) {
//      final LuaParameter[] parameters = ((LuaMethod)parent).getParameters();
//      for (LuaParameter parameter : parameters) {
//        addNode(new ReadWriteVariableInstructionImpl(parameter, myInstructionNumber++));
//      }
//    }
  //  super.visitOpenBlock(block);

//    if (!(block.getParent() instanceof LuaBlockStatement && block.getParent().getParent() instanceof LuaLoopStatement)) {
//      final LuaStatement[] statements = block.getStatements();
//      if (statements.length > 0) {
//        final LuaStatement last = statements[statements.length - 1];
//        if (last instanceof LuaExpression) {
//          final MaybeReturnInstruction instruction = new MaybeReturnInstruction((LuaExpression)last, myInstructionNumber++);
//          checkPending(instruction);
//          addNode(instruction);
//        }
//      }
//    }
  }

  public Instruction[] buildControlFlow(LuaPsiElement scope, LuaPsiElement startInScope, LuaPsiElement endInScope) {
    myInstructions = new ArrayList<InstructionImpl>();
    myProcessingStack = new Stack<InstructionImpl>();
    myCatchedExceptionInfos = new Stack<ExceptionInfo>();
    myPending = new ArrayList<Pair<InstructionImpl, LuaPsiElement>>();
    myInstructionNumber = 0;
    myStartInScope = startInScope;
    myEndInScope = endInScope;
    myIsInScope = startInScope == null;

    startNode(null);
    if (scope instanceof LuaFunctionDefinition) {
      buildFlowForClosure((LuaFunctionDefinition)scope);
    }
    scope.accept(this);

    final InstructionImpl end = startNode(null);
    checkPending(end); //collect return edges
    return myInstructions.toArray(new Instruction[myInstructions.size()]);
  }

  private void buildFlowForClosure(final LuaFunctionDefinition closure) {
    for (LuaParameter parameter : closure.getParameters().getParameters()) {
      addNode(new ReadWriteVariableInstructionImpl(parameter, myInstructionNumber++));
    }

    final Set<String> names = new LinkedHashSet<String>();

    closure.accept(new LuaRecursiveElementVisitor() {
      public void visitReferenceExpression(LuaReferenceExpression refExpr) {
        super.visitReferenceExpression(refExpr);
        if (refExpr.getQualifierExpression() == null && !PsiUtil.isLValue(refExpr)) {
          if (!(refExpr.getParent() instanceof LuaFunctionCallExpression)) {
            final String refName = refExpr.getName();
            if (!hasDeclaredVariable(refName, closure, refExpr)) {
              names.add(refName);
            }
          }
        }
      }
    });

    names.add("owner");

    for (String name : names) {
      addNode(new ReadWriteVariableInstructionImpl(name, closure.getEndElement(), myInstructionNumber++, true));
    }

    PsiElement child = closure.getFirstChild();
    while (child != null) {
      if (child instanceof LuaPsiElement) {
        ((LuaPsiElement)child).accept(this);
      }
      child = child.getNextSibling();
    }
  }

  private void addNode(InstructionImpl instruction) {
    myInstructions.add(instruction);
    if (myHead != null) {
      addEdge(myHead, instruction);
    }
    myHead = instruction;
  }

  void addEdge(InstructionImpl beg, InstructionImpl end) {
    if (!beg.mySucc.contains(end)) {
      beg.mySucc.add(end);
    }

    if (!end.myPred.contains(beg)) {
      end.myPred.add(beg);
    }
  }

  public void visitClosure(LuaFunctionCallExpression closure) {
    //do not go inside closures
  }

  public void visitBreakStatement(LuaBreakStatement breakStatement) {
    super.visitBreakStatement(breakStatement);
    final LuaStatementElement target = breakStatement.findTargetStatement();
    if (target != null && myHead != null) {
      addPendingEdge(target, myHead);
    }

    flowAbrupted();
  }

  public void visitReturnStatement(LuaReturnStatement returnStatement) {
    boolean isNodeNeeded = myHead == null || myHead.getElement() != returnStatement;
    final LuaExpression value = returnStatement.getReturnValue();
    if (value != null) value.accept(this);

    if (isNodeNeeded) {
      InstructionImpl retInsn = startNode(returnStatement);
      addPendingEdge(null, myHead);
      finishNode(retInsn);
    }
    else {
      addPendingEdge(null, myHead);
    }
    flowAbrupted();
  }

//  public void visitAssertStatement(LuaAssertStatement assertStatement) {
//    final LuaExpression assertion = assertStatement.getAssertion();
//    if (assertion != null) {
//      assertion.accept(this);
//      final InstructionImpl assertInstruction = startNode(assertStatement);
//      final PsiType type = JavaPsiFacade.getInstance(assertStatement.getProject()).getElementFactory()
//        .createTypeByFQClassName("java.lang.AssertionError", assertStatement.getResolveScope());
//      ExceptionInfo info = findCatch(type);
//      if (info != null) {
//        info.myThrowers.add(assertInstruction);
//      }
//      else {
//        addPendingEdge(null, assertInstruction);
//      }
//      finishNode(assertInstruction);
//    }
//  }



  private void flowAbrupted() {
    myHead = null;
  }

//  @Nullable
//  private ExceptionInfo findCatch(PsiType thrownType) {
//    for (int i = myCatchedExceptionInfos.size() - 1; i >= 0; i--) {
//      final ExceptionInfo info = myCatchedExceptionInfos.get(i);
//      final LuaCatchClause clause = info.myClause;
//      final LuaParameter parameter = clause.getParameter();
//      if (parameter != null) {
//        final PsiType type = parameter.getType();
//        if (type.isAssignableFrom(thrownType)) return info;
//      }
//    }
//    return null;
//  }

//  public void visitLabeledStatement(LuaLabeledStatement labeledStatement) {
//    final InstructionImpl instruction = startNode(labeledStatement);
//    super.visitLabeledStatement(labeledStatement);
//    finishNode(instruction);
//  }

  public void visitAssignmentExpression(LuaAssignmentStatement expression) {
    LuaExpression lValue = expression.getLeftExprs();
    if (expression.getOperationTokenType() != LuaElementTypes.ASSIGN) {
      if (lValue instanceof LuaReferenceExpression) {
        ReadWriteVariableInstructionImpl instruction =
          new ReadWriteVariableInstructionImpl((LuaReferenceExpression)lValue, myInstructionNumber++, false);
        addNode(instruction);
        checkPending(instruction);
      }
    }

    LuaExpression rValue = expression.getRightExprs();
    if (rValue != null) {
      rValue.accept(this);
      lValue.accept(this);
    }
  }

  @Override
  public void visitParenthesizedExpression(LuaParenthesizedExpression expression) {
    expression.getOperand().accept(this);
  }

  @Override
  public void visitUnaryExpression(LuaUnaryExpression expression) {
    final LuaExpression operand = expression.getOperand();
    if (operand != null) {
      myNegate = !myNegate;
      operand.accept(this);
      myNegate = !myNegate;
    }
  }

  public void visitReferenceExpression(LuaReferenceExpression referenceExpression) {
    super.visitReferenceExpression(referenceExpression);
    if (referenceExpression.getQualifierExpression() == null) {
      if (isIncOrDecOperand(referenceExpression) && !myAssertionsOnly) {
        final ReadWriteVariableInstructionImpl i = new ReadWriteVariableInstructionImpl(referenceExpression, myInstructionNumber++, false);
        addNode(i);
        addNode(new ReadWriteVariableInstructionImpl(referenceExpression, myInstructionNumber++, true));
        checkPending(i);
      }
      else {
        final ReadWriteVariableInstructionImpl i =
          new ReadWriteVariableInstructionImpl(referenceExpression, myInstructionNumber++, !myAssertionsOnly && PsiUtil.isLValue(referenceExpression));
        addNode(i);
        checkPending(i);
      }
    }
  }

  private boolean isIncOrDecOperand(LuaReferenceExpression referenceExpression) {
    final PsiElement parent = referenceExpression.getParent();
    //if (parent instanceof LuaPostfixExpression) return true;
    if (parent instanceof LuaUnaryExpression) {
      final IElementType opType = ((LuaUnaryExpression)parent).getOperationTokenType();
      return opType == LuaElementTypes.MINUS;
    }

    return false;
  }

  public void visitIfStatement(LuaIfThenStatement ifStatement) {
    InstructionImpl ifInstruction = startNode(ifStatement);
    final LuaConditionalExpression condition = ifStatement.getIfCondition();

    final InstructionImpl head = myHead;
    final LuaStatementElement thenBranch = ifStatement.getIfBlock();
    if (thenBranch != null) {
      if (condition != null) {
        condition.accept(this);
      }
      thenBranch.accept(this);
      addPendingEdge(ifStatement, myHead);
    }

    // TODO: Loop of ElseIf's


    myHead = head;
    final LuaStatementElement elseBranch = ifStatement.getElseBlock();
    if (elseBranch != null) {
      if (condition != null) {
        myNegate = !myNegate;
        final boolean old = myAssertionsOnly;
        myAssertionsOnly = true;
        condition.accept(this);
        myNegate = !myNegate;
        myAssertionsOnly = old;
      }
      elseBranch.accept(this);
      addPendingEdge(ifStatement, myHead);
    }

    finishNode(ifInstruction);
  }

//  public void visitForStatement(LuaForStatement forStatement) {
//    final LuaForClause clause = forStatement.getClause();
//    if (clause instanceof LuaTraditionalForClause) {
//      for (LuaCondition initializer : ((LuaTraditionalForClause)clause).getInitialization()) {
//        initializer.accept(this);
//      }
//    }
//    else if (clause instanceof LuaForInClause) {
//      final LuaExpression expression = ((LuaForInClause)clause).getIteratedExpression();
//      if (expression != null) {
//        expression.accept(this);
//      }
//      for (LuaVariable variable : clause.getDeclaredVariables()) {
//        ReadWriteVariableInstructionImpl writeInsn = new ReadWriteVariableInstructionImpl(variable, myInstructionNumber++);
//        checkPending(writeInsn);
//        addNode(writeInsn);
//      }
//    }
//
//    InstructionImpl instruction = startNode(forStatement);
//    if (clause instanceof LuaTraditionalForClause) {
//      final LuaExpression condition = ((LuaTraditionalForClause)clause).getCondition();
//      if (condition != null) {
//        condition.accept(this);
//      }
//    }
//    addPendingEdge(forStatement, myHead); //break cycle
//
//    final LuaStatement body = forStatement.getBody();
//    if (body != null) {
//      InstructionImpl bodyInstruction = startNode(body);
//      body.accept(this);
//      finishNode(bodyInstruction);
//    }
//    checkPending(instruction); //check for breaks targeted here
//
//    if (clause instanceof LuaTraditionalForClause) {
//      for (LuaExpression expression : ((LuaTraditionalForClause)clause).getUpdate()) {
//        expression.accept(this);
//      }
//    }
//    if (myHead != null) addEdge(myHead, instruction);  //loop
//    flowAbrupted();
//
//    finishNode(instruction);
//  }

  private void checkPending(InstructionImpl instruction) {
    final PsiElement element = instruction.getElement();
    if (element == null) {
      //add all
      for (Pair<InstructionImpl, LuaPsiElement> pair : myPending) {
        addEdge(pair.getFirst(), instruction);
      }
      myPending.clear();
    }
    else {
      for (int i = myPending.size() - 1; i >= 0; i--) {
        final Pair<InstructionImpl, LuaPsiElement> pair = myPending.get(i);
        final PsiElement scopeWhenToAdd = pair.getSecond();
        if (scopeWhenToAdd == null) continue;
        if (!PsiTreeUtil.isAncestor(scopeWhenToAdd, element, false)) {
          addEdge(pair.getFirst(), instruction);
          myPending.remove(i);
        }
        else {
          break;
        }
      }
    }
  }

  //add edge when instruction.getElement() is not contained in scopeWhenAdded
  private void addPendingEdge(LuaPsiElement scopeWhenAdded, InstructionImpl instruction) {
    if (instruction == null) return;

    int i = 0;
    if (scopeWhenAdded != null) {
      for (; i < myPending.size(); i++) {
        Pair<InstructionImpl, LuaPsiElement> pair = myPending.get(i);

        final LuaPsiElement currScope = pair.getSecond();
        if (currScope == null) continue;
        if (!PsiTreeUtil.isAncestor(currScope, scopeWhenAdded, true)) break;
      }
    }
    myPending.add(i, new Pair<InstructionImpl, LuaPsiElement>(instruction, scopeWhenAdded));
  }

  public void visitWhileStatement(LuaWhileStatement whileStatement) {
    final InstructionImpl instruction = startNode(whileStatement);
    final LuaConditionalExpression condition = whileStatement.getCondition();
    if (condition != null) {
      condition.accept(this);
    }
    final boolean endless = Boolean.TRUE.equals(myConstantEvaluator.computeConstantExpression(condition));
    if (!endless) {
      addPendingEdge(whileStatement, myHead); //break
    }
    final LuaConditionalExpression body = whileStatement.getCondition();
    if (body != null) {
      body.accept(this);
    }
    checkPending(instruction); //check for breaks targeted here
    if (myHead != null) addEdge(myHead, instruction); //loop
    flowAbrupted();
    finishNode(instruction);
  }


//
//  public void visitTryStatement(LuaTryCatchStatement tryCatchStatement) {
//    final LuaOpenBlock tryBlock = tryCatchStatement.getTryBlock();
//    final LuaCatchClause[] catchClauses = tryCatchStatement.getCatchClauses();
//    final LuaFinallyClause finallyClause = tryCatchStatement.getFinallyClause();
//    for (int i = catchClauses.length - 1; i >= 0; i--) {
//      myCatchedExceptionInfos.push(new ExceptionInfo(catchClauses[i]));
//    }
//
//    List<Pair<InstructionImpl, LuaPsiElement>> oldPending = null;
//    if (finallyClause != null) {
//      //copy pending instructions
//      oldPending = myPending;
//      myPending = new ArrayList<Pair<InstructionImpl, LuaPsiElement>>();
//    }
//
//    InstructionImpl tryBeg = null;
//    InstructionImpl tryEnd = null;
//    if (tryBlock != null) {
//      tryBeg = startNode(tryBlock);
//      tryBlock.accept(this);
//      tryEnd = myHead;
//      finishNode(tryBeg);
//    }
//
//    InstructionImpl[][] throwers = new InstructionImpl[catchClauses.length][];
//    for (int i = 0; i < catchClauses.length; i++) {
//      final List<InstructionImpl> list = myCatchedExceptionInfos.pop().myThrowers;
//      throwers[i] = list.toArray(new InstructionImpl[list.size()]);
//    }
//
//    InstructionImpl[] catches = new InstructionImpl[catchClauses.length];
//
//    for (int i = 0; i < catchClauses.length; i++) {
//      flowAbrupted();
//      final InstructionImpl catchBeg = startNode(catchClauses[i]);
//      for (InstructionImpl thrower : throwers[i]) {
//        addEdge(thrower, catchBeg);
//      }
//
//      if (tryBeg != null) addEdge(tryBeg, catchBeg);
//      if (tryEnd != null) addEdge(tryEnd, catchBeg);
//      catchClauses[i].accept(this);
//      catches[i] = myHead;
//      finishNode(catchBeg);
//    }
//
//    if (finallyClause != null) {
//      flowAbrupted();
//      final InstructionImpl finallyInstruction = startNode(finallyClause, false);
//      Set<PostCallInstructionImpl> postCalls = new LinkedHashSet<PostCallInstructionImpl>();
//      addFinallyEdges(finallyInstruction, postCalls);
//
//      if (tryEnd != null) {
//        postCalls.add(addCallNode(finallyInstruction, tryCatchStatement, tryEnd));
//      }
//
//      for (InstructionImpl catchEnd : catches) {
//        if (catchEnd != null) {
//          postCalls.add(addCallNode(finallyInstruction, tryCatchStatement, catchEnd));
//        }
//      }
//      myHead = finallyInstruction;
//      finallyClause.accept(this);
//      final RetInstruction retInsn = new RetInstruction(myInstructionNumber++);
//      for (PostCallInstructionImpl postCall : postCalls) {
//        postCall.setReturnInstruction(retInsn);
//        addEdge(retInsn, postCall);
//      }
//      addNode(retInsn);
//      flowAbrupted();
//      finishNode(finallyInstruction);
//
//      assert oldPending != null;
//      oldPending.addAll(myPending);
//      myPending = oldPending;
//    }
//    else {
//      if (tryEnd != null) {
//        addPendingEdge(tryCatchStatement, tryEnd);
//      }
//    }
//  }

  private PostCallInstructionImpl addCallNode(InstructionImpl finallyInstruction,
                                              LuaPsiElement scopeWhenAddPending,
                                              InstructionImpl src) {
    flowAbrupted();
    final CallInstructionImpl call = new CallInstructionImpl(myInstructionNumber++, finallyInstruction);
    addNode(call);
    addEdge(call, finallyInstruction);
    addEdge(src, call);
    PostCallInstructionImpl postCall = new PostCallInstructionImpl(myInstructionNumber++, call);
    addNode(postCall);
    addPendingEdge(scopeWhenAddPending, postCall);
    return postCall;
  }

  private void addFinallyEdges(InstructionImpl finallyInstruction, Set<PostCallInstructionImpl> calls) {
    final List<Pair<InstructionImpl, LuaPsiElement>> copy = myPending;
    myPending = new ArrayList<Pair<InstructionImpl, LuaPsiElement>>();
    for (Pair<InstructionImpl, LuaPsiElement> pair : copy) {
      calls.add(addCallNode(finallyInstruction, pair.getSecond(), pair.getFirst()));
    }
  }

  private InstructionImpl startNode(LuaPsiElement element) {
    return startNode(element, true);
  }

  private InstructionImpl startNode(LuaPsiElement element, boolean checkPending) {
    final InstructionImpl instruction = new InstructionImpl(element, myInstructionNumber++);
    addNode(instruction);
    if (checkPending) checkPending(instruction);
    return myProcessingStack.push(instruction);
  }

  private void finishNode(InstructionImpl instruction) {
    assert instruction.equals(myProcessingStack.pop());
/*    myHead = myProcessingStack.peek();*/
  }
//
//  public void visitField(LuaField field) {
//  }

  public void visitParameter(LuaParameter parameter) {
  }

  public void visitMethod(LuaMethod method) {
  }

//  public void visitTypeDefinition(LuaTypeDefinition typeDefinition) {
//  }

  public void visitVariable(LuaVariable variable) {
    super.visitVariable(variable);
    if (variable.getInitializer() != null) {
      ReadWriteVariableInstructionImpl writeInsn = new ReadWriteVariableInstructionImpl(variable, myInstructionNumber++);
      checkPending(writeInsn);
      addNode(writeInsn);
    }
  }

  private InstructionImpl findInstruction(PsiElement element) {
    for (int i = myProcessingStack.size() - 1; i >= 0; i--) {
      InstructionImpl instruction = myProcessingStack.get(i);
      if (element.equals(instruction.getElement())) return instruction;
    }
    return null;
  }

  class CallInstructionImpl extends InstructionImpl implements CallInstruction {
    private final InstructionImpl myCallee;

    public String toString() {
      return super.toString() + " CALL " + myCallee.num();
    }

    public Iterable<? extends Instruction> succ(CallEnvironment env) {
      getStack(env, myCallee).push(this);
      return Collections.singletonList(myCallee);
    }

    public Iterable<? extends Instruction> allSucc() {
      return Collections.singletonList(myCallee);
    }

    protected String getElementPresentation() {
      return "";
    }

    CallInstructionImpl(int num, InstructionImpl callee) {
      super(null, num);
      myCallee = callee;
    }
  }

  class PostCallInstructionImpl extends InstructionImpl implements AfterCallInstruction {
    private final CallInstructionImpl myCall;
    private RetInstruction myReturnInsn;

    public String toString() {
      return super.toString() + "AFTER CALL " + myCall.num();
    }

    public Iterable<? extends Instruction> allPred() {
      return Collections.singletonList(myReturnInsn);
    }

    public Iterable<? extends Instruction> pred(CallEnvironment env) {
      getStack(env, myReturnInsn).push(myCall);
      return Collections.singletonList(myReturnInsn);
    }

    protected String getElementPresentation() {
      return "";
    }

    PostCallInstructionImpl(int num, CallInstructionImpl call) {
      super(null, num);
      myCall = call;
    }

    public void setReturnInstruction(RetInstruction retInstruction) {
      myReturnInsn = retInstruction;
    }
  }

  class RetInstruction extends InstructionImpl {
    RetInstruction(int num) {
      super(null, num);
    }

    public String toString() {
      return super.toString() + " RETURN";
    }

    protected String getElementPresentation() {
      return "";
    }

    public Iterable<? extends Instruction> succ(CallEnvironment env) {
      final Stack<CallInstruction> callStack = getStack(env, this);
      if (callStack.isEmpty()) return Collections.emptyList();     //can be true in case env was not populated (e.g. by DFA)

      final CallInstruction callInstruction = callStack.peek();
      final List<InstructionImpl> succ = ((CallInstructionImpl)callInstruction).mySucc;
      final Stack<CallInstruction> copy = (Stack<CallInstruction>)callStack.clone();
      copy.pop();
      for (InstructionImpl instruction : succ) {
        env.update(copy, instruction);
      }

      return succ;
    }
  }

  private static boolean hasDeclaredVariable(String name, LuaFunctionDefinition scope, PsiElement place) {
    PsiElement prev = null;
    while (place != null) {
      if (place instanceof LuaBlock) {
        LuaStatementElement[] statements = ((LuaBlock)place).getStatements();
        for (LuaStatementElement statement : statements) {
          if (statement == prev) break;
          if (statement instanceof LuaLocalDefinitionStatement) {
            LuaVariable[] variables = (LuaVariable[]) ((LuaLocalDefinitionStatement)statement).getDeclarations();
            for (LuaVariable variable : variables) {
              if (name.equals(variable.getName())) return true;
            }
          }
        }
      }

      if (place == scope) {
        break;
      }
      else {
        prev = place;
        place = place.getParent();
      }
    }

    return false;
  }
}
