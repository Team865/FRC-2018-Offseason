package ca.warp7.frc2018_5.actions

import ca.warp7.action.impl.Singleton
import ca.warp7.frc.kt.Robot
import ca.warp7.frc2018_5.output.DriveOutput
import ca.warp7.frc2018_5.states.DrivePIDState

class DriveForDistance(private val distance: Double,
                       private val duration: Double) : Singleton() {

    override fun start_() {
        Robot.setState(DrivePIDState to DriveOutput, lockOutput = true)
        DrivePIDState.setTargets(distance, 0.0)
    }

    override fun shouldFinish_(): Boolean {
        return DrivePIDState.shouldFinish()
    }

    override fun stop() {
        Robot.clearState(DriveOutput)
    }
}