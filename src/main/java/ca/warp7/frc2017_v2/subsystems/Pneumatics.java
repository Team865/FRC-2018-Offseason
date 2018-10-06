package ca.warp7.frc2017_v2.subsystems;

import ca.warp7.frc.annotation.InputStateModifier;
import ca.warp7.frc.annotation.SystemCurrentState;
import ca.warp7.frc.annotation.SystemInputState;
import ca.warp7.frc.annotation.SystemStateUpdator;
import ca.warp7.frc.comms.ReportType;
import ca.warp7.frc.core.ISubsystem;
import ca.warp7.frc.core.Robot;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;

import static ca.warp7.frc2017_v2.constants.RobotMap.RIO.pneumaticsCompressorPin;
import static ca.warp7.frc2017_v2.constants.RobotMap.RIO.pneumaticsShifterSolenoidPin;

public class Pneumatics implements ISubsystem {

	@SystemInputState
	private final InputState mInputState = new InputState();
	@SystemCurrentState
	private final CurrentState mCurrentState = new CurrentState();

	private Compressor mCompressor;
	private Solenoid mShifterSolenoid;

	@Override
	public void onConstruct() {
		mCompressor = new Compressor(pneumaticsCompressorPin.first());
		mShifterSolenoid = new Solenoid(pneumaticsShifterSolenoidPin.first());
		mShifterSolenoid.set(false);
	}

	@Override
	public synchronized void onDisabled() {
		mInputState.shouldBeginClosedLoop = false;
		mInputState.shouldSolenoidBeOnForShifter = false;
	}

	@Override
	public void onAutonomousInit() {
	}

	@Override
	public void onTeleopInit() {
		mInputState.shouldBeginClosedLoop = true;
	}

	@Override
	public void onInputLoop() {
	}

	@Override
	public void onOutputLoop() {
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
	@SystemStateUpdator
	public void onUpdateState() {
		mCurrentState.isClosedLoop = mInputState.shouldBeginClosedLoop;
		mCurrentState.isSolenoidOnForShifter = mInputState.shouldSolenoidBeOnForShifter;
	}

	@Override
	public void onReportState() {
		Robot.reportState(this, ReportType.STATE_INPUT, mInputState);
		Robot.reportState(this, ReportType.STATE_CURRENT, mCurrentState);
	}

	@InputStateModifier
	public synchronized void setClosedLoop(boolean on) {
		mInputState.shouldBeginClosedLoop = on;
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