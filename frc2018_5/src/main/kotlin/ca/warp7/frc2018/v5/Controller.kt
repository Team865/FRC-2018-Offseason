package ca.warp7.frc2018.v5


import ca.warp7.frc.ControlLoop
import ca.warp7.frc.ControlLoop.HeldDown
import ca.warp7.frc.ControlLoop.Pressed
import ca.warp7.frc.RobotController
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
import ca.warp7.frckt.setIdle
import ca.warp7.frckt.setState


object Controller : ControlLoop {

    private val controller0 = robotController(port = 0, active = true)
    private val controller1 = robotController(port = 1, active = true)

    override fun setup() {
        setState { CheesyDrive to Drive }
        setState { OpenPiston to Intake }
        setState { ReleasePosition to Superstructure }
        setIdle { Climber }
    }

    override fun periodic() {
        controller0.updateDriver()
        controller1.updateOperator()
    }

    private fun RobotController.updateDriver() {
        if (backButton == Pressed) Superstructure.compressorOn = !(Superstructure.compressorOn)
        CheesyDrive.solenoidOnForShifter = rightBumper != HeldDown
        CheesyDrive.wheel = rightXAxis
        CheesyDrive.throttle = leftXAxis
        CheesyDrive.quickTurn = leftBumper == HeldDown
        when (HeldDown) {
            aButton -> setState { OpenPiston to Intake }
            rightTrigger -> setState { KeepCube to Intake }
            leftTrigger -> {
                setState { HoldPosition to Superstructure }
                setState { OuttakeCube to Intake }
            }
            else -> setIdle { Intake }
        }
    }

    private fun RobotController.updateOperator() {
        when (HeldDown) {
            yButton -> setState { ReleasePosition to Superstructure }
            xButton -> {
                setState { GoToPosition to Superstructure }
                GoToPosition.liftHeight = LiftConstants.kSetPoint1
            }
            rightTrigger -> {
                setState { GoToPosition to Superstructure }
                GoToPosition.liftHeight = LiftConstants.kSetPoint2
            }
            aButton -> {
                setState { GoToPosition to Superstructure }
                GoToPosition.liftHeight = leftYAxis
            }
        }
        GoToPosition.wristSpeed = rightYAxis
        if (startButton == HeldDown) {
            setState { ManualClimb to Climber }
            ManualClimb.speed = leftYAxis
        } else {
            setIdle { Climber }
        }
    }
}