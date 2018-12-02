package ca.warp7.frc2018_4.actions;

import ca.warp7.action.IAction;
import ca.warp7.frc.PIDValues;

import static ca.warp7.frc2018_4.Components.drive;


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
        if (!drive.shouldBeginLinearPID()) {
            return true;
        }

        boolean isWithinTolerance = drive.isWithinDistanceRange(mDistance, mTolerance);

        if (isWithinTolerance) mTicks++;
        return mTicks > 17;
    }

    @Override
    public void update() {
    }

    @Override
    public void stop() {
        drive.openLoopDrive(0, 0);
    }

    @Override
    public void start() {
        drive.onZeroSensors();
        drive.setPIDTargetDistance(mPID, mDistance);
    }
}
