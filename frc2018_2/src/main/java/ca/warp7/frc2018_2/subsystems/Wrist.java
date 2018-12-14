package ca.warp7.frc2018_2.subsystems;

import ca.warp7.frc2018_2.misc.MotorGroup;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.Timer;

import static ca.warp7.frc2018_2.Constants.ACTUATION_MOTOR_IDS;

public class Wrist {

    private MotorGroup ActuationMotor;
    public boolean actuationSpeedIsRamped = false;
    double time;
    double angleDegrees;

    public Wrist() {
        ActuationMotor = new MotorGroup(ACTUATION_MOTOR_IDS, WPI_VictorSPX.class);
        ActuationMotor.setInverted(true);
        angleDegrees = 0;
    }


    public void setActuationSpeed(double speed) {
        ActuationMotor.set(speed);
    }

    private double ramp = 0;
    private double rampSpeed = 6;

    public void periodic(){
        onMeasure();
    }

    public void onMeasure(){
        double newTime = Timer.getFPGATimestamp();
        double dt = newTime - time;
        double angularDisplacement = dt * ramp;
        angleDegrees += angularDisplacement;
    }
    public void actuationRamp(double speed) {
        ramp += (speed - ramp) / rampSpeed;
        ActuationMotor.set(ramp);
    }
}
