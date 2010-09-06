/*
* Copyright 2000-2005 JetBrains s.r.o.
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
package com.sylvanaar.idea.Lua.lang.formatter.blocks;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.sylvanaar.idea.Lua.lang.formatter.LuaBlockVisitor;
import com.sylvanaar.idea.Lua.lang.formatter.processors.LuaSpacingProcessorBasic;
import com.sylvanaar.idea.Lua.lang.lexer.LuaTokenTypes;
import com.sylvanaar.idea.Lua.lang.parser.LuaElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author ven
 */
public class LuaBlock implements Block {
    private ASTNode myNode;

    private final CodeStyleSettings mySettings;

    private Alignment myAlignment;
    private Indent myIndent;
    private Wrap myWrap;
    private List<Block> mySubBlocks = null;

    public LuaBlock(final ASTNode node, final Alignment alignment, final Indent indent, final Wrap wrap, final CodeStyleSettings settings) {
        myAlignment = alignment;
        myIndent = indent;
        myNode = node;
        myWrap = wrap;
        mySettings = settings;
    }

    public ASTNode getNode() {
        return myNode;
    }

    @NotNull
    public TextRange getTextRange() {
        return myNode.getTextRange();
    }

    @NotNull
    public List<Block> getSubBlocks() {
        if (mySubBlocks == null) {
            LuaBlockVisitor visitor = new LuaBlockVisitor(getSettings());
            visitor.visit(myNode);
            mySubBlocks = visitor.getBlocks();
        }
        return mySubBlocks;
    }

    @Nullable
    public Wrap getWrap() {
        return myWrap;
    }

    @Nullable
    public Indent getIndent() {
        return myIndent;
    }

    @Nullable
    public Alignment getAlignment() {
        return myAlignment;
    }

    @Nullable
    public Spacing getSpacing(Block child1, Block child2) {
        if ((child1 instanceof LuaBlock) && (child2 instanceof LuaBlock)) {
            LuaSpacingProcessorBasic.getSpacing(((LuaBlock) child1), ((LuaBlock) child2), mySettings);
        }
        return null;


    //    return new LuaSpacingProcessor(getNode(), ((LuaBlock) child1).getNode(), ((LuaBlock) child2).getNode(), mySettings).getResult();
    }

    @NotNull
    public ChildAttributes getChildAttributes(final int newChildIndex) {
        Indent indent = null;
        if (myNode.getElementType() == LuaElementTypes.BLOCK) {
            indent = Indent.getNormalIndent();
        } else if (myNode.getElementType() == LuaElementTypes.FILE) {
            indent = Indent.getNoneIndent();
        }
//        else if (LuaElementTypes.SOURCE_ELEMENTS.isInSet(myNode.getElementType())) {
//            indent = Indent.getNoneIndent();
//        }

        Alignment alignment = null;
        final List<Block> subBlocks = getSubBlocks();
        for (int i = 0; i < newChildIndex; i++) {
            final Alignment childAlignment = subBlocks.get(i).getAlignment();
            if (childAlignment != null) {
                alignment = childAlignment;
                break;
            }
        }

        // in for loops, alignment is required only for items within parentheses
        if (myNode.getElementType() == LuaElementTypes.NUMERIC_FOR_BLOCK ||
                myNode.getElementType() == LuaElementTypes.GENERIC_FOR_BLOCK) {
            for (int i = 0; i < newChildIndex; i++) {
                if (((LuaBlock) subBlocks.get(i)).getNode().getElementType() == LuaTokenTypes.RPAREN) {
                    alignment = null;
                    break;
                }
            }
        }

        return new ChildAttributes(indent, alignment);
    }

    public boolean isIncomplete() {
        return isIncomplete(myNode);
    }

    private boolean isIncomplete(ASTNode node) {
        ASTNode lastChild = node.getLastChildNode();
        while (lastChild != null && lastChild.getPsi() instanceof PsiWhiteSpace) {
            lastChild = lastChild.getTreePrev();
        }
        if (lastChild == null) return false;
        if (lastChild.getPsi() instanceof PsiErrorElement) return true;
        return isIncomplete(lastChild);
    }

    public CodeStyleSettings getSettings() {
        return mySettings;
    }

    public boolean isLeaf() {
        return myNode.getFirstChildNode() == null;
    }
}
