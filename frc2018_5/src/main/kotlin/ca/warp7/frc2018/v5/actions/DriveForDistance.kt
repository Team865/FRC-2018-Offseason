package ca.warp7.frc2018.v5.actions

import ca.warp7.action.impl.Singleton
import ca.warp7.frc2018.v5.states.drive.PIDDrive
import ca.warp7.frc2018.v5.subsystems.Drive

class DriveForDistance(private val distance: Double) : Singleton() {

    override fun start_() {
        Drive.state = PIDDrive
        PIDDrive.setTargets(distance, 0.0)
    }

    override fun shouldFinish_(): Boolean {
        return PIDDrive.shouldFinish()
    }

    override fun stop() {
        Drive.idle()
    }
}