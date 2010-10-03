package com.sylvanaar.idea.Lua.lang.psi;

import com.sylvanaar.idea.Lua.lang.psi.controlFlow.Instruction;

/**
 * Created by IntelliJ IDEA.
 * User: Jon S Akhtar
 * Date: Oct 2, 2010
 * Time: 5:39:40 AM
 */
public interface LuaControlFlowOwner {
    Instruction[] getControlFlow();
}
