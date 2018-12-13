package ca.warp7.frc2018_5.state.intake

import ca.warp7.action.IAction
import ca.warp7.frc2018_5.output.IntakeOutput

object OpenPiston : IAction {

    override fun start() {
        IntakeOutput.pistonOn = true
    }

    @Synchronized
    override fun shouldFinish(): Boolean {
        return true
    }
}