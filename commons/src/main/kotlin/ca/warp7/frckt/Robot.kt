@file:Suppress("unused")

package ca.warp7.frckt

import ca.warp7.action.IAction
import ca.warp7.frc.ControlLoop
import ca.warp7.frc.Input
import ca.warp7.frc.RobotController
import ca.warp7.frc.RobotRuntime.ROBOT_RUNTIME
import ca.warp7.frc.Subsystem

fun registerInput(input: Input) = ROBOT_RUNTIME.registerInput(input)
fun startRobot(loopsPerSecond: Int = 50) = ROBOT_RUNTIME.start(loopsPerSecond)
fun startRobotControls(controlLoop: ControlLoop) = ROBOT_RUNTIME.initControls(controlLoop)
fun disableRobot() = ROBOT_RUNTIME.disableOutputs()
fun setIdle(toSubsystem: () -> Subsystem) = ROBOT_RUNTIME.setState(null, toSubsystem.invoke())
fun warning(message: String) = println("WARNING $message")
fun robotController(port: Int, active: Boolean): RobotController = ROBOT_RUNTIME.getController(port, active)
fun limit(value: Double, lim: Double): Double = Math.max(-1 * Math.abs(lim), Math.min(value, Math.abs(lim)))
fun runRobotAuto(mode: IAction.Mode, updatesPerSecond: Int = 20, timeout: Double = 15.0) =
        ROBOT_RUNTIME.initAutonomousMode(mode, 1.0 / updatesPerSecond, timeout)

fun setState(toSubsystem: () -> Pair<IAction, Subsystem>) {
    val (first, second) = toSubsystem.invoke()
    ROBOT_RUNTIME.setState(first, second)
}