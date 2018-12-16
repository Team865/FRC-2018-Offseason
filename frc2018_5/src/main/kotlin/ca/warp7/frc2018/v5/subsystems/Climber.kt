package ca.warp7.frc2018.v5.subsystems

import ca.warp7.frc.Subsystem
import ca.warp7.frc2018.v5.constants.RobotPins.kClimberPin
import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.VictorSPX

object Climber : Subsystem {
    private val climberMaster = VictorSPX(kClimberPin)

    var percentOutput = 0.0

    @Synchronized
    override fun onDisabled() = climberMaster.set(ControlMode.Disabled, 0.0)

    override fun onIdle() = onDisabled()

    @Synchronized
    override fun onOutput() = climberMaster.set(ControlMode.PercentOutput, percentOutput)
}
