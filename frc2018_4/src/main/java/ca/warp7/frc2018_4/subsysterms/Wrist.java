package ca.warp7.frc2018_4.subsysterms;

import ca.warp7.frc.core.ISubsystem;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

import static ca.warp7.frc.Functions.limit;
import static ca.warp7.frc.core.Robot.reportInputAndState;
import static ca.warp7.frc2018_4.constants.Pins.*;
import static ca.warp7.frc2018_4.constants.WristConstants.*;

public class Wrist implements ISubsystem {
    private SpeedControllerGroup mWristMotorGroup;
    public InputState mInputState = new InputState();
    private State mState = new State();
    @Override
    public void onConstruct() {
        mWristMotorGroup = new SpeedControllerGroup(new WPI_VictorSPX(kWristPin));
    }

    @Override
    public void onDisabled() {
        mInputState.mDemandedWristAngularVelocity = 0;
    }

    @Override
    public void onAutonomousInit() {

    }

    @Override
    public void onTeleopInit() {

    }

    @Override
    public void onMeasure() {

    }

    @Override
    public void onZeroSensors() {
        mState.mCurrentPredictedAngle = 0;
    }

    @Override
    public void onOutput() {
        mWristMotorGroup.set(limit(mState.mSpeed, kMaxOutputPower));
    }

    @Override
    public void onUpdateState() {
        if (mInputState.mShouldSlowFall){
            // TODO add general ramping method to commons
            mState.mSpeed = (mState.mSpeed - kSlowFallSpeed)/kRandomDiffDivision;
        }
        else if (mInputState.mShouldUseTargetAngle) {
            // TODO add this
        }
        else{
            mState.mSpeed = (mState.mSpeed - mInputState.mDemandedWristAngularVelocity) / kRandomDiffDivision;
        }
    }

    @Override
    public void onReportState() {
        reportInputAndState(this, mInputState, mState);
    }

    public class InputState{
        public boolean mShouldSlowFall;
        public double mDemandedWristAngularVelocity;
        public double mSuperstructureWantedAngularVelocity;
        public double mDemandedWristAngle;
        public boolean mShouldUseTargetAngle;
    }

    public class State{
        double mSpeed;
        double mCurrentPredictedAngle;
    }
}
