package ca.warp7.frc2017.subsystems;

import ca.warp7.frc.ISubsystem;
import ca.warp7.frc.drive.CheesyDrive;
import ca.warp7.frc.drive.ICheesyDriveController;
import ca.warp7.frc.drive.IDriveSignalReceiver;
import ca.warp7.frc.utils.Hardware;
import ca.warp7.frc.utils.Limit;
import ca.warp7.frc.utils.MotorGroup;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

import static ca.warp7.frc2017.Mapping.DriveConstants.*;
import static ca.warp7.frc2017.Mapping.RIO.*;
import static edu.wpi.first.wpilibj.CounterBase.EncodingType.k4X;


public class Drive implements ISubsystem, IDriveSignalReceiver {

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

		mLeftEncoder = Hardware.new_encoder(driveLeftEncoderChannels, false, k4X);
		mLeftEncoder.setReverseDirection(true);
		mLeftEncoder.setDistancePerPulse(inchesPerTick);

		mRightEncoder = Hardware.new_encoder(driveRightEncoderChannels, false, k4X);
		mRightEncoder.setReverseDirection(false);
		mRightEncoder.setDistancePerPulse(inchesPerTick);

		mShifter = new Solenoid(driveShifterPins.first());
	}

	@Override
	public void onReset() {
		mLeftMotors.set(0);
		mRightMotors.set(0);
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

	public void cheesyDrive(ICheesyDriveController driver) {
		mCheesyDrive.driveWithController(driver);
	}

	public void setShift(boolean shift) {
		mShifter.set(shift);
	}

	public void setReversed(boolean reversed) {
		mState.reversed = reversed;
	}
}