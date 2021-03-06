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

package com.sylvanaar.idea.Lua.lang.luadoc.psi.api;

import com.intellij.psi.PsiElement;
import com.sylvanaar.idea.Lua.lang.luadoc.psi.LuaDocElementWithDescriptions;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LuaDocComment extends /*PsiDocComment,*/ LuaDocPsiElement, LuaDocElementWithDescriptions {

    @Nullable
    LuaDocCommentOwner getOwner();

    @NotNull
    LuaDocTag[] getTags();

    @Nullable
    LuaDocTag findTagByName(@NonNls String name);

    @NotNull
    LuaDocTag[] findTagsByName(@NonNls String name);

    // Return the first line of the description
    // up to and including the first '.'
    @NotNull
    String getSummaryDescription();
}
