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

package com.sylvanaar.idea.Lua.lang.psi.util;

import com.intellij.util.IncorrectOperationException;
import com.sylvanaar.idea.Lua.lang.psi.LuaPsiElement;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaIdentifier;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaDeclarationStatement;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaStatementElement;


/**
 * @author ilyas
 */
public interface LuaVariableDeclarationOwner extends LuaPsiElement {

  /**
   * Removes variable from its declaration. In case of alone variablein declaration,
   * it also will be removed.
   * @param variable to remove
   * @throws com.intellij.util.IncorrectOperationException in case the operation cannot be performed
   */
  void removeVariable(LuaIdentifier variable);

  /**
   * Adds new variable declaration after anchor spectified. If anchor == null, adds variable at owner's first position
   * @param declaration declaration to insert
   * @param anchor Anchor after which new variabler declaration will be placed
   * @return inserted variable declaration
   * @throws com.intellij.util.IncorrectOperationException in case the operation cannot be performed
   */
  LuaDeclarationStatement addVariableDeclarationBefore(LuaDeclarationStatement declaration, LuaStatementElement anchor) throws IncorrectOperationException;

}