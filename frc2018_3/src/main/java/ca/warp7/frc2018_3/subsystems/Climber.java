package ca.warp7.frc2018_3.subsystems;

import ca.warp7.frc.commons.core.Functions;
import ca.warp7.frc.commons.core.ISubsystem;
import ca.warp7.frc.commons.wpi_wrapper.MotorGroup;
import ca.warp7.frc2018_3.constants.RobotMap;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import static ca.warp7.frc.commons.core.Functions.limit;

/**
 * Lets us climb the bar in endgame. No encoders or PID for this
 */

public class Climber implements ISubsystem {

    private static final double kAbsoluteMaxOutputPower = 1.0;

    private MotorGroup mClimberMotors;

    private double mCurrentSpeed;
    private double mWantedSpeed;

    public void setSpeed(double speed) {
        mWantedSpeed = speed;
    }

    @Override
    public void onConstruct() {
        mClimberMotors = new MotorGroup(WPI_VictorSPX.class, RobotMap.RIO.climberPins);
    }

    @Override
    public void onDisabled() {
        mWantedSpeed = 0;
    }

    @Override
    public void onOutput() {
        mClimberMotors.set(limit(mCurrentSpeed, kAbsoluteMaxOutputPower));
    }

    @Override
    public void onUpdateState() {
        mCurrentSpeed += (mWantedSpeed - mCurrentSpeed) / 6.0;
    }
}
