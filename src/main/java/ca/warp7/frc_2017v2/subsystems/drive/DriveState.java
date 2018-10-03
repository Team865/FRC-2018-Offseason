package ca.warp7.frc_2017v2.subsystems.drive;

import ca.warp7.frc.math.PID;

class DriveState {
	static class InputState {
		boolean shouldReverse;
		boolean shouldSolenoidBeOnForShifter;
		boolean shouldBeginOpenLoop;
		boolean shouldBeginPIDLoop;
		double demandedLeftSpeed;
		double demandedRightSpeed;
		double measuredLeftDistance;
		double measuredRightDistance;
		PID.InputState leftPIDInput = new PID.InputState();
		PID.InputState rightPIDInput = new PID.InputState();
	}

	static class CurrentState {
		boolean isReversed;
		boolean isSolenoidOnForShifter;
		boolean isOpenLoop;
		boolean isPIDLoop;
		double leftSpeed;
		double rightSpeed;
		PID.CurrentState leftPID = new PID.CurrentState();
		PID.CurrentState rightPID = new PID.CurrentState();
	}
}
