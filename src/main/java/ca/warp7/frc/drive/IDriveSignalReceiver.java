package ca.warp7.frc.drive;

public interface IDriveSignalReceiver {
	void onDrive(double leftPowerDemand, double rightPowerDemand);
}
