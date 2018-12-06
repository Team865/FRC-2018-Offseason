package ca.warp7.frc2018_4.subsysterms;

import ca.warp7.frc.core.ISubsystem;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

import static ca.warp7.frc.core.Robot.reportInputAndState;
import static ca.warp7.frc2018_4.constants.ClimberConstants.kRandomDiffDivision;
import static ca.warp7.frc2018_4.constants.Pins.kClimberPin;

public class Climber implements ISubsystem {

    private SpeedControllerGroup mClimbMotorGroup;
    public InputState mInputState = new InputState();
    public State mState = new State();
    @Override
    public void onConstruct() {
        mClimbMotorGroup = new SpeedControllerGroup(new WPI_VictorSPX(kClimberPin));
    }

    @Override
    public void onDisabled() {
        mInputState.mDemandedMotorSpeed = 0;
    }
    @Override
    public void onOutput() {
        mClimbMotorGroup.set(mState.mSpeed);
    }

    @Override
    public void onUpdateState() {
        mState.mSpeed += (mState.mSpeed - mInputState.mDemandedMotorSpeed)/kRandomDiffDivision;
    }

    @Override
    public void onReportState() {
        reportInputAndState(this, mInputState, mState);
    }
    public class InputState{
        double mDemandedMotorSpeed;
    }
    public class State{
        double mSpeed;
    }
}
