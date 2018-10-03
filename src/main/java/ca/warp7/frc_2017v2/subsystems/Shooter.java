package ca.warp7.frc_2017v2.subsystems;


import ca.warp7.frc.core.ISubsystem;
import ca.warp7.frc.wpi_wrapper.MotorGroup;
import ca.warp7.frc_2017v2.constants.RobotMap.RIO;
import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.VictorSP;

@SuppressWarnings("deprecation")
public class Shooter implements ISubsystem {
	private MotorGroup mHopperSpin;
	private MotorGroup mTowerSpin;
	private MotorGroup mIntake;
	private CANTalon mMasterTalon;
	private CANTalon mSlaveTalon;
	private DigitalInput mPhotoSensor;

	@Override
	public void onConstruct() {
		mHopperSpin = new MotorGroup(VictorSP.class, RIO.hopperSpinPins);
		mHopperSpin.setInverted(true);
		mTowerSpin = new MotorGroup(VictorSP.class, RIO.towerSpinPins);
		mIntake = new MotorGroup(VictorSP.class, RIO.intakeSpinPins);
		mPhotoSensor = new DigitalInput(RIO.photoSensorPin.first());

		mSlaveTalon = new CANTalon(RIO.shooterSlave.first());
		mMasterTalon = new CANTalon(RIO.shooterMaster.first());

		mSlaveTalon.reverseOutput(true);
		mSlaveTalon.enableBrakeMode(false);
		mSlaveTalon.changeControlMode(CANTalon.TalonControlMode.Follower);
		mSlaveTalon.set(mMasterTalon.getDeviceID());

		mMasterTalon.configEncoderCodesPerRev(12 * 86);
		mMasterTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		mMasterTalon.configNominalOutputVoltage(+0.0f, -0.0f);
		mMasterTalon.configPeakOutputVoltage(+12.0f, -12.0f);
		mMasterTalon.changeControlMode(TalonControlMode.Speed);
		mMasterTalon.reverseOutput(true);
		mMasterTalon.reverseSensor(true);
		mMasterTalon.setProfile(0);
		mMasterTalon.setF(0);
		mMasterTalon.setP(0.2);
		mMasterTalon.setI(0.00035);
		mMasterTalon.setD(12);
	}

	@Override
	public synchronized void onDisabledReset() {
		stop();
	}

	@Override
	public void onInputLoop() {

	}

	@Override
	public void onOutputLoop() {

	}

	@Override
	public void onUpdateState() {
	}

	@Override
	public void onReportState() {

	}

	public synchronized void setRPM(double targetSpeed) {
		if (targetSpeed > 0) {
			mMasterTalon.enable();
			mSlaveTalon.enable();
			mMasterTalon.setSetpoint(targetSpeed);
			mSlaveTalon.set(mMasterTalon.getDeviceID());
		} else {
			stop();
		}
	}

	public double getRPM() {
		return mMasterTalon.getSpeed();
	}

	public boolean withinRange(double allowableError) {
		double speed = mMasterTalon.getSpeed();
		double setPoint = mMasterTalon.getSetpoint();
		return Math.abs(speed - setPoint) < allowableError;
	}

	private synchronized void stop() {
		mMasterTalon.disable();
		mSlaveTalon.disable();
		mMasterTalon.set(0);
		mSlaveTalon.set(mMasterTalon.getDeviceID());
	}

	public synchronized void setHopperSpeed(double speed) {
		mHopperSpin.set(speed);
	}

	public synchronized void setTowerSpeed(double speed) {
		mTowerSpin.set(speed);
	}

	public synchronized void setIntakeSpeed(double speed) {
		mIntake.set(speed);
	}

	public synchronized boolean getSensor() {
		return mPhotoSensor.get();
	}

	public synchronized double getSetPoint() {
		return mMasterTalon.isEnabled() ? mMasterTalon.getSetpoint() : 0.0;
	}
}
