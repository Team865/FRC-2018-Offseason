package ca.warp7.frc2018.v5.state.superstructure

import ca.warp7.action.IAction
import ca.warp7.frc2018.v5.subsystems.Climber

object ReleasePosition : IAction {
    var speed = 0.0

    override fun start() {
    }

    @Synchronized
    override fun update() {
        Climber.percentOutput = speed
    }

    @Synchronized
    override fun shouldFinish(): Boolean {
        return false
    }
}