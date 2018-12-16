package ca.warp7.frc2018.v5


import ca.warp7.frc.ControlLoop
import ca.warp7.frc.ControlLoop.HeldDown
import ca.warp7.frc.ControlLoop.Pressed
import ca.warp7.frc2018.v5.constants.LiftConstants
import ca.warp7.frc2018.v5.state.ManualClimb
import ca.warp7.frc2018.v5.state.drive.CheesyDrive
import ca.warp7.frc2018.v5.state.intake.KeepCube
import ca.warp7.frc2018.v5.state.intake.OpenPiston
import ca.warp7.frc2018.v5.state.intake.OuttakeCube
import ca.warp7.frc2018.v5.state.superstructure.GoToPosition
import ca.warp7.frc2018.v5.state.superstructure.HoldPosition
import ca.warp7.frc2018.v5.state.superstructure.ReleasePosition
import ca.warp7.frc2018.v5.subsystems.Climber
import ca.warp7.frc2018.v5.subsystems.Drive
import ca.warp7.frc2018.v5.subsystems.Intake
import ca.warp7.frc2018.v5.subsystems.Superstructure
import ca.warp7.frckt.robotController
import ca.warp7.frckt.setIdleState
import ca.warp7.frckt.setState


object Controller : ControlLoop {

    private val driver = robotController(0, true)
    private val operator = robotController(1, true)

    override fun setup() {
        setState { CheesyDrive to Drive }
        setState { OpenPiston to Intake }
        setState { ReleasePosition to Superstructure }
        setIdleState { Climber }
    }

    override fun periodic() {
        // Compressor
        if (driver.backButton == Pressed) {
            Superstructure.compressorOn = !(Superstructure.compressorOn)
        }

        // Drive
        CheesyDrive.solenoidOnForShifter = driver.rightBumper != HeldDown
        CheesyDrive.wheel = driver.rightXAxis
        CheesyDrive.throttle = driver.leftXAxis
        CheesyDrive.quickTurn = driver.leftBumper == HeldDown

        // Intake
        when (HeldDown) {
            driver.aButton -> setState { OpenPiston to Intake }
            driver.rightTrigger -> setState { KeepCube to Intake }
            driver.leftTrigger -> {
                setState { HoldPosition to Superstructure }
                setState { OuttakeCube to Intake }
            }
            else -> setIdleState { Intake }
        }

        // Lift/Wrist
        when (HeldDown) {
            operator.yButton -> {
                setState { ReleasePosition to Superstructure }
            }
            operator.xButton -> {
                setState { GoToPosition to Superstructure }
                GoToPosition.liftHeight = LiftConstants.kSetPoint1
            }
            operator.rightTrigger -> {
                setState { GoToPosition to Superstructure }
                GoToPosition.liftHeight = LiftConstants.kSetPoint2
            }
            operator.aButton -> {
                setState { GoToPosition to Superstructure }
                GoToPosition.liftHeight = operator.leftYAxis
            }
        }

        GoToPosition.wristSpeed = operator.rightYAxis

        // Climber
        if (operator.startButton == HeldDown) {
            setIdleState { Superstructure }
            setState { ManualClimb to Climber }
            ManualClimb.speed = operator.leftYAxis
        } else {
            setIdleState { Climber }
        }
    }
}