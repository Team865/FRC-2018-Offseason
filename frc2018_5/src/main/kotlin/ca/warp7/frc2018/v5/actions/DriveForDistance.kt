package ca.warp7.frc2018.v5.actions

import ca.warp7.action.impl.Singleton
import ca.warp7.frc2018.v5.state.drive.PIDDrive
import ca.warp7.frc2018.v5.subsystems.Drive
import ca.warp7.frckt.setIdle
import ca.warp7.frckt.setState

class DriveForDistance(private val distance: Double) : Singleton() {

    override fun start_() {
        setState { PIDDrive to Drive }
        PIDDrive.setTargets(distance, 0.0)
    }

    override fun shouldFinish_(): Boolean {
        return PIDDrive.shouldFinish()
    }

    override fun stop() {
        setIdle { Drive }
    }
}