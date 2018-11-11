package ca.warp7.frc.commons.action.dsl.impl;

import ca.warp7.frc.commons.core.IAction;
import edu.wpi.first.wpilibj.Timer;

public class WaitFor implements IAction {
    private double mStartTime;
    private double mDuration;

    WaitFor(double duration) {
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
