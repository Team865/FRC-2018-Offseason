package ca.warp7.frc2017_v2.operator_input;

import static ca.warp7.frc2017_v2.constants.RobotMap.Subsystems.*;

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

		// driver RIGHT stick button PRESSED
		drive.setReversed(sController.driveShouldReverse());

		// driver RIGHT bumper NOT HELD_DOWN
		drive.setShift(sController.driveShouldShift());

		// Wheel: driver RIGHT x-axis
		// Throttle: driver LEFT y-axis
		// QuickTurn: driver LEFT bumper HELD_DOWN
		drive.cheesyDrive(sController);


		// The following code are not modified for the new codebase

		switch (sController.getShooterMode()) {
			case RPM_4425:
				// operator B button HELD_DOWN
				shooter.setRPM(4425);
			case RPM_4450:
				// operator LEFT trigger HELD_DOWN
				shooter.setRPM(4450);
			case NONE:
				// Neither above
				shooter.setRPM(0);
		}

		if (sController.hopperShouldReverse()) {
			// operator RIGHT d-pad HELD_DOWN
			shooter.setHopperSpeed(-1.0);
			shooter.setIntakeSpeed(1.0);
			shooter.setTowerSpeed(0.0);

		} else if (sController.shooterShouldShoot()) {
			// operator A button HELD_DOWN
			shooter.setHopperSpeed(1.0);
			shooter.setIntakeSpeed(1.0);
			if (shooter.withinRange(25) && shooter.getSetPoint() > 0.0) {
				shooter.setTowerSpeed(1.0);
			} else if (shooter.getSensor()) {
				shooter.setTowerSpeed(0.5);
			} else {
				shooter.setTowerSpeed(0.0);
			}

		} else if (sController.shooterShouldStop()) {
			// operator A button KEPT_UP
			shooter.setIntakeSpeed(0.0);
			shooter.setHopperSpeed(0.0);
			shooter.setTowerSpeed(0.0);
		}
	}
}
