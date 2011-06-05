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

package com.sylvanaar.idea.Lua.lang.formatter;

import com.intellij.application.options.CodeStyleAbstractConfigurable;
import com.intellij.application.options.CodeStyleAbstractPanel;
import com.intellij.openapi.options.Configurable;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider;
import com.sylvanaar.idea.Lua.LuaIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jon S Akhtar
 * Date: Sep 19, 2010
 * Time: 7:37:11 PM
 */
public class LuaCodeStyleSettingsProvider extends CodeStyleSettingsProvider {
  @NotNull
  public Configurable createSettingsPage(final CodeStyleSettings settings, final CodeStyleSettings originalSettings) {
    return new CodeStyleAbstractConfigurable(settings, originalSettings, "Lua"){
      protected CodeStyleAbstractPanel createPanel(final CodeStyleSettings settings) {
        return new CodeStyleLuaPanel(settings);
      }
      public Icon getIcon() {
        return LuaIcons.LUA_ICON;
      }

      public String getHelpTopic() {
        return null;
      }

        public JPanel createPanel() {
          return null;
        }
    };
  }

  @Override
  public String getConfigurableDisplayName() {
    return "Lua";
  }
}
