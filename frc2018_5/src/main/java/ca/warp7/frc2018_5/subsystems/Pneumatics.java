package ca.warp7.frc2018_5.subsystems;

import ca.warp7.frc.core.ISubsystem;
import ca.warp7.frc.core.Robot;
import ca.warp7.frc.core.StateType;
import ca.warp7.frc2018_5.Constants;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;

public class Pneumatics implements ISubsystem {

    @InputField
    private final InputState mInputState = new InputState();
    @StateField
    private final CurrentState mCurrentState = new CurrentState();

    private Compressor mCompressor;
    private Solenoid mGrapplingHookSolenoid;

    @Override
    public void onConstruct() {
        mCompressor = new Compressor(Constants.kPneumaticsCompressorPin);
        mGrapplingHookSolenoid = new Solenoid(Constants.kGrapplingHookSolenoidPin);
        mGrapplingHookSolenoid.set(false);
        mCompressor.setClosedLoopControl(true);
    }

    @Override
    public synchronized void onDisabled() {
        mInputState.shouldBeginClosedLoop = false;
        mInputState.shouldDeployGrapplingHook = false;
    }

    @Override
    public void onTeleopInit() {
        mInputState.shouldBeginClosedLoop = false;
    }

    @Override
    public void onOutput() {
        mCompressor.setClosedLoopControl(mCurrentState.isClosedLoop);
        mGrapplingHookSolenoid.set(mInputState.shouldDeployGrapplingHook);
    }

    @Override
    public void onUpdateState() {
        mCurrentState.isClosedLoop = mInputState.shouldBeginClosedLoop;
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
    public synchronized void setGrapplingHook(boolean shouldDeployGrapplingHook) {
        mInputState.shouldDeployGrapplingHook = shouldDeployGrapplingHook;
    }

    private static class InputState {
        boolean shouldBeginClosedLoop;
        boolean shouldDeployGrapplingHook;
    }

    private static class CurrentState {
        boolean isClosedLoop;
    }
}
