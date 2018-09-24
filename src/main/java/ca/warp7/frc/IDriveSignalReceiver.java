package ca.warp7.frc;

public interface IDriveSignalReceiver {
	void onDrive(double leftPowerDemand, double rightPowerDemand);
}
