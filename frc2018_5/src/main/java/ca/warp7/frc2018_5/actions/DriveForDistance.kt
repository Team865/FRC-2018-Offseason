package ca.warp7.frc2018_5.actions

import ca.warp7.action.impl.Singleton
import ca.warp7.frc2018_5.output.DriveOutput
import ca.warp7.frc2018_5.state.drive.PIDDrive
import ca.warp7.frckt.setIdleState
import ca.warp7.frckt.setState

class DriveForDistance(private val distance: Double) : Singleton() {

    override fun start_() {
        setState { PIDDrive to DriveOutput }
        PIDDrive.setTargets(distance, 0.0)
    }

    override fun shouldFinish_(): Boolean {
        return PIDDrive.shouldFinish()
    }

    override fun stop() {
        setIdleState { DriveOutput }
    }
}