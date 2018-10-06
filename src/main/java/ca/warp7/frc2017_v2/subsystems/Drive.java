package ca.warp7.frc2017_v2.subsystems;

import ca.warp7.frc.annotation.InputStateModifier;
import ca.warp7.frc.annotation.SystemCurrentState;
import ca.warp7.frc.annotation.SystemInputState;
import ca.warp7.frc.annotation.SystemStateUpdator;
import ca.warp7.frc.cheesy_drive.CheesyDrive;
import ca.warp7.frc.cheesy_drive.ICheesyDriveInput;
import ca.warp7.frc.comms.ReportType;
import ca.warp7.frc.core.ISubsystem;
import ca.warp7.frc.core.Robot;
import ca.warp7.frc.math.PID;
import ca.warp7.frc.values.Creator;
import ca.warp7.frc.values.PIDValues;
import ca.warp7.frc.wpi_wrapper.MotorGroup;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;

import static ca.warp7.frc.math.Functions.constrainMinimum;
import static ca.warp7.frc.math.Functions.limit;
import static ca.warp7.frc2017_v2.constants.RobotMap.DriveConstants.*;
import static ca.warp7.frc2017_v2.constants.RobotMap.RIO.*;

public class Drive implements ISubsystem {

	private static final double kAbsolutePowerLimit = 1.0;
	private static final double kRampIntervals = 6.0;
	private static final double kMinimumRampDiffTolerance = 1.0E-03;

	@SystemInputState
	private final InputState mInputState = new InputState();
	@SystemCurrentState
	private final CurrentState mCurrentState = new CurrentState();

	private CheesyDrive mCheesyDrive;
	private MotorGroup mLeftMotorGroup;
	private MotorGroup mRightMotorGroup;
	private Encoder mLeftEncoder;
	private Encoder mRightEncoder;

	@Override
	public void onConstruct() {
		mCheesyDrive = new CheesyDrive();
		mCheesyDrive.setDriveSignalReceiver((leftSpeedDemand, rightSpeedDemand) -> {
			mInputState.demandedLeftSpeed = leftSpeedDemand;
			mInputState.demandedRightSpeed = rightSpeedDemand;
		});

		mLeftMotorGroup = new MotorGroup(VictorSP.class, driveLeftPins);
		mRightMotorGroup = new MotorGroup(VictorSP.class, driveRightPins);
		mRightMotorGroup.setInverted(true);

		mLeftEncoder = Creator.encoderFromPins(driveLeftEncoderChannels, false, EncodingType.k4X);
		mLeftEncoder.setReverseDirection(true);
		mLeftEncoder.setDistancePerPulse(driveInchesPerTick);

		mRightEncoder = Creator.encoderFromPins(driveRightEncoderChannels, false, EncodingType.k4X);
		mRightEncoder.setReverseDirection(false);
		mRightEncoder.setDistancePerPulse(driveInchesPerTick);
	}

	@Override
	public synchronized void onDisabled() {
		mInputState.demandedRightSpeed = 0.0;
		mInputState.demandedLeftSpeed = 0.0;
		mInputState.shouldBeginOpenLoop = false;
		mInputState.shouldBeginPIDLoop = false;
		mInputState.measuredLeftDistance = 0.0;
		mInputState.measuredRightDistance = 0.0;
		mInputState.shouldReverse = false;
		mInputState.leftPIDInput.reset();
		mInputState.rightPIDInput.reset();
	}

	@Override
	public void onAutonomousInit() {
	}

	@Override
	public void onTeleopInit() {
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
		double limitedLeft = limit(mCurrentState.leftSpeed, preDriftSpeedLimit);
		double limitedRight = limit(mCurrentState.rightSpeed, preDriftSpeedLimit);
		mLeftMotorGroup.set(limit(limitedLeft * leftDriftOffset, kAbsolutePowerLimit));
		mRightMotorGroup.set(limit(limitedRight * rightDriftOffset, kAbsolutePowerLimit));
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

		if (mCurrentState.isOpenLoop) {

			if (mCurrentState.isReversed) {
				mInputState.demandedLeftSpeed *= -1;
				mInputState.demandedRightSpeed *= -1;
			}

			mCurrentState.leftSpeed += constrainMinimum(mInputState.demandedLeftSpeed -
					mCurrentState.leftSpeed, kMinimumRampDiffTolerance) / kRampIntervals;
			mCurrentState.rightSpeed += constrainMinimum(mInputState.demandedRightSpeed -
					mCurrentState.rightSpeed, kMinimumRampDiffTolerance) / kRampIntervals;

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
		Robot.reportState(this, ReportType.STATE_INPUT, mInputState);
		Robot.reportState(this, ReportType.STATE_CURRENT, mCurrentState);
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
		mInputState.demandedLeftSpeed = leftSpeedDemand;
		mInputState.demandedRightSpeed = rightSpeedDemand;
	}

	@InputStateModifier
	public synchronized void setPIDTargetDistance(PIDValues pidValues, double targetDistance) {
		mInputState.shouldBeginOpenLoop = false;
		mInputState.shouldBeginPIDLoop = true;
		mInputState.leftPIDInput.setPID(pidValues).setTargetValue(targetDistance);
		mInputState.rightPIDInput.setPID(pidValues).setTargetValue(targetDistance);
	}

	@InputStateModifier
	public synchronized void setReversed(boolean reversed) {
		mInputState.shouldReverse = reversed;
	}

	@InputStateModifier
	public synchronized void zeroSensors() {
		mInputState.measuredLeftDistance = 0.0;
		mInputState.measuredRightDistance = 0.0;
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
		boolean isOpenLoop;
		boolean isPIDLoop;
		double leftSpeed;
		double rightSpeed;
		PID.CurrentState leftPID = new PID.CurrentState();
		PID.CurrentState rightPID = new PID.CurrentState();
	}
}