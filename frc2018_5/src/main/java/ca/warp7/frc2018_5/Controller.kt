package ca.warp7.frc2018_5

import ca.warp7.frc.CheesyDrive
import ca.warp7.frc.core.XboxControlsState
import ca.warp7.frc.kt.Robot
import ca.warp7.frc.kt.Robot.setState
import ca.warp7.frc.next.ControlLoop
import ca.warp7.frc.next.ControlLoop.HeldDown
import ca.warp7.frc2018_5.output.DriveOutput
import ca.warp7.frc2018_5.output.IntakeOutput
import ca.warp7.frc2018_5.output.Superstructure
import ca.warp7.frc2018_5.state.drive.OpenLoopDrive
import ca.warp7.frc2018_5.state.intake.StopIntaking


object Controller : ControlLoop {

    private val driver: XboxControlsState = Robot.getController(4, true)
    private val cheesyDrive = CheesyDrive(OpenLoopDrive::setPercent)

    fun init() {
        setState { OpenLoopDrive to DriveOutput }
        setState { StopIntaking to IntakeOutput }

        Superstructure.compressorOn = false
    }

    override fun periodic() {
        // Drive
        DriveOutput.solenoidOnForShifter = driver.RightBumper != HeldDown
        cheesyDrive.cheesyDrive(driver.RightXAxis, driver.LeftYAxis, driver.LeftBumper == HeldDown)
    }
}