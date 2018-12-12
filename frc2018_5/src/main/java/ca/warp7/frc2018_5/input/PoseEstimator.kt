package ca.warp7.frc2018_5.input

import ca.warp7.frc.next.InputSystem

object PoseEstimator : InputSystem {

    var predictedX = 0.0
    var predictedY = 0.0

    private var prevLeft = 0.0
    private var prevRight = 0.0

    override fun onMeasure(dt: Double) {
        val dLeft = DriveInput.leftDistance - prevLeft
        val dRight = DriveInput.rightDistance - prevRight
        val dAverage = (dLeft + dRight) / 2.0
        predictedX += Math.cos(NavX.yaw) * dAverage
        predictedY += Math.sin(NavX.yaw) * dAverage
        prevLeft = DriveInput.leftDistance
        prevRight = DriveInput.rightDistance
    }
}