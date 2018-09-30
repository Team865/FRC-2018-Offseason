package ca.warp7.frc2017.subsystems;

import ca.warp7.frc.Robot;
import edu.wpi.first.wpilibj.Compressor;

import static ca.warp7.frc2017.mapping.Mapping.RIO.compressorPin;

public class Pneumatics implements Robot.ISubsystem {

	private static class InternalState {
		boolean closedLoop = false;
	}

	private final InternalState mState = new InternalState();
	private Compressor mCompressor;

	@Override
	public void onConstruct() {
		mCompressor = new Compressor(compressorPin.first());
	}

	@Override
	public synchronized void onDisabledReset() {
	}

	@Override
	public void onInputLoop() {

	}

	@Override
	public void onOutputLoop() {

	}

	@Override
	public void onUpdateState() {

	}

	@Override
	public void onReportState() {

	}

	public synchronized void setClosedLoop(boolean on) {
		synchronized (mState) {
			mState.closedLoop = on;
			update();
		}
	}

	public synchronized void toggleClosedLoop() {
		synchronized (mState) {
			mState.closedLoop = !mState.closedLoop;
			update();
		}
	}

	private synchronized void update() {
		synchronized (mState) {
			mCompressor.setClosedLoopControl(mState.closedLoop);
		}
	}
}
