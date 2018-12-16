package ca.warp7.frc2018.v5.subsystems

import ca.warp7.frc.Subsystem
import ca.warp7.frc2018.v5.constants.RobotPins.*
import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.VictorSPX
import edu.wpi.first.wpilibj.Solenoid

object Drive : Subsystem {

    private val leftMaster = VictorSPX(kDriveLeftA)
    private val rightMaster = VictorSPX(kDriveRightA)
    private val shifterSolenoid = Solenoid(kDriveShifterSolenoidPin)

    var leftPercentOutput = 0.0
    var rightPercentOutput = 0.0
    var solenoidOnForShifter = false

    init {
        rightMaster.inverted = true
        val leftFollower = VictorSPX(kDriveLeftB)
        val rightFollower = VictorSPX(kDriveRightB)
        leftFollower.set(ControlMode.Follower, leftMaster.deviceID.toDouble())
        rightFollower.set(ControlMode.Follower, rightMaster.deviceID.toDouble())
        shifterSolenoid.set(true)
        shifterSolenoid.set(false)
    }

    @Synchronized
    override fun onDisabled() {
        leftMaster.set(ControlMode.Disabled, 0.0)
        rightMaster.set(ControlMode.Disabled, 0.0)
    }

    override fun onIdle() {
        leftMaster.set(ControlMode.PercentOutput, 0.0)
        rightMaster.set(ControlMode.PercentOutput, 0.0)
    }

    @Synchronized
    override fun onOutput() {
        leftMaster.set(ControlMode.PercentOutput, leftPercentOutput)
        rightMaster.set(ControlMode.PercentOutput, rightPercentOutput)
        if (solenoidOnForShifter) {
            if (!shifterSolenoid.get()) {
                shifterSolenoid.set(true)
            }
        } else if (shifterSolenoid.get()) {
            shifterSolenoid.set(false)
        }
    }
}
