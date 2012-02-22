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

package com.sylvanaar.idea.Lua.console;

import com.intellij.execution.console.*;
import com.intellij.execution.process.*;
import com.intellij.execution.runners.*;
import com.intellij.openapi.project.Project;
import com.sylvanaar.idea.Lua.LuaFileType;

/**
 * Created by IntelliJ IDEA.
 * User: Jon S Akhtar
 * Date: 2/20/11
 * Time: 4:28 PM
 */
public class LuaLanguageConsole extends LanguageConsoleImpl {
    public LuaLanguageConsole(Project project, String title) {
        super(project, title, LuaFileType.LUA_LANGUAGE);
    }


    public static class View extends LanguageConsoleViewImpl {
        public View(Project project, String title) {
            super(project, new LuaLanguageConsole(project, title));
        }
    }

    public static class ActionHandler extends ConsoleExecuteActionHandler {
        public ActionHandler(ProcessHandler processHandler, boolean preserveMarkup) {
            super(processHandler, preserveMarkup);
        }
    }
}
