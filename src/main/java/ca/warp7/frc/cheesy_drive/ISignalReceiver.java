package ca.warp7.frc.cheesy_drive;

@FunctionalInterface
public interface ISignalReceiver {
	void setDemandedDriveSpeed(double leftSpeedDemand, double rightSpeedDemand);
}
