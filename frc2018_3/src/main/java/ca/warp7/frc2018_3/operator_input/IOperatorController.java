package ca.warp7.frc2018_3.operator_input;

import ca.warp7.frc.commons.cheesy_drive.ICheesyDriveInput;

interface IOperatorController extends ICheesyDriveInput {

    boolean compressorShouldSwitch();

    boolean driveShouldSolenoidBeOnForShifter();

    boolean driveShouldReverse();

    IntakeMode getIntakeMode();

    double getClimberSpeed();

    double getArmSpeed();

    double getArmDistance();

    boolean intakeShouldTogglePiston();

    boolean cameraShouldSwitch();
}