package ca.warp7.frc_commons.wpi_wrapper;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Gets rid of the default messages in IterativeRobot and
 * actually print out some useful stuff. Also makes methods
 * final so subclasses don't extend them.
 */

@SuppressWarnings("EmptyMethod")
public abstract class IterativeRobotWrapper extends IterativeRobot {

	@Override
	public void robotInit() {
		logStateChange("Init");
	}

	@Override
	public void disabledInit() {
		logStateChange("Disabled");
	}

	@Override
	public void autonomousInit() {
		logStateChange("Auto");
	}

	@Override
	public void teleopInit() {
		logStateChange("Teleop");
	}

	@Override
	public void testInit() {
		logStateChange("Test");
	}

	private String mLoggedState;
	private double mOldTime;

	protected IterativeRobotWrapper() {
		super();
		mLoggedState = "";
		mOldTime = Timer.getFPGATimestamp();
	}

	private void logStateChange(String state) {
		if (state.equals(mLoggedState)) {
			return;
		}
		String oldState = mLoggedState;
		mLoggedState = state;
		SmartDashboard.putString("Robot State", state);
		double newTime = Timer.getFPGATimestamp();
		double dt = newTime - mOldTime;
		mOldTime = newTime;
		printRobotPrefix();
		System.err.print("Robot State: " + mLoggedState);
		if (!oldState.isEmpty()) {
			System.err.print(String.format(", %.0f seconds after %s began", dt, oldState));
		}
		System.err.println();
	}

	protected final String getPackageName() {
		return getClass().getPackage().getName();
	}

	protected final void printRobotPrefix() {
		System.out.print("(" + getClass().getSimpleName() + ") ");
	}


	@Override
	public final void robotPeriodic() {
		// Do nothing
	}

	@Override
	public final void disabledPeriodic() {
		// Do nothing
	}

	@Override
	public final void autonomousPeriodic() {
		// Do nothing
	}

	@Override
	public final void teleopPeriodic() {
		// Do nothing
	}

	@Override
	public final void testPeriodic() {
		// Do nothing
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
