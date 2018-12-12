package ca.warp7.frc2018_5.state.intake

import ca.warp7.action.IAction
import ca.warp7.frc2018_5.output.IntakeOutput

object Intaking : IAction {


    private var speed = 0.0

    fun setIntakeSpeed(s: Double) {
        speed = s
    }

    override fun start() {
        update()
    }

    @Synchronized
    override fun update() {
        IntakeOutput.percentOutput = speed
    }

    @Synchronized
    override fun shouldFinish(): Boolean {
        return true
    }
}