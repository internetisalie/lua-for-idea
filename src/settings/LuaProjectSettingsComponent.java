/*
 * Copyright 2009 Joachim Ansorg, mail@ansorg-it.com
 * File: LuaProjectSettingsComponent.java, Class: LuaProjectSettingsComponent
 * Last modified: 2010-02-11
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sylvanaar.idea.Lua.settings;

import com.intellij.openapi.components.*;
import org.jetbrains.annotations.NotNull;

/**
 * User: jansorg
 * Date: Oct 30, 2009
 * Time: 9:12:54 PM
 */
@State(
        name = "LuaSupportProjectSettings",
        storages = {
                @Storage(id = "default",
                        file = "$PROJECT_FILE$"),
                @Storage(id = "dir",
                        file = "$PROJECT_CONFIG_DIR$/Luasupport_project.xml",
                        scheme = StorageScheme.DIRECTORY_BASED)}
)
public class LuaProjectSettingsComponent implements PersistentStateComponent<LuaProjectSettings>, ProjectComponent {
    private LuaProjectSettings settings = new LuaProjectSettings();

    public LuaProjectSettings getState() {
        return settings;
    }

    public void loadState(LuaProjectSettings state) {
        this.settings = state;
    }

    public void projectOpened() {
    }

    public void projectClosed() {
    }

    @NotNull
    public String getComponentName() {
        return "LuaSupportProject";
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }
}
