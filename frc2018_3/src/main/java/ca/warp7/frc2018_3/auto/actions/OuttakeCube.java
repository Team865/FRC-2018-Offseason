package ca.warp7.frc2018_3.auto.actions;

import ca.warp7.frc.commons.core.IAction;
import edu.wpi.first.wpilibj.Timer;

import static ca.warp7.frc2018_3.Components.intake;

public class OuttakeCube implements IAction {

    private double mStartTime;
    private final double mDuration;
    private final double mSpeed;

    public OuttakeCube(double duration, double speed) {
        mDuration = duration;
        mSpeed = speed;
    }

    @Override
    public void onStart() {
        intake.setSpeed(mSpeed);
        mStartTime = Timer.getFPGATimestamp();
    }

    @Override
    public boolean shouldFinish() {
        return ((Timer.getFPGATimestamp() - mStartTime) >= mDuration);
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onStop() {
        intake.setSpeed(0);
    }
}
