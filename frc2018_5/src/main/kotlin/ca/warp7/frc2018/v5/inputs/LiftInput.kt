package ca.warp7.frc2018.v5.inputs

import ca.warp7.frc.Input
import ca.warp7.frc2018.v5.constants.RobotPins.*
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.Encoder

object LiftInput : Input {
    private val encoder = Encoder(kLiftEncoderAPin, kLiftEncoderBPin)
    private val limitSwitch = DigitalInput(kLimitSwitchPin)

    var limitSwitchPressed = false
        private set
    var encoderTicks = 0.0
        private set
    var encoderRate = 0.0
        private set

    @Synchronized
    override fun onMeasure(dt: Double) {
        limitSwitchPressed = limitSwitch.get()
        encoderTicks = encoder.distance
        encoderRate = encoder.rate
    }
}