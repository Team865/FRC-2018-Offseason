package ca.warp7.frc2017.subsystems;

import ca.warp7.frc.*;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

import static ca.warp7.frc2017.Mapping.DriveConstants.*;
import static ca.warp7.frc2017.Mapping.RIO.*;
import static edu.wpi.first.wpilibj.CounterBase.EncodingType.k4X;

public class Drive implements Robot.ISubsystem, IDriveSignalReceiver {

	private static final double kRampIntervals = 6.0;

	private static class InternalState {
		boolean reversed = false;
		double leftRamp = 0.0;
		double rightRamp = 0.0;
	}

	private final InternalState mState = new InternalState();

	private CheesyDrive mCheesyDrive;
	private MotorGroup mLeftMotors;
	private MotorGroup mRightMotors;
	private Encoder mLeftEncoder;
	private Encoder mRightEncoder;
	private Solenoid mShifter;

	@Override
	public Object getState() {
		return mState;
	}

	@Override
	public void onInit() {
		mCheesyDrive = new CheesyDrive();
		mCheesyDrive.setDriveSignalReceiver(this);

		mLeftMotors = new MotorGroup(VictorSP.class, driveLeftPins);
		mRightMotors = new MotorGroup(VictorSP.class, driveRightPins);
		mRightMotors.setInverted(true);

		mLeftEncoder = Robot.encoderFromPins(driveLeftEncoderChannels, false, k4X);
		mLeftEncoder.setReverseDirection(true);
		mLeftEncoder.setDistancePerPulse(inchesPerTick);

		mRightEncoder = Robot.encoderFromPins(driveRightEncoderChannels, false, k4X);
		mRightEncoder.setReverseDirection(false);
		mRightEncoder.setDistancePerPulse(inchesPerTick);

		mShifter = new Solenoid(driveShifterPins.first());
		mShifter.set(false);
	}

	@Override
	public synchronized void onReset() {
		mLeftEncoder.reset();
		mRightEncoder.reset();
	}

	@Override
	public synchronized void onDriveSpeedDemand(double leftSpeedDemand, double rightSpeedDemand) {
		synchronized (mState) {
			if (mState.reversed) {
				leftSpeedDemand = leftSpeedDemand * -1;
				rightSpeedDemand = rightSpeedDemand * -1;
			}
			mState.leftRamp += (leftSpeedDemand - mState.leftRamp) / kRampIntervals;
			mState.rightRamp += (rightSpeedDemand - mState.rightRamp) / kRampIntervals;
			double leftSpeed = Functions.limit(mState.leftRamp, speedLimit);
			double rightSpeed = Functions.limit(mState.rightRamp, speedLimit);
			mLeftMotors.set(leftSpeed * leftDriftOffset);
			mRightMotors.set(rightSpeed * rightDriftOffset);
		}
	}

	public void cheesyDrive(CheesyDrive.IControls driver) {
		mCheesyDrive.feedForward(driver);
	}

	public synchronized void setShift(boolean shift) {
		if (shift) {
			if (!mShifter.get()) mShifter.set(true);
			else if (mShifter.get()) mShifter.set(false);
		}
	}

	public synchronized void setReversed(boolean reversed) {
		synchronized (mState) {
			mState.reversed = reversed;
		}
	}
}