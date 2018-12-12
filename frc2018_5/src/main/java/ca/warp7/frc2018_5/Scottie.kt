package ca.warp7.frc2018_5

import ca.warp7.frc.kt.Robot
import ca.warp7.frc2018_5.auto.Baseline
import ca.warp7.frc2018_5.input.DriveInput
import ca.warp7.frc2018_5.input.LiftInput
import ca.warp7.frc2018_5.input.NavX
import ca.warp7.frc2018_5.input.PoseEstimator
import ca.warp7.frc2018_5.output.*
import edu.wpi.first.wpilibj.IterativeRobot

class Scottie : IterativeRobot() {

    override fun robotInit() {
        println("Hello me is robit!")
        Robot.init(
                inputSystems = arrayOf(
                        DriveInput,
                        LiftInput,
                        NavX,
                        PoseEstimator
                ),
                outputSystems = arrayOf(
                        DriveOutput,
                        Superstructure,
                        LiftOutput,
                        Climber,
                        Intake,
                        Wrist
                )
        )
    }

    override fun disabledInit() {
        Robot.disable()
    }

    override fun autonomousInit() {
        Robot.initAuto(
                mode = Baseline,
                timeout = 15.0
        )
    }

    override fun teleopInit() {
        Robot.initTeleop(controlLoop = Controls)
    }

    override fun testInit() {
        Robot.initTest(controlLoop = Controls)
    }

    override fun robotPeriodic() = Unit
    override fun autonomousPeriodic() = Unit
    override fun teleopPeriodic() = Unit
    override fun disabledPeriodic() = Unit
    override fun testPeriodic() = Unit
}
