package ca.warp7.frc2017_v2.auto.actions;

import ca.warp7.frc.action_graph.IAction;
import edu.wpi.first.wpilibj.Timer;

import static ca.warp7.frc2017_v2.constants.RobotMap.Subsystems.drive;

public class DriveForTimeAction implements IAction {

	private double mStartTime;
	private final double mDuration, mLeft, mRight;

	public DriveForTimeAction(double duration, double left, double right) {
		mDuration = duration;
		mLeft = left;
		mRight = right;
	}

	@Override
	public boolean shouldFinish() {
		return !drive.isOpenLoop() || Timer.getFPGATimestamp() - mStartTime > mDuration;
	}

	@Override
	public void onStart() {
		drive.openLoopDrive(mLeft, mRight);
		mStartTime = Timer.getFPGATimestamp();
	}

	@Override
	public void onUpdate() {
		// Do nothing as IO is handled by Drive in this case
	}

	@Override
	public void onStop() {
		drive.openLoopDrive(0, 0);
	}
}
