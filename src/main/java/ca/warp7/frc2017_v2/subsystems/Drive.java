package ca.warp7.frc2017_v2.subsystems;

import ca.warp7.frc.annotation.InputStateModifier;
import ca.warp7.frc.annotation.SystemCurrentState;
import ca.warp7.frc.annotation.SystemInputState;
import ca.warp7.frc.annotation.SystemStateUpdator;
import ca.warp7.frc.cheesy_drive.CheesyDrive;
import ca.warp7.frc.cheesy_drive.ICheesyDriveInput;
import ca.warp7.frc.cheesy_drive.IDriveSignalReceiver;
import ca.warp7.frc.comms.StateType;
import ca.warp7.frc.core.ISubsystem;
import ca.warp7.frc.core.Robot;
import ca.warp7.frc.math.PID;
import ca.warp7.frc.values.Creator;
import ca.warp7.frc.values.PIDValues;
import ca.warp7.frc.wpi_wrapper.MotorGroup;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

import static ca.warp7.frc.math.Functions.limit;
import static ca.warp7.frc2017_v2.constants.RobotMap.DriveConstants.*;
import static ca.warp7.frc2017_v2.constants.RobotMap.RIO.*;
import static edu.wpi.first.wpilibj.CounterBase.EncodingType.k4X;

public class Drive implements ISubsystem, IDriveSignalReceiver {

	@SystemInputState
	private final InputState mInputState = new InputState();
	@SystemCurrentState
	private final CurrentState mCurrentState = new CurrentState();

	private CheesyDrive mCheesyDrive;
	private MotorGroup mLeftMotorGroup;
	private MotorGroup mRightMotorGroup;
	private Encoder mLeftEncoder;
	private Encoder mRightEncoder;
	private Solenoid mShifterSolenoid;

	private static final double kRampIntervals = 6.0;

	@Override
	public void onConstruct() {
		mCheesyDrive = new CheesyDrive();
		mCheesyDrive.setDriveSignalReceiver(this);

		mLeftMotorGroup = new MotorGroup(VictorSP.class, driveLeftPins);
		mRightMotorGroup = new MotorGroup(VictorSP.class, driveRightPins);
		mRightMotorGroup.setInverted(true);

		mLeftEncoder = Creator.encoderFromPins(driveLeftEncoderChannels, false, k4X);
		mLeftEncoder.setReverseDirection(true);
		mLeftEncoder.setDistancePerPulse(inchesPerTick);

		mRightEncoder = Creator.encoderFromPins(driveRightEncoderChannels, false, k4X);
		mRightEncoder.setReverseDirection(false);
		mRightEncoder.setDistancePerPulse(inchesPerTick);

		mShifterSolenoid = new Solenoid(driveShifterSolenoidPin.first());
		mShifterSolenoid.set(false);
	}


	@Override
	public synchronized void onDisabledReset() {
		mInputState.demandedRightSpeed = 0.0;
		mInputState.demandedLeftSpeed = 0.0;
		mInputState.shouldBeginOpenLoop = false;
		mInputState.shouldBeginPIDLoop = false;
		mInputState.measuredLeftDistance = 0.0;
		mInputState.measuredRightDistance = 0.0;
		mInputState.shouldSolenoidBeOnForShifter = false;
		mInputState.shouldReverse = false;
		mInputState.leftPIDInput.reset();
		mInputState.rightPIDInput.reset();
	}

	@Override
	public synchronized void onInputLoop() {
		mInputState.measuredLeftDistance = mLeftEncoder.getDistance();
		mInputState.measuredRightDistance = mRightEncoder.getDistance();
		mInputState.leftPIDInput.setMeasuredValue(mInputState.measuredLeftDistance);
		mInputState.rightPIDInput.setMeasuredValue(mInputState.measuredRightDistance);
	}

	@Override
	public synchronized void onOutputLoop() {
		double limitedLeft = limit(mCurrentState.leftSpeed, speedLimit);
		double limitedRight = limit(mCurrentState.rightSpeed, speedLimit);
		mLeftMotorGroup.set(limit(limitedLeft * leftDriftOffset, speedLimit));
		mRightMotorGroup.set(limit(limitedRight * rightDriftOffset, speedLimit));

		if (mCurrentState.isSolenoidOnForShifter) {
			if (!mShifterSolenoid.get()) {
				mShifterSolenoid.set(true);
			}
		} else if (mShifterSolenoid.get()) {
			mShifterSolenoid.set(false);
		}

	}

	@SystemStateUpdator
	@Override
	public synchronized void onUpdateState() {
		if (mInputState.shouldBeginOpenLoop) {
			if (!mCurrentState.isOpenLoop) {
				mCurrentState.isOpenLoop = true;
				mCurrentState.isPIDLoop = false;
			}
		} else if (mInputState.shouldBeginPIDLoop) {
			if (!mCurrentState.isPIDLoop) {
				mCurrentState.isOpenLoop = false;
				mCurrentState.isPIDLoop = true;
			}
		} else {
			mCurrentState.isPIDLoop = false;
			mCurrentState.isOpenLoop = false;
		}

		mCurrentState.isReversed = mInputState.shouldReverse;
		mCurrentState.isSolenoidOnForShifter = mInputState.shouldSolenoidBeOnForShifter;

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
		Robot.reportState(this, StateType.INPUT, mInputState);
		Robot.reportState(this, StateType.CURRENT, mCurrentState);
	}

	@Override
	public synchronized void setDemandedDriveSpeed(double leftSpeedDemand, double rightSpeedDemand) {
		mInputState.demandedLeftSpeed = leftSpeedDemand;
		mInputState.demandedRightSpeed = rightSpeedDemand;
	}

	@InputStateModifier
	public synchronized void cheesyDrive(ICheesyDriveInput driver) {
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
	public synchronized void setShift(boolean shouldSolenoidBeOnForShifter) {
		mInputState.shouldSolenoidBeOnForShifter = shouldSolenoidBeOnForShifter;
	}

	@InputStateModifier
	public synchronized void setReversed(boolean reversed) {
		mInputState.shouldReverse = reversed;
	}


	public synchronized void zeroSensors() {
		mLeftEncoder.reset();
		mRightEncoder.reset();
	}

	public synchronized boolean isWithinDistanceRange(double distanceRange, double tolerance) {
		double average = (mInputState.measuredLeftDistance + mInputState.measuredRightDistance) / 2;
		return Math.abs(distanceRange - average) < Math.abs(tolerance);
	}

	public boolean isOpenLoop() {
		return mCurrentState.isOpenLoop;
	}

	public boolean isPIDLoop() {
		return mCurrentState.isPIDLoop;
	}

	static class InputState {
		boolean shouldReverse;
		boolean shouldSolenoidBeOnForShifter;
		boolean shouldBeginOpenLoop;
		boolean shouldBeginPIDLoop;
		double demandedLeftSpeed;
		double demandedRightSpeed;
		double measuredLeftDistance;
		double measuredRightDistance;
		PID.InputState leftPIDInput = new PID.InputState();
		PID.InputState rightPIDInput = new PID.InputState();
	}

	static class CurrentState {
		boolean isReversed;
		boolean isSolenoidOnForShifter;
		boolean isOpenLoop;
		boolean isPIDLoop;
		double leftSpeed;
		double rightSpeed;
		PID.CurrentState leftPID = new PID.CurrentState();
		PID.CurrentState rightPID = new PID.CurrentState();
	}
}