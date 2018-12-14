package ca.warp7.frckt

import ca.warp7.action.IAction
import ca.warp7.frc.core.XboxControlsState
import ca.warp7.frc.next.ControlLoop
import ca.warp7.frc.next.InputSystem
import ca.warp7.frc.next.OutputSystem
import ca.warp7.frc.next.RobotRuntime.RUNTIME

fun initRobotSystems(loopsPerSecond: Int = 50,
                     inputSystems: Array<InputSystem>,
                     outputSystems: Array<OutputSystem>) {
    RUNTIME.setInputSystems(*inputSystems)
    RUNTIME.setOutputSystems(*outputSystems)
    RUNTIME.start(loopsPerSecond)
}

fun runRobotAuto(mode: IAction.Mode, timeout: Double = 15.0) = RUNTIME.initAutonomousMode(mode, timeout)
fun startRobotControls(controlLoop: ControlLoop) = RUNTIME.initControls(controlLoop)
fun disableRobot() = RUNTIME.disableOutputs()
fun setIdleState(toOutput: () -> OutputSystem) = RUNTIME.setState(toOutput.invoke(), null)
fun setState(toOutput: () -> Pair<IAction, OutputSystem>) {
    val (first, second) = toOutput.invoke()
    RUNTIME.setState(second, first)
}

fun robotController(port: Int, isActive: Boolean): XboxControlsState {
    return RUNTIME.getXboxController(port, isActive)
}

fun limit(value: Double, lim: Double): Double {
    val absLim = Math.abs(lim)
    return Math.max(-1 * absLim, Math.min(value, absLim))
}