package ca.warp7.frc2018_3.actions;

import ca.warp7.action.impl.Singleton;

import static ca.warp7.frc2018_3.Components.intake;

public class OuttakeCube2 extends Singleton {

    private final double mDuration;
    private final double mSpeed;

    public OuttakeCube2(double duration, double speed) {
        mDuration = duration;
        mSpeed = speed;
    }

    @Override
    public void start_() {
        intake.setSpeed(mSpeed);
    }

    @Override
    public boolean shouldFinish_() {
        return getElapsed() > mDuration;
    }

    @Override
    public void stop() {
        intake.setSpeed(0);
    }
}
