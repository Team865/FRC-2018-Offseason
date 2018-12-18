package ca.warp7.frc2018.v5

import ca.warp7.frc2018.v5.actions.DriveForAngle
import ca.warp7.frc2018.v5.actions.DriveForDistance
import ca.warp7.frc2018.v5.actions.DriveForTime
import ca.warp7.frc2018.v5.actions.SuperstructureCloseLoop
import ca.warp7.frc2018.v5.inputs.AutoInput
import ca.warp7.frc2018.v5.inputs.AutoInput.Selected.*
import ca.warp7.frc2018.v5.inputs.AutoInput.Side.Left
import ca.warp7.frc2018.v5.inputs.AutoInput.Side.Right
import ca.warp7.frc2018.v5.states.intake.KeepCube
import ca.warp7.frc2018.v5.states.intake.OuttakeCube
import ca.warp7.frc2018.v5.subsystems.Intake
import ca.warp7.frckt.autonomousMode
import ca.warp7.frckt.setState

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

    private val baseline = { DriveForTime(0.5, 0.5, 2.0) }

    private val leftNearScale = baseline
    private val leftFarScale = baseline
    private val rightNearScale = baseline
    private val rightFarScale = baseline
    private val leftMiddleSwitch = autonomousMode {
        asyncAll(
                setState(Intake, KeepCube),
                SuperstructureCloseLoop(liftHeight = 0.5, wristAngle = 0.0),
                queue(
                        DriveForDistance(distance = 1.5, stop = false),
                        DriveForAngle(angle = -52.0, stop = false),
                        DriveForDistance(distance = 1.35, stop = false),
                        DriveForAngle(angle = 80.0, stop = true)
                )
        ).setState(Intake, OuttakeCube).waitFor(0.5).async(
                setState(Intake, KeepCube),
                SuperstructureCloseLoop(liftHeight = 0.0, wristAngle = 0.0),
                DriveForDistance(-90.0, stop = false)
        )
    }

    private val rightMiddleSwitch = baseline
    private val leftSideSwitch = baseline
    private val rightSideSwitch = baseline
}