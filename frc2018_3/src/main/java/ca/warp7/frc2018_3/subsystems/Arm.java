package ca.warp7.frc2018_3.subsystems;

import ca.warp7.frc.commons.PIDValues;
import ca.warp7.frc.commons.core.ISubsystem;
import ca.warp7.frc.commons.core.Robot;
import ca.warp7.frc.commons.core.StateType;
import ca.warp7.frc.commons.wrapper.MotorGroup;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.stormbots.MiniPID;
import edu.wpi.first.wpilibj.Encoder;

import static ca.warp7.frc.commons.Functions.limit;
import static ca.warp7.frc2018_3.Constants.*;
import static edu.wpi.first.wpilibj.CounterBase.EncodingType.k4X;

/**
 * Based on climber subsystem (for testing purposes)
 */

public class Arm implements ISubsystem {

    private static final double kAbsoluteMaxOutputPower = 1.0;

    @InputStateField
    private final InputState mInputState = new InputState();
    @CurrentStateField
    private final CurrentState mCurrentState = new CurrentState();

    private MotorGroup mArmMotor;
    private Encoder mArmEncoder;

    @Override
    public void onConstruct() {
        mArmMotor = new MotorGroup(WPI_VictorSPX.class, kArmPin);
        mArmEncoder = new Encoder(kArmEncoder.get(0), kArmEncoder.get(1), true, k4X);
        mArmEncoder.setDistancePerPulse(kArmInchesPerTick);
    }

    @Override
    public void onDisabled() {

    }

    @Override
    public void onMeasure() {
        mCurrentState.measuredDistance = mArmEncoder.getDistance();
    }

    public void onTeleopInit() {
        zeroEncoder();
    }

    @Override
    public void onOutput() {
        mArmMotor.set(limit(mCurrentState.speed, kAbsoluteMaxOutputPower));
    }

    @Override
    public void onUpdateState() {
        mInputState.PIDValues.copyTo(mCurrentState.MiniPID);
        mCurrentState.MiniPID.setSetpoint(mInputState.targetDistance);
        mCurrentState.speed = mCurrentState.MiniPID.getOutput(mCurrentState.measuredDistance);
    }

    @Override
    public void onZeroSensors() {

    }

    public void setLoc(double distance) {
        mInputState.targetDistance = distance;
    }

    @Override
    public void onReportState() {
        Robot.report(this, StateType.COMPONENT_INPUT, mInputState);
        Robot.report(this, StateType.COMPONENT_STATE, mCurrentState);
    }

    private void zeroEncoder() {
        mArmEncoder.reset();
    }

    static class InputState {
        double targetDistance;
        final PIDValues PIDValues = new PIDValues(0, 0, 0, 0);
    }

    static class CurrentState {
        double speed;
        double measuredDistance;
        final MiniPID MiniPID = new MiniPID(0, 0, 0, 0);
    }
}
