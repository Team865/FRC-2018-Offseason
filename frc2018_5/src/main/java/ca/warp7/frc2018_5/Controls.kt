package ca.warp7.frc2018_5

import ca.warp7.frc.CheesyDrive
import ca.warp7.frc.core.XboxControlsState
import ca.warp7.frc.kt.RobotKt
import ca.warp7.frc.next.ControlLoop
import ca.warp7.frc.next.ControlLoop.HeldDown
import ca.warp7.frc2018_5.actions.OpenLoopDrive
import ca.warp7.frc2018_5.output.DriveOutput


object Controls : ControlLoop {

    private val driver: XboxControlsState = RobotKt.getController(4, true)

    private val cheesyDrive = CheesyDrive { leftSpeedDemand, rightSpeedDemand ->
        OpenLoopDrive.leftPercent = leftSpeedDemand
        OpenLoopDrive.rightPercent = rightSpeedDemand
    }

    override fun init() {
        RobotKt.assign(OpenLoopDrive to DriveOutput)
    }

    override fun periodic() {
        // Drive
        DriveOutput.solenoidOnForShifter = driver.RightBumper != HeldDown
        cheesyDrive.cheesyDrive(driver.RightXAxis, driver.LeftYAxis, driver.LeftBumper == HeldDown)
    }
}