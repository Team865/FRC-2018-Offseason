package ca.warp7.frc2018.v5.auto

import ca.warp7.action.IAction
import ca.warp7.action.impl.ActionMode
import ca.warp7.frc2018.v5.actions.DriveForTime

object Baseline : ActionMode() {
    override fun getAction(): IAction {
        return DriveForTime(0.5, 0.5, 2.0)
    }
}