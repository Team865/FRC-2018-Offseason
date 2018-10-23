package ca.warp7.frc2018_3.subsystems;

import ca.warp7.frc.commons.core.ISubsystem;
import ca.warp7.frc.commons.core.Robot;
import ca.warp7.frc.commons.core.StateType;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;

import static ca.warp7.frc2018_3.Constants.*;

public class Pneumatics implements ISubsystem {

    @InputStateField
    private final InputState mInputState = new InputState();
    @CurrentStateField
    private final CurrentState mCurrentState = new CurrentState();

    private Compressor mCompressor;
    private Solenoid mShifterSolenoid;
    private Solenoid mGrapplingHookSolenoid;

    @Override
    public void onConstruct() {
        mCompressor = new Compressor(kPneumaticsCompressorPin.first());
        mShifterSolenoid = new Solenoid(kPneumaticsShifterSolenoidPin.first());
        mGrapplingHookSolenoid = new Solenoid(kGrapplingHookSolenoidPin.first());
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
        Robot.report(this, StateType.COMPONENT_INPUT, mInputState);
        Robot.report(this, StateType.COMPONENT_STATE, mCurrentState);
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
    public synchronized void setGrapplingHook(boolean shouldDeploy) {
        mInputState.shouldDeployGrapplingHook = shouldDeploy;
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
