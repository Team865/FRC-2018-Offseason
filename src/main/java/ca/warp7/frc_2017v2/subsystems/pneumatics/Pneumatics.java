package ca.warp7.frc_2017v2.subsystems.pneumatics;

import ca.warp7.frc.annotation.InputStateModifier;
import ca.warp7.frc.annotation.SystemStateUpdator;
import ca.warp7.frc.core.ISubsystem;
import edu.wpi.first.wpilibj.Compressor;

import static ca.warp7.frc_2017v2.constants.RobotMap.RIO.compressorPin;

public class Pneumatics implements ISubsystem {

	private static class InputState {
		boolean shouldCloseLoop;
	}

	private static class CurrentState {
		boolean isClosedLoop;
	}

	private final InputState mInputState = new InputState();
	private final CurrentState mCurrentState = new CurrentState();

	private Compressor mCompressor;

	@Override
	public void onConstruct() {
		mCompressor = new Compressor(compressorPin.first());
	}

	@Override
	public synchronized void onDisabledReset() {
		mInputState.shouldCloseLoop = false;
		mCurrentState.isClosedLoop = false;
	}

	@Override
	public void onInputLoop() {
	}

	@Override
	public void onOutputLoop() {
		mCompressor.setClosedLoopControl(mCurrentState.isClosedLoop);
	}

	@Override
	@SystemStateUpdator
	public void onUpdateState() {
		mCurrentState.isClosedLoop = mInputState.shouldCloseLoop;
	}

	@Override
	public void onReportState() {

	}

	@InputStateModifier
	public synchronized void setClosedLoop(boolean on) {
		mInputState.shouldCloseLoop = on;
	}

	@InputStateModifier
	public synchronized void toggleClosedLoop() {
		mInputState.shouldCloseLoop = !mCurrentState.isClosedLoop;
	}
}
