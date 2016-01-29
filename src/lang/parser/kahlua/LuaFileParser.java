/*
 * Copyright 2016 Jon S Akhtar (Sylvanaar)
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

package com.sylvanaar.idea.Lua.lang.parser.kahlua;

import com.intellij.diagnostic.PluginException;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.psi.tree.IElementType;
import com.sylvanaar.idea.Lua.lang.parser.LuaElementTypes;
import com.sylvanaar.idea.Lua.lang.parser.LuaPsiBuilder;
import org.jetbrains.annotations.NotNull;

public class LuaFileParser implements PsiParser {

    static Logger log = Logger.getInstance("Lua.parser.LuaFileParser");

    @NotNull
    @Override
    public ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder builder) {
        final KahluaParser parser = KahluaParser.standardParser(builder);
        final LuaPsiBuilder psiBuilder = parser.builder;

        try {
            final PsiBuilder.Marker rootMarker = psiBuilder.mark();

            FuncState funcstate = new FuncState(parser);

            /* main func. is always vararg */
            funcstate.isVararg = FuncState.VARARG_ISVARARG;
            funcstate.f.name = parser.source;

            psiBuilder.mark().done(LuaElementTypes.MAIN_CHUNK_VARARGS);

            parser.t = psiBuilder.getTokenType();

            if (log.isDebugEnabled())
                parser.builder.debug();
            parser.chunk();

            int pos = psiBuilder.getCurrentOffset();
            PsiBuilder.Marker mark = psiBuilder.mark();
            while (!psiBuilder.eof())
                psiBuilder.advanceLexer();

            if (psiBuilder.getCurrentOffset() > pos)
                mark.error("Unparsed code");
            else
                mark.drop();

            // lexstate.check(EMPTY_INPUT);
            parser.close_func();

            FuncState._assert(funcstate.prev == null);
            FuncState._assert(funcstate.f.numUpvalues == 0);
            FuncState._assert(parser.fs == null);

            rootMarker.done(root);
        } catch (ProcessCanceledException e) {
            throw e;
        } catch (Exception e) {
            throw new PluginException("Exception During Parse At Offset: " + builder.getCurrentOffset() + "\n\n" + builder.getOriginalText(), e, PluginId.getId("Lua"));
        }

        return builder.getTreeBuilt();
    }
}
