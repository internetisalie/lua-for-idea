
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
  package com.sylvanaar.idea.Lua.lang.formatter;

  import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.sylvanaar.idea.Lua.lang.formatter.blocks.LuaBlock;
import com.sylvanaar.idea.Lua.lang.lexer.LuaTokenTypes;
import com.sylvanaar.idea.Lua.lang.parser.LuaElementTypes;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaBinaryExpression;

import java.util.ArrayList;
import java.util.List;

  /**
   * @author ven
   */
  public class LuaBlockVisitor extends LuaNodeVisitor {
    List<Block> myBlocks = new ArrayList<Block>();
    private final CodeStyleSettings mySettings;

    public LuaBlockVisitor(CodeStyleSettings settings) {
      mySettings = settings;
    }

    public List<Block> getBlocks() {
      return myBlocks;
    }

    public void visitElement(final ASTNode node) {
      Alignment alignment = getDefaultAlignment(node);

      ASTNode child = node.getFirstChildNode();
      while(child != null) {
        if (child.getElementType() != LuaTokenTypes.WS &&
          child.getTextRange().getLength() > 0) {
          Wrap wrap = getWrap(node, child);
          Alignment childAlignment = alignmentProjection(alignment, node, child);
          Indent childIndent = getIndent(node, child);
          myBlocks.add(new LuaBlock(child, childAlignment, childIndent, wrap, mySettings));
        }
        child = child.getTreeNext();
      }
    }

    static Alignment getDefaultAlignment(final ASTNode node) {
      if (node.getElementType() == LuaElementTypes.NUMERIC_FOR_BLOCK ||
        node.getElementType() == LuaElementTypes.GENERIC_FOR_BLOCK ||
            node.getElementType() == LuaElementTypes.PARAMETER_LIST ||
          node.getElementType() == LuaElementTypes.BINARY_EXP ||        
          node.getElementType() == LuaElementTypes.CONDITIONAL_EXPR) {
        return Alignment.createAlignment();
      }

      return null;
    }

    private Indent getIndent(final ASTNode node, final ASTNode child) {
      if (node.getElementType() == LuaElementTypes.FILE ) {
        return Indent.getNoneIndent();
      }

      if (child.getElementType() == LuaElementTypes.BLOCK) {
        if (node.getElementType() == LuaElementTypes.FUNCTION_DEFINITION &&
          (mySettings.METHOD_BRACE_STYLE == CodeStyleSettings.NEXT_LINE_SHIFTED ||
           mySettings.METHOD_BRACE_STYLE == CodeStyleSettings.NEXT_LINE_SHIFTED2)) {
          return Indent.getNormalIndent();
        }
        if (mySettings.BRACE_STYLE == CodeStyleSettings.NEXT_LINE_SHIFTED ||
          mySettings.BRACE_STYLE == CodeStyleSettings.NEXT_LINE_SHIFTED2) {
          return Indent.getNormalIndent();
        }
        return Indent.getNoneIndent();
      }

//      if (node.getElementType() == LuaElementTypes.IF_STATEMENT) {
//        if (child.getElementType() == LuaTokenTypes.ELSE_KEYWORD) {
//          return Indent.getNoneIndent();
//        }
//        if (LuaElementTypes.SOURCE_ELEMENTS.isInSet(child.getElementType())) {
//          return Indent.getNormalIndent();
//        }
//      }
//
//      if (node.getElementType() == LuaElementTypes.WITH_STATEMENT &&
//          LuaElementTypes.SOURCE_ELEMENTS.isInSet(child.getElementType())) {
//        return Indent.getNormalIndent();
//      }
//
//      if (node.getElementType() == LuaElementTypes.DOWHILE_STATEMENT && child.getElementType() == LuaTokenTypes.WHILE_KEYWORD) {
//        return Indent.getNoneIndent();
//      }


      if (node.getElementType() == LuaElementTypes.BLOCK) {
        final ASTNode parent = node.getTreeParent();
        if (parent != null && parent.getElementType() == LuaElementTypes.FUNCTION_DEFINITION &&
          mySettings.METHOD_BRACE_STYLE == CodeStyleSettings.NEXT_LINE_SHIFTED) {
          return Indent.getNoneIndent();
        }
        if (mySettings.BRACE_STYLE == CodeStyleSettings.NEXT_LINE_SHIFTED) {
          return Indent.getNoneIndent();
        }
//        if (LuaElementTypes.SOURCE_ELEMENTS.isInSet(child.getElementType()) ||
//            LuaTokenTypes.COMMENTS.isInSet(child.getElementType())) {
//          return Indent.getNormalIndent();
//        }
        return Indent.getNoneIndent();
      }
//      else if (node.getPsi() instanceof LuaLoopStatement) {
//        if (child.getPsi() == ((LuaLoopStatement)node.getPsi()).getBody()) {
//          if (child.getElementType() == LuaElementTypes.BLOCK) {
//            return Indent.getNoneIndent();
//          } else {
//            return Indent.getNormalIndent();
//          }
//        }
//      }

      if (LuaTokenTypes.COMMENT_SET.isInSet(child.getElementType()) ||
          child.getElementType() == LuaElementTypes.LITERAL_EXPRESSION) {
        return Indent.getNoneIndent();
      }

//      if (node.getElementType() == LuaElementTypes.OBJECT_LITERAL_EXPRESSION) {
//        if (child.getElementType() == LuaTokenTypes.LBRACE ||
//          child.getElementType() == LuaTokenTypes.RBRACE) {
//          return Indent.getNoneIndent();
//        }
//        return Indent.getNormalIndent();
//      }
      return null;
    }

    private Alignment alignmentProjection(final Alignment defaultAlignment, final ASTNode parent, final ASTNode child) {
//      if (parent.getElementType() == LuaElementTypes.FOR_STATEMENT &&
//          (LuaElementTypes.EXPRESSION_SET.isInSet(child.getElementType()) ||
//          child.getElementType() == LuaElementTypes.VAR_STATEMENT)) {
//        return defaultAlignment;
//      }
//      else

      if (parent.getElementType() == LuaElementTypes.PARAMETER_LIST &&
               child.getElementType() == LuaElementTypes.PARAMETER) {
        return defaultAlignment;
      }
      else if (parent.getPsi() instanceof LuaBinaryExpression &&
               LuaElementTypes.EXPRESSION_SET.isInSet(child.getElementType())) {
        return defaultAlignment;
      }
      else if (parent.getElementType() == LuaElementTypes.CONDITIONAL_EXPR &&
               LuaElementTypes.EXPRESSION_SET.isInSet(child.getElementType())) {
        return defaultAlignment;
      }

      return null;
    }

    private Wrap getWrap(final ASTNode node, final ASTNode child) {
      WrapType wrapType = null;
//      if (node.getElementType() ==  LuaElementTypes.ASSIGNMENT_EXPRESSION) {
//        final LuaAssignmentExpression assignment = (LuaAssignmentExpression)node.getPsi();
//        if (child.getElementType() == assignment.getOperationSign() && mySettings.PLACE_ASSIGNMENT_SIGN_ON_NEXT_LINE ||
//            child.getPsi() == assignment.getROperand() && !mySettings.PLACE_ASSIGNMENT_SIGN_ON_NEXT_LINE) {
//          wrapType = Util.getWrapType(mySettings.ASSIGNMENT_WRAP);
//        }
//      } else
      if (node.getElementType() ==  LuaElementTypes.BINARY_EXP) {
        final LuaBinaryExpression binary = (LuaBinaryExpression)node.getPsi();
        if (child.getElementType() == binary.getOperationTokenType() && mySettings.BINARY_OPERATION_SIGN_ON_NEXT_LINE ||
            child.getPsi() == binary.getRightOperand() && !mySettings.BINARY_OPERATION_SIGN_ON_NEXT_LINE) {
          wrapType = Util.getWrapType(mySettings.BINARY_OPERATION_WRAP);
        }
      }
//      else if (node.getElementType() ==  LuaElementTypes.PARENTHESIZED_EXPRESSION) {
//        if (child == node.findChildByType(LuaTokenTypes.LPAREN) && mySettings.PARENTHESES_EXPRESSION_LPAREN_WRAP) {
//          wrapType = Wrap.NORMAL;
//        } else if (child == node.findChildByType(LuaTokenTypes.RPAREN) && mySettings.PARENTHESES_EXPRESSION_RPAREN_WRAP) {
//          wrapType = Wrap.ALWAYS;
//        }
//      }
//      else if (node.getElementType() ==  LuaElementTypes.ARRAY_LITERAL_EXPRESSION) {
//        if (child == node.findChildByType(LuaTokenTypes.LBRACK) && mySettings.ARRAY_INITIALIZER_LBRACE_ON_NEXT_LINE) {
//          wrapType = Wrap.NORMAL;
//        } else if (child == node.findChildByType(LuaTokenTypes.RPAREN) && mySettings.ARRAY_INITIALIZER_RBRACE_ON_NEXT_LINE) {
//          wrapType = Wrap.ALWAYS;
//        }
//      } else if (node.getElementType() ==  LuaElementTypes.CONDITIONAL_EXPR) {
//        final IElementType elementType = child.getElementType();
//        if ((mySettings.TERNARY_OPERATION_SIGNS_ON_NEXT_LINE && (elementType == LuaTokenTypes.QUEST || elementType == LuaTokenTypes.COLON)) ||
//            (!mySettings.TERNARY_OPERATION_SIGNS_ON_NEXT_LINE && child.getPsi() instanceof LuaExpression)) {
//          wrapType = Util.getWrapType(mySettings.TERNARY_OPERATION_WRAP);
//        }
//      }
      else if (node.getElementType() ==  LuaElementTypes.FUNCTION_CALL_EXPR) {
        if (child == node.findChildByType(LuaTokenTypes.LPAREN) && mySettings.CALL_PARAMETERS_LPAREN_ON_NEXT_LINE) {
          wrapType = Wrap.NORMAL;
        } else if (child == node.findChildByType(LuaTokenTypes.RPAREN) && mySettings.CALL_PARAMETERS_RPAREN_ON_NEXT_LINE) {
          wrapType = Wrap.ALWAYS;
        }
      }

      return wrapType == null ? null : Wrap.createWrap(wrapType, false);
    }
  }


