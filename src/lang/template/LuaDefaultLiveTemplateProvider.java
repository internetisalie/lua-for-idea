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

package com.sylvanaar.idea.Lua.lang.template;

import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider;

/**
 * Created by IntelliJ IDEA.
 * User: Jon S Akhtar
 * Date: 2/23/11
 * Time: 1:03 AM
 */
public class LuaDefaultLiveTemplateProvider
    implements DefaultLiveTemplatesProvider {

    public String[] getDefaultLiveTemplateFiles()
    {
        return DEFAULT_TEMPLATES;
    }

    public String[] getHiddenLiveTemplateFiles()
    {
        return null;
    }

    private static final String DEFAULT_TEMPLATES[] = {
        "/liveTemplates/Lua",
    };

}
