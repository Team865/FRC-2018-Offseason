package ca.warp7.frc2018_5

import ca.warp7.frc2018_5.auto.Baseline
import ca.warp7.frc2018_5.input.DriveInput
import ca.warp7.frc2018_5.input.LiftInput
import ca.warp7.frc2018_5.input.NavX
import ca.warp7.frc2018_5.input.PoseEstimator
import ca.warp7.frc2018_5.output.*
import ca.warp7.frckt.*
import edu.wpi.first.wpilibj.IterativeRobot

class Scottie : IterativeRobot() {

    override fun robotInit() {
        println("Hello me is robit!")
        initRobotSystems(
                loopsPerSecond = 50,
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
                        ClimberOutput,
                        IntakeOutput,
                        WristOutput
                )
        )
    }

    override fun disabledInit() {
        disableRobot()
    }

    override fun autonomousInit() {
        runRobotAuto(
                mode = Baseline,
                timeout = 15.0
        )
    }

    override fun teleopInit() {
        Controller.setup()
        startRobotTeleop(controlLoop = Controller)
    }

    override fun testInit() {
        Controller.setup()
        startRobotTest(controlLoop = Controller)
    }

    override fun robotPeriodic() = Unit
    override fun autonomousPeriodic() = Unit
    override fun teleopPeriodic() = Unit
    override fun disabledPeriodic() = Unit
    override fun testPeriodic() = Unit
}