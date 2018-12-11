package ca.warp7.frc2018_5.io;

import ca.warp7.frc.next.Robot;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import static ca.warp7.frc2018_5.constants.DriveConstants.kLeftDriftOffset;
import static ca.warp7.frc2018_5.constants.DriveConstants.kRightDriftOffset;
import static ca.warp7.frc2018_5.constants.RobotPins.*;

public class DriveOutput implements Robot.OutputSystem {

    private static DriveOutput instance;

    public static DriveOutput getInstance() {
        if (instance == null) instance = new DriveOutput();
        return instance;
    }

    private VictorSPX mLeftMaster;
    private VictorSPX mRightMaster;

    private double leftPercentOutput = 0;
    private double rightPercentOutput = 0;

    private DriveOutput() {
        mLeftMaster = new VictorSPX(kDriveLeftA);
        mRightMaster = new VictorSPX(kDriveRightA);

        VictorSPX leftFollower = new VictorSPX(kDriveLeftB);
        VictorSPX rightFollower = new VictorSPX(kDriveRightB);

        mRightMaster.setInverted(true);

        leftFollower.set(ControlMode.Follower, mLeftMaster.getDeviceID());
        rightFollower.set(ControlMode.Follower, mRightMaster.getDeviceID());
    }

    @Override
    public void onDisabled() {
        mLeftMaster.set(ControlMode.Disabled, 0);
        mRightMaster.set(ControlMode.Disabled, 0);
    }

    @Override
    public void onOutput() {
        mLeftMaster.set(ControlMode.PercentOutput, leftPercentOutput);
        mRightMaster.set(ControlMode.PercentOutput, rightPercentOutput);
    }

    public void setPercentOutput(double left, double right) {
        leftPercentOutput = Robot.limit(left, 1.0) * kLeftDriftOffset;
        rightPercentOutput = Robot.limit(right, 1.0) * kRightDriftOffset;
    }
}
