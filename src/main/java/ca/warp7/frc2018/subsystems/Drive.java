package ca.warp7.frc2018.subsystems;

import ca.warp7.frc.Helper;
import ca.warp7.frc.MathUtil;
import ca.warp7.frc.construct.ISubsystem;
import ca.warp7.frc.drive.CheesyDrive;
import ca.warp7.frc.drive.ICheesyDriveController;
import ca.warp7.frc.drive.IDriveSignalReceiver;
import ca.warp7.frc.drive.MotorGroup;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

import static ca.warp7.frc2018.Mapping.DriveConstants.*;
import static ca.warp7.frc2018.Mapping.RIO.*;
import static edu.wpi.first.wpilibj.CounterBase.EncodingType.k4X;


public class Drive implements ISubsystem, IDriveSignalReceiver {

	private static final double kRampIntervals = 6.0;

	private boolean mDriveReversed = false;
	private double mLeftRamp = 0.0;
	private double mRightRamp = 0.0;

	private CheesyDrive mCheesyDrive;
	private MotorGroup mLeftMotors;
	private MotorGroup mRightMotors;
	private Encoder mLeftEncoder;
	private Encoder mRightEncoder;
	private Solenoid mShifter;

	@Override
	public void onInit() {
		mCheesyDrive = new CheesyDrive();
		mCheesyDrive.setSignalReceiver(this);

		mLeftMotors = new MotorGroup(VictorSP.class, driveLeftPins.array());
		mRightMotors = new MotorGroup(VictorSP.class, driveRightPins.array());
		mRightMotors.setInverted(true);

		mLeftEncoder = Helper.encoder(driveLeftEncoderChannels, false, k4X);
		mLeftEncoder.setReverseDirection(true);
		mLeftEncoder.setDistancePerPulse(inchesPerTick);

		mRightEncoder = Helper.encoder(driveRightEncoderChannels, false, k4X);
		mRightEncoder.setReverseDirection(false);
		mRightEncoder.setDistancePerPulse(inchesPerTick);

		mShifter = new Solenoid(driveShifterPins.first());
	}

	@Override
	public void onReset() {
		mLeftEncoder.reset();
		mRightEncoder.reset();
	}


	@Override
	public void onDrive(double leftDemand, double rightDemand) {
		if (mDriveReversed) {
			leftDemand = leftDemand * -1;
			rightDemand = rightDemand * -1;
		}
		mLeftRamp += (leftDemand - mLeftRamp) / kRampIntervals;
		mRightRamp += (rightDemand - mRightRamp) / kRampIntervals;
		double leftSpeed = MathUtil.limit(mLeftRamp, speedLimit);
		double rightSpeed = MathUtil.limit(mRightRamp, speedLimit);
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
		mDriveReversed = reversed;
	}
}