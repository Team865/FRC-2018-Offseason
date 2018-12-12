package ca.warp7.frc2018_5.state.drive

import ca.warp7.action.IAction
import ca.warp7.frc2018_5.output.DriveOutput

object CheesyDrive : IAction {

    private val cheesyDrive = ca.warp7.frc.CheesyDrive(this::setPercent)

    private var leftPercent = 0.0
    private var rightPercent = 0.0

    var wheel = 0.0
    var throttle = 0.0
    var quickTurn = false

    @Synchronized
    private fun setPercent(left: Double, right: Double) {
        leftPercent = left
        rightPercent = right
    }

    @Synchronized
    override fun start() = Unit

    @Synchronized
    override fun shouldFinish(): Boolean {
        return false
    }

    @Synchronized
    override fun update() {
        cheesyDrive.cheesyDrive(wheel, throttle, quickTurn)
        DriveOutput.leftPercentOutput = leftPercent
        DriveOutput.rightPercentOutput = rightPercent
    }
}