package ca.warp7.frc2018_5.output

import ca.warp7.frc.next.OutputSystem
import ca.warp7.frc2018_5.constants.RobotPins.kClimberPin
import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.VictorSPX

object Climber : OutputSystem {
    private val climberMaster = VictorSPX(kClimberPin)

    var percentOutput = 0.0

    @Synchronized
    override fun onDisabled() {
        climberMaster.set(ControlMode.Disabled, 0.0)
    }

    override fun onIdle() {
        onDisabled()
    }

    @Synchronized
    override fun onOutput() {
        climberMaster.set(ControlMode.PercentOutput, percentOutput)
    }
}
