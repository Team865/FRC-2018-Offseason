package ca.warp7.frc2018.v5.inputs

import ca.warp7.frc.Input
import ca.warp7.frc2018.v5.constants.DriveConstants.kEncoderTicksPerRevolution
import ca.warp7.frc2018.v5.constants.DriveConstants.kWheelRadius
import ca.warp7.frc2018.v5.constants.RobotPins.*
import edu.wpi.first.wpilibj.Encoder

object DriveInput : Input {

    private val leftEncoder = Encoder(kDriveLeftEncoderA, kDriveLeftEncoderB, false)
    private val rightEncoder = Encoder(kDriveRightEncoderA, kDriveLeftEncoderB, true)

    var leftDistance = 0.0
        private set
    var rightDistance = 0.0
        private set
    var leftRate = 0.0
        private set
    var rightRate = 0.0
        private set

    val averageDistance
        get() = (leftDistance + rightDistance) / 2.0

    init {
        leftEncoder.distancePerPulse = (kWheelRadius * Math.PI) / kEncoderTicksPerRevolution * 0.0254
        rightEncoder.distancePerPulse = (kWheelRadius * Math.PI) / kEncoderTicksPerRevolution * 0.0254
    }

    @Synchronized
    override fun onMeasure(dt: Double) {
        leftDistance = leftEncoder.distance
        rightDistance = rightEncoder.distance
        leftRate = leftEncoder.rate
        rightRate = rightEncoder.rate
    }
}