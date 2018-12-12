package ca.warp7.frc2018_5.actions

import ca.warp7.action.impl.Singleton
import ca.warp7.frc.kt.Robot
import ca.warp7.frc2018_5.output.DriveOutput
import ca.warp7.frc2018_5.state.drive.PIDDrive

class DriveForDistance(private val distance: Double) : Singleton() {

    override fun start_() {
        Robot.lockState { PIDDrive to DriveOutput }
        PIDDrive.setTargets(distance, 0.0)
    }

    override fun shouldFinish_(): Boolean {
        return PIDDrive.shouldFinish()
    }

    override fun stop() {
        Robot.unlockStates()
    }
}