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

package com.sylvanaar.idea.Lua.editor.inspections;

import com.intellij.codeInspection.CustomSuppressableInspectionTool;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.SuppressionUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.sylvanaar.idea.Lua.lang.psi.impl.PsiUtil;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;

/**
 * @author peter
 */
public abstract class LuaSuppressableInspectionTool extends LocalInspectionTool implements CustomSuppressableInspectionTool {
  @Nullable
//  public SuppressIntentionAction[] getSuppressActions(final LuaPsiElement element) {
//    final HighlightDisplayKey displayKey = HighlightDisplayKey.find(getShortName());
//    return new SuppressIntentionAction[]{
//      new AddNoInspectionLuaCommentFix(displayKey),
//      new SuppressForMemberFix(displayKey, false),
//      new SuppressForMemberFix(displayKey, true),
//    };
//
//  }

  public boolean isSuppressedFor(final PsiElement element) {
    return getElementToolSuppressedIn(element, getID()) != null;
  }

  @Nullable
  private static PsiElement getElementToolSuppressedIn(final PsiElement place, final String toolId) {
    if (place == null) return null;
    return ApplicationManager.getApplication().runReadAction(new Computable<PsiElement>() {
      @Nullable
      public PsiElement compute() {
        final PsiElement statement = PsiUtil.findEnclosingStatement(place);
        if (statement != null) {
          PsiElement prev = statement.getPrevSibling();
          while (prev != null && StringUtil.isEmpty(prev.getText().trim())) {
            prev = prev.getPrevSibling();
          }
          if (prev instanceof PsiComment) {
            String text = prev.getText();
            Matcher matcher = SuppressionUtil.SUPPRESS_IN_LINE_COMMENT_PATTERN.matcher(text);
            if (matcher.matches() && SuppressionUtil.isInspectionToolIdMentioned(matcher.group(1), toolId)) {
              return prev;
            }
          }
        }

//        LuaMember member = PsiTreeUtil.getNonStrictParentOfType(place, LuaMember.class);
//        while (member != null) {
//          LuaModifierList modifierList = member.getModifierList();
//          for (String ids : getInspectionIdsSuppressedInAnnotation(modifierList)) {
//            if (SuppressionUtil.isInspectionToolIdMentioned(ids, toolId)) {
//              return modifierList;
//            }
//          }
//
//          member = PsiTreeUtil.getParentOfType(member, LuaMember.class);
//        }

        return null;
      }
    });
  }





}
