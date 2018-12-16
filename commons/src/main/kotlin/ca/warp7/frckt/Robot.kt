@file:Suppress("unused")

package ca.warp7.frckt

import ca.warp7.action.IAction
import ca.warp7.frc.ControlLoop
import ca.warp7.frc.Input
import ca.warp7.frc.Subsystem
import ca.warp7.frc.RobotController
import ca.warp7.frc.RobotRuntime.ROBOT_RUNTIME

fun setInputs(vararg inputs: Input) = ROBOT_RUNTIME.setInputs(*inputs)
fun startRobot(loopsPerSecond: Int = 50) = ROBOT_RUNTIME.start(loopsPerSecond)
fun startRobotControls(controlLoop: ControlLoop) = ROBOT_RUNTIME.initControls(controlLoop)
fun disableRobot() = ROBOT_RUNTIME.disableOutputs()
fun setIdleState(toOutput: () -> Subsystem) = ROBOT_RUNTIME.setState(null, toOutput.invoke())
fun warning(message: String) = println("Warning $message")
fun robotController(port: Int, isActive: Boolean): RobotController = ROBOT_RUNTIME.getController(port, isActive)
fun limit(value: Double, lim: Double): Double = Math.max(-1 * Math.abs(lim), Math.min(value, Math.abs(lim)))
fun runRobotAuto(mode: IAction.Mode, updatesPerSecond: Int = 20, timeout: Double = 15.0) =
        ROBOT_RUNTIME.initAutonomousMode(mode, 1.0 / updatesPerSecond, timeout)

fun setState(toOutput: () -> Pair<IAction, Subsystem>) {
    val (first, second) = toOutput.invoke()
    ROBOT_RUNTIME.setState(first, second)
}