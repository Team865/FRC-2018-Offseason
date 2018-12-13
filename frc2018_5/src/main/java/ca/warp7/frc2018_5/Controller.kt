package ca.warp7.frc2018_5


import ca.warp7.frc.core.XboxControlsState
import ca.warp7.frc.kt.Robot
import ca.warp7.frc.next.ControlLoop
import ca.warp7.frc.next.ControlLoop.HeldDown
import ca.warp7.frc2018_5.output.*
import ca.warp7.frc2018_5.state.drive.CheesyDrive


object Controller : ControlLoop {

    private val driver: XboxControlsState = Robot.getController(4, true)

    fun setup() {
        Robot.setState { CheesyDrive to DriveOutput }
        Robot.setIdle { IntakeOutput }
        Robot.setIdle { LiftOutput }
        Robot.setIdle { ClimberOutput }
        Robot.setIdle { WristOutput }
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