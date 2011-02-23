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

package com.sylvanaar.idea.Lua.lang.documentor;

import com.intellij.codeInsight.javadoc.JavaDocExternalFilter;
import com.intellij.openapi.project.Project;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by IntelliJ IDEA.
 * User: Jon S Akhtar
 * Date: 2/21/11
 * Time: 8:45 AM
 */
public class KahluaExternalDocFilter extends JavaDocExternalFilter {
    public KahluaExternalDocFilter(Project project) {
        super(project);
    }


    @Override
    protected void doBuildFromStream(String s, Reader reader, StringBuffer stringBuffer) throws IOException {
        super.doBuildFromStream(s, reader, stringBuffer);
    }
}
