package ca.warp7.frc.drive;

public interface ICheesyDriveController {
	double getWheel();
	double getThrottle();
	boolean shouldQuickTurn();
	boolean shouldAltQuickTurn();
}
