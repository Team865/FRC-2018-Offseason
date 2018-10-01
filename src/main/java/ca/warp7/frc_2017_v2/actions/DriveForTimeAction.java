package ca.warp7.frc_2017_v2.actions;

import ca.warp7.frc.core.IAutonomousAction;
import edu.wpi.first.wpilibj.Timer;

import static ca.warp7.frc_2017_v2.mapping.Mapping.Subsystems.drive;

public class DriveForTimeAction implements IAutonomousAction {

	private double mStartTime;
	private final double mDuration, mLeft, mRight;

	public DriveForTimeAction(double duration, double left, double right) {
		mDuration = duration;
		mLeft = left;
		mRight = right;
	}

	/*
	public void onConfigureAction(ActionDelegate delegate){
	delegate.acquireSubsystem(drive)
	delegate.setTerminationMode(UNTIL_DURATION_OR_ACQUIRE)
	}
	 */

	@Override
	public boolean actionShouldFinish() {
		return !drive.isOpenLoop() || Timer.getFPGATimestamp() - mStartTime > mDuration;
	}

	@Override
	public void onUpdate() {
		// Do nothing as IO is handled by Drive in this case
	}

	@Override
	public void onStop() {
		drive.openLoopDrive(0, 0);
	}

	@Override
	public void onStart() {
		drive.openLoopDrive(mLeft, mRight);
		mStartTime = Timer.getFPGATimestamp();
	}
}
