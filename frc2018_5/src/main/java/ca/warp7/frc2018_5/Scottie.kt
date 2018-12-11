package ca.warp7.frc2018_5

import ca.warp7.frc.next.Robot
import ca.warp7.frc2018_5.input.NavX
import ca.warp7.frc2018_5.output.DriveOutput
import edu.wpi.first.wpilibj.IterativeRobot

class Scottie : IterativeRobot() {

    override fun robotInit() {
        Robot.initSystems(
                DriveOutput,
                NavX
        )
    }

    override fun disabledInit() {
        Robot.disable()
    }

    override fun autonomousInit() {
        Robot.initAutonomousMode(null, 15.0)
    }

    override fun teleopInit() {
        Robot.initTeleop(Controls)
    }

    override fun testInit() {
        Robot.initTest(Controls)
    }

    override fun robotPeriodic() = Unit
    override fun autonomousPeriodic() = Unit
    override fun teleopPeriodic() = Unit
    override fun disabledPeriodic() = Unit
    override fun testPeriodic() = Unit
}
