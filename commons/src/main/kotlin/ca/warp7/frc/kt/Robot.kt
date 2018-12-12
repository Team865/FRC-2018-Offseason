package ca.warp7.frc.kt

import ca.warp7.action.IAction
import ca.warp7.frc.core.XboxControlsState
import ca.warp7.frc.next.ControlLoop
import ca.warp7.frc.next.InputSystem
import ca.warp7.frc.next.OutputSystem
import ca.warp7.frc.next.RobotStatic.*

@Suppress("unused")
object Robot {
    fun setState(toOutput: Pair<IAction, OutputSystem>, lockOutput: Boolean = false) {
        runAction(toOutput.second, toOutput.first)
    }

    fun clearState(outputSystem: OutputSystem) {
        runAction(outputSystem, null)
    }

    fun run(action: IAction) {
        runAction(action)
    }

    fun initAuto(mode: IAction.Mode?, timeout: Double = 15.0) {
        initAutonomousMode(mode, timeout)
    }

    fun initTeleop(controlLoop: ControlLoop) {
        initTeleopControls(controlLoop)
    }

    fun initTest(controlLoop: ControlLoop) {
        initTestControls(controlLoop)
    }

    fun init(inputSystems: Array<InputSystem>, outputSystems: Array<OutputSystem>) {
        setInputSystems(*inputSystems)
        setOutputSystems(*outputSystems)
    }

    fun disable() {
        disableOutputs()
    }

    fun limit(value: Double, lim: Double): Double {
        val absLim = Math.abs(lim)
        return Math.max(-1 * absLim, Math.min(value, absLim))
    }

    fun getController(port: Int, isActive: Boolean): XboxControlsState {
        return getXboxController(port, isActive)
    }
}