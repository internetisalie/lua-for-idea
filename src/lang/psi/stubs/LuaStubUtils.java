/*
 * Copyright 2011 Jon S Akhtar (Sylvanaar)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sylvanaar.idea.Lua.lang.psi.stubs;

import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * User: Dmitry.Krasilschikov
 * Date: 02.06.2009
 */
public class LuaStubUtils {
    public static void writeNullableString(StubOutputStream dataStream, @Nullable String typeText) throws IOException {
        dataStream.writeBoolean(typeText != null);
        if (typeText != null) {
            dataStream.writeUTFFast(typeText);
        }
    }

    @Nullable
    public static String readNullableString(StubInputStream dataStream) throws IOException {
        final boolean hasTypeText = dataStream.readBoolean();
        return hasTypeText ? dataStream.readUTFFast() : null;
    }
}
