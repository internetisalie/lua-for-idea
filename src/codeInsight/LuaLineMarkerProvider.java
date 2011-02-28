/*
 * Copyright 2011 Jon S Akhtar (Sylvanaar)
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
package com.sylvanaar.idea.Lua.codeInsight;

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzerSettings;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiElement;
import com.intellij.util.NullableFunction;
import com.sylvanaar.idea.Lua.LuaIcons;
import com.sylvanaar.idea.Lua.lang.psi.statements.LuaReturnStatement;

import java.util.Collection;
import java.util.List;

public class LuaLineMarkerProvider implements LineMarkerProvider, DumbAware {
    DaemonCodeAnalyzerSettings myDaemonSettings = null;
    EditorColorsManager myColorsManager = null;

    public LuaLineMarkerProvider(DaemonCodeAnalyzerSettings myDaemonSettings, EditorColorsManager myColorsManager) {
        this.myDaemonSettings = myDaemonSettings;
        this.myColorsManager = myColorsManager;
    }


    NullableFunction<PsiElement, String> tailCallTooltip = new NullableFunction<PsiElement, String>() {
        @Override
        public String fun(PsiElement psiElement) {
            return "Tail Call: " + psiElement.getText();
        }
    };

    @Override
    public LineMarkerInfo getLineMarkerInfo(final PsiElement element) {
        if (element instanceof LuaReturnStatement) {
            LuaReturnStatement e = (LuaReturnStatement) element;

            if (e.isTailCall())
                return new LineMarkerInfo<PsiElement>(element, element.getTextRange(),
                        LuaIcons.TAIL_RECURSION, Pass.UPDATE_ALL,
                        tailCallTooltip, null,
                        GutterIconRenderer.Alignment.LEFT);
        }

        //need to draw method separator above docComment
//        if (myDaemonSettings.SHOW_METHOD_SEPARATORS && element.getFirstChild() == null) {
//            boolean drawSeparator = false;
//
//            if (drawSeparator) {
//
//                LineMarkerInfo info =
//                        new LineMarkerInfo<PsiElement>(element, element.getTextRange(), null, Pass.UPDATE_ALL,
//                                NullableFunction.NULL, null, GutterIconRenderer.Alignment.RIGHT);
//                EditorColorsScheme scheme = myColorsManager.getGlobalScheme();
//                info.separatorColor = scheme.getColor(CodeInsightColors.METHOD_SEPARATORS_COLOR);
//                info.separatorPlacement = SeparatorPlacement.TOP;
//                return info;
//            }
//        }

        return null;
    }

    @Override
    public void collectSlowLineMarkers(final List<PsiElement> elements, final Collection<LineMarkerInfo> result) {
    }
}