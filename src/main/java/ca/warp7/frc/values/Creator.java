package ca.warp7.frc.values;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;

public class Creator {
	public static Encoder encoderFromPins(Pins pins, boolean reverse, CounterBase.EncodingType encodingType) {
		return new Encoder(pins.get(0), pins.get(1), reverse, encodingType);
	}
}
