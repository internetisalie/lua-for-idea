/*
 * Copyright 2000-2009 JetBrains s.r.o.
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

package com.sylvanaar.idea.Lua.lang.luadoc.psi.impl;

import com.intellij.lang.ASTNode;
import com.sylvanaar.idea.Lua.lang.luadoc.psi.api.LuaDocMethodParameter;
import com.sylvanaar.idea.Lua.lang.luadoc.psi.api.LuaDocReferenceElement;
import com.sylvanaar.idea.Lua.lang.luadoc.psi.api.LuaDocTagValueToken;
import com.sylvanaar.idea.Lua.lang.psi.visitor.LuaElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;






/**
 * @author ilyas
 */
public class LuaDocMethodParameterImpl extends LuaDocPsiElementImpl implements LuaDocMethodParameter {

  public LuaDocMethodParameterImpl(@NotNull ASTNode node) {
    super(node);
  }

  public String toString() {
    return "LuaDocMethodParameter";
  }

  public void accept(LuaElementVisitor visitor) {
    visitor.visitDocMethodParameter(this);
  }

  @NotNull
  public LuaDocReferenceElement getTypeElement(){
    LuaDocReferenceElement child = findChildByClass(LuaDocReferenceElement.class);
    assert child != null;
    return child;
  }

  @Nullable
  public LuaDocTagValueToken getParameterElement(){
    return findChildByClass(LuaDocTagValueToken.class);
  }
}
