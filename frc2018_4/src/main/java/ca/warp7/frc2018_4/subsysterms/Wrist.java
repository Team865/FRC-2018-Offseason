package ca.warp7.frc2018_4.subsysterms;

import ca.warp7.frc.core.ISubsystem;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.stormbots.MiniPID;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Timer;

import static ca.warp7.frc.Functions.limit;
import static ca.warp7.frc.core.Robot.reportInputAndState;
import static ca.warp7.frc2018_4.constants.Pins.*;
import static ca.warp7.frc2018_4.constants.WristConstants.*;

public class Wrist implements ISubsystem {
    private SpeedControllerGroup mWristMotorGroup;
    public InputState mInputState = new InputState();
    private State mState = new State();
    final MiniPID wristLinearPID = new MiniPID(0, 0, 0, 0);

    @Override
    public void onConstruct() {
        mWristMotorGroup = new SpeedControllerGroup(new WPI_VictorSPX(kWristPin));
    }

    @Override
    public void onDisabled() {
        mInputState.mDemandedWristAngularVelocity = 0;
        mInputState.mShouldUseTargetAngle = false;
        mInputState.mDemandedWristAngle = 0;
        mInputState.mShouldSlowFall = false;
        mWristMotorGroup.disable();
    }

    @Override
    public void onAutonomousInit() {

    }

    @Override
    public void onTeleopInit() {

    }

    @Override
    public void onMeasure() {

        double old_time = mState._timestamp;
        mState._timestamp = Timer.getFPGATimestamp();
        double dt = mState._timestamp - old_time;
        mState.mCurrentPredictedAngle+= mState.mSpeed * dt * kWristDegreesPerMotorRotation;

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
        // TODO use actions and a switch statement
        if (mInputState.mShouldSlowFall){
            // TODO add general ramping method to commons
            mState.mSpeed = slowFallOuput(mState.mSpeed);
            onZeroSensors();
        }
        else if (mInputState.mShouldUseTargetAngle) {
            // TODO add dead reckoning to target angle based on time and speeds
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
        double _timestamp;
    }

    private static double slowFallOuput(double currentSpeed){
        return currentSpeed + (kWristSlowFallTargetSpeed - currentSpeed) / kRandomDiffDivision;
    }
}
