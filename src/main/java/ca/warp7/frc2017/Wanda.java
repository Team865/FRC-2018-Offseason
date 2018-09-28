package ca.warp7.frc2017;

import ca.warp7.frc.Robot;
import ca.warp7.frc2017.controls.DualRemote;
import ca.warp7.frc2017.controls.IControlsInterface;

import static ca.warp7.frc2017.Mapping.DriveConstants.*;
import static ca.warp7.frc2017.Mapping.RIO.*;
import static ca.warp7.frc2017.Mapping.Subsystems.*;

public final class Wanda extends Robot.Main<IControlsInterface> {
	@Override
	public void onInit() {
		System.out.println("Hello me is robit!");
		setControlLoopDelta(WAIT_FOR_DRIVER_STATION);
		setController(new DualRemote(0, 1));
	}

	@Override
	public void onTeleopInit() {
		compressor.setClosedLoop(true);
		Robot.utils.getStateObserver().registerAllSubsystems();
	}

	@Override
	public void onTeleopPeriodic(IControlsInterface controller_) {
		DualRemote controller = (DualRemote) controller_;

		// Compressor
		if (controller.compressorShouldSwitch()) {
			compressor.toggleClosedLoop();
		}

		// Drive
		drive.setReversed(controller.driveShouldReverse());
		drive.setShift(controller.driveShouldShift());
		drive.cheesyDrive(controller);

		//Shooter
		switch (controller.getShooterMode()) {
			case RPM_4425:
				shooter.setRPM(4425);
			case RPM_4450:
				shooter.setRPM(4450);
			case NONE:
				shooter.setRPM(0);
		}
		if (controller.hopperShouldReverse()) {
			shooter.setHopperSpeed(-1.0);
			shooter.setIntakeSpeed(1.0);
			shooter.setTowerSpeed(0.0);
		} else if (controller.shooterShouldShoot()) {
			shooter.setHopperSpeed(1.0);
			shooter.setIntakeSpeed(1.0);
			if (shooter.withinRange(25) && shooter.getSetPoint() > 0.0) {
				shooter.setTowerSpeed(1.0);
			} else if (shooter.getSensor()) {
				shooter.setTowerSpeed(0.5);
			} else {
				shooter.setTowerSpeed(0.0);
			}
		} else if (controller.shooterShouldStop()) {
			shooter.setIntakeSpeed(0.0);
			shooter.setHopperSpeed(0.0);
			shooter.setTowerSpeed(0.0);
		}
	}

	@Override
	public void onConfigureMapping() {
		// Pins
		compressorPin = pin(0);

		driveLeftPins = pins(2, 3);
		driveLeftPins = pins(2, 3);
		driveRightPins = pins(0, 1);
		driveLeftEncoderChannels = channels(0, 1);
		driveRightEncoderChannels = channels(2, 3);
		driveShifterPins = pin(1);

		hopperSpinPins = pin(7);
		towerSpinPins = pin(6);
		intakeSpinPins = pin(8);
		photoSensorPin = pin(9);
		shooterSlave = pin(0);
		shooterMaster = pin(1);

		// Drive constants
		inchesPerTick = (4 * Math.PI) / 1024;
		leftDriftOffset = 1.0;
		rightDriftOffset = 1.0;
		speedLimit = 0.99;
	}
}
