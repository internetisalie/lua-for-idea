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

package com.sylvanaar.idea.Lua.lang.psi.resolve.completion;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.sylvanaar.idea.Lua.editor.completion.LuaLookupElement;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaExpression;
import com.sylvanaar.idea.Lua.lang.psi.impl.symbols.LuaCompoundReferenceElementImpl;
import com.sylvanaar.idea.Lua.lang.psi.resolve.LuaResolveResult;
import com.sylvanaar.idea.Lua.lang.psi.resolve.processors.SymbolResolveProcessor;
import com.sylvanaar.idea.Lua.lang.psi.symbols.LuaCompoundIdentifier;
import com.sylvanaar.idea.Lua.lang.psi.symbols.LuaLocal;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * @author ilyas
 */
public class CompletionProcessor extends SymbolResolveProcessor {

    public CompletionProcessor(PsiElement myPlace) {
        super(null, myPlace, true);
        setFilter(false);
    }

//    public Collection<PsiElement> getResultCollection() {
//        return ContainerUtil.map(myCandidates, new Function<LuaResolveResult, PsiElement>() {
//            @Override
//            public PsiElement fun(LuaResolveResult luaResolveResult) {
//                return luaResolveResult.getElement();
//            }
//        });
//    }

    @Override
    public void addCandidate(LuaResolveResult candidate) {
        if (getPlace() instanceof LuaCompoundReferenceElementImpl)
            if (!(candidate.getElement() instanceof LuaCompoundIdentifier))
                return;

        if (getPlace() instanceof LuaLocal)
            if (candidate.getElement() instanceof LuaCompoundIdentifier)
                return;

        super.addCandidate(candidate);
    }

    public LookupElement[] getResultElements() {

        LookupElement[] res = new LookupElement[myCandidates.size()];

        Iterator<LuaResolveResult> iter = myCandidates.iterator();

        for (int i = 0; i < res.length; i++)
            res[i] = LuaLookupElement.createElement(((LuaExpression) iter.next().getElement()));

        return res;
    }

    public boolean execute(@NotNull PsiElement element, ResolveState state) {
        super.execute(element, state);
        return true;
    }
}
