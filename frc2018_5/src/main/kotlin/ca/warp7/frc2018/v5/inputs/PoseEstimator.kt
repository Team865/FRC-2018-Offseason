package ca.warp7.frc2018.v5.inputs

import ca.warp7.frc.Input

object PoseEstimator : Input {

    var predictedX = 0.0
        private set
    var predictedY = 0.0
        private set
    var heading = 0.0
        private set

    private var prevLeft = 0.0
    private var prevRight = 0.0

    override fun onMeasure(dt: Double) {
        val dLeft = DriveInput.leftDistance - prevLeft
        val dRight = DriveInput.rightDistance - prevRight
        val dAverage = (dLeft + dRight) / 2.0
        heading = NavX.yaw
        predictedX += Math.cos(heading) * dAverage
        predictedY += Math.sin(heading) * dAverage
        prevLeft = DriveInput.leftDistance
        prevRight = DriveInput.rightDistance
    }
}