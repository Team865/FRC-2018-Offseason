package ca.warp7.frckt

import ca.warp7.action.IAction
import ca.warp7.frc.core.XboxControlsState
import ca.warp7.frc.next.ControlLoop
import ca.warp7.frc.next.InputSystem
import ca.warp7.frc.next.OutputSystem
import ca.warp7.frc.next.RobotStatic

/**
 * Kotlin Robot interface
 */

fun initRobotSystems(loopsPerSecond: Int = 50, inputSystems: Array<InputSystem>, outputSystems: Array<OutputSystem>) {
    RobotStatic.setInputSystems(*inputSystems)
    RobotStatic.setOutputSystems(*outputSystems)
}

fun setIdleState(toOutput: () -> OutputSystem) {
    RobotStatic.runAction(toOutput.invoke(), null)
}

fun runRobotAuto(mode: IAction.Mode?, timeout: Double = 15.0) = RobotStatic.initAutonomousMode(mode, timeout)

fun startRobotTeleop(controlLoop: ControlLoop) = RobotStatic.initTeleopControls(controlLoop)

fun startRobotTest(controlLoop: ControlLoop) = RobotStatic.initTestControls(controlLoop)

fun disableRobot() = RobotStatic.disableOutputs()

fun robotController(port: Int, isActive: Boolean): XboxControlsState {
    return RobotStatic.getXboxController(port, isActive)
}

fun setState(toOutput: () -> Pair<IAction, OutputSystem>) {
    val pair = toOutput.invoke()
    RobotStatic.runAction(pair.second, pair.first)
}

fun limit(value: Double, lim: Double): Double {
    val absLim = Math.abs(lim)
    return Math.max(-1 * absLim, Math.min(value, absLim))
}