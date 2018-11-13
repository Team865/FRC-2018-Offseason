package ca.warp7.frc2017_2.operator_input;

import ca.warp7.frc.CheesyDrive;

interface IOperatorController extends CheesyDrive.IControlsInput {

    boolean compressorShouldSwitch();

    boolean driveShouldShift();

    boolean driveShouldReverse();

    ShooterMode getShooterMode();

    boolean hopperShouldReverse();

    boolean shooterShouldShoot();

    boolean shooterShouldStop();
}
