package ca.warp7.frc.kt

import ca.warp7.action.IAction
import ca.warp7.action.impl.ActionMode
import ca.warp7.frc.next.OutputSystem

abstract class ModeKt : ActionMode() {
    fun setState(toOutput: Pair<IAction, OutputSystem>, forceAssign: Boolean = true): IAction.API {
        return exec(Runnable { RobotKt.setState(toOutput, forceAssign) })
    }
}