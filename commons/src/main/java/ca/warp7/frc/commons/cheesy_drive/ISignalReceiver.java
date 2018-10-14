package ca.warp7.frc.commons.cheesy_drive;

@FunctionalInterface
public interface ISignalReceiver {
    void setDemandedDriveSpeed(double leftSpeedDemand, double rightSpeedDemand);
}
