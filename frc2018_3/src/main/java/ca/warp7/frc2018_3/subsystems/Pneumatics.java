package ca.warp7.frc2018_3.subsystems;

import ca.warp7.frc.commons.core.ISubsystem;
import ca.warp7.frc.commons.core.ReportType;
import ca.warp7.frc.commons.core.Robot;
import ca.warp7.frc2018_3.constants.RobotMap;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;

public class Pneumatics implements ISubsystem {

    @InputStateField
    private final InputState mInputState = new InputState();
    @CurrentStateField
    private final CurrentState mCurrentState = new CurrentState();

    private Compressor mCompressor;
    private Solenoid mShifterSolenoid;

    @Override
    public void onConstruct() {
        mCompressor = new Compressor(RobotMap.RIO.pneumaticsCompressorPin.first());
        mShifterSolenoid = new Solenoid(RobotMap.RIO.pneumaticsShifterSolenoidPin.first());
        mShifterSolenoid.set(false);
    }

    @Override
    public synchronized void onDisabled() {
        mInputState.shouldBeginClosedLoop = false;
        mInputState.shouldSolenoidBeOnForShifter = false;
    }

    @Override
    public void onTeleopInit() {
        mInputState.shouldBeginClosedLoop = false;
    }

    @Override
    public void onMeasure() {
    }

    @Override
    public void onZeroSensors() {
    }

    @Override
    public void onOutput() {
        mCompressor.setClosedLoopControl(mCurrentState.isClosedLoop);
        if (mCurrentState.isSolenoidOnForShifter) {
            if (!mShifterSolenoid.get()) {
                mShifterSolenoid.set(true);
            }
        } else if (mShifterSolenoid.get()) {
            mShifterSolenoid.set(false);
        }
    }

    @Override
    public void onUpdateState() {
        mCurrentState.isClosedLoop = mInputState.shouldBeginClosedLoop;
        mCurrentState.isSolenoidOnForShifter = mInputState.shouldSolenoidBeOnForShifter;
    }

    @Override
    public void onReportState() {
        Robot.reportState(this, ReportType.REFLECT_STATE_INPUT, mInputState);
        Robot.reportState(this, ReportType.REFLECT_STATE_CURRENT, mCurrentState);
    }

    @InputStateModifier
    public synchronized void toggleClosedLoop() {
        mInputState.shouldBeginClosedLoop = !mCurrentState.isClosedLoop;
    }

    @InputStateModifier
    public synchronized void setShift(boolean shouldSolenoidBeOnForShifter) {
        mInputState.shouldSolenoidBeOnForShifter = shouldSolenoidBeOnForShifter;
    }

    private static class InputState {
        boolean shouldBeginClosedLoop;
        boolean shouldSolenoidBeOnForShifter;
    }

    private static class CurrentState {
        boolean isClosedLoop;
        boolean isSolenoidOnForShifter;
    }
}
