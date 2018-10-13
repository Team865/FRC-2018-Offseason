package ca.warp7.frc2018_3.operator_input;


import static ca.warp7.frc2018_3.constants.RobotMap.Subsystems.drive;
import static ca.warp7.frc2018_3.constants.RobotMap.Subsystems.pneumatics;

public class OperatorInput {

    private static IOperatorController sController;

    public static void setController(IOperatorController controller) {
        sController = controller;
    }

    public static void onUpdate() {

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
