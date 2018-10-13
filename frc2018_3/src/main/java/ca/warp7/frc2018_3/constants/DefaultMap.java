package ca.warp7.frc2018_3.constants;

import ca.warp7.frc.commons.state.PIDValues;
import ca.warp7.frc2018_3.constants.RobotMap.DriveConstants;
import ca.warp7.frc2018_3.constants.RobotMap.PID;
import ca.warp7.frc2018_3.constants.RobotMap.RIO;

import static ca.warp7.frc.commons.state.Pins.*;

public final class DefaultMap {
    public static void configure() {

        RIO.pneumaticsCompressorPin = pin(0); // LAST UPDATED OCT 13 2018, NOT CHECKED

        RIO.driveLeftPins = pins(3, 4); // LAST UPDATED OCT 13 2018, NOT CHECKED
        RIO.driveRightPins = pins(5, 1); // LAST UPDATED OCT 13 2018, NOT CHECKED
        RIO.driveLeftEncoderChannels = channels(2, 3); // LAST UPDATED OCT 13 2018, NOT CHECKED
        RIO.driveRightEncoderChannels = channels(0, 1); // LAST UPDATED OCT 13 2018, NOT CHECKED

        RIO.pneumaticsShifterSolenoidPin = pin(1); // LAST UPDATED OCT 13 2018, NOT CHECKED

        DriveConstants.inchesPerTick = (6 * Math.PI) / 256; // LAST UPDATED OCT 13 2018, NOT CHECKED
        DriveConstants.leftDriftOffset = 1.0;
        DriveConstants.rightDriftOffset = 1.0;
        DriveConstants.preDriftSpeedLimit = 0.98;

        PID.uniformDrivePID = new PIDValues(0, 0, 0);
    }
}
