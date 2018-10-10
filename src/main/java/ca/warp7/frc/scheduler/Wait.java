package ca.warp7.frc.scheduler;

import edu.wpi.first.wpilibj.Timer;

public class Wait implements IAction {
	private double mStartTime;
	private double mDuration;

	public Wait(double duration) {
		mDuration = duration;
	}

	@Override
	public void onStart() {
		mStartTime = Timer.getFPGATimestamp();
	}

	@Override
	public boolean shouldFinish() {
		return Timer.getFPGATimestamp() >= (mStartTime + mDuration);
	}
}
