/*
 * Copyright 2009 Max Ishchenko
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

package com.sylvanaar.idea.Lua;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.sylvanaar.idea.Lua.lexer.LuaTokenTypes.*;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 13.07.2009
 * Time: 20:37:13
 */
public class LuaBraceMatcher implements PairedBraceMatcher {

    public static final BracePair[] BRACES =
            { new BracePair(LPAREN, RPAREN, false),
           // new BracePair(LBRACK, RBRACK, false),
            new BracePair(LCURLY, RCURLY, true),
            new BracePair(REPEAT, UNTIL, false),
            new BracePair(DO, END, false),
            new BracePair(IF, END, false),
            new BracePair(FUNCTION, END, true),
            new BracePair(LCURLY, RCURLY, false),
            new BracePair(LONGSTRING_BEGIN, LONGSTRING_END, true),
            new BracePair(LONGCOMMENT_BEGIN, LONGCOMMENT_END, true),                    
           };

    public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
        return openingBraceOffset;
    }

    public BracePair[] getPairs() {
        return BRACES;
    }

    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType) {
        return true;
    }
}
