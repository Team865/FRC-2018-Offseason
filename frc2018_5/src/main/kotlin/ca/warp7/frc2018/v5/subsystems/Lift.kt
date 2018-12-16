package ca.warp7.frc2018.v5.subsystems

import ca.warp7.frc.Subsystem
import ca.warp7.frc2018.v5.constants.RobotPins.kLiftMotorPin
import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.VictorSPX

object Lift : Subsystem {
    private val liftMaster = VictorSPX(kLiftMotorPin)

    var percentOutput = 0.0

    @Synchronized
    override fun onDisabled() = liftMaster.set(ControlMode.Disabled, 0.0)

    override fun onIdle() = onDisabled()

    @Synchronized
    override fun onOutput() = liftMaster.set(ControlMode.PercentOutput, percentOutput)
}
