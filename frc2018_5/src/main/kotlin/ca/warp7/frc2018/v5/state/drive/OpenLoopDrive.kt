package ca.warp7.frc2018.v5.state.drive

import ca.warp7.action.IAction
import ca.warp7.frc2018.v5.subsystems.Drive

object OpenLoopDrive : IAction {

    private var leftPercent = 0.0
    private var rightPercent = 0.0

    @Synchronized
    fun setPercent(left: Double, right: Double) {
        leftPercent = left
        rightPercent = right
    }

    @Synchronized
    override fun start() {
        update()
    }

    @Synchronized
    override fun shouldFinish(): Boolean {
        return false
    }

    @Synchronized
    override fun update() {
        Drive.leftPercentOutput = leftPercent
        Drive.rightPercentOutput = rightPercent
    }
}