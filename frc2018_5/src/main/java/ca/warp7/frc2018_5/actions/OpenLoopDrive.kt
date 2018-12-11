package ca.warp7.frc2018_5.actions

import ca.warp7.action.IAction
import ca.warp7.frc2018_5.output.DriveOutput

object OpenLoopDrive : IAction {

    private var leftPercent = 0.0
    private var rightPercent = 0.0

    fun of(left: Double, right: Double): OpenLoopDrive {
        leftPercent = left
        rightPercent = right
        return this
    }

    override fun start() {
    }

    override fun shouldFinish(): Boolean {
        return false
    }

    override fun update() {
        DriveOutput.leftPercentOutput = leftPercent
        DriveOutput.rightPercentOutput = rightPercent
    }

    override fun stop() {
        DriveOutput.leftPercentOutput = 0.0
        DriveOutput.rightPercentOutput = 0.0
    }
}