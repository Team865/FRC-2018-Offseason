package ca.warp7.frc2018_5.state.drive

import ca.warp7.action.IAction
import ca.warp7.frc2018_5.input.DriveInput
import ca.warp7.frc2018_5.input.NavX
import ca.warp7.frc2018_5.output.DriveOutput
import com.stormbots.MiniPID
import kotlin.math.PI
import kotlin.math.abs

object PIDDrive : IAction {

    private val linearPID = MiniPID(0.0, 0.0, 0.0)
    private val angularPID = MiniPID(0.0, 0.0, 0.0)

    private var startDistance = 0.0
    private var targetLinearChange = 0.0
    private var startYaw = 0.0
    private var targetYawChange = 0.0
    private var reset = false
    private var yawSetpoint = 0.0

    fun setTargets(linear: Double, angular: Double) {
        if (targetLinearChange != linear || targetYawChange != angular) {
            reset = true
        }
        targetLinearChange = linear
        targetYawChange = angular
    }

    override fun start() {
        reset = false
    }

    override fun update() {
        if (reset) {
            startDistance = DriveInput.averageDistance
            startYaw = NavX.yaw
            linearPID.setSetpoint(startDistance + targetLinearChange)
            yawSetpoint = (startYaw + targetYawChange) % (2 * PI)
            angularPID.setSetpoint(yawSetpoint)
        }
        val linear = linearPID.getOutput(DriveInput.leftDistance)
        val currentAngle = NavX.yaw
        val diff = abs(yawSetpoint - currentAngle)
        val diff1 = abs(yawSetpoint - (currentAngle - 2 * PI))
        val diff2 = abs(yawSetpoint - (currentAngle + 2 * PI))
        var angularValue = 0.0
        if (diff < diff1 && diff < diff2) {
            angularValue = currentAngle
        } else if (diff1 < diff && diff1 < diff2) {
            angularValue = currentAngle - 2 * PI
        } else if (diff2 < diff && diff2 < diff1) {
            angularValue = currentAngle + 2 * PI
        }
        val angular = angularPID.getOutput(angularValue)
        DriveOutput.leftPercentOutput = linear - angular
        DriveOutput.rightPercentOutput = linear + angular
    }

    private var ticks = 0

    override fun shouldFinish(): Boolean {
        if (abs(startDistance + targetYawChange - DriveInput.averageDistance) > 1.0) {
            ticks++
        } else {
            ticks = 0
        }
        return ticks > 17
    }
}