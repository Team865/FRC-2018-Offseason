package ca.warp7.frc2018_3.actions;

import ca.warp7.action.IAction;
import edu.wpi.first.wpilibj.Timer;

import static ca.warp7.frc2018_3.Components.drive;

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
        // This action should finish when drive is not going open loop
        // or if the set duration is reached
        return !drive.shouldBeginOpenLoop() || ((Timer.getFPGATimestamp() - mStartTime) >= mDuration);
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
