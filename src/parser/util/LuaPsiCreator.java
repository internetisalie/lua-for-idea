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

package com.sylvanaar.idea.Lua.parser.util;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.sylvanaar.idea.Lua.lexer.LuaElementType;
import com.sylvanaar.idea.Lua.parser.LuaElementTypes;

/**
 * Creates Lua PSI element by given AST node
 *
 * @author ilyas, Dmitry.Krasilschikov
 */
public class LuaPsiCreator implements LuaElementTypes {

  /**
   * Creates Lua PSI element by given AST node
   *
   * @param node Given node
   * @return Respective PSI element
   */
  public static PsiElement createElement(ASTNode node) {
    IElementType elem = node.getElementType();

    if (elem instanceof LuaElementType.PsiCreator) {
      return ((LuaElementType.PsiCreator)elem).createPsi(node);
    }

    //Identifiers & literal
//    if (elem.equals(LITERAL)) return new LuaLiteralImpl(node);
//    if (elem.equals(LABEL)) return new LuaLabelImpl(node);
////    if (elem.equals(IDENTIFIER)) return new LuaIdentifierImpl(node);
//    //Lists, maps etc...
//    if (elem.equals(LIST_OR_MAP)) return new LuaListOrMapImpl(node);
//
//    if (elem.equals(MODIFIERS)) return new LuaModifierListImpl(node);
//    if (elem.equals(ANNOTATION)) return new LuaAnnotationImpl(node);
//    if (elem.equals(ANNOTATION_ARGUMENTS)) return new LuaAnnotationArgumentListImpl(node);
//    if (elem.equals(ANNOTATION_ARRAY_INITIALIZER)) return new LuaAnnotationArrrayInitializerImpl(node);
//    if (elem.equals(ANNOTATION_MEMBER_VALUE_PAIR)) return new LuaAnnotationNameValuePairImpl(node);
//    if (elem.equals(ANNOTATION_MEMBER_VALUE_PAIRS)) return new LuaAnnotationNameValuePairsImpl(node);
//
//    if (elem.equals(DEFAULT_ANNOTATION_VALUE)) return new LuaDefaultAnnotationValueImpl(node);
//
//    //throws
//    if (elem.equals(THROW_CLAUSE)) return new LuaThrowsClauseImpl(node);
//
//    // Imports
//    if (elem.equals(IMPORT_STATEMENT)) return new LuaImportStatementImpl(node);
//
//    // Packaging
//    if (elem.equals(PACKAGE_DEFINITION)) return new LuaPackageDefinitionImpl(node);
//
//    //statements
//    if (elem.equals(LABELED_STATEMENT)) return new LuaLabeledStatementImpl(node);
//    if (elem.equals(IF_STATEMENT)) return new LuaIfStatementImpl(node);
//    if (elem.equals(FOR_STATEMENT)) return new LuaForStatementImpl(node);
//    if (elem.equals(FOR_IN_CLAUSE)) return new LuaForInClauseImpl(node);
//    if (elem.equals(FOR_TRADITIONAL_CLAUSE)) return new LuaTraditionalForClauseImpl(node);
//    if (elem.equals(WHILE_STATEMENT)) return new LuaWhileStatementImpl(node);
//    if (elem.equals(TRY_BLOCK_STATEMENT)) return new LuaTryCatchStatementImpl(node);
//    if (elem.equals(CATCH_CLAUSE)) return new LuaCatchClauseImpl(node);
//    if (elem.equals(FINALLY_CLAUSE)) return new LuaFinallyClauseImpl(node);
//    if (elem.equals(SYNCHRONIZED_STATEMENT)) return new LuaSynchronizedStatementImpl(node);
//    if (elem.equals(SWITCH_STATEMENT)) return new LuaSwitchStatementImpl(node);
//    if (elem.equals(CASE_LABEL)) return new LuaCaseLabelImpl(node);
//    if (elem.equals(CASE_SECTION)) return new LuaCaseSectionImpl(node);
//    if (elem.equals(VARIABLE_DEFINITION) || elem.equals(VARIABLE_DEFINITION_ERROR))
//      return new LuaVariableDeclarationImpl(node);
//    if (elem.equals(MULTIPLE_VARIABLE_DEFINITION))
//      return new LuaMultipleVariableDeclarationImpl(node);
//    if (elem.equals(TUPLE_DECLARATION) || elem.equals(TUPLE_ERROR)) return new LuaTupleDeclarationImpl(node);
//    if (elem.equals(TUPLE_EXPRESSION)) return new LuaTupleExpressionImpl(node);
//    if (elem.equals(VARIABLE)) return new LuaVariableImpl(node);
//
//    if (elem.equals(FIELD)) return new LuaFieldImpl(node);
//    if (elem.equals(CLASS_INITIALIZER)) return new LuaClassInitializerImpl(node);
//
//    //type definitions
//    if (elem.equals(CLASS_DEFINITION)) return new LuaClassDefinitionImpl(node);
//    if (elem.equals(INTERFACE_DEFINITION))
//      return new LuaInterfaceDefinitionImpl(node);
//    if (elem.equals(ENUM_DEFINITION)) return new LuaEnumTypeDefinitionImpl(node);
//    if (elem.equals(ANNOTATION_DEFINITION))
//      return new LuaAnnotationTypeDefinitionImpl(node);
//    if (elem.equals(ANNOTATION_METHOD)) return new LuaAnnotationMethodImpl(node);
//
//    if (elem.equals(REFERENCE_ELEMENT)) return new LuaCodeReferenceElementImpl(node);
//    if (elem.equals(CLASS_TYPE_ELEMENT)) return new LuaClassTypeElementImpl(node);
//
//    //clauses
//    if (elem.equals(IMPLEMENTS_CLAUSE)) return new LuaImplementsClauseImpl(node);
//    if (elem.equals(EXTENDS_CLAUSE)) return new LuaExtendsClauseImpl(node);
//
//    //bodies
//    if (elem.equals(CLASS_BODY)) return new LuaTypeDefinitionBodyImpl(node);
//    if (elem.equals(ENUM_BODY)) return new LuaEnumDefinitionBodyImpl(node);
//    if (elem.equals(CLOSABLE_BLOCK)) return new LuaClosableBlockImpl(node);
//    if (elem.equals(OPEN_BLOCK)) return new LuaOpenBlockImpl(node);
//    if (elem.equals(BLOCK_STATEMENT)) return new LuaBlockStatementImpl(node);
//    if (elem.equals(EXPLICIT_CONSTRUCTOR)) return new LuaConstructorInvocationImpl(node);
//
//    //enum
//    if (elem.equals(ENUM_CONSTANTS)) return new LuaEnumConstantListImpl(node);
//    if (elem.equals(ENUM_CONSTANT)) return new LuaEnumConstantImpl(node);
//
//    //members
//    if (elem.equals(CONSTRUCTOR_DEFINITION)) return new LuaConstructorImpl(node);
//    if (elem.equals(METHOD_DEFINITION)) return new LuaMethodImpl(node);
//
//    //parameters
//    if (elem.equals(PARAMETERS_LIST)) return new LuaParameterListImpl(node);
//    if (elem.equals(PARAMETER)) return new LuaParameterImpl(node);
//
//    //type parameters
//    if (elem.equals(TYPE_ARGUMENT)) return new LuaWildcardTypeArgumentImpl(node);
//    if (elem.equals(TYPE_ARGUMENTS)) return new LuaTypeArgumentListImpl(node);
//
//
//    if (elem.equals(TYPE_PARAMETER_LIST)) return new LuaTypeParameterListImpl(node);
//    if (elem.equals(TYPE_PARAMETER)) return new LuaTypeParameterImpl(node);
//    if (elem.equals(TYPE_PARAMETER_EXTENDS_BOUND_LIST)) return new LuaTypeParameterParameterExtendsListImpl(node);
//
//    //Branch statements
//    if (elem.equals(RETURN_STATEMENT)) return new LuaReturnStatementImpl(node);
//    if (elem.equals(THROW_STATEMENT)) return new LuaThrowStatementImpl(node);
//    if (elem.equals(ASSERT_STATEMENT)) return new LuaAssertStatementImpl(node);
//    if (elem.equals(BREAK_STATEMENT)) return new LuaBreakStatementImpl(node);
//    if (elem.equals(CONTINUE_STATEMENT)) return new LuaContinueStatementImpl(node);
//
//    //expressions
//    if (elem.equals(CALL_EXPRESSION)) return new LuaApplicationStatementImpl(node);
//    if (elem.equals(COMMAND_ARGUMENTS)) return new LuaCommandArgumentListImpl(node);
//    if (elem.equals(CONDITIONAL_EXPRESSION)) return new LuaConditionalExprImpl(node);
//    if (elem.equals(ELVIS_EXPRESSION)) return new LuaElvisExprImpl(node);
//    if (elem.equals(ASSIGNMENT_EXPRESSION)) return new LuaAssignmentExpressionImpl(node);
//    if (elem.equals(LOGICAL_OR_EXPRESSION)) return new LuaLogicalOrExpressionImpl(node);
//    if (elem.equals(LOGICAL_AND_EXPRESSION)) return new LuaLogicalAndExpressionImpl(node);
//    if (elem.equals(EXCLUSIVE_OR_EXPRESSION)) return new LuaExclusiveOrExpressionImpl(node);
//    if (elem.equals(INCLUSIVE_OR_EXPRESSION)) return new LuaInclusiveOrExpressionImpl(node);
//    if (elem.equals(AND_EXPRESSION)) return new LuaAndExpressionImpl(node);
//    if (elem.equals(REGEX_MATCH_EXPRESSION)) return new LuaRegexMatchExpressionImpl(node);
//    if (elem.equals(REGEX_FIND_EXPRESSION)) return new LuaRegexFindExpressionImpl(node);
//    if (elem.equals(EQUALITY_EXPRESSION)) return new LuaEqualityExpressionImpl(node);
//    if (elem.equals(RELATIONAL_EXPRESSION)) return new LuaRelationalExpressionImpl(node);
//    if (elem.equals(SHIFT_EXPRESSION)) return new LuaShiftExpressionImpl(node);
//    if (elem.equals(RANGE_EXPRESSION)) return new LuaRangeExpressionImpl(node);
//    if (elem.equals(COMPOSITE_SHIFT_SIGN)) return new LuaOperationSignImpl(node);
//    if (elem.equals(ADDITIVE_EXPRESSION)) return new LuaAdditiveExpressionImpl(node);
//    if (elem.equals(MULTIPLICATIVE_EXPRESSION)) return new LuaMultiplicativeExpressionImpl(node);
//    if (elem.equals(POWER_EXPRESSION)) return new LuaPowerExpressionImpl(node);
//    if (elem.equals(POWER_EXPRESSION_SIMPLE)) return new LuaPowerExpressionImpl(node);
//    if (elem.equals(UNARY_EXPRESSION)) return new LuaUnaryExpressionImpl(node);
//    if (elem.equals(POSTFIX_EXPRESSION)) return new LuaPostfixExprImpl(node);
//    if (elem.equals(CAST_EXPRESSION)) return new LuaTypeCastExpressionImpl(node);
//    if (elem.equals(SAFE_CAST_EXPRESSION)) return new LuaSafeCastExpressionImpl(node);
//    if (elem.equals(INSTANCEOF_EXPRESSION)) return new LuaInstanceofExpressionImpl(node);
//    if (elem.equals(BUILT_IN_TYPE_EXPRESSION)) return new LuaBuiltinTypeClassExpressionImpl(node);
//    if (elem.equals(ARRAY_TYPE)) return new LuaArrayTypeElementImpl(node);
//    if (elem.equals(BUILT_IN_TYPE)) return new LuaBuiltInTypeElementImpl(node);
//    if (elem.equals(GSTRING)) return new LuaStringImpl(node);
//    if (elem.equals(REGEX)) return new LuaRegexImpl(node);
//    if (elem.equals(GSTRING_INJECTION)) return new LuaStringInjectionImpl(node);
//    if (elem.equals(REFERENCE_EXPRESSION)) return new LuaReferenceExpressionImpl(node);
//    if (elem.equals(THIS_REFERENCE_EXPRESSION)) return new LuaThisReferenceExpressionImpl(node);
//    if (elem.equals(SUPER_REFERENCE_EXPRESSION)) return new LuaSuperReferenceExpressionImpl(node);
//    if (elem.equals(PARENTHESIZED_EXPRESSION)) return new LuaParenthesizedExpressionImpl(node);
//    if (elem.equals(NEW_EXPRESSION)) return new LuaNewExpressionImpl(node);
//    if (elem.equals(ANONYMOUS_CLASS_DEFINITION)) return new LuaAnonymousClassDefinitionImpl(node);
//    if (elem.equals(ARRAY_DECLARATOR)) return new LuaArrayDeclarationImpl(node);
//
//    //Paths
//    if (elem.equals(PATH_PROPERTY)) return new LuaPropertySelectorImpl(node);
//    if (elem.equals(PATH_PROPERTY_REFERENCE)) return new LuaPropertySelectionImpl(node);
//    if (elem.equals(PATH_METHOD_CALL)) return new LuaMethodCallExpressionImpl(node);
//    if (elem.equals(PATH_INDEX_PROPERTY)) return new LuaIndexPropertyImpl(node);
//
//    // Arguments
//    if (elem.equals(ARGUMENTS)) return new LuaArgumentListImpl(node);
//    if (elem.equals(ARGUMENT)) return new LuaNamedArgumentImpl(node);
//    if (elem.equals(ARGUMENT_LABEL)) return new LuaArgumentLabelImpl(node);
//
//    if (elem.equals(BALANCED_BRACKETS)) return new LuaBalancedBracketsImpl(node);

    return new ASTWrapperPsiElement(node);
  }

}
