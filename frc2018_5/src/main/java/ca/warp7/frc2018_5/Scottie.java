package ca.warp7.frc2018_5;

import ca.warp7.action.IAction;
import ca.warp7.action.impl.ActionMode;
import ca.warp7.frc.next.Robot;
import ca.warp7.frc2018_5.io.DriveOutput;
import edu.wpi.first.wpilibj.IterativeRobot;

public class Scottie extends IterativeRobot {

    @Override
    public void robotInit() {
        Robot.initSystems(
                DriveOutput.getInstance()
        );
    }

    @Override
    public final void disabledInit() {
        Robot.disable();
    }

    @Override
    public final void autonomousInit() {
        Robot.initAutonomousMode(new ActionMode() {
            @Override
            public IAction getAction() {
                return async();
            }
        }, 15);
    }

    @Override
    public final void teleopInit() {
        Robot.initTeleop(new TeleopRemote());
    }

    @Override
    public final void testInit() {
        Robot.initTest(null);
    }
}
