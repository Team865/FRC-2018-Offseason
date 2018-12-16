package ca.warp7.frc2018.v5.input

import ca.warp7.frc.InputSystem
import ca.warp7.frc2018.v5.constants.DriveConstants.kEncoderTicksPerRevolution
import ca.warp7.frc2018.v5.constants.DriveConstants.kWheelRadius
import ca.warp7.frc2018.v5.constants.RobotPins.*
import edu.wpi.first.wpilibj.Encoder

object DriveInput : InputSystem {

    private val leftEncoder = Encoder(kDriveLeftEncoderA, kDriveLeftEncoderB, false)
    private val rightEncoder = Encoder(kDriveRightEncoderA, kDriveLeftEncoderB, true)

    var leftDistance = 0.0
    var rightDistance = 0.0
    var leftRate = 0.0
    var rightRate = 0.0
    var averageDistance = 0.0

    init {
        leftEncoder.distancePerPulse = (kWheelRadius * Math.PI) / kEncoderTicksPerRevolution * 0.0254
        rightEncoder.distancePerPulse = (kWheelRadius * Math.PI) / kEncoderTicksPerRevolution * 0.0254
    }

    @Synchronized
    override fun onMeasure(dt: Double) {
        leftDistance = leftEncoder.distance
        rightDistance = rightEncoder.distance
        averageDistance = (leftDistance + rightDistance) / 2.0
        leftRate = leftEncoder.rate
        rightRate = rightEncoder.rate
    }
}