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

package com.sylvanaar.idea.Lua.editor.completion;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.util.text.StringUtil;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaDeclarationExpression;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Jon S Akhtar
 * Date: Jun 16, 2010
 * Time: 10:50:28 AM
 */
public class LuaLookupElement extends LookupElement  {
    private String str;
    private Object obj;

    public LuaLookupElement(String str) {
        this.str = str;
    }

    public LuaLookupElement(LuaDeclarationExpression symbol) {
        this.str = StringUtil.notNullize(symbol.getDefinedName(), symbol.getText());
        this.obj = symbol;
    }

    @NotNull
    @Override
    public Object getObject() {
        if (obj == null)
            return super.getObject();

        return obj;
    }

    @NotNull
    public String getLookupString() {
        return str;
    }
}



