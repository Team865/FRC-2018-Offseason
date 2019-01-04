package ca.warp7.frc2018.v5.actions

import ca.warp7.action.impl.Singleton
import ca.warp7.frc2018.v5.states.drive.TrapezoidalVelocityPIDDrive
import ca.warp7.frc2018.v5.subsystems.Drive

class DriveForDistanceTrapezoidalVelocityPID(private val distance: Double) : Singleton() {
    override fun start_() {
        Drive.state = TrapezoidalVelocityPIDDrive
        TrapezoidalVelocityPIDDrive.setSetpoint(distance)
    }

    override fun shouldFinish_(): Boolean {
        return TrapezoidalVelocityPIDDrive.shouldFinish()
    }
}