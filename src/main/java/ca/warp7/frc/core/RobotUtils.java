package ca.warp7.frc.core;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;

public class RobotUtils {
	public static Pins pins(int... n) {
		return new Pins(n);
	}

	public static Pins channels(int... n) {
		return pins(n);
	}

	public static Pins pin(int n) {
		return pins(n);
	}

	public static Encoder encoderFromPins(Pins pins, boolean reverse, CounterBase.EncodingType encodingType) {
		return new Encoder(pins.get(0), pins.get(1), reverse, encodingType);
	}
}
