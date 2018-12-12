package ca.warp7.frc2018_5

import ca.warp7.frc.kt.RobotKt
import ca.warp7.frc2018_5.input.DriveInput
import ca.warp7.frc2018_5.input.LiftInput
import ca.warp7.frc2018_5.input.NavX
import ca.warp7.frc2018_5.input.PoseEstimator
import ca.warp7.frc2018_5.output.*
import edu.wpi.first.wpilibj.IterativeRobot

class Scottie : IterativeRobot() {

    override fun robotInit() {
        println("Hello me is robit!")
        RobotKt.init(
                inputSystems = arrayOf(
                        DriveInput,
                        LiftInput,
                        NavX,
                        PoseEstimator
                ),
                outputSystems = arrayOf(
                        DriveOutput,
                        LiftOutput,
                        Climber,
                        Intake,
                        Pneumatics
                )
        )
    }

    override fun disabledInit() {
        RobotKt.disable()
    }

    override fun autonomousInit() {
        RobotKt.initAuto(
                mode = null,
                timeout = 15.0
        )
    }

    override fun teleopInit() {
        RobotKt.initTeleop(controlLoop = Controls)
    }

    override fun testInit() {
        RobotKt.initTest(controlLoop = Controls)
    }

    override fun robotPeriodic() = Unit
    override fun autonomousPeriodic() = Unit
    override fun teleopPeriodic() = Unit
    override fun disabledPeriodic() = Unit
    override fun testPeriodic() = Unit
}
