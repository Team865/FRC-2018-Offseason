package ca.warp7.frc2018.v5

import ca.warp7.frc2018.v5.auto.Baseline
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
        setInputs(DriveInput, LiftInput, NavX, PoseEstimator)
        setIdleState { Drive }
        setIdleState { Superstructure }
        setIdleState { Lift }
        setIdleState { Climber }
        setIdleState { Intake }
        setIdleState { Wrist }
        startRobot(loopsPerSecond = 50)
    }

    override fun autonomousInit() = runRobotAuto(mode = Baseline, updatesPerSecond = 50, timeout = 15.0)
    override fun teleopInit() = startRobotControls(controlLoop = Controller)
    override fun testInit() = startRobotControls(controlLoop = Controller)
    override fun disabledInit() = disableRobot()
    override fun robotPeriodic() = Unit
    override fun autonomousPeriodic() = Unit
    override fun teleopPeriodic() = Unit
    override fun disabledPeriodic() = Unit
    override fun testPeriodic() = Unit
}