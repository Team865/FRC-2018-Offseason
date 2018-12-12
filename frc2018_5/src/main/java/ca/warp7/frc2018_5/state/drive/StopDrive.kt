package ca.warp7.frc2018_5.state.drive

import ca.warp7.action.IAction
import ca.warp7.frc2018_5.output.DriveOutput

object StopDrive : IAction {

    @Synchronized
    override fun start() {
        DriveOutput.leftPercentOutput = 0.0
        DriveOutput.rightPercentOutput = 0.0
    }

    @Synchronized
    override fun shouldFinish(): Boolean {
        return true
    }
}