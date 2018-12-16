package ca.warp7.frc2018.v5.input

import ca.warp7.frc.InputSystem
import ca.warp7.frc2018.v5.constants.RobotPins.*
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.Encoder

object LiftInput : InputSystem {
    private val encoder = Encoder(kLiftEncoderAPin, kLiftEncoderBPin)
    private val limitSwitch = DigitalInput(kLimitSwitchPin)

    var limitSwitchPressed = false
    var encoderTicks = 0.0
    var encoderRate = 0.0

    @Synchronized
    override fun onMeasure(dt: Double) {
        limitSwitchPressed = limitSwitch.get()
        encoderTicks = encoder.distance
        encoderRate = encoder.rate
    }
}