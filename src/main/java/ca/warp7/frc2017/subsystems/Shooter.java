package ca.warp7.frc2017.subsystems;


import ca.warp7.frc.ISubsystem;
import ca.warp7.frc.utils.MotorGroup;
import ca.warp7.frc2017.Mapping.RIO;
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
	public Object getState() {
		return null;
	}

	@Override
	public void onInit() {
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
	public void onReset() {
		stop();
	}

	public void setRPM(double targetSpeed) {
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

	private void stop() {
		mMasterTalon.disable();
		mSlaveTalon.disable();
		mMasterTalon.set(0);
		mSlaveTalon.set(mMasterTalon.getDeviceID());
	}

	public void setHopperSpeed(double speed) {
		mHopperSpin.set(speed);
	}

	public void setTowerSpeed(double speed) {
		mTowerSpin.set(speed);
	}

	public void setIntakeSpeed(double speed) {
		mIntake.set(speed);
	}

	public boolean getSensor() {
		return mPhotoSensor.get();
	}

	public double getSetPoint() {
		return mMasterTalon.isEnabled() ? mMasterTalon.getSetpoint() : 0.0;
	}
}
