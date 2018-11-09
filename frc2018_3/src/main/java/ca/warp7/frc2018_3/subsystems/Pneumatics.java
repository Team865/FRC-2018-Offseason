package ca.warp7.frc2018_3.subsystems;

import ca.warp7.frc.commons.core.ISubsystem;
import ca.warp7.frc.commons.core.Robot;
import ca.warp7.frc.commons.core.StateType;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;

import static ca.warp7.frc2018_3.Constants.*;

public class Pneumatics implements ISubsystem {

    @InputField
    private final InputState mInputState = new InputState();
    @StateField
    private final CurrentState mCurrentState = new CurrentState();

    private Compressor mCompressor;
    private Solenoid mShifterSolenoid;
    private Solenoid mGrapplingHookSolenoid;

    @Override
    public void onConstruct() {
        mCompressor = new Compressor(kPneumaticsCompressorPin);
        mShifterSolenoid = new Solenoid(kDriveShifterSolenoidPin);
        mGrapplingHookSolenoid = new Solenoid(kGrapplingHookSolenoidPin);
        mGrapplingHookSolenoid.set(false);
        mShifterSolenoid.set(false);
        mCompressor.setClosedLoopControl(true);
    }

    @Override
    public synchronized void onDisabled() {
        mInputState.shouldBeginClosedLoop = false;
        mInputState.shouldSolenoidBeOnForShifter = false;
        mInputState.shouldDeployGrapplingHook = false;
    }

    @Override
    public void onTeleopInit() {
        mInputState.shouldBeginClosedLoop = false;
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

        mGrapplingHookSolenoid.set(mInputState.shouldDeployGrapplingHook);
    }

    @Override
    public void onUpdateState() {
        mCurrentState.isClosedLoop = mInputState.shouldBeginClosedLoop;
        mCurrentState.isSolenoidOnForShifter = mInputState.shouldSolenoidBeOnForShifter;
    }

    @Override
    public void onReportState() {
        Robot.report(this, StateType.ComponentInput, mInputState);
        Robot.report(this, StateType.ComponentState, mCurrentState);
    }

    @InputModifier
    public synchronized void toggleClosedLoop() {
        mInputState.shouldBeginClosedLoop = !mCurrentState.isClosedLoop;
    }

    @InputModifier
    public synchronized void setShouldSolenoidBeOnForShifter(boolean shouldSolenoidBeOnForShifter) {
        mInputState.shouldSolenoidBeOnForShifter = shouldSolenoidBeOnForShifter;
    }

    @InputModifier
    public synchronized void setGrapplingHook(boolean shouldDeployGrapplingHook) {
        mInputState.shouldDeployGrapplingHook = shouldDeployGrapplingHook;
    }

    private static class InputState {
        boolean shouldBeginClosedLoop;
        boolean shouldSolenoidBeOnForShifter;
        boolean shouldDeployGrapplingHook;
    }

    private static class CurrentState {
        boolean isClosedLoop;
        boolean isSolenoidOnForShifter;
    }
}
