package ca.warp7.frc.action.dsl.impl;

import edu.wpi.first.wpilibj.Timer;

public class WaitFor extends BaseAction {
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