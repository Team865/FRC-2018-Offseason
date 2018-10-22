package ca.warp7.frc2017_2.constants;

import ca.warp7.frc.commons.PIDValues;
import ca.warp7.frc.commons.Pins;

import static ca.warp7.frc2017_2.constants.RobotMap.*;

public final class DefaultMap {
    public static void configure() {

        RIO.pneumaticsCompressorPin = Pins.pin(0);

        RIO.driveLeftPins = Pins.pins(2, 3);
        RIO.driveRightPins = Pins.pins(0, 1);
        RIO.driveLeftEncoderChannels = Pins.channels(0, 1);
        RIO.driveRightEncoderChannels = Pins.channels(2, 3);
        RIO.pneumaticsShifterSolenoidPin = Pins.pin(5);

        RIO.hopperSpinPins = Pins.pin(7);
        RIO.towerSpinPins = Pins.pin(6);
        RIO.intakeSpinPins = Pins.pin(8);
        RIO.photoSensorPin = Pins.pin(9);
        RIO.shooterSlave = Pins.pin(0);
        RIO.shooterMaster = Pins.pin(1);

        DriveConstants.inchesPerTick = (4 * Math.PI) / 1024;
        DriveConstants.leftDriftOffset = 1.0;
        DriveConstants.rightDriftOffset = 1.0;
        DriveConstants.preDriftSpeedLimit = 0.98;

        PID.uniformDrivePID = new PIDValues(0, 0, 0);
    }
}
