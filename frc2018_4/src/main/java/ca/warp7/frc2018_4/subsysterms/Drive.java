package ca.warp7.frc2018_4.subsysterms;

import ca.warp7.frc.CheesyDrive;
import ca.warp7.frc.DifferentialVector;
import ca.warp7.frc.DtMeasurement;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import com.stormbots.MiniPID;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

import java.util.LinkedList;

import static ca.warp7.frc2018_4.Constants.DriveConstants.*;

public class Drive {
    private CheesyDrive mCheesyDrive;
    private AHRS mAHRS;
    private VictorSPX mLeftDriveMotorA;
    private VictorSPX mLeftDriveMotorB;
    private VictorSPX mRightDriveMotorA;
    private VictorSPX mRightDriveMotorB;
    private SpeedControllerGroup mLeftGroup;
    private SpeedControllerGroup mRightGroup;
    private DifferentialVector<Encoder> mEncoders;
    private Solenoid mShifterSolenoid;

    private final Input mInput = new Input();
    private final State mState = new State();

    private static Encoder configEncoder(int channelA, int channelB, boolean reversed) {
        Encoder encoder = new Encoder(channelA, channelB, reversed, CounterBase.EncodingType.k4X);
        encoder.setDistancePerPulse((kWheelRadius * Math.PI) / kEncoderTicksPerRevolution);
        encoder.reset();
        return encoder;
    }

    private static WPI_VictorSPX createVictor(int deviceID) {
        return new WPI_VictorSPX(deviceID);
    }

    private static WPI_VictorSPX cast(VictorSPX victorSPX) {
        return (WPI_VictorSPX) victorSPX;
    }

    private static double linearScaleDeadband(double n) {
        return Math.abs(n) < kAxisDeadband ? 0 : (n - Math.copySign(kAxisDeadband, n)) / (1 - kAxisDeadband);
    }

    private enum Action {
        Brake,
        OpenLoop,
        LinearPID
    }

    static class Input {
        Action wantedAction;
        boolean shouldReverse;
        boolean shouldSolenoidBeOnForShifter;
        double leftPercentOutputDemand;
        double rightPercentOutputDemand;
        double leftTargetDistance;
        double rightTargetDistance;
    }

    static class State {
        Action action;
        boolean isReversed;
        boolean isSolenoidOnForShifter;
        double _timestamp;
        double leftPercentOutput;
        double rightPercentOutput;
        double leftDistance;
        double rightDistance;
        double accelerationX;
        double accelerationY;
        double _yaw;
        double yawChangeVelocity;
        double chassisLinearVelocity;
        double chassisAngularVelocity;
        double leftVoltage;
        double rightVoltage;
        double maxVoltage;
        double minVoltage;
        double leftCurrent;
        double rightCurrent;
        double leftTemperature;
        double rightTemperature;
        double predictedX;
        double predictedY;
        final DifferentialVector<Double> measuredVelocity = DifferentialVector.zeroes();
        final LinkedList<DifferentialVector<DtMeasurement>> velocityAverages = new LinkedList<>();
        final DifferentialVector<Double> encoderRate = DifferentialVector.zeroes();
        final DifferentialVector<Double> encoderAcceleration = DifferentialVector.zeroes();
        final MiniPID leftLinearPID = new MiniPID(0, 0, 0, 0);
        final MiniPID rightLinearPID = new MiniPID(0, 0, 0, 0);
    }
}
