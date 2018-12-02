package ca.warp7.frc2018_4;

import ca.warp7.frc.core.IComponent;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.VictorSPXConfiguration;

@SuppressWarnings("ALL")
public class MotorConfig {

    public static final VictorSPXConfiguration kDefaultVictorSPX = new VictorSPXConfiguration();

    static {
        kDefaultVictorSPX.slot_0.kP = 0.0;
        kDefaultVictorSPX.slot_0.kI = 0.0;
        kDefaultVictorSPX.slot_0.kD = 0.0;
        kDefaultVictorSPX.slot_0.kF = 0.0;
        kDefaultVictorSPX.slot_0.integralZone = 0;
        kDefaultVictorSPX.slot_0.allowableClosedloopError = 0;
        kDefaultVictorSPX.slot_0.maxIntegralAccumulator = 0.0;
        kDefaultVictorSPX.slot_0.closedLoopPeakOutput = 1.0;
        kDefaultVictorSPX.slot_0.closedLoopPeriod = 1;

        kDefaultVictorSPX.slot_1.kP = 0.0;
        kDefaultVictorSPX.slot_1.kI = 0.0;
        kDefaultVictorSPX.slot_1.kD = 0.0;
        kDefaultVictorSPX.slot_1.kF = 0.0;
        kDefaultVictorSPX.slot_1.integralZone = 0;
        kDefaultVictorSPX.slot_1.allowableClosedloopError = 0;
        kDefaultVictorSPX.slot_1.maxIntegralAccumulator = 0.0;
        kDefaultVictorSPX.slot_1.closedLoopPeakOutput = 1.0;
        kDefaultVictorSPX.slot_1.closedLoopPeriod = 1;

        kDefaultVictorSPX.slot_2.kP = 0.0;
        kDefaultVictorSPX.slot_2.kI = 0.0;
        kDefaultVictorSPX.slot_2.kD = 0.0;
        kDefaultVictorSPX.slot_2.kF = 0.0;
        kDefaultVictorSPX.slot_2.integralZone = 0;
        kDefaultVictorSPX.slot_2.allowableClosedloopError = 0;
        kDefaultVictorSPX.slot_2.maxIntegralAccumulator = 0.0;
        kDefaultVictorSPX.slot_2.closedLoopPeakOutput = 1.0;
        kDefaultVictorSPX.slot_2.closedLoopPeriod = 1;

        kDefaultVictorSPX.slot_3.kP = 0.0;
        kDefaultVictorSPX.slot_3.kI = 0.0;
        kDefaultVictorSPX.slot_3.kD = 0.0;
        kDefaultVictorSPX.slot_3.kF = 0.0;
        kDefaultVictorSPX.slot_3.integralZone = 0;
        kDefaultVictorSPX.slot_3.allowableClosedloopError = 0;
        kDefaultVictorSPX.slot_3.maxIntegralAccumulator = 0.0;
        kDefaultVictorSPX.slot_3.closedLoopPeakOutput = 1.0;
        kDefaultVictorSPX.slot_3.closedLoopPeriod = 1;

        kDefaultVictorSPX.filter_0.remoteSensorDeviceID = 0;
        kDefaultVictorSPX.filter_0.remoteSensorSource = RemoteSensorSource.Off;

        kDefaultVictorSPX.filter_1.remoteSensorDeviceID = 0;
        kDefaultVictorSPX.filter_1.remoteSensorSource = RemoteSensorSource.Off;

        kDefaultVictorSPX.openloopRamp = 0.0;
        kDefaultVictorSPX.closedloopRamp = 0.0;
        kDefaultVictorSPX.peakOutputForward = 1.0;
        kDefaultVictorSPX.peakOutputReverse = -1.0;
        kDefaultVictorSPX.nominalOutputForward = 0.0;
        kDefaultVictorSPX.nominalOutputReverse = 0.0;
        kDefaultVictorSPX.neutralDeadband = 0.04;
        kDefaultVictorSPX.voltageCompSaturation = 0.0;
        kDefaultVictorSPX.voltageMeasurementFilter = 32;
        kDefaultVictorSPX.velocityMeasurementPeriod = VelocityMeasPeriod.Period_100Ms;
        kDefaultVictorSPX.velocityMeasurementWindow = 64;
        kDefaultVictorSPX.forwardLimitSwitchDeviceID = 0;
        kDefaultVictorSPX.reverseLimitSwitchDeviceID = 0;
        kDefaultVictorSPX.forwardLimitSwitchNormal = LimitSwitchNormal.NormallyOpen;
        kDefaultVictorSPX.reverseLimitSwitchNormal = LimitSwitchNormal.NormallyOpen;
        kDefaultVictorSPX.forwardSoftLimitThreshold = 0;
        kDefaultVictorSPX.reverseSoftLimitThreshold = 0;
        kDefaultVictorSPX.forwardSoftLimitEnable = false;
        kDefaultVictorSPX.reverseSoftLimitEnable = false;
        kDefaultVictorSPX.auxPIDPolarity = false;
        kDefaultVictorSPX.motionCruiseVelocity = 0;
        kDefaultVictorSPX.motionAcceleration = 0;
        kDefaultVictorSPX.motionProfileTrajectoryPeriod = 0;
        kDefaultVictorSPX.feedbackNotContinuous = false;
        kDefaultVictorSPX.remoteSensorClosedLoopDisableNeutralOnLOS = false;
        kDefaultVictorSPX.clearPositionOnLimitF = false;
        kDefaultVictorSPX.clearPositionOnLimitR = false;
        kDefaultVictorSPX.clearPositionOnQuadIdx = false;
        kDefaultVictorSPX.limitSwitchDisableNeutralOnLOS = false;
        kDefaultVictorSPX.softLimitDisableNeutralOnLOS = false;
        kDefaultVictorSPX.pulseWidthPeriod_EdgesPerRot = 1;
        kDefaultVictorSPX.pulseWidthPeriod_FilterWindowSz = 1;

        kDefaultVictorSPX.primaryPID.selectedFeedbackSensor = RemoteFeedbackDevice.RemoteSensor0;
        kDefaultVictorSPX.auxilaryPID.selectedFeedbackSensor = RemoteFeedbackDevice.RemoteSensor0;

        kDefaultVictorSPX.forwardLimitSwitchSource = RemoteLimitSwitchSource.Deactivated;
        kDefaultVictorSPX.reverseLimitSwitchSource = RemoteLimitSwitchSource.Deactivated;

        kDefaultVictorSPX.sum_0 = RemoteFeedbackDevice.RemoteSensor0;
        kDefaultVictorSPX.sum_1 = RemoteFeedbackDevice.RemoteSensor0;
        kDefaultVictorSPX.diff_0 = RemoteFeedbackDevice.RemoteSensor0;
        kDefaultVictorSPX.diff_1 = RemoteFeedbackDevice.RemoteSensor0;
    }

