package ca.warp7.frc2018.v5.state.intake

import ca.warp7.action.IAction
import ca.warp7.frc2018.v5.constants.IntakeConstants
import ca.warp7.frc2018.v5.output.IntakeOutput

object KeepCube : IAction {

    override fun start() {
        update()
    }

    @Synchronized
    override fun update() {
        IntakeOutput.percentOutput = IntakeConstants.kIntakePower
    }

    @Synchronized
    override fun shouldFinish(): Boolean {
        return false
    }
}