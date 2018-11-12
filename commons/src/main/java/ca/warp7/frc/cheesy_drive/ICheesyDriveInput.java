package ca.warp7.frc.cheesy_drive;

@SuppressWarnings("SameReturnValue")
@Deprecated
public interface ICheesyDriveInput {
    double getWheel();

    double getThrottle();

    boolean shouldQuickTurn();
}
