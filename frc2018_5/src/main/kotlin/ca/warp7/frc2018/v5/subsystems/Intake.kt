package ca.warp7.frc2018.v5.subsystems

import ca.warp7.frc.Subsystem
import ca.warp7.frc2018.v5.constants.RobotPins.*
import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.VictorSPX
import edu.wpi.first.wpilibj.Solenoid

object Intake : Subsystem {
    private val leftMaster = VictorSPX(kIntakeLeftPin)
    private val rightMaster = VictorSPX(kIntakeRightPin)
    private val piston = Solenoid(kIntakePistonSolenoidPin)

    var percentOutput = 0.0
    var pistonOn = false

    @Synchronized
    override fun onDisabled() {
        leftMaster.set(ControlMode.Disabled, 0.0)
        rightMaster.set(ControlMode.Disabled, 0.0)
    }

    override fun onIdle() {
        percentOutput = 0.2
    }

    @Synchronized
    override fun onOutput() {
        piston.set(pistonOn)
        leftMaster.set(ControlMode.PercentOutput, percentOutput)
        rightMaster.set(ControlMode.PercentOutput, percentOutput)
    }
}
