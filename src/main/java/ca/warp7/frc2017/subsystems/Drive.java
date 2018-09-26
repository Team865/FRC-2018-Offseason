package ca.warp7.frc2017.subsystems;

import ca.warp7.frc.CheesyDrive;
import ca.warp7.frc.IDriveSignalReceiver;
import ca.warp7.frc.Robot;
import ca.warp7.frc.utils.Limit;
import ca.warp7.frc.utils.MotorGroup;
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

	private InternalState mState = new InternalState();
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
	public void onReset() {
		mLeftEncoder.reset();
		mRightEncoder.reset();
	}


	@Override
	public void onDrive(double leftPowerDemand, double rightPowerDemand) {
		if (mState.reversed) {
			leftPowerDemand = leftPowerDemand * -1;
			rightPowerDemand = rightPowerDemand * -1;
		}
		mState.leftRamp += (leftPowerDemand - mState.leftRamp) / kRampIntervals;
		mState.rightRamp += (rightPowerDemand - mState.rightRamp) / kRampIntervals;
		double leftSpeed = Limit.limit(mState.leftRamp, speedLimit);
		double rightSpeed = Limit.limit(mState.rightRamp, speedLimit);
		mLeftMotors.set(leftSpeed * leftDriftOffset);
		mRightMotors.set(rightSpeed * rightDriftOffset);
	}

	public void cheesyDrive(CheesyDrive.IControls driver) {
		mCheesyDrive.feedForward(driver);
	}

	public void setShift(boolean shift) {
		if (shift) {
			if (!mShifter.get()) mShifter.set(true);
			else if (mShifter.get()) mShifter.set(false);
		}
	}

	public void setReversed(boolean reversed) {
		mState.reversed = reversed;
	}
}