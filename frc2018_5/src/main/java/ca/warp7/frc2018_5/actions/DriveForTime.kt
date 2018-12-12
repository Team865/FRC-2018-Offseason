package ca.warp7.frc2018_5.actions

import ca.warp7.action.impl.Singleton
import ca.warp7.frc.kt.Robot
import ca.warp7.frc2018_5.output.DriveOutput
import ca.warp7.frc2018_5.states.DriveOpenLoopState

class DriveForTime(private val left: Double,
                   private val right: Double,
                   private val duration: Double) : Singleton() {

    override fun start_() {
        Robot.setState(DriveOpenLoopState to DriveOutput, lockOutput = true)
        DriveOpenLoopState.setPercent(left, right)
    }

    override fun shouldFinish_(): Boolean {
        return elapsed > duration
    }

    override fun stop() {
        Robot.clearState(DriveOutput)
    }
}