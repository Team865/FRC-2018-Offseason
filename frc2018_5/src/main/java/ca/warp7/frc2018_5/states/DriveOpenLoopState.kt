package ca.warp7.frc2018_5.states

import ca.warp7.action.IAction
import ca.warp7.frc2018_5.output.DriveOutput

object DriveOpenLoopState : IAction {

    var leftPercent = 0.0
    var rightPercent = 0.0
    var brakeWhenDone = true

    fun setPercent(left: Double, right: Double) {
        leftPercent = left
        rightPercent = right
    }

    fun of(left: Double, right: Double): IAction {
        setPercent(left, right)
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
        if (brakeWhenDone) {
            DriveOutput.leftPercentOutput = 0.0
            DriveOutput.rightPercentOutput = 0.0
        }
    }
}