package ca.warp7.frc.cheesy_drive;

public interface ICheesyDriveInput {
	double getWheel();

	double getThrottle();

	boolean shouldQuickTurn();

	boolean shouldAltQuickTurn();
}
