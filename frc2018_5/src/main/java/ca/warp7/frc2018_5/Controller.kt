package ca.warp7.frc2018_5


import ca.warp7.frc.next.ControlLoop
import ca.warp7.frc.next.ControlLoop.HeldDown
import ca.warp7.frc.next.ControlLoop.Pressed
import ca.warp7.frc2018_5.output.*
import ca.warp7.frc2018_5.state.ManualClimb
import ca.warp7.frc2018_5.state.drive.CheesyDrive
import ca.warp7.frc2018_5.state.intake.KeepCube
import ca.warp7.frc2018_5.state.intake.OpenPiston
import ca.warp7.frc2018_5.state.intake.OuttakeCube
import ca.warp7.frckt.robotController
import ca.warp7.frckt.setIdleState
import ca.warp7.frckt.setState


object Controller : ControlLoop {

    private val driver = robotController(0, true)
    private val operator = robotController(1, true)

    override fun setup() {
        setState { CheesyDrive to DriveOutput }
        setIdleState { IntakeOutput }
        setIdleState { LiftOutput }
        setIdleState { ClimberOutput }
        setIdleState { WristOutput }
        setIdleState { Superstructure }
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
            driver.aButton -> setState { OpenPiston to IntakeOutput }
            driver.leftTrigger -> setState { OuttakeCube to IntakeOutput }
            driver.rightTrigger -> setState { KeepCube to IntakeOutput }
            else -> setIdleState { IntakeOutput }
        }

        // Climber
        if (operator.startButton == HeldDown) {
            setState { ManualClimb to ClimberOutput }
            ManualClimb.speed = operator.leftYAxis
        } else {
            setIdleState { ClimberOutput }
        }
    }
}