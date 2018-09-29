package ca.warp7.frc;

public interface IDriveSignalReceiver {
	void onDriveSpeedDemand(double leftPowerDemand, double rightPowerDemand);
}
