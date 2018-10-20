package ca.warp7.frc.commons.wpi_wrapper;

import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * Make methods final so subclasses don't extend them
 */

@SuppressWarnings("EmptyMethod")
public abstract class IterativeRobotWrapper extends IterativeRobot {

    public IterativeRobotWrapper() {
        super();
    }

    @Override
    public final void robotPeriodic() {
    }

    @Override
    public final void disabledPeriodic() {
    }

    @Override
    public final void autonomousPeriodic() {
    }

    @Override
    public final void teleopPeriodic() {
    }

    @Override
    public final void testPeriodic() {
    }

    @Override
    protected final void loopFunc() {
        super.loopFunc();
    }

    @Override
    public final void free() {
        super.free();
    }

    @Override
    public final boolean isDisabled() {
        return super.isDisabled();
    }

    @Override
    public final boolean isEnabled() {
        return super.isEnabled();
    }

    @Override
    public final boolean isAutonomous() {
        return super.isAutonomous();
    }

    @Override
    public final boolean isTest() {
        return super.isTest();
    }

    @Override
    public final boolean isOperatorControl() {
        return super.isOperatorControl();
    }

    @Override
    public final boolean isNewDataAvailable() {
        return super.isNewDataAvailable();
    }
}
