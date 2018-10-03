package ca.warp7.frc.wpi_wrapper;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Gets rid of the default messages in IterativeRobot
 */

public abstract class IterativeRobotWrapper extends IterativeRobot {

	@Override
	public void robotInit() {
		logState("Started");
	}

	@Override
	public void disabledInit() {
		logState("Disabled");
	}

	@Override
	public void autonomousInit() {
		logState("Auto");
	}

	@Override
	public void teleopInit() {
		logState("Teleop");
	}

	@Override
	public void testInit() {
		logState("Test");
	}

	@Override
	public void robotPeriodic() {
		// Do nothing
	}

	@Override
	public void disabledPeriodic() {
		// Do nothing
	}

	@Override
	public void autonomousPeriodic() {
		// Do nothing
	}

	@Override
	public void teleopPeriodic() {
		// Do nothing
	}

	@Override
	public void testPeriodic() {
		// Do nothing
	}

	private String mLoggedRobotState;

	protected IterativeRobotWrapper() {
		super();
	}

	private void logState(String state) {
		if (state.equals(mLoggedRobotState)) {
			return;
		}
		mLoggedRobotState = state;
		SmartDashboard.putString("Robot State", state);
		printRobotPrefix();
		System.out.println("Robot State: " + state);
	}


	protected String getPackageName() {
		return getClass().getPackage().getName();
	}

	protected String getRobotPrefix() {
		return "(" + getClass().getSimpleName() + ") ";
	}

	protected void printRobotPrefix() {
		System.out.print(getRobotPrefix());
	}
}
