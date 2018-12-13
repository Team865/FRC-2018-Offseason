package ca.warp7.frc2018_5


import ca.warp7.frc.core.XboxControlsState
import ca.warp7.frc.next.ControlLoop
import ca.warp7.frc.next.ControlLoop.HeldDown
import ca.warp7.frc2018_5.output.*
import ca.warp7.frc2018_5.state.drive.CheesyDrive
import ca.warp7.frckt.getRobotController
import ca.warp7.frckt.setIdleState
import ca.warp7.frckt.setRobotState


object Controller : ControlLoop {

    private val driver: XboxControlsState = getRobotController(4, true)

    fun setup() {
        setRobotState { CheesyDrive to DriveOutput }
        setIdleState { IntakeOutput }
        setIdleState { LiftOutput }
        setIdleState { ClimberOutput }
        setIdleState { WristOutput }
        setIdleState { Superstructure }
    }

    override fun periodic() {
        // Drive
        DriveOutput.solenoidOnForShifter = driver.RightBumper != HeldDown
        CheesyDrive.wheel = driver.RightXAxis
        CheesyDrive.throttle = driver.LeftYAxis
        CheesyDrive.quickTurn = driver.LeftBumper == HeldDown
    }
}