package ca.warp7.frc2018_3.subsystems;

import ca.warp7.frc.commons.core.Creator;
import ca.warp7.frc.commons.core.ISubsystem;
import ca.warp7.frc.commons.core.ReportType;
import ca.warp7.frc.commons.core.Robot;
import ca.warp7.frc.commons.state.PIDValues;
import ca.warp7.frc.commons.wpi_wrapper.MotorGroup;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.stormbots.MiniPID;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;

import static ca.warp7.frc.commons.core.Functions.limit;
import static ca.warp7.frc2018_3.Constants.kArmEncoderChannel;
import static ca.warp7.frc2018_3.Constants.kArmInchesPerTick;
import static ca.warp7.frc2018_3.Constants.kArmPin;

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
    public Encoder mArmEncoder;

    @Override
    public void onConstruct() {
        mArmMotor = new MotorGroup(WPI_VictorSPX.class, kArmPin);
        mArmEncoder = Creator.encoder(kArmEncoderChannel, true, CounterBase.EncodingType.k4X);
        mArmEncoder.setDistancePerPulse(kArmInchesPerTick);
    }

    @Override
    public void onDisabled() {

    }

    @Override
    public void onMeasure() {
        mCurrentState.measuredDistance = mArmEncoder.getDistance();
    }

    public void onTeleopInit(){
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

    public void setLoc(double distance){
        mInputState.targetDistance = distance;
    }

    @Override
    public void onReportState() {
        Robot.reportState(this, ReportType.REFLECT_STATE_INPUT, mInputState);
        Robot.reportState(this, ReportType.REFLECT_STATE_CURRENT, mCurrentState);
    }

    public void zeroEncoder(){
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
