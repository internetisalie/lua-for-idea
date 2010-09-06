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

package com.sylvanaar.idea.Lua.lang.formatter;

import com.intellij.formatting.WrapType;
import com.intellij.psi.codeStyle.CodeStyleSettings;

/**
 * Created by IntelliJ IDEA.
 * User: Jon S Akhtar
 * Date: Sep 6, 2010
 * Time: 3:31:45 PM
 */

   public class Util {
     public static WrapType getWrapType (int settings) {
       switch (settings) {
         case CodeStyleSettings.WRAP_ALWAYS:
           return WrapType.ALWAYS;
         case CodeStyleSettings.WRAP_AS_NEEDED:
           return WrapType.NORMAL;
         case CodeStyleSettings.DO_NOT_WRAP:
           return WrapType.NONE;
         default:
           return WrapType.CHOP_DOWN_IF_LONG;
       }
     }
   }



