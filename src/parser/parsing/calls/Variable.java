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

package com.sylvanaar.idea.Lua.parser.parsing.calls;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.sylvanaar.idea.Lua.parser.LuaElementTypes;
import com.sylvanaar.idea.Lua.parser.util.LuaPsiBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: markov
 * Date: 05.11.2007
 */
public class Variable implements LuaElementTypes {

	//	variable:
	//		base_variable_with_function_calls ARROW object_property method_or_not variable_properties
	//		| base_variable_with_function_calls
	//	;

	//	variable_properties:
	//		variable_properties variable_property
	//		| /* empty */
	//	;
	//
	//	variable_property:
	//		ARROW object_property method_or_not
	//	;
	public static IElementType parse(LuaPsiBuilder builder) {
        PsiBuilder.Marker variable = builder.mark();
        while (builder.compareAndEat(IDENTIFIER_SET))
            ;
        variable.done(LuaElementTypes.IDENTIFIER_EXPR);
        return IDENTIFIER_EXPR;
	}

	//	method_or_not:
	//		'(' function_call_parameter_list ')'
	//		| /* empty */
	//	;
	private static IElementType parseMethodOrNot(LuaPsiBuilder builder) {
		if (builder.compare(LPAREN)) {
			Function.parseFunctionCallParameterList(builder);
			return LuaElementTypes.FUNCTION_CALL;
		}
		return LuaElementTypes.EMPTY_INPUT;
	}

//	//	object_property:
//	//		object_dim_list
//	//		| variable_without_objects
//	//	;
//	public static IElementType parseObjectProperty(LuaPsiBuilder builder) {
//		IElementType result = parseVariableWithoutObjects(builder);
//		if (result == LuaElementTypes.EMPTY_INPUT) {
//			result = parseObjectDimList(builder);
//		}
//		return result;
//	}

//	//	object_dim_list:
//	//		object_dim_list '[' dim_offset ']'
//	//		| object_dim_list '{' expr '}'
//	//		| variable_name
//	//	;
//	private static IElementType parseObjectDimList(LuaPsiBuilder builder) {
//		TokenSet tokens = TokenSet.create(chLBRACE, chLBRACKET);
//		PsiBuilder.Marker preceder = builder.mark();
//		IElementType result = parseVariableName(builder);
//		if (result == LuaElementTypes.EMPTY_INPUT) {
//			preceder.drop();
//			return result;
//		}
//
//		while (builder.compare(tokens)) {
//			preceder.done(result);
//			preceder = preceder.precede();
//			if (builder.compareAndEat(chLBRACE)) {
//				Expression.parse(builder);
//				builder.match(chRBRACE);
//				result = LuaElementTypes.ARRAY;
//			} else if (builder.compareAndEat(chLBRACKET)) {
//				PsiBuilder.Marker arrayIndex = builder.mark();
//				parseDimOffset(builder);
//				arrayIndex.done(LuaElementTypes.ARRAY_INDEX);
//				builder.match(chRBRACKET);
//				result = LuaElementTypes.ARRAY;
//			} else {
//				builder.error(LuaParserErrors.unexpected(builder.getTokenType()));
//			}
//		}
//
//		preceder.drop();
//		return result;
//	}
//
//	//	variable_name:
//	//		IDENTIFIER
//	//		| '{' expr '}'
//	//	;
//	private static IElementType parseVariableName(LuaPsiBuilder builder) {
//		if (builder.compareAndEat(IDENTIFIER)) {
//			return LuaElementTypes.OBJECT_PROPERTY;
//		}
//		if (builder.compareAndEat(chLBRACE)) {
//			Expression.parse(builder);
//			builder.match(chRBRACE);
//			return LuaElementTypes.OBJECT_PROPERTY;
//		}
//		return LuaElementTypes.EMPTY_INPUT;
//	}
//
	//	base_variable_with_function_calls:
	//		base_variable
	//		| function_call
	//	;
	private static IElementType parseBaseVariableOrFunctionCall(LuaPsiBuilder builder) {
		IElementType result;// = parseBaseVariable(builder);
//		if (result == LuaElementTypes.EMPTY_INPUT) {
//			result = Function.parse(builder);
//		}
        result = LuaElementTypes.NAME;
		return result;
	}

//	//	base_variable:
//	//		reference_variable
//	//		| simple_indirect_reference reference_variable
//	//		| static_member
//	//	;
//	public static IElementType parseBaseVariable(LuaPsiBuilder builder) {
//		IElementType result = parseVariableWithoutObjects(builder);
//		if (result == LuaElementTypes.EMPTY_INPUT) {
//			result = parseStaticMember(builder);
//		}
//		return result;
//	}
//
//	//	static_member:
//	//		fully_qualified_class_name SCOPE_RESOLUTION variable_without_objects
//	//	;
//	private static IElementType parseStaticMember(LuaPsiBuilder builder) {
//		PsiBuilder.Marker rollback = builder.mark();
//		if (ClassReference.parse(builder) != LuaElementTypes.EMPTY_INPUT) {
//			if (builder.compareAndEat(SCOPE_RESOLUTION)) {
//				IElementType result = parseVariableWithoutObjects(builder);
//				if (result != LuaElementTypes.EMPTY_INPUT) {
//					rollback.drop();
//					return result;
//				}
//			}
//		}
//		rollback.rollbackTo();
//		return LuaElementTypes.EMPTY_INPUT;
//	}
//
	//	variable_without_objects:
	//		reference_variable
	//		| simple_indirect_reference reference_variable
	//	;
	public static IElementType parseVariableWithoutObjects(LuaPsiBuilder builder) {

        if (builder.compare(NAME)) {

            return LuaElementTypes.IDENTIFIER_EXPR;
        }
		return builder.getTokenType();
	}
//
//	//	simple_indirect_reference:
//	//		'$'
//	//		| simple_indirect_reference '$'
//	//	;
//	private static void parseSimpleIndirectReference(LuaPsiBuilder builder){
//		while(builder.compareAndEat(DOLLAR));
//	}
//
//	//	reference_variable:
//	//		reference_variable '[' dim_offset ']'
//	//		| reference_variable '{' expr '}'
//	//		| compound_variable
//	//	;
//	private static IElementType parseReferenceVariable(LuaPsiBuilder builder) {
//		TokenSet tokens = TokenSet.create(chLBRACE, chLBRACKET);
//		PsiBuilder.Marker preceder = builder.mark();
//		IElementType result = parseCompoundVariable(builder);
//		if (result == LuaElementTypes.EMPTY_INPUT) {
//			preceder.drop();
//			return result;
//		}
//
//		while (builder.compare(tokens)) {
//			preceder.done(result);
//			preceder = preceder.precede();
//			if (builder.compareAndEat(chLBRACE)) {
//				Expression.parse(builder);
//				builder.match(chRBRACE);
//				result = LuaElementTypes.ARRAY;
//			} else if (builder.compareAndEat(chLBRACKET)) {
//				PsiBuilder.Marker arrayIndex = builder.mark();
//				parseDimOffset(builder);
//				arrayIndex.done(LuaElementTypes.ARRAY_INDEX);
//				builder.match(chRBRACKET);
//				result = LuaElementTypes.ARRAY;
//			} else {
//				builder.error(LuaParserErrors.unexpected(builder.getTokenType()));
//			}
//		}
//
//		preceder.drop();
//		return result;
//	}
//
//	//	compound_variable:
//	//		VARIABLE
//	//		| '$' '{' expr '}'
//	//	;
//	private static IElementType parseCompoundVariable(LuaPsiBuilder builder) {
//		if (builder.compareAndEat(VARIABLE)) {
//			return LuaElementTypes.VARIABLE;
//		}
//		if (builder.compareAndEat(DOLLAR)) {
//			builder.match(chLBRACE);
//			Expression.parse(builder);
//			builder.match(chRBRACE);
//			return LuaElementTypes.OBSCURE_VARIABLE;
//		}
//		return LuaElementTypes.EMPTY_INPUT;
//	}
//
//	//	dim_offset:
//	//		/* empty */
//	//		| expr
//	//	;
//	private static void parseDimOffset(LuaPsiBuilder builder) {
//		Expression.parse(builder);
//	}

}
