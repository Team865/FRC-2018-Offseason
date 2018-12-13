package ca.warp7.frc2018_5.state

import ca.warp7.action.IAction
import ca.warp7.frc2018_5.output.ClimberOutput

object ManualClimb : IAction {
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