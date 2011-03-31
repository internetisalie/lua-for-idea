/*
 * Copyright 2000-2009 JetBrains s.r.o.
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

package com.sylvanaar.idea.Lua.lang.luadoc.completion;

import com.intellij.codeInsight.TailType;
import com.intellij.codeInsight.completion.CompletionData;
import com.intellij.codeInsight.completion.CompletionVariant;
import com.intellij.psi.filters.*;
import com.intellij.psi.filters.position.LeftNeighbour;
import com.intellij.psi.impl.source.tree.LeafPsiElement;



/**
 * @author ilyas
 */
public class LuaDocCompletionData extends CompletionData {

  private static final String[] INLINED_DOC_TAGS = {"code", "docRoot", "inheritDoc", "link", "linkplain", "literal"};
  private static final String[] DOC_TAGS = {"author", "deprecated", "exception", "param", "return", "see", "serial", "serialData",
      "serialField", "since", "throws", "version"};

  public LuaDocCompletionData() {
    registerAllCompletions();
  }

  private void registerAllCompletions() {
    registerTagNameCompletion();
  }

  private void registerTagNameCompletion() {
//    registerStandardCompletion(new SimpleTagNameFilter(), DOC_TAGS);
//    registerStandardCompletion(new InlinedTagNameFilter(), INLINED_DOC_TAGS);
  }


  /**
   * Template to add all standard keywords completions
   *
   * @param filter   - Semantic filter for given keywords
   * @param keywords - Keywords to be completed
   */
  private void registerStandardCompletion(ElementFilter filter, String... keywords) {
    LeftNeighbour afterDotFilter = new LeftNeighbour(new TextFilter("."));
    CompletionVariant variant = new CompletionVariant(new AndFilter(new NotFilter(afterDotFilter), filter));
    variant.includeScopeClass(LeafPsiElement.class);
    variant.addCompletionFilter(TrueFilter.INSTANCE);
    addCompletions(variant, keywords);
    registerVariant(variant);
  }


  /**
   * Adds all completion variants in sequence
   *
   * @param comps   Given completions
   * @param variant Variant for completions
   */
  private void addCompletions(CompletionVariant variant, String... comps) {
    for (String completion : comps) {
      variant.addCompletion(completion, TailType.SPACE);
    }
  }
}
