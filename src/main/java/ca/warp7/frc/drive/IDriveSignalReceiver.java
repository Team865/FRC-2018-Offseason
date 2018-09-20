package ca.warp7.frc.drive;

public interface IDriveSignalReceiver {
	void onDrive(double leftDemand, double rightDemand);
}
