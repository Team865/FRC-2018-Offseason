package ca.warp7.frc2018_5

import ca.warp7.frc.CheesyDrive
import ca.warp7.frc.core.IControls
import ca.warp7.frc.core.XboxControlsState
import ca.warp7.frc.next.Robot
import ca.warp7.frc.next.Robot.Controls.HeldDown
import ca.warp7.frc2018_5.actions.OpenLoopDrive
import ca.warp7.frc2018_5.io.DriveOutput

object Controls : IControls {

    private val driver: XboxControlsState = Robot.getController(4, true)

    private var driveLeft = 0.0
    private var driveRight = 0.0

    private val cheesyDrive = CheesyDrive { leftSpeedDemand, rightSpeedDemand ->
        driveLeft = leftSpeedDemand
        driveRight = rightSpeedDemand
    }

    override fun mainPeriodic() {
        // Drive
        DriveOutput.solenoidOnForShifter = driver.RightBumper != HeldDown
        cheesyDrive.cheesyDrive(driver.RightXAxis, driver.LeftYAxis, driver.LeftBumper == HeldDown)
        Robot.runAction(DriveOutput, OpenLoopDrive.of(left = driveLeft, right = driveRight))
    }
}