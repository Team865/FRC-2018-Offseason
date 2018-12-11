package ca.warp7.frc2018_5

import ca.warp7.action.IAction
import ca.warp7.frc.next.Robot

object RobotKt {
    fun assign(actionToSystem: Pair<IAction, Robot.System>, forceAssign: Boolean = true) {
        Robot.runAction(actionToSystem.second, actionToSystem.first)
    }

    fun initAuto(mode: IAction.Mode?, timeout: Double = 15.0) {
        Robot.initAutonomousMode(mode, timeout)
    }


    fun initTeleop(controlLoop: Robot.ControlLoop) {
        Robot.initTeleop(controlLoop)
    }

    fun initTest(controlLoop: Robot.ControlLoop) {
        Robot.initTest(controlLoop)
    }


    fun init(inputSystems: Array<Robot.InputSystem>, outputSystems: Array<Robot.OutputSystem>) {
        Robot.initSystems(*inputSystems, *outputSystems)
    }
}