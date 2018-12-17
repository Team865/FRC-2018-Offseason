package ca.warp7.frc2018.v5

import ca.warp7.frc2018.v5.inputs.*
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
        registerInput(AutoInput)

        Drive.setIdle()
        Superstructure.setIdle()
        Superstructure.setIdle()
        Lift.setIdle()
        Climber.setIdle()
        Intake.setIdle()
        Wrist.setIdle()

        startRobot(loopsPerSecond = 50)
    }

    override fun autonomousInit() = runRobotAuto(mode = Autonomous.getMode(), timeout = 15.0)
    override fun teleopInit() = startRobotControls(controlLoop = Controller)
    override fun testInit() = startRobotControls(controlLoop = Controller)
    override fun disabledInit() = disableRobot()
    override fun robotPeriodic() = Unit
    override fun autonomousPeriodic() = Unit
    override fun teleopPeriodic() = Unit
    override fun disabledPeriodic() = Unit
    override fun testPeriodic() = Unit
}