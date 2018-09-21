package ca.warp7.frc2017.subsystems;

import ca.warp7.frc.robot.ISubsystem;
import edu.wpi.first.wpilibj.Compressor;

import static ca.warp7.frc2017.Mapping.RIO.compressorPin;

public class Compressor_ implements ISubsystem {

	private static class InternalState {
		boolean closedLoop = false;
	}

	private InternalState mState = new InternalState();
	private Compressor mCompressor;

	@Override
	public Object getState() {
		return mState;
	}

	@Override
	public void onInit() {
		mCompressor = new Compressor(compressorPin.get(0));
	}

	@Override
	public void onReset() {
	}

	public void setClosedLoop(boolean on){
		mState.closedLoop = on;
		update();
	}

	public void toggleClosedLoop(){
		mState.closedLoop = !mState.closedLoop;
		update();
	}

	private void update(){
		mCompressor.setClosedLoopControl(mState.closedLoop);
	}
}
