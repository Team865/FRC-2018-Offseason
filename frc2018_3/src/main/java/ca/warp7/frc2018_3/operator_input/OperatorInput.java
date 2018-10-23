package ca.warp7.frc2018_3.operator_input;

import ca.warp7.frc.commons.core.Components;
import ca.warp7.frc.commons.core.IControllerLoop;
import ca.warp7.frc2018_3.subsystems.Intake;

import static ca.warp7.frc2018_3.Components.*;

public class OperatorInput implements IControllerLoop {

    private IOperatorController mController;

    public OperatorInput(IOperatorController controller){
        mController = controller;
    }

    @Override
    public void onRegister(Components components) {
        // Do nothing for now
    }

    @Override
    public void onPeriodic() {

        // driver BACK button PRESSED
        if (mController.compressorShouldSwitch()) {
            pneumatics.toggleClosedLoop();
        }

        // driver RIGHT bumper NOT HELD_DOWN
        pneumatics.setShouldSolenoidBeOnForShifter(mController.driveShouldSolenoidBeOnForShifter());

        // driver RIGHT stick button PRESSED
        drive.setReversed(mController.driveShouldReverse());

        // Wheel: driver RIGHT x-axis
        // Throttle: driver LEFT y-axis
        // QuickTurn: driver LEFT bumper HELD_DOWN
        drive.cheesyDrive(mController);

        switch (mController.getIntakeMode()){
            case NONE:
                // None of the other three cases
                intake.setSpeed(0);
                break;
            case INTAKE:
                // driver RIGHT trigger HELD_DOWN or
                // driver RIGHT d-pad HELD DOWN
                intake.setSpeed(Intake.kIntakePower);
                break;
            case FAST_OUTTAKE:
                // driver LEFT trigger HELD_DOWN
                intake.setSpeed(Intake.kFastOuttakePower);
                break;
            case SLOW_OUTTAKE:
                // driver LEFT d-pad HELD_DOWN
                intake.setSpeed(Intake.kSlowOuttakePower);
                break;
        }

        // driver A button PRESSED
        if (mController.intakeShouldTogglePiston()){
            intake.togglePiston();
        }

        // drive X button PRESSED
        if (mController.cameraShouldSwitch()){
            limelight.switchCamera();
        }

        // operator RIGHT y-axis when operator B button HELD_DOWN exclusive or
        // driver LEFT y-axis when driver B button HELD_DOWN or 0
        climber.setSpeed(mController.getClimberSpeed());
//        armClimber.setSpeed(mController.getArmSpeed());
//        armLift.setLoc(mController.getArmDistance());
        pneumatics.setGrapplingHook(mController.grapplingHookShouldDeploy());

        actualClimber.setSpeed(mController.getActualClimberSpeed());
    }
}
