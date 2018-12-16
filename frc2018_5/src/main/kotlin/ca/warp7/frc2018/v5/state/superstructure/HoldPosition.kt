package ca.warp7.frc2018.v5.state.superstructure

import ca.warp7.action.IAction
import ca.warp7.frc2018.v5.output.ClimberOutput

object HoldPosition : IAction {
    var speed = 0.0

    override fun start() {
    }

    @Synchronized
    override fun update() {
        ClimberOutput.percentOutput = speed
    }

    @Synchronized
    override fun shouldFinish(): Boolean {
        return false
    }
}