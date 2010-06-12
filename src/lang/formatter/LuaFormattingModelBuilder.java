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

package com.sylvanaar.idea.Lua.lang.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.sylvanaar.idea.Lua.LuaFileType;
import com.sylvanaar.idea.Lua.lang.formatter.blocks.LuaBlock;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 13.07.2009
 * Time: 22:51:23
 */
public class LuaFormattingModelBuilder implements FormattingModelBuilder {

    @NotNull
    public FormattingModel createModel(PsiElement element, CodeStyleSettings settings) {

        ASTNode node = element.getNode();
        assert node != null;
        PsiFile containingFile = element.getContainingFile().getViewProvider().getPsi(LuaFileType.LUA_LANGUAGE);
        ASTNode astNode = containingFile.getNode();
        assert astNode != null;

        return FormattingModelProvider.createFormattingModelForPsiFile(containingFile,
                new LuaBlock(astNode, null, Indent.getAbsoluteNoneIndent(), null, settings), settings);

    }

    public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset) {
        return null;
    }

}
