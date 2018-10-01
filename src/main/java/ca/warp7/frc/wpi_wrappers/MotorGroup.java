package ca.warp7.frc.wpi_wrappers;

import ca.warp7.frc.core.Pins;
import edu.wpi.first.wpilibj.SpeedController;

import java.lang.reflect.InvocationTargetException;

import static java.util.Arrays.stream;

public class MotorGroup implements SpeedController {

	private SpeedController[] mMotors;
	private boolean mInverted;

	private MotorGroup(int[] pins, Class<?> type) {
		assert type.isAssignableFrom(SpeedController.class);
		mMotors = new SpeedController[pins.length];
		for (int i = 0; i < pins.length; i++) {
			try {
				mMotors[i] = (SpeedController) type.getConstructor(Integer.TYPE).newInstance(pins[i]);
			} catch (NoSuchMethodException | InstantiationException
					| IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	public MotorGroup(Class<?> type, Pins pins) {
		this(pins.array(), type);
	}

	@Override
	public void set(double speed) {
		stream(mMotors).forEach(motor -> motor.set(speed));
	}

	@Override
	public void pidWrite(double output) {
		stream(mMotors).forEach(motor -> motor.pidWrite(output));
	}

	@Override
	public double get() {
		return mMotors[0].get();
	}

	@Override
	public void setInverted(boolean isInverted) {
		this.mInverted = isInverted;
		for (SpeedController motor : mMotors) motor.setInverted(isInverted);
	}

	@Override
	public boolean getInverted() {
		return mInverted;
	}

	@Override
	public void disable() {
		stream(mMotors).forEach(SpeedController::disable);
	}

	@Override
	public void stopMotor() {
		stream(mMotors).forEach(SpeedController::stopMotor);
	}
}
