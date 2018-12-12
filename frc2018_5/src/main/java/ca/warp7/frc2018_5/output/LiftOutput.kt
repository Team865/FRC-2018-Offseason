package ca.warp7.frc2018_5.output

import ca.warp7.frc.next.OutputSystem
import ca.warp7.frc2018_5.constants.RobotPins.kLiftMotorPin
import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.VictorSPX

object LiftOutput : OutputSystem {
    private val liftMaster = VictorSPX(kLiftMotorPin)

    var percentOutput = 0.0

    @Synchronized
    override fun onDisabled() {
        liftMaster.set(ControlMode.Disabled, 0.0)
    }

    override fun onIdle() {
        super.onDisabled()
    }

    @Synchronized
    override fun onOutput() {
        liftMaster.set(ControlMode.PercentOutput, percentOutput)
    }
}
