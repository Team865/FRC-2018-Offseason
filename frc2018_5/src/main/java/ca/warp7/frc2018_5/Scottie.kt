package ca.warp7.frc2018_5

import ca.warp7.action.IAction
import ca.warp7.action.impl.ActionMode
import ca.warp7.frc.next.Robot
import ca.warp7.frc2018_5.output.DriveOutput
import edu.wpi.first.wpilibj.IterativeRobot

class Scottie : IterativeRobot() {

    override fun robotInit() {
        Robot.initSystems(
                DriveOutput
        )
    }

    override fun disabledInit() {
        Robot.disable()
    }

    override fun autonomousInit() {
        Robot.initAutonomousMode(object : ActionMode() {
            override fun getAction(): IAction {
                return async()
            }
        }, 15.0)
    }

    override fun teleopInit() {
        Robot.initTeleop(TeleopRemote())
    }

    override fun testInit() {
        Robot.initTest(null)
    }

    override fun robotPeriodic() = Unit
    override fun autonomousPeriodic() = Unit
    override fun teleopPeriodic() = Unit
    override fun disabledPeriodic() = Unit
    override fun testPeriodic() = Unit
}
