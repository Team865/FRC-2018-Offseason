package ca.warp7.frc2018.v5

import ca.warp7.action.IAction
import ca.warp7.frc2018.v5.input.DriveInput
import ca.warp7.frc2018.v5.input.LiftInput
import ca.warp7.frc2018.v5.input.NavX
import ca.warp7.frc2018.v5.input.PoseEstimator
import ca.warp7.frc2018.v5.subsystems.*
import ca.warp7.frckt.*
import edu.wpi.first.wpilibj.IterativeRobot

class Scottie : IterativeRobot() {
    override fun robotInit() {
        println("Hello me is robit!")

        registerInput(DriveInput)
        registerInput(LiftInput)
        registerInput(NavX)
        registerInput(PoseEstimator)

        Drive.idle()
        Superstructure.idle()
        Superstructure.idle()
        Lift.idle()
        Climber.idle()
        Intake.idle()
        Wrist.idle()

        startRobot(loopsPerSecond = 50)
    }

    private fun selectAuto(): () -> IAction {
        return Autonomous.baseline
    }

    override fun autonomousInit() = runRobotAuto(mode = selectAuto(), updatesPerSecond = 50, timeout = 15.0)
    override fun teleopInit() = startRobotControls(controlLoop = Controller)
    override fun testInit() = startRobotControls(controlLoop = Controller)
    override fun disabledInit() = disableRobot()
    override fun robotPeriodic() = Unit
    override fun autonomousPeriodic() = Unit
    override fun teleopPeriodic() = Unit
    override fun disabledPeriodic() = Unit
    override fun testPeriodic() = Unit
}