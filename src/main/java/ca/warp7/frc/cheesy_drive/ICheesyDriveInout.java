package ca.warp7.frc.cheesy_drive;

public interface ICheesyDriveInout {
	double getWheel();

	double getThrottle();

	boolean shouldQuickTurn();

	boolean shouldAltQuickTurn();
}
