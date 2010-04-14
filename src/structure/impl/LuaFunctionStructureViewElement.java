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
package com.sylvanaar.idea.Lua.structure.impl;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiMethod;
import com.sylvanaar.idea.Lua.structure.LuaStructureViewTreeElement;
import com.sylvanaar.idea.Lua.structure.itemsPresentations.impl.LuaFunctionItemPresentation;

/**
 * User: Dmitry.Krasilschikov
 * Date: 30.10.2007
 */
public class LuaFunctionStructureViewElement extends LuaStructureViewTreeElement {
  private final boolean isInherit;

  public LuaFunctionStructureViewElement(PsiMethod element, boolean isInherit) {
    super(element);
    this.isInherit = isInherit;
  }

  public ItemPresentation getPresentation() {
    return new LuaFunctionItemPresentation(((PsiMethod) myElement), isInherit);
  }

  public TreeElement[] getChildren() {
    return StructureViewTreeElement.EMPTY_ARRAY;
  }

  public boolean isInherit() {
    return isInherit;
  }
}
