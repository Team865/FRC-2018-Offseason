package ca.warp7.frc2018.v5.actions

import ca.warp7.action.impl.Singleton
import ca.warp7.frc2018.v5.states.drive.AngularPIDDrive

class SuperstructureCloseLoop(private val liftHeight: Double,
                              private val wristAngle: Double) : Singleton() {

    override fun start_() {
    }

    override fun shouldFinish_(): Boolean {
        return AngularPIDDrive.shouldFinish()
    }

    override fun stop() {
    }
}