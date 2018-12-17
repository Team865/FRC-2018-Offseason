package ca.warp7.frc2018.v5.actions

import ca.warp7.action.impl.Singleton
import ca.warp7.frc2018.v5.states.drive.AngularPIDDrive
import ca.warp7.frc2018.v5.subsystems.Drive

class DriveForAngle(private val angle: Double, private val stop: Boolean) : Singleton() {

    override fun start_() {
        Drive.state = AngularPIDDrive
        AngularPIDDrive.setSetpoint(angle)
    }

    override fun shouldFinish_(): Boolean {
        return AngularPIDDrive.shouldFinish()
    }

    override fun stop() {
        if (stop) Drive.idle()
    }
}