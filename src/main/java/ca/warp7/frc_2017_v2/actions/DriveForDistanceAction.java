package ca.warp7.frc_2017_v2.actions;

import ca.warp7.frc.core.IAutonomousAction;
import ca.warp7.frc_2017_v2.mapping.Mapping;

import static ca.warp7.frc_2017_v2.mapping.Mapping.Subsystems.drive;

public class DriveForDistanceAction implements IAutonomousAction {

	private static final double kPIDTolerance = 15;

	private final double mDistance;

	public DriveForDistanceAction(double distance) {
		mDistance = distance;
	}

	@Override
	public boolean actionShouldFinish() {
		return !drive.isPIDLoop() || drive.isWithinDistanceRange(mDistance, kPIDTolerance);
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
		drive.setPIDTargetDistance(Mapping.PID.uniformDrivePID, mDistance);
	}
}
