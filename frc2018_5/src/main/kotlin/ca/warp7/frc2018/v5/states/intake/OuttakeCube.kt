package ca.warp7.frc2018.v5.states.intake

import ca.warp7.action.IAction
import ca.warp7.frc2018.v5.constants.IntakeConstants
import ca.warp7.frc2018.v5.subsystems.Intake

object OuttakeCube : IAction {
    override fun start() {
        update()
    }

    @Synchronized
    override fun update() {
        Intake.percentOutput = IntakeConstants.kFastOuttakePower
    }

    @Synchronized
    override fun shouldFinish(): Boolean {
        return false
    }
}