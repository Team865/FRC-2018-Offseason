package ca.warp7.frc2018_5.input

import ca.warp7.frc.next.InputSystem
import ca.warp7.frc2018_5.constants.RobotPins
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.Encoder

object LiftInputs : InputSystem {
    private val encoder: Encoder = Encoder(RobotPins.kLiftEncoderAPin, RobotPins.kLiftEncoderBPin)
    private val limitSwitch: DigitalInput = DigitalInput(RobotPins.kLimitSwitchPin)

    var limitSwitchPressed = false
    var encoderTicks = 0.0
    var encoderRate = 0.0

    override fun onMeasure() {
        limitSwitchPressed = limitSwitch.get()
        encoderTicks = encoder.distance
        encoderRate = encoder.rate
    }
}