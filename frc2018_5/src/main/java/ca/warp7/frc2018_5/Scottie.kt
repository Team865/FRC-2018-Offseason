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

    override fun autonomousInit() = runRobotAuto(mode = Baseline, timeout = 15.0)
    override fun teleopInit() = startRobotTeleop(controlLoop = Controller)
    override fun testInit() = startRobotTest(controlLoop = Controller)
    override fun disabledInit() = disableRobot()
    override fun robotPeriodic() = Unit
    override fun autonomousPeriodic() = Unit
    override fun teleopPeriodic() = Unit
    override fun disabledPeriodic() = Unit
    override fun testPeriodic() = Unit
}