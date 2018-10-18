package ca.warp7.frc2018_3.auto.actions;

import ca.warp7.frc.commons.scheduler.IAction;
import ca.warp7.frc.commons.state.PIDValues;

import static ca.warp7.frc2018_3.Components.drive;


public class DriveForDistanceAction implements IAction {

    private final double mTolerance;
    private final double mDistance;
    private final PIDValues mPID;

    private int mTicks;

    public DriveForDistanceAction(PIDValues pidValues, double distance, double tolerance) {
        mPID = pidValues;
        mDistance = distance;
        mTolerance = tolerance;
    }

    @Override
    public boolean shouldFinish() {
        if (!drive.shouldBeginPIDLoop()) {
            return true;
        }

        boolean isWithinTolerance = drive.isWithinDistanceRange(mDistance, mTolerance);

        if (isWithinTolerance) mTicks++;
        return mTicks > 17;
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
        drive.onZeroSensors();
        drive.setPIDTargetDistance(mPID, mDistance);
    }
}
