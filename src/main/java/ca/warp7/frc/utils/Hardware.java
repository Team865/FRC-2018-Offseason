package ca.warp7.frc.utils;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;

public class Hardware {
	public static Encoder new_encoder(Pins pins, boolean reverse, CounterBase.EncodingType encodingType) {
		return new Encoder(pins.get(0), pins.get(1), reverse, encodingType);
	}
}
