package ca.warp7.frc2018_4.actions;

import ca.warp7.action.IAction;
import edu.wpi.first.wpilibj.Timer;

import static ca.warp7.frc2018_4.Components.intake;

public class OuttakeCube implements IAction {

    private double mStartTime;
    private final double mDuration;
    private final double mSpeed;

    public OuttakeCube(double duration, double speed) {
        mDuration = duration;
        mSpeed = speed;
    }

    @Override
    public void start() {
        intake.setSpeed(mSpeed);
        mStartTime = Timer.getFPGATimestamp();
    }

    @Override
    public boolean shouldFinish() {
        return ((Timer.getFPGATimestamp() - mStartTime) >= mDuration);
    }

    @Override
    public void update() {
    }

    @Override
    public void stop() {
        intake.setSpeed(0);
    }
}
