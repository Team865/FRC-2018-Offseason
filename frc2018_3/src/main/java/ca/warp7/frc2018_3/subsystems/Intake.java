package ca.warp7.frc2018_3.subsystems;

import ca.warp7.frc.commons.core.ISubsystem;
import ca.warp7.frc.commons.wpi_wrapper.MotorGroup;
import ca.warp7.frc2018_3.sensors.LimelightPhotoSensor;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.Solenoid;

import static ca.warp7.frc.commons.core.Functions.limit;
import static ca.warp7.frc2018_3.Components.limelight;
import static ca.warp7.frc2018_3.Constants.*;

public class Intake implements ISubsystem {

    private static final double kAbsoluteMaxOutputPower = 1.0;

    public static final double kIntakePower = 0.75;
    public static final double kFastOuttakePower = -0.75;
    public static final double kSlowOuttakePower = -0.5;

    private MotorGroup mIntakeMotorRight;
    private MotorGroup mIntakeMotorLeft;
    private Solenoid mIntakePistons;
    private LimelightPhotoSensor mPhotoSensor;

    private double mLeftSpeed;
    private double mRightSpeed;
    private double mDemandedLeftSpeed;
    private double mDemandedRightSpeed;

    @Override
    public void onConstruct() {
        mIntakeMotorLeft = new MotorGroup(WPI_VictorSPX.class, kIntakeLeftPin);
        mIntakeMotorRight = new MotorGroup(WPI_VictorSPX.class, kIntakeRightPin);
        mIntakeMotorRight.setInverted(true);
        mIntakePistons = new Solenoid(kIntakePiston.first());
        mPhotoSensor = new LimelightPhotoSensor(limelight, 1);
    }

    @Override
    public void onDisabled() {
        mDemandedLeftSpeed = 0;
        mDemandedRightSpeed = 0;
    }

    @Override
    public void onOutput() {
        mIntakeMotorLeft.set(limit(mLeftSpeed, kAbsoluteMaxOutputPower));
        mIntakeMotorRight.set(limit(mRightSpeed, kAbsoluteMaxOutputPower));
    }

    @Override
    public void onUpdateState() {
        mLeftSpeed += (mLeftSpeed - mDemandedLeftSpeed) / 6.0;
        mRightSpeed += (mRightSpeed - mDemandedRightSpeed) / 6.0;
    }

    public void setSpeed(double speed) {
        mDemandedLeftSpeed = speed;
        mDemandedRightSpeed = speed;
    }

    public double getSpeed() {
        return mIntakeMotorRight.get();
    }

    public void pistonToggle() {
        mIntakePistons.set(!mIntakePistons.get());
    }

    public boolean hasCube() {
        return mPhotoSensor.isTriggered();
    }
}
