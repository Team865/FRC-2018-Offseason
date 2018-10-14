package ca.warp7.frc2018_3.operator_input;


import static ca.warp7.frc2018_3.Components.drive;
import static ca.warp7.frc2018_3.Components.pneumatics;

public class OperatorInput {

    private static IOperatorController sController;

    public static Runnable getRunnerFromController(IOperatorController controller){
        sController = controller;
        return OperatorInput::onUpdate;
    }

    private static void onUpdate() {

        // driver BACK button PRESSED
        if (sController.compressorShouldSwitch()) {
            pneumatics.toggleClosedLoop();
        }

        // driver RIGHT bumper NOT HELD_DOWN
        pneumatics.setShift(sController.driveShouldShift());

        // driver RIGHT stick button PRESSED
        drive.setReversed(sController.driveShouldReverse());

        // Wheel: driver RIGHT x-axis
        // Throttle: driver LEFT y-axis
        // QuickTurn: driver LEFT bumper HELD_DOWN
        drive.cheesyDrive(sController);
    }
}
