package ca.warp7.frc2018_5


import ca.warp7.frc.core.XboxControlsState
import ca.warp7.frc.next.ControlLoop
import ca.warp7.frc.next.ControlLoop.HeldDown
import ca.warp7.frc2018_5.output.*
import ca.warp7.frc2018_5.state.drive.CheesyDrive
import ca.warp7.frckt.getRobotController
import ca.warp7.frckt.setIdleState
import ca.warp7.frckt.setState


object Controller : ControlLoop {

    private val driver: XboxControlsState = getRobotController(4, true)

    fun setup() {
        setState { CheesyDrive to DriveOutput }
        setIdleState { IntakeOutput }
        setIdleState { LiftOutput }
        setIdleState { ClimberOutput }
        setIdleState { WristOutput }
        setIdleState { Superstructure }
    }

    override fun periodic() {
        // Drive
        DriveOutput.solenoidOnForShifter = driver.rightBumper != HeldDown
        CheesyDrive.wheel = driver.rightXAxis
        CheesyDrive.throttle = driver.leftXAxis
        CheesyDrive.quickTurn = driver.leftBumper == HeldDown
    }
}