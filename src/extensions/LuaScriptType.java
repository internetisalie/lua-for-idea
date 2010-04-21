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
package com.sylvanaar.idea.Lua.extensions;

import com.intellij.execution.Location;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.psi.search.GlobalSearchScope;
import com.sylvanaar.idea.Lua.LuaIcons;
import com.sylvanaar.idea.Lua.psi.LuaPsiFile;
import com.sylvanaar.idea.Lua.run.LuaRunConfiguration;
import com.sylvanaar.idea.Lua.run.LuaRunner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jon S Akhtar
 * Date: Apr 21, 2010
 * Time: 1:23:18 AM
 */
public abstract class LuaScriptType {
  public static final ExtensionPointName<LuaScriptExtensionPoint> EP_NAME = ExtensionPointName.create("com.sylvanaar.idea.Lua.extensions.LuaScriptExtensionPoint");
    
  private static final LuaScriptType DEFAULT_TYPE = new LuaScriptType() {
    @Override
    public boolean isSpecificScriptFile(LuaPsiFile file) {
      return true;
    }

    @NotNull
    @Override
    public Icon getScriptIcon() {
      return LuaIcons.LUA_ICON;
    }

    @Override
    public LuaRunner getRunner() {
      return new LuaRunner();
    }
  };

  @NotNull
  public static LuaScriptType getScriptType(@NotNull LuaPsiFile script) {
    assert script.isScript();
    for (final LuaScriptExtensionPoint typeEP : EP_NAME.getExtensions()) {
      final LuaScriptType descriptor = typeEP.getTypeDescriptor();
      if (descriptor.isSpecificScriptFile(script)) {
        return descriptor;
      }
    }
    return DEFAULT_TYPE;
  }


  public abstract boolean isSpecificScriptFile(LuaPsiFile file);

  @NotNull
  public abstract Icon getScriptIcon();

  public void tuneConfiguration(@NotNull LuaPsiFile file, @NotNull LuaRunConfiguration configuration, Location location) {
  }

  public GlobalSearchScope patchResolveScope(@NotNull LuaPsiFile file, @NotNull GlobalSearchScope baseScope) {
    return baseScope;
  }

  @Nullable
  public LuaRunner getRunner() {
    return null;
  }

  public List<String> appendImplicitImports(@NotNull LuaPsiFile file) {
    return Collections.emptyList();
  }
}