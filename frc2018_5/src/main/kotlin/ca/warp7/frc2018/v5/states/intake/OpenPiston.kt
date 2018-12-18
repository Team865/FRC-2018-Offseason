package ca.warp7.frc2018.v5.states.intake

import ca.warp7.action.IAction
import ca.warp7.frc2018.v5.subsystems.Intake

object OpenPiston : IAction {

    override fun start() {
        Intake.pistonOn = true
    }

    @Synchronized
    override fun shouldFinish(): Boolean {
        return true
    }
}