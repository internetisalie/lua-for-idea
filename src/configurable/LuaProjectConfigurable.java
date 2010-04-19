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

package com.sylvanaar.idea.Lua.configurable;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.sylvanaar.idea.Lua.configurable.ui.LuaProfilesPanel;
import com.sylvanaar.idea.Lua.configurable.ui.ProjectSettingsPanel;

import javax.swing.*;

public class LuaProjectConfigurable extends SearchableConfigurable.Parent.Abstract {
  private final Project project;
  private ProjectSettingsPanel optionsPanel = null;
  LuaProfilesPanel myProfilesPanel = null;
  

  private static final Logger logger = Logger.getInstance(LuaProjectConfigurable.class.getName());



  public LuaProjectConfigurable(Project project) {

    this.project = project;
 
  }

  public String getDisplayName() {
    return "Copyright";
  }

  public Icon getIcon() {
    return null;
  }

  public String getHelpTopic() {
    return getId();
  }

  public JComponent createComponent() {
    logger.info("createComponent()");
    optionsPanel = new ProjectSettingsPanel(project, optionsPanel);
    return optionsPanel.getMainComponent();
  }

  public boolean isModified() {
    logger.info("isModified()");
    boolean res = false;
    if (optionsPanel != null) {
      res = optionsPanel.isModified();
    }

    logger.info("isModified() = " + res);

    return res;
  }

  public void apply() throws ConfigurationException {
    logger.info("apply()");
    if (optionsPanel != null) {
      optionsPanel.apply();
    }
  }

  public void reset() {
    logger.info("reset()");
    if (optionsPanel != null) {
      optionsPanel.reset();
    }
  }

  public void disposeUIResources() {
    optionsPanel = null;
  }

  public boolean hasOwnContent() {
    return true;
  }

  public boolean isVisible() {
    return true;
  }

  public String getId() {
    return "copyright";
  }

  public Runnable enableSearch(String option) {
    return null;
  }

  protected Configurable[] buildConfigurables() {
    return new Configurable[]{myProfilesPanel, new LuaProjectConfigurable(project)};
  }

}
