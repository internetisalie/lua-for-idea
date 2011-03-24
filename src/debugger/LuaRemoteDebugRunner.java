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

package com.sylvanaar.idea.Lua.debugger;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.GenericProgramRunner;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugProcessStarter;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XDebuggerManager;
import com.sylvanaar.idea.Lua.run.LuaCommandLineState;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Jon S Akhtar
 * Date: 3/19/11
 * Time: 6:42 PM
 */
public class LuaRemoteDebugRunner extends GenericProgramRunner {
    private static final Logger log = Logger.getInstance("#Lua.LuaRemoteDebugRunner");

    LuaCommandLineState luaCommandLineState;
    
    private final XDebugProcessStarter processStarter = new XDebugProcessStarter() {
        @NotNull
        @Override
        public XDebugProcess start(@NotNull XDebugSession session) throws ExecutionException {
            return new LuaDebugProcess(session, luaCommandLineState);
        }
    };

    @Override
    protected RunContentDescriptor doExecute(Project project, Executor executor, RunProfileState state,
                                             RunContentDescriptor contentToReuse,
                                             ExecutionEnvironment env) throws ExecutionException {
        FileDocumentManager.getInstance().saveAllDocuments();




        if (log.isDebugEnabled()) log.debug("Starting LuaDebugProcess");

        luaCommandLineState = (LuaCommandLineState) state;

        XDebugSession session = XDebuggerManager.getInstance(project).startSession(this, env, contentToReuse,
                processStarter);

        return session.getRunContentDescriptor();
    }

    @NotNull
    @Override
    public String getRunnerId() {
        return "com.sylvanaar.idea.Lua.debugger.LuaRemoteDebugRunner";
    }

    @Override
    public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
        return true;
    }
}
