package ca.warp7.frc2017_2.constants;

import ca.warp7.frc.PIDValues;
import ca.warp7.frc.Pins;

/**
 * Static variables that maps the robot and includes constants
 */

public final class RobotMap {

    public static final class RIO {
        public static Pins driveLeftPins;
        public static Pins driveRightPins;
        public static Pins driveLeftEncoderChannels;
        public static Pins driveRightEncoderChannels;
        public static Pins pneumaticsShifterSolenoidPin;
        public static Pins pneumaticsCompressorPin;

        public static Pins hopperSpinPins;
        public static Pins towerSpinPins;
        public static Pins intakeSpinPins;
        public static Pins photoSensorPin;

        public static Pins shooterMaster;
        public static Pins shooterSlave;
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