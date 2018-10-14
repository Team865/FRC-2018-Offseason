package ca.warp7.frc2018_3.constants;

import ca.warp7.frc.commons.state.PIDValues;
import ca.warp7.frc2018_3.constants.RobotMap.DriveConstants;
import ca.warp7.frc2018_3.constants.RobotMap.PID;
import ca.warp7.frc2018_3.constants.RobotMap.RIO;

import static ca.warp7.frc.commons.state.Pins.*;

public final class DefaultMap {
    public static void configure() {

        RIO.driveLeftPins = pins(3, 4);
        RIO.driveRightPins = pins(5, 1);
        RIO.driveLeftEncoderChannels = channels(2, 3);
        RIO.driveRightEncoderChannels = channels(0, 1);

        RIO.pneumaticsCompressorPin = pin(0);
        RIO.pneumaticsShifterSolenoidPin = pin(1);

        RIO.climberPins = pins(9, 0);
        RIO.intakeLeftPin = pin(7);
        RIO.intakeRightPin = pin(2);
        RIO.intakePiston = pin(0);

        DriveConstants.inchesPerTick = (6 * Math.PI) / 256;
        DriveConstants.leftDriftOffset = 1.0;
        DriveConstants.rightDriftOffset = 1.0;
        DriveConstants.preDriftSpeedLimit = 0.98;

        PID.uniformDrivePID = new PIDValues(0, 0, 0);
    }
}
