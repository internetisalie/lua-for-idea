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

package com.sylvanaar.idea.Lua.lang.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.lang.Language;
import com.intellij.psi.FileViewProvider;
import com.sylvanaar.idea.Lua.lang.psi.LuaPsiFileBase;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaDeclarationExpression;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaFunctionDefinitionStatement;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaStatementElement;
import com.sylvanaar.idea.Lua.lang.psi.visitor.LuaElementVisitor;
import com.sylvanaar.idea.Lua.lang.psi.visitor.LuaRecursiveElementVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jon S Akhtar
 * Date: Jun 13, 2010
 * Time: 7:54:09 PM
 */
public abstract class LuaPsiFileBaseImpl extends PsiFileBase implements LuaPsiFileBase {
    private LuaFunctionDefinitionStatement[] funcs;

    protected LuaPsiFileBaseImpl(FileViewProvider viewProvider, @NotNull Language language) {
        super(viewProvider, language);
    }

    @Override
    public LuaStatementElement[] getStatements() {
        return findChildrenByClass(LuaStatementElement.class);
    }    

    @Override
    public LuaFunctionDefinitionStatement[] getFunctionDefs() {
        final List<LuaFunctionDefinitionStatement> funcs =
                new ArrayList<LuaFunctionDefinitionStatement>();

        LuaElementVisitor v = new LuaRecursiveElementVisitor() {
            public void visitFunctionDef(LuaFunctionDefinitionStatement e) {
                super.visitFunctionDef(e);
                funcs.add(e);
            }
        };

        v.visitElement(this);

        return funcs.toArray(new LuaFunctionDefinitionStatement[funcs.size()]);
    }

    @Override
    public LuaDeclarationExpression[] getDeclaredIdentifiers() {
        final List<LuaDeclarationExpression> decls =
                new ArrayList<LuaDeclarationExpression>();

        LuaElementVisitor v = new LuaElementVisitor() {
            public void visitDeclarationExpression(LuaDeclarationExpression e) {
                super.visitDeclarationExpression(e);
                decls.add(e);
            }
        };

        v.visitElement(this);

        return decls.toArray(new LuaDeclarationExpression[decls.size()]);
    }
}
