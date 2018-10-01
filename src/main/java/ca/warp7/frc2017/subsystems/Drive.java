package ca.warp7.frc2017.subsystems;

import ca.warp7.frc.annotation.InputStateModifier;
import ca.warp7.frc.annotation.SystemCurrentState;
import ca.warp7.frc.annotation.SystemInputState;
import ca.warp7.frc.cheesy_drive.CheesyDrive;
import ca.warp7.frc.cheesy_drive.ICheesyDriveInout;
import ca.warp7.frc.cheesy_drive.IDriveSignalReceiver;
import ca.warp7.frc.core.ISubsystem;
import ca.warp7.frc.core.Robot;
import ca.warp7.frc.math.PID;
import ca.warp7.frc.math.PIDValues;
import ca.warp7.frc.wpi_wrappers.MotorGroup;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

import static ca.warp7.frc.core.Robot.utils;
import static ca.warp7.frc.math.Functions.limit;
import static ca.warp7.frc2017.mapping.Mapping.DriveConstants.*;
import static ca.warp7.frc2017.mapping.Mapping.RIO.*;
import static edu.wpi.first.wpilibj.CounterBase.EncodingType.k4X;

public class Drive implements ISubsystem, IDriveSignalReceiver {

	private static class InputState {
		boolean shouldReverse;
		boolean shouldShift;
		boolean shouldBeginOpenLoop;
		boolean shouldBeginPIDLoop;
		double demandedLeftSpeed;
		double demandedRightSpeed;
		PID.InputState leftPIDInput = new PID.InputState();
		PID.InputState rightPIDInput = new PID.InputState();
	}

	private static class CurrentState {
		boolean isReversed;
		boolean isShifted;
		boolean isOpenLoop;
		boolean isPIDLoop;
		double leftSpeed;
		double rightSpeed;
		PID.CurrentState leftPID = new PID.CurrentState();
		PID.CurrentState rightPID = new PID.CurrentState();
	}

	@SystemInputState
	private final InputState mInputState = new InputState();
	@SystemCurrentState
	private final CurrentState mCurrentState = new CurrentState();

	private static final double kRampIntervals = 4.0;

	private CheesyDrive mCheesyDrive;
	private MotorGroup mLeftMotors;
	private MotorGroup mRightMotors;
	private Encoder mLeftEncoder;
	private Encoder mRightEncoder;
	private Solenoid mShifter;

	@Override
	public void onConstruct() {
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
	public synchronized void onDisabledReset() {
		mLeftEncoder.reset();
		mRightEncoder.reset();
		mInputState.demandedRightSpeed = 0.0;
		mInputState.demandedLeftSpeed = 0.0;
		mInputState.shouldBeginOpenLoop = false;
		mInputState.shouldBeginPIDLoop = false;
		mInputState.shouldShift = false;
		mInputState.shouldReverse = false;
		mInputState.leftPIDInput.reset();
		mInputState.rightPIDInput.reset();
	}

	@Override
	public synchronized void onInputLoop() {
		mInputState.leftPIDInput.setMeasuredValue(mLeftEncoder.getDistance());
		mInputState.rightPIDInput.setMeasuredValue(mRightEncoder.getDistance());
	}

	@Override
	public synchronized void onOutputLoop() {
		double limitedLeft = limit(mCurrentState.leftSpeed, speedLimit);
		double limitedRight = limit(mCurrentState.rightSpeed, speedLimit);
		mLeftMotors.set(limit(limitedLeft * leftDriftOffset, speedLimit));
		mRightMotors.set(limit(limitedRight * rightDriftOffset, speedLimit));

		if (mCurrentState.isShifted) {
			if (!mShifter.get()) mShifter.set(true);
			else if (mShifter.get()) mShifter.set(false);
		}
	}

	@Override
	public synchronized void onUpdateState() {
		if (mInputState.shouldBeginOpenLoop && !mCurrentState.isOpenLoop) {
			mCurrentState.isOpenLoop = true;
			mCurrentState.isPIDLoop = false;
		} else if (mInputState.shouldBeginPIDLoop && !mCurrentState.isPIDLoop) {
			mCurrentState.isPIDLoop = true;
			mCurrentState.isOpenLoop = false;
		} else {
			mCurrentState.isPIDLoop = false;
			mCurrentState.isOpenLoop = false;
		}

		mCurrentState.isReversed = mInputState.shouldReverse;
		mCurrentState.isShifted = mInputState.shouldShift;

		if (mCurrentState.isOpenLoop) {

			if (mCurrentState.isReversed) {
				mInputState.demandedLeftSpeed *= -1;
				mInputState.demandedRightSpeed *= -1;
			}

			mCurrentState.leftSpeed += (mInputState.demandedLeftSpeed - mCurrentState.leftSpeed) / kRampIntervals;
			mCurrentState.rightSpeed += (mInputState.demandedRightSpeed - mCurrentState.rightSpeed) / kRampIntervals;

		} else if (mCurrentState.isPIDLoop) {
			mCurrentState.leftSpeed = mCurrentState.leftPID.calculate(mInputState.leftPIDInput);
			mCurrentState.rightSpeed = mCurrentState.rightPID.calculate(mInputState.rightPIDInput);

		} else {
			mCurrentState.leftSpeed = 0;
			mCurrentState.rightSpeed = 0;
		}
	}

	@Override
	public synchronized void onReportState() {
		utils.reportState(this, Robot.StateType.INPUT, mInputState);
		utils.reportState(this, Robot.StateType.CURRENT, mCurrentState);
	}

	@Override
	public synchronized void setDemandedDriveSpeed(double leftSpeedDemand, double rightSpeedDemand) {
		mInputState.demandedLeftSpeed = leftSpeedDemand;
		mInputState.demandedRightSpeed = rightSpeedDemand;
	}

	@InputStateModifier
	public synchronized void cheesyDrive(ICheesyDriveInout driver) {
		mInputState.shouldBeginOpenLoop = true;
		mInputState.shouldBeginPIDLoop = false;
		mCheesyDrive.setInputsFromControls(driver);
		mCheesyDrive.calculateFeed();
	}

	@InputStateModifier
	public synchronized void openLoopDrive(double leftSpeedDemand, double rightSpeedDemand) {
		mInputState.shouldBeginOpenLoop = true;
		mInputState.shouldBeginPIDLoop = false;
		setDemandedDriveSpeed(leftSpeedDemand, rightSpeedDemand);
	}

	@InputStateModifier
	public synchronized void setPIDTargetDistance(PIDValues pidValues, double targetDistance) {
		mInputState.shouldBeginOpenLoop = false;
		mInputState.shouldBeginPIDLoop = true;
		mInputState.leftPIDInput.setPID(pidValues).setTargetValue(targetDistance);
		mInputState.rightPIDInput.setPID(pidValues).setTargetValue(targetDistance);
	}

	@InputStateModifier
	public synchronized void setShift(boolean shift) {
		mInputState.shouldShift = shift;
	}

	@InputStateModifier
	public synchronized void setReversed(boolean reversed) {
		mInputState.shouldReverse = reversed;
	}
}