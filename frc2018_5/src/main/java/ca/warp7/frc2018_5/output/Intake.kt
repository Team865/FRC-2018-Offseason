package ca.warp7.frc2018_5.output

import ca.warp7.frc.next.OutputSystem
import ca.warp7.frc2018_5.constants.RobotPins.kIntakeLeftPin
import ca.warp7.frc2018_5.constants.RobotPins.kIntakeRightPin
import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.VictorSPX

object Intake : OutputSystem {
    private val leftMaster: VictorSPX = VictorSPX(kIntakeLeftPin)
    private val rightMaster: VictorSPX = VictorSPX(kIntakeRightPin)

    var speed = 0.0

    @Synchronized
    override fun onDisabled() {
        leftMaster.set(ControlMode.Disabled, 0.0)
        rightMaster.set(ControlMode.Disabled, 0.0)
    }

    @Synchronized
    override fun onOutput() {
        leftMaster.set(ControlMode.PercentOutput, speed)
        rightMaster.set(ControlMode.PercentOutput, speed)
    }
}
