package ca.warp7.frc2018_3.operator_input;


import ca.warp7.frc2018_3.subsystems.Intake;

import static ca.warp7.frc2018_3.Components.drive;
import static ca.warp7.frc2018_3.Components.intake;
import static ca.warp7.frc2018_3.Components.pneumatics;

public class OperatorInput {

    private IOperatorController mController;

    public OperatorInput(IOperatorController controller){
        mController = controller;
    }

    public void onUpdate() {

        // driver BACK button PRESSED
        if (mController.compressorShouldSwitch()) {
            pneumatics.toggleClosedLoop();
        }

        // driver RIGHT bumper NOT HELD_DOWN
        pneumatics.setShift(mController.driveShouldShift());

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
                // driver LEFT trigger HELD DOWN
                intake.setSpeed(Intake.kFastOuttakePower);
                break;
            case SLOW_OUTTAKE:
                // driver LEFT d-pad HELD DOWN
                intake.setSpeed(Intake.kSlowOuttakePower);
                break;
        }
    }
}
