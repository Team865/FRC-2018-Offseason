package ca.warp7.frc_commons.cheesy_drive;

@FunctionalInterface
public interface ISignalReceiver {
	void setDemandedDriveSpeed(double leftSpeedDemand, double rightSpeedDemand);
}
