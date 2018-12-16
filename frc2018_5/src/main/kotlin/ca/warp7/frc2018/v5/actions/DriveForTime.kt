package ca.warp7.frc2018.v5.actions

import ca.warp7.action.impl.Singleton
import ca.warp7.frc2018.v5.state.drive.OpenLoopDrive
import ca.warp7.frc2018.v5.subsystems.Drive
import ca.warp7.frckt.setIdle
import ca.warp7.frckt.setState

class DriveForTime(private val left: Double,
                   private val right: Double,
                   private val duration: Double) : Singleton() {

    override fun start_() {
        setState { OpenLoopDrive to Drive }
        OpenLoopDrive.setPercent(left, right)
    }

    override fun shouldFinish_(): Boolean {
        return elapsed > duration
    }

    override fun stop() {
        setIdle { Drive }
    }
}