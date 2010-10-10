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
package com.sylvanaar.idea.Lua.lang.surroundWith.descriptors;

import com.intellij.lang.surroundWith.Surrounder;
import com.sylvanaar.idea.Lua.lang.surroundWith.surrounders.surroundersImpl.blocks.open.LuaWithWhileSurrounder;
import com.sylvanaar.idea.Lua.lang.surroundWith.surrounders.surroundersImpl.expressions.LuaWithParenthesisExprSurrounder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Dmitry.Krasilschikov
 * Date: 22.05.2007
 */
public class LuaStmtsSurroundDescriptor extends LuaSurroundDescriptor {
  private static final Surrounder[] stmtsSurrounders = new Surrounder[]{
//
//      new LuaWithIfSurrounder(),
//      new LuaWithIfElseSurrounder(),
//

      new LuaWithWhileSurrounder(),
//
//      new LuaSurrounderByClosure(),
  };

      /********** ***********/
  private static final Surrounder[] exprSurrounders = new Surrounder[]{
      new LuaWithParenthesisExprSurrounder(),
//      new LuaWithTypeCastSurrounder(),
//
//      new LuaWithIfExprSurrounder(),
//      new LuaWithIfElseExprSurrounder(),
//
//      new LuaWithWhileExprSurrounder()
  };

  public static Surrounder[] getStmtsSurrounders() {
    return stmtsSurrounders;
  }

  public static Surrounder[] getExprSurrounders() {
    return exprSurrounders;
  }

  @NotNull
  public Surrounder[] getSurrounders() {
    List<Surrounder> surroundersList = new ArrayList<Surrounder>();
    surroundersList.addAll(Arrays.asList(exprSurrounders));
    surroundersList.addAll(Arrays.asList(stmtsSurrounders));
    return surroundersList.toArray(new Surrounder[0]);
  }
}
