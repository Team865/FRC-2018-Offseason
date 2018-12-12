package ca.warp7.frc2018_5


import ca.warp7.frc.core.XboxControlsState
import ca.warp7.frc.kt.Robot
import ca.warp7.frc.next.ControlLoop
import ca.warp7.frc.next.ControlLoop.HeldDown
import ca.warp7.frc2018_5.output.DriveOutput
import ca.warp7.frc2018_5.output.IntakeOutput
import ca.warp7.frc2018_5.output.Superstructure
import ca.warp7.frc2018_5.state.drive.CheesyDrive
import ca.warp7.frc2018_5.state.intake.StopIntaking


object Controller : ControlLoop {

    private val driver: XboxControlsState = Robot.getController(4, true)

    fun init() {
        Robot.setState { CheesyDrive to DriveOutput }
        Robot.setState { StopIntaking to IntakeOutput }
        Robot.setIdle { Superstructure }
    }

    override fun periodic() {
        // Drive
        DriveOutput.solenoidOnForShifter = driver.RightBumper != HeldDown
        CheesyDrive.wheel = driver.RightXAxis
        CheesyDrive.throttle = driver.LeftYAxis
        CheesyDrive.quickTurn = driver.LeftBumper == HeldDown
    }
}