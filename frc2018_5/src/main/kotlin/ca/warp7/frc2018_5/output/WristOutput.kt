package ca.warp7.frc2018_5.output

import ca.warp7.frc.next.OutputSystem
import ca.warp7.frc2018_5.constants.RobotPins.kWristPin
import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.VictorSPX

object WristOutput : OutputSystem {
    private val wristMaster = VictorSPX(kWristPin)

    var percentOutput = 0.0

    @Synchronized
    override fun onDisabled() {
        wristMaster.set(ControlMode.Disabled, 0.0)
    }

    override fun onIdle() {
        onDisabled()
    }

    @Synchronized
    override fun onOutput() {
        wristMaster.set(ControlMode.PercentOutput, percentOutput)
    }
}
