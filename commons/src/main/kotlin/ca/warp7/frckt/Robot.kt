@file:Suppress("unused")

package ca.warp7.frckt

import ca.warp7.action.IAction
import ca.warp7.action.impl.ActionMode
import ca.warp7.frc.ControlLoop
import ca.warp7.frc.Input
import ca.warp7.frc.RobotController
import ca.warp7.frc.RobotRuntime.ROBOT_RUNTIME

fun registerInput(input: Input) = ROBOT_RUNTIME.registerInput(input)
fun startRobot(loopsPerSecond: Int = 50) = ROBOT_RUNTIME.start(loopsPerSecond)
fun startRobotControls(controlLoop: ControlLoop) = ROBOT_RUNTIME.initControls(controlLoop)
fun disableRobot() = ROBOT_RUNTIME.disableOutputs()
fun warning(message: String) = println("WARNING $message")
fun robotController(port: Int, active: Boolean): RobotController = ROBOT_RUNTIME.getController(port, active)
fun limit(value: Double, lim: Double): Double = Math.max(-1 * Math.abs(lim), Math.min(value, Math.abs(lim)))
fun action(factory: IAction.API.() -> IAction): IAction = factory.invoke(ActionFactory())
fun mode(factory: IAction.API.() -> IAction): () -> IAction = { action(factory) }
fun runRobotAuto(mode: () -> IAction, updatesPerSecond: Int = 20, timeout: Double = 15.0) =
        ROBOT_RUNTIME.initAutonomousMode(mode, 1.0 / updatesPerSecond, timeout)

private val queueFactory = object : ActionMode() {
    override fun getAction(): IAction? = null
}::head

private class ActionFactory : IAction.HeadClass() {
    override fun head(): IAction.API = queueFactory.invoke()
}