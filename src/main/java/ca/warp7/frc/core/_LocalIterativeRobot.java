package ca.warp7.frc.core;

import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * Gets rid of the overload messages in IterativeRobot
 */

abstract class _LocalIterativeRobot extends IterativeRobot {

	@Override
	public void robotInit() {
	}

	@Override
	public void disabledInit() {
	}

	@Override
	public void autonomousInit() {
	}

	@Override
	public void teleopInit() {
	}

	@Override
	public void testInit() {
	}

	@Override
	public void robotPeriodic() {
	}

	@Override
	public void disabledPeriodic() {
	}

	@Override
	public void autonomousPeriodic() {
	}

	@Override
	public void teleopPeriodic() {
	}

	@Override
	public void testPeriodic() {
	}
}
