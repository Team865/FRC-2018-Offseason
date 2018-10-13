package ca.warp7.frc_commons.cheesy_drive;

@SuppressWarnings("SameReturnValue")
public interface ICheesyDriveInput {
	double getWheel();

	double getThrottle();

	boolean shouldQuickTurn();

	boolean shouldAltQuickTurn();
}
