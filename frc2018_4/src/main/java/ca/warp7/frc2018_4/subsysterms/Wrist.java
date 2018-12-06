package ca.warp7.frc2018_4.subsysterms;

import ca.warp7.frc.core.ISubsystem;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

import static ca.warp7.frc.Functions.limit;
import static ca.warp7.frc.core.Robot.reportInputAndState;
import static ca.warp7.frc2018_4.constants.Pins.*;
import static ca.warp7.frc2018_4.constants.WristConstants.kAbsoluteMaxOutputPower;
import static ca.warp7.frc2018_4.constants.WristConstants.kRandomDiffDivision;

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

    }

    @Override
    public void onOutput() {
        mWristMotorGroup.set(limit(mState.mSpeed, kAbsoluteMaxOutputPower));
    }

    @Override
    public void onUpdateState() {
        mState.mSpeed = (mState.mSpeed - mInputState.mDemandedWristAngularVelocity)/kRandomDiffDivision;
    }

    @Override
    public void onReportState() {
        reportInputAndState(this, mInputState, mState);
    }

    public class InputState{
        public double mDemandedWristAngularVelocity;
    }

    public class State{
        double mSpeed;
    }
}
