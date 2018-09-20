package ca.warp7.frc2018.subsystems;

import ca.warp7.frc.construct.ISubsystem;
import edu.wpi.first.wpilibj.Compressor;

import static ca.warp7.frc2018.Mapping.RIO.compressorPin;

public class Compressors implements ISubsystem {

	private Compressor mCompressor;

	@Override
	public void onInit() {
		mCompressor = new Compressor(compressorPin.get(0));
	}

	@Override
	public void onReset() {
	}

	public void setClosedLoop(boolean on){
		mCompressor.setClosedLoopControl(on);
	}

	public void toggleClosedLoop(){
		mCompressor.setClosedLoopControl(!mCompressor.getClosedLoopControl());
	}
}
