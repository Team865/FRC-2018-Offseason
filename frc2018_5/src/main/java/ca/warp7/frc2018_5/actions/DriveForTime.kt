package ca.warp7.frc2018_5.actions

import ca.warp7.action.impl.Singleton
import ca.warp7.frc.kt.Robot
import ca.warp7.frc2018_5.output.DriveOutput
import ca.warp7.frc2018_5.state.drive.OpenLoopDrive

class DriveForTime(private val left: Double,
                   private val right: Double,
                   private val duration: Double) : Singleton() {

    override fun start_() {
        Robot.lockState { OpenLoopDrive to DriveOutput }
        OpenLoopDrive.setPercent(left, right)
    }

    override fun shouldFinish_(): Boolean {
        return elapsed > duration
    }

    override fun stop() {
        Robot.unlockStates()
        Robot.setIdle { DriveOutput }
    }
}