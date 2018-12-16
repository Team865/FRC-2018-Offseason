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
import ca.warp7.frckt.action
import ca.warp7.frckt.robotController


object Controller : ControlLoop {

    private val controller0 = robotController(port = 0, active = true)
    private val controller1 = robotController(port = 1, active = true)

    override fun setup() {
        Drive.state = CheesyDrive
        Intake.state = OpenPiston
        Superstructure.state = ReleasePosition
        Climber.idle()
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
            aButton -> Intake.state = OpenPiston
            rightTrigger -> Intake.state = KeepCube
            leftTrigger -> {
                Superstructure.state = HoldPosition
                Intake.resetNextStates(OpenPiston, OuttakeCube)
                Intake.state = action { queue(OpenPiston, OuttakeCube) }
            }
            else -> Intake.idle()
        }
    }

    private fun RobotController.updateOperator() {
        when (HeldDown) {
            yButton -> Superstructure.state = ReleasePosition
            xButton -> {
                Superstructure.state = GoToPosition
                GoToPosition.liftHeight = LiftConstants.kSetPoint1
            }
            rightTrigger -> {
                Superstructure.state = GoToPosition
                GoToPosition.liftHeight = LiftConstants.kSetPoint2
            }
            aButton -> {
                Superstructure.state = GoToPosition
                GoToPosition.liftHeight = leftYAxis
            }
        }
        GoToPosition.wristSpeed = rightYAxis
        if (startButton == HeldDown) {
            Climber.state = ManualClimb
            ManualClimb.speed = leftYAxis
        } else {
            Climber.idle()
        }
    }
}