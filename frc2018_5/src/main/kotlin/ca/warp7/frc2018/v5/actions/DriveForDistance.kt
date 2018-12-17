package ca.warp7.frc2018.v5.actions

import ca.warp7.action.impl.Singleton
import ca.warp7.frc2018.v5.states.drive.LinearPIDDrive
import ca.warp7.frc2018.v5.subsystems.Drive

class DriveForDistance(private val distance: Double, private val stop: Boolean) : Singleton() {

    override fun start_() {
        Drive.state = LinearPIDDrive
        LinearPIDDrive.setSetpoint(distance)
    }

    override fun shouldFinish_(): Boolean {
        return LinearPIDDrive.shouldFinish()
    }

    override fun stop() {
        if (stop) Drive.idle()
    }
}