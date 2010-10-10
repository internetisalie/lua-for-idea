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
package com.sylvanaar.idea.Lua.lang.surroundWith.descriptors;

import com.intellij.lang.ASTNode;
import com.intellij.lang.surroundWith.SurroundDescriptor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaStatementElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Dmitry.Krasilschikov
 * Date: 22.05.2007
 */
public abstract class LuaSurroundDescriptor implements SurroundDescriptor {
    @NotNull
    public PsiElement[] getElementsToSurround(PsiFile file, int startOffset, int endOffset) {
        LuaStatementElement[] statements = findStatementsInRange(file, startOffset, endOffset);

        if (statements == null) return PsiElement.EMPTY_ARRAY;
        return statements;
    }

    @Nullable
    private LuaStatementElement[] findStatementsInRange(PsiFile file, int startOffset, int endOffset) {

        LuaStatementElement statement = null;
        int endOffsetLocal = endOffset;
        int startOffsetLocal = startOffset;

        List<LuaStatementElement> statements = new ArrayList<LuaStatementElement>();
        do {
            PsiElement element1 = file.findElementAt(startOffsetLocal);
            PsiElement element2 = file.findElementAt(endOffsetLocal - 1);

            if (element1 == null) break;
            ASTNode node1 = element1.getNode();

            while (!(element1 instanceof LuaStatementElement))
                element1 = element1.getContext();

            statements.add((LuaStatementElement) element1);

            while (!(element2 instanceof LuaStatementElement))
                element1 = element2.getContext();

            statements.add(statement);

            startOffsetLocal = statement.getTextRange().getEndOffset();
        } while (true);

        return statements.toArray(new LuaStatementElement[0]);
    }
}
