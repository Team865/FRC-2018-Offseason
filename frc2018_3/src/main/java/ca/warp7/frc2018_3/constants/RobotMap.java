package ca.warp7.frc2018_3.constants;

import ca.warp7.frc.commons.state.PIDValues;
import ca.warp7.frc.commons.state.Pins;
import ca.warp7.frc2018_3.subsystems.Climber;
import ca.warp7.frc2018_3.subsystems.Drive;
import ca.warp7.frc2018_3.subsystems.Intake;
import ca.warp7.frc2018_3.subsystems.Pneumatics;

public final class RobotMap {

    public static final class Subsystems {
        public static final Drive drive = new Drive();
        public static final Pneumatics pneumatics = new Pneumatics();
        public static final Intake intake = new Intake();
        public static final Climber climber = new Climber();
    }

    public static final class RIO {
        public static Pins driveLeftPins;
        public static Pins driveRightPins;
        public static Pins driveLeftEncoderChannels;
        public static Pins driveRightEncoderChannels;

        public static Pins pneumaticsShifterSolenoidPin;
        public static Pins pneumaticsCompressorPin;

        public static Pins intakeLeftPin;
        public static Pins intakeRightPin;
        public static Pins intakePiston;
        public static Pins climberPins;
    }

    public static final class PID {
        public static PIDValues uniformDrivePID;
    }

    public static final class DriveConstants {
        public static double leftDriftOffset;
        public static double rightDriftOffset;
        public static double inchesPerTick;
        public static double preDriftSpeedLimit;
    }
}