package ca.warp7.frc2018_5.state.intake

import ca.warp7.action.IAction
import ca.warp7.frc2018_5.output.IntakeOutput

object StopIntaking : IAction {

    @Synchronized
    override fun start() {
        IntakeOutput.percentOutput = 0.0
    }

    @Synchronized
    override fun shouldFinish(): Boolean {
        return true
    }
}