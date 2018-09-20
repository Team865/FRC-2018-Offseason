package ca.warp7.frc.drive;

import edu.wpi.first.wpilibj.SpeedController;

import java.lang.reflect.InvocationTargetException;

public class MotorGroup implements SpeedController {
	private SpeedController[] mMotors;
	private boolean mInverted;

	private MotorGroup(int[] pins, Class<?> type) {
		assert type.isAssignableFrom(SpeedController.class);
		mMotors = new SpeedController[pins.length];
		for (int i = 0; i < pins.length; i++) {
			try {
				mMotors[i] = (SpeedController) type.getConstructor(Integer.TYPE).newInstance(pins[i]);
			} catch (NoSuchMethodException
					| SecurityException
					| InstantiationException
					| IllegalAccessException
					| IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	public MotorGroup(Class<?> type, int... pins) {
		this(pins, type);
	}

	@Override
	public void set(double speed) {
		for (SpeedController motor : mMotors) {
			motor.set(speed);
		}
	}

	@Override
	public void pidWrite(double output) {
		for (SpeedController motor : mMotors) {
			motor.pidWrite(output);
		}
	}

	@Override
	public double get() {
		return mMotors[0].get();
	}

	@Override
	public void setInverted(boolean isInverted) {
		this.mInverted = isInverted;
		for (SpeedController motor : mMotors) {
			motor.setInverted(isInverted);
		}
	}

	// THIS IS DANGEROUS.
	/*
	 * public void setInverted(int index, boolean mInverted) {
	 * mMotors[index].setInverted(mInverted); }
	 */

	@Override
	public boolean getInverted() {
		return mInverted;
	}

	@Override
	public void disable() {
		for (SpeedController motor : mMotors) {
			motor.disable();
		}
	}

	@Override
	public void stopMotor() {
		for (SpeedController motor : mMotors) {
			motor.stopMotor();
		}
	}
}