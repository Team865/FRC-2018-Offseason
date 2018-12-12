package ca.warp7.frc2018_5.auto

import ca.warp7.action.IAction
import ca.warp7.frc.kt.ModeKt
import ca.warp7.frc2018_5.output.DriveOutput
import ca.warp7.frc2018_5.states.OpenLoopDrive

object Baseline : ModeKt() {
    override fun getAction(): IAction {
        return setState(OpenLoopDrive.of(0.5, 0.5) to DriveOutput).waitFor(3.0)
    }
}