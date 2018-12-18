package ca.warp7.frc2018.v5.states.drive

import ca.warp7.action.IAction
import ca.warp7.frc2018.v5.inputs.DriveInput
import ca.warp7.frc2018.v5.subsystems.Drive
import com.stormbots.MiniPID
import kotlin.math.abs

object AngularPIDDrive : IAction {

    private val linearPID = MiniPID(0.0, 0.0, 0.0)

    private var startDistance = 0.0
    private var targetLinearChange = 0.0
    private var reset = false

    fun setSetpoint(linear: Double) {
        if (targetLinearChange != linear) reset = true
        targetLinearChange = linear
    }

    override fun start() {
        reset = false
    }

    override fun update() {
        if (reset) {
            startDistance = DriveInput.averageDistance
            linearPID.setSetpoint(startDistance + targetLinearChange)
        }
        val linear = linearPID.getOutput(DriveInput.leftDistance)
        Drive.leftPercentOutput = linear
        Drive.rightPercentOutput = linear
    }

    private var ticks = 0

    override fun shouldFinish(): Boolean {
        if (abs(startDistance + targetLinearChange - DriveInput.averageDistance) > 1.0) {
            ticks++
        } else {
            ticks = 0
        }
        return ticks > 17
    }
}