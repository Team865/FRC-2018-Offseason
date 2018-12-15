@file:Suppress("unused")

package ca.warp7.frckt

import ca.warp7.action.IAction
import ca.warp7.frc.next.ControlLoop
import ca.warp7.frc.next.InputSystem
import ca.warp7.frc.next.OutputSystem
import ca.warp7.frc.next.RobotController
import ca.warp7.frc.next.RobotRuntime.ROBOT_RUNTIME

fun initRobotSystems(loopsPerSecond: Int = 50,
                     inputSystems: Array<InputSystem>,
                     outputSystems: Array<OutputSystem>) {
    ROBOT_RUNTIME.setInputSystems(*inputSystems)
    ROBOT_RUNTIME.setOutputSystems(*outputSystems)
    ROBOT_RUNTIME.start(loopsPerSecond)
}

fun runRobotAuto(mode: IAction.Mode, updatesPerSecond: Int = 20, timeout: Double = 15.0) {
    ROBOT_RUNTIME.initAutonomousMode(mode, 1.0 / updatesPerSecond, timeout)
}

fun startRobotControls(controlLoop: ControlLoop) = ROBOT_RUNTIME.initControls(controlLoop)
fun disableRobot() = ROBOT_RUNTIME.disableOutputs()
fun setIdleState(toOutput: () -> OutputSystem) = ROBOT_RUNTIME.setState(toOutput.invoke(), null)
fun warning(message: String) = println("Warning $message")
fun setState(toOutput: () -> Pair<IAction, OutputSystem>) {
    val (first, second) = toOutput.invoke()
    ROBOT_RUNTIME.setState(second, first)
}

fun robotController(port: Int, isActive: Boolean): RobotController {
    return ROBOT_RUNTIME.getController(port, isActive)
}

fun limit(value: Double, lim: Double): Double {
    val absLim = Math.abs(lim)
    return Math.max(-1 * absLim, Math.min(value, absLim))
}