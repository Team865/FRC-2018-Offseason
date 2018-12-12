package ca.warp7.frc2018_5.input

import ca.warp7.frc.next.InputSystem
import ca.warp7.frc2018_5.constants.DriveConstants.kEncoderTicksPerRevolution
import ca.warp7.frc2018_5.constants.DriveConstants.kWheelRadius
import ca.warp7.frc2018_5.constants.RobotPins.*
import edu.wpi.first.wpilibj.Encoder

object DriveEncoders : InputSystem {

    private val leftEncoder: Encoder = Encoder(kDriveLeftEncoderA, kDriveLeftEncoderB, false)
    private val rightEncoder: Encoder = Encoder(kDriveRightEncoderA, kDriveLeftEncoderB, true)

    var leftDistance = 0.0
    var rightDistance = 0.0
    var leftRate = 0.0
    var rightRate = 0.0

    init {
        leftEncoder.distancePerPulse = (kWheelRadius * Math.PI) / kEncoderTicksPerRevolution * 0.0254
        rightEncoder.distancePerPulse = (kWheelRadius * Math.PI) / kEncoderTicksPerRevolution * 0.0254
    }

    @Synchronized
    override fun onMeasure() {
        leftDistance = leftEncoder.distance
        rightDistance = rightEncoder.distance
        leftRate = leftEncoder.rate
        rightRate = rightEncoder.rate
    }
}