package ca.warp7.frc2018_3.auto.actions;

import ca.warp7.frc.commons.scheduler.IAction;
import ca.warp7.frc.commons.state.PIDValues;

import static ca.warp7.frc2018_3.Components.drive;


public class DriveForDistanceAction implements IAction {

    private final double mTolerance;
    private final double mDistance;
    private final PIDValues mPID;

    public DriveForDistanceAction(PIDValues pidValues, double distance, double tolerance) {
        mPID = pidValues;
        mDistance = distance;
        mTolerance = tolerance;
    }

    @Override
    public boolean shouldFinish() {
        return !drive.shouldBeginPIDLoop() || drive.isWithinDistanceRange(mDistance, mTolerance);
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
        drive.onZeroSensors();
        drive.setPIDTargetDistance(mPID, mDistance);
    }
}
