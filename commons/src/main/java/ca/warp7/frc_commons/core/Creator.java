package ca.warp7.frc_commons.core;

import ca.warp7.frc_commons.state.Pins;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;

public class Creator {
	public static Encoder encoder(Pins pins, boolean reverse, CounterBase.EncodingType encodingType) {
		return new Encoder(pins.get(0), pins.get(1), reverse, encodingType);
	}
}