    static class VictorFactory implements IComponent {
        private VictorSPXConfiguration configuration;
        private VictorSPX victorSPX;
        private int deviceNumber;

        public VictorFactory(int deviceNumber, VictorSPXConfiguration configuration) {
            this.configuration = configuration;
            this.deviceNumber = deviceNumber;
        }

        @Override
        public void onConstruct() {
            victorSPX = new VictorSPX(deviceNumber);
            victorSPX.configAllSettings(configuration);
            victorSPX.set(ControlMode.Disabled, 0);
            victorSPX.setInverted(false);
            victorSPX.setNeutralMode(NeutralMode.Brake);
            victorSPX.selectProfileSlot(0, 0);
            victorSPX.clearStickyFaults();
        }
    }

    public static final TalonSRXConfiguration kDefaultTalonSRX = new TalonSRXConfiguration();

    static {
        kDefaultTalonSRX.slot_0.kP = 0.0;
        kDefaultTalonSRX.slot_0.kI = 0.0;
        kDefaultTalonSRX.slot_0.kD = 0.0;
        kDefaultTalonSRX.slot_0.kF = 0.0;
        kDefaultTalonSRX.slot_0.integralZone = 0;
        kDefaultTalonSRX.slot_0.allowableClosedloopError = 0;
        kDefaultTalonSRX.slot_0.maxIntegralAccumulator = 0.0;
        kDefaultTalonSRX.slot_0.closedLoopPeakOutput = 1.0;
        kDefaultTalonSRX.slot_0.closedLoopPeriod = 1;

        kDefaultTalonSRX.slot_1.kP = 0.0;
        kDefaultTalonSRX.slot_1.kI = 0.0;
        kDefaultTalonSRX.slot_1.kD = 0.0;
        kDefaultTalonSRX.slot_1.kF = 0.0;
        kDefaultTalonSRX.slot_1.integralZone = 0;
        kDefaultTalonSRX.slot_1.allowableClosedloopError = 0;
        kDefaultTalonSRX.slot_1.maxIntegralAccumulator = 0.0;
        kDefaultTalonSRX.slot_1.closedLoopPeakOutput = 1.0;
        kDefaultTalonSRX.slot_1.closedLoopPeriod = 1;

        kDefaultTalonSRX.slot_2.kP = 0.0;
        kDefaultTalonSRX.slot_2.kI = 0.0;
        kDefaultTalonSRX.slot_2.kD = 0.0;
        kDefaultTalonSRX.slot_2.kF = 0.0;
        kDefaultTalonSRX.slot_2.integralZone = 0;
        kDefaultTalonSRX.slot_2.allowableClosedloopError = 0;
        kDefaultTalonSRX.slot_2.maxIntegralAccumulator = 0.0;
        kDefaultTalonSRX.slot_2.closedLoopPeakOutput = 1.0;
        kDefaultTalonSRX.slot_2.closedLoopPeriod = 1;

        kDefaultTalonSRX.slot_3.kP = 0.0;
        kDefaultTalonSRX.slot_3.kI = 0.0;
        kDefaultTalonSRX.slot_3.kD = 0.0;
        kDefaultTalonSRX.slot_3.kF = 0.0;
        kDefaultTalonSRX.slot_3.integralZone = 0;
        kDefaultTalonSRX.slot_3.allowableClosedloopError = 0;
        kDefaultTalonSRX.slot_3.maxIntegralAccumulator = 0.0;
        kDefaultTalonSRX.slot_3.closedLoopPeakOutput = 1.0;
        kDefaultTalonSRX.slot_3.closedLoopPeriod = 1;

        kDefaultTalonSRX.filter_0.remoteSensorDeviceID = 0;
        kDefaultTalonSRX.filter_0.remoteSensorSource = RemoteSensorSource.Off;

        kDefaultTalonSRX.filter_1.remoteSensorDeviceID = 0;
        kDefaultTalonSRX.filter_1.remoteSensorSource = RemoteSensorSource.Off;

        kDefaultTalonSRX.openloopRamp = 0.0;
        kDefaultTalonSRX.closedloopRamp = 0.0;
        kDefaultTalonSRX.peakOutputForward = 1.0;
        kDefaultTalonSRX.peakOutputReverse = -1.0;
        kDefaultTalonSRX.nominalOutputForward = 0.0;
        kDefaultTalonSRX.nominalOutputReverse = 0.0;
        kDefaultTalonSRX.neutralDeadband = 0.04;
        kDefaultTalonSRX.voltageCompSaturation = 0.0;
        kDefaultTalonSRX.voltageMeasurementFilter = 32;
        kDefaultTalonSRX.velocityMeasurementPeriod = VelocityMeasPeriod.Period_100Ms;
        kDefaultTalonSRX.velocityMeasurementWindow = 64;
        kDefaultTalonSRX.forwardLimitSwitchDeviceID = 0;
        kDefaultTalonSRX.reverseLimitSwitchDeviceID = 0;
        kDefaultTalonSRX.forwardLimitSwitchNormal = LimitSwitchNormal.NormallyOpen;
        kDefaultTalonSRX.reverseLimitSwitchNormal = LimitSwitchNormal.NormallyOpen;
        kDefaultTalonSRX.forwardSoftLimitThreshold = 0;
        kDefaultTalonSRX.reverseSoftLimitThreshold = 0;
        kDefaultTalonSRX.forwardSoftLimitEnable = false;
        kDefaultTalonSRX.reverseSoftLimitEnable = false;
        kDefaultTalonSRX.auxPIDPolarity = false;
        kDefaultTalonSRX.motionCruiseVelocity = 0;
        kDefaultTalonSRX.motionAcceleration = 0;
        kDefaultTalonSRX.motionProfileTrajectoryPeriod = 0;
        kDefaultTalonSRX.feedbackNotContinuous = false;
        kDefaultTalonSRX.remoteSensorClosedLoopDisableNeutralOnLOS = false;
        kDefaultTalonSRX.clearPositionOnLimitF = false;
        kDefaultTalonSRX.clearPositionOnLimitR = false;
        kDefaultTalonSRX.clearPositionOnQuadIdx = false;
        kDefaultTalonSRX.limitSwitchDisableNeutralOnLOS = false;
        kDefaultTalonSRX.softLimitDisableNeutralOnLOS = false;
        kDefaultTalonSRX.pulseWidthPeriod_EdgesPerRot = 1;
        kDefaultTalonSRX.pulseWidthPeriod_FilterWindowSz = 1;

        kDefaultTalonSRX.forwardLimitSwitchSource = LimitSwitchSource.FeedbackConnector;
        kDefaultTalonSRX.reverseLimitSwitchSource = LimitSwitchSource.FeedbackConnector;
        kDefaultTalonSRX.sum_0 = FeedbackDevice.QuadEncoder;
        kDefaultTalonSRX.sum_1 = FeedbackDevice.QuadEncoder;
        kDefaultTalonSRX.diff_0 = FeedbackDevice.QuadEncoder;
        kDefaultTalonSRX.diff_1 = FeedbackDevice.QuadEncoder;
        kDefaultTalonSRX.peakCurrentLimit = 1;
        kDefaultTalonSRX.peakCurrentDuration = 1;
        kDefaultTalonSRX.continuousCurrentLimit = 1;
    }

    static class TalonFactory implements IComponent {
        private TalonSRXConfiguration configuration;
        private TalonSRX talonSRX;
        private int deviceNumber;

        public TalonFactory(int deviceNumber, TalonSRXConfiguration configuration) {
            this.configuration = configuration;
            this.deviceNumber = deviceNumber;
        }

        @Override
        public void onConstruct() {
            talonSRX = new TalonSRX(deviceNumber);
            talonSRX.configAllSettings(configuration);
            talonSRX.set(ControlMode.Disabled, 0);
            talonSRX.setInverted(false);
            talonSRX.setNeutralMode(NeutralMode.Brake);
            talonSRX.selectProfileSlot(0, 0);
            talonSRX.clearStickyFaults();
        }
    }
}
