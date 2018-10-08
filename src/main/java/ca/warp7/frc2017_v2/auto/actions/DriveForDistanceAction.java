package ca.warp7.frc2017_v2.auto.actions;

import ca.warp7.frc.scheduler.IAction;
import ca.warp7.frc2017_v2.constants.RobotMap;

import static ca.warp7.frc2017_v2.constants.RobotMap.Subsystems.drive;

public class DriveForDistanceAction implements IAction {

	private final double mTolerance;
	private final double mDistance;

	public DriveForDistanceAction(double distance, double tolerance) {
		mDistance = distance;
		mTolerance = tolerance;
	}

	@Override
	public boolean shouldFinish() {
		return !drive.isPIDLoop() || drive.isWithinDistanceRange(mDistance, mTolerance);
	}

	@Override
	public void onUpdate() {
	}

	@Override
	public void onStop() {
		drive.openLoopDrive(0, 0);
	}

	@Override
	public void onStart() {
		// Reset the encoders and start the PIDLoop
		drive.zeroSensors();
		drive.setPIDTargetDistance(RobotMap.PID.uniformDrivePID, mDistance);
	}
}
