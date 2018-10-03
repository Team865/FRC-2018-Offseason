package ca.warp7.frc.cheesy_drive;

class CheesyDriveState {
	static class CurrentState {
		double quickStopAccumulator = 0;
		double oldWheel = 0;
	}

	static class InputState {
		double wheel;
		double throttle;
		boolean quickTurn;
		boolean altQuickTurn;
	}
}
