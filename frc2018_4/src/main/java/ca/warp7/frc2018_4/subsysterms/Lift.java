package ca.warp7.frc2018_4.subsysterms;

import ca.warp7.frc.core.ISubsystem;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.stormbots.MiniPID;
import edu.wpi.first.wpilibj.*;

import static ca.warp7.frc.core.Robot.reportInputAndState;
import static ca.warp7.frc2018_4.constants.LiftConstants.*;
import static ca.warp7.frc2018_4.constants.Pins.*;

public class Lift implements ISubsystem {

    private SpeedControllerGroup mLiftMotorGroup;
    private Encoder mEncoder;
    private DigitalInput mLimitSwitch;
    private MiniPID liftPID;
    public InputState mInputState;
    private State mState;

    @Override
    public void onConstruct() {
        mInputState = new InputState();
        mState = new State();
        mLiftMotorGroup = new SpeedControllerGroup(new WPI_VictorSPX(kLiftMotorPin));
        Encoder mEncoder = new Encoder(kLiftEncoderAPin, kLiftEncoderBPin, kIsEncoderReversed, CounterBase.EncodingType.k4X);
        DigitalInput mLimitSwitch = new DigitalInput(kLimitSwitchPin);
        liftPID = new MiniPID(kLiftP, kLiftI, kLiftD);
        liftPID.setOutputLimits(kLiftMinOutput, kLiftMaxOutput);
        onZeroSensors();
    }

    @Override
    public void onDisabled() {

    }

    @Override
    public void onAutonomousInit() {

    }

    @Override
    public void onTeleopInit() {

    }

    @Override
    public void onMeasure() {
        mState.mPredictedHeight =mEncoder.getDistance();
        mState.mLimitSwitchPressed = mLimitSwitch.get();
        if (mState.mLimitSwitchPressed) mState.mPredictedHeight = 0;
    }

    @Override
    public void onZeroSensors() {
        // reset and zero lift encoder
        mEncoder.reset();
    }

    @Override
    public void onOutput() {
        mLiftMotorGroup.set(mState.mSpeed);
    }

    @Override
    public void onUpdateState() {

        // Creates deadband for Operator so intake height is consistent
        if (Math.abs(mInputState.mDemandedHeight) <= kLiftZeroDeadband){
            mState.mTargetHeight = 0;
        }
        else {
            mState.mTargetHeight = mInputState.mDemandedHeight;
        }

        liftPID.setSetpoint(mState.mTargetHeight);

        /* If the operator is trying to go to zero, and the encoder says it's at zero but the limit switch does not,
        go down slowly until limit switch is pressed to reset the zero.*/

        if (mState.mTargetHeight <= 0 && mState.mPredictedHeight < 0 && kLiftShouldUseSlowFall){
            mState.mSpeed = slowFallOuput(mState.mSpeed);
            onZeroSensors();
        }
        else {
            double scaledLift = mState.mPredictedHeight / kLiftHeight;
            mState.mSpeed = liftPID.getOutput(scaledLift);
        }
    }

    @Override
    public void onReportState() {
        reportInputAndState(this,mInputState,mState);
    }

    public class InputState{
        public double mDemandedHeight;
        public double mSuperstructureDemandedHeight;
    }

    private class State{
        double mTargetHeight;
        double mPredictedHeight;
        double mSpeed;
        boolean mLimitSwitchPressed;
    }

    // TODO implement a generic ramp method from commons
    private static double slowFallOuput(double currentSpeed){
        return currentSpeed + (kLiftSlowFallTargetSpeed - currentSpeed) / kRampSpeed;
    }
}
