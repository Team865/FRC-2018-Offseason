package ca.warp7.frc.kt

import ca.warp7.action.IAction
import ca.warp7.frc.core.XboxControlsState
import ca.warp7.frc.next.ControlLoop
import ca.warp7.frc.next.InputSystem
import ca.warp7.frc.next.OutputSystem
import ca.warp7.frc.next.Robot

@Suppress("unused")
object RobotKt {
    fun assign(actionToSystem: Pair<IAction, OutputSystem>, forceAssign: Boolean = true) {
        Robot.runAction(actionToSystem.second, actionToSystem.first)
    }

    fun run(action: IAction) {
        Robot.runAction(action)
    }

    fun initAuto(mode: IAction.Mode?, timeout: Double = 15.0) {
        Robot.initAutonomousMode(mode, timeout)
    }

    fun initTeleop(controlLoop: ControlLoop) {
        Robot.initTeleop(controlLoop)
    }

    fun initTest(controlLoop: ControlLoop) {
        Robot.initTest(controlLoop)
    }

    fun init(inputSystems: Array<InputSystem>, outputSystems: Array<OutputSystem>) {
        Robot.setInputSystems(*inputSystems)
        Robot.setOutputSystems(*outputSystems)
    }

    fun disable() {
        Robot.disable()
    }

    fun limit(value: Double, lim: Double): Double {
        val absLim = Math.abs(lim)
        return Math.max(-absLim, Math.min(value, absLim))
    }

    fun getController(port: Int, isActive: Boolean): XboxControlsState {
        return Robot.getController(port, isActive)
    }
}