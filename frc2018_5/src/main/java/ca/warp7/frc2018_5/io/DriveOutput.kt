package ca.warp7.frc2018_5.io

import ca.warp7.frc.next.Robot
import ca.warp7.frc2018_5.constants.RobotPins.*
import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.VictorSPX
import edu.wpi.first.wpilibj.Solenoid

object DriveOutput : Robot.OutputSystem {

    private val mLeftMaster: VictorSPX = VictorSPX(kDriveLeftA)
    private val mRightMaster: VictorSPX = VictorSPX(kDriveRightA)
    private val mShifterSolenoid = Solenoid(kDriveShifterSolenoidPin)

    var leftPercentOutput = 0.0
    var rightPercentOutput = 0.0
    var solenoidOnForShifter: Boolean = false

    init {
        mRightMaster.inverted = true

        val leftFollower = VictorSPX(kDriveLeftB)
        val rightFollower = VictorSPX(kDriveRightB)
        leftFollower.set(ControlMode.Follower, mLeftMaster.deviceID.toDouble())
        rightFollower.set(ControlMode.Follower, mRightMaster.deviceID.toDouble())

        mShifterSolenoid.set(true)
        mShifterSolenoid.set(false)
    }

    @Synchronized
    override fun onDisabled() {
        mLeftMaster.set(ControlMode.Disabled, 0.0)
        mRightMaster.set(ControlMode.Disabled, 0.0)
    }

    @Synchronized
    override fun onOutput() {
        if (solenoidOnForShifter) {
            if (!mShifterSolenoid.get()) {
                mShifterSolenoid.set(true)
            }
        } else if (mShifterSolenoid.get()) {
            mShifterSolenoid.set(false)
        }

        mLeftMaster.set(ControlMode.PercentOutput, leftPercentOutput)
        mRightMaster.set(ControlMode.PercentOutput, rightPercentOutput)
    }
}
