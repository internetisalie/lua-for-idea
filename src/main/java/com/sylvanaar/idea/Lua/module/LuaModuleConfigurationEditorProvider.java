/*
 * Copyright 2011 Jon S Akhtar (Sylvanaar)
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

package com.sylvanaar.idea.Lua.module;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleConfigurationEditor;
import com.intellij.openapi.roots.ui.configuration.DefaultModuleConfigurationEditorFactory;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationEditorProvider;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationState;
import com.sylvanaar.idea.Lua.util.LuaModuleUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LuaModuleConfigurationEditorProvider implements ModuleConfigurationEditorProvider {
    public ModuleConfigurationEditor[] createEditors(@NotNull final ModuleConfigurationState state) {
        final Module module = state.getRootModel().getModule();
        if (!LuaModuleUtil.isLuaModule(module)) return ModuleConfigurationEditor.EMPTY;
        final DefaultModuleConfigurationEditorFactory editorFactory = DefaultModuleConfigurationEditorFactory.getInstance();
        final List<ModuleConfigurationEditor> editors = new ArrayList<ModuleConfigurationEditor>();

        editors.add(editorFactory.createModuleContentRootsEditor(state));
        editors.add(editorFactory.createClasspathEditor(state));

        return editors.toArray(new ModuleConfigurationEditor[editors.size()]);
    }
}