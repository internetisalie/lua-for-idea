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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.extensions.AbstractExtensionPointBean;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.util.xmlb.annotations.Attribute;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Jon S Akhtar
 * Date: Apr 21, 2010
 * Time: 1:23:18 AM
 */
public class LuaScriptExtensionPoint extends AbstractExtensionPointBean {

  @Attribute("extensions")
  public String extensions;

  @Attribute("descriptorClass")
  public String descriptorClass;

  private final AtomicNotNullLazyValue<LuaScriptType> myInstance = new AtomicNotNullLazyValue<LuaScriptType>() {
    @NotNull
    @Override
    protected LuaScriptType compute() {
      try {
        return instantiate(descriptorClass, ApplicationManager.getApplication().getPicoContainer());
      }
      catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
  };

  public LuaScriptType getTypeDescriptor() {
    return myInstance.getValue();
  }
}

