package com.sylvanaar.idea.Lua.lang.lexer;

import com.intellij.lang.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IReparseableElementType;
import com.sylvanaar.idea.Lua.LuaFileType;
import com.sylvanaar.idea.Lua.lang.parser.LuaElementTypes;
import com.sylvanaar.idea.Lua.lang.parser.LuaParserDefinition;
import com.sylvanaar.idea.Lua.lang.parser.kahlua.LuaFunctionParser;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

public class LuaReparseableElementType extends IReparseableElementType {

    private String debugName = null;
    private boolean leftBound = false;

    public LuaReparseableElementType(@NonNls String debugName) {
        this(debugName, false);
    }

    public LuaReparseableElementType(@NonNls String debugName, boolean leftBound) {
        super(debugName, LuaFileType.LUA_LANGUAGE);
        this.debugName = debugName;
        this.leftBound = leftBound;
    }

    public String toString() {
        return debugName;
    }

    @Override
    public boolean isLeftBound() {
        return leftBound;
    }

    @Override
    public boolean isParsable(CharSequence buffer, Language fileLanguage, Project project) {
        return true;
    }

    @Override
    public boolean isParsable(@Nullable ASTNode parent, CharSequence buffer, Language fileLanguage, Project project) {
        return true;
    }

    protected PsiParser getParser() {
        if (this == LuaElementTypes.FUNCTION_DEFINITION
            || this == LuaElementTypes.LOCAL_FUNCTION) {
            return new LuaFunctionParser();
        }
        return null;
    }

    @Nullable
    public ASTNode createNode(CharSequence text) {
        if (text == null)
            return null;

        final PsiParser parser = getParser();
        if (parser == null)
            return null;

        PsiBuilder builder = PsiBuilderFactory.getInstance().createBuilder(
                new LuaParserDefinition(),
                new LuaLexer(),
                text
        );

        return parser.parse(this, builder);
    }
}
