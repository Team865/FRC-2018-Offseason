package ca.warp7.frc2018.v5

import ca.warp7.frc2018.v5.actions.DriveForTime
import ca.warp7.frc2018.v5.inputs.AutoInput
import ca.warp7.frc2018.v5.inputs.AutoInput.Selected.*
import ca.warp7.frc2018.v5.inputs.AutoInput.Side.Left
import ca.warp7.frc2018.v5.inputs.AutoInput.Side.Right

@Suppress("unused")
object Autonomous {

    fun getMode() = when (AutoInput.selectedMode) {
        None -> baseline
        LeftScale -> when (AutoInput.scalePlates) {
            Left -> leftNearScale
            Right -> leftFarScale
        }
        MiddleSwitch -> when (AutoInput.switchPlates) {
            Left -> leftMiddleSwitch
            Right -> rightMiddleSwitch
        }
        RightScale -> when (AutoInput.scalePlates) {
            Left -> rightFarScale
            Right -> rightNearScale
        }
        LeftSwitch -> when (AutoInput.switchPlates) {
            Left -> leftSideSwitch
            Right -> baseline
        }
        RightSwitch -> when (AutoInput.switchPlates) {
            Left -> baseline
            Right -> rightSideSwitch
        }
    }

    private val baseline = {
        DriveForTime(0.5, 0.5, 2.0)
    }

    private val leftNearScale = baseline
    private val leftFarScale = baseline
    private val rightNearScale = baseline
    private val rightFarScale = baseline
    private val leftMiddleSwitch = baseline
    private val rightMiddleSwitch = baseline
    private val leftSideSwitch = baseline
    private val rightSideSwitch = baseline
}