package ca.warp7.frc2018_5

import ca.warp7.frc.CheesyDrive
import ca.warp7.frc.core.XboxControlsState
import ca.warp7.frc.kt.Robot
import ca.warp7.frc.next.ControlLoop
import ca.warp7.frc.next.ControlLoop.HeldDown
import ca.warp7.frc2018_5.output.DriveOutput
import ca.warp7.frc2018_5.states.DriveOpenLoopState


object Controls : ControlLoop {

    private val driver: XboxControlsState = Robot.getController(4, true)
    private val cheesyDrive = CheesyDrive(DriveOpenLoopState::setPercent)

    override fun init() {
        Robot.setState(DriveOpenLoopState to DriveOutput)
    }

    override fun periodic() {
        // Drive
        DriveOutput.solenoidOnForShifter = driver.RightBumper != HeldDown
        cheesyDrive.cheesyDrive(driver.RightXAxis, driver.LeftYAxis, driver.LeftBumper == HeldDown)
    }
}