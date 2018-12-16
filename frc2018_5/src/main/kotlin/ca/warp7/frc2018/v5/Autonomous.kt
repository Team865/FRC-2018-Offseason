package ca.warp7.frc2018.v5

import ca.warp7.frc2018.v5.actions.DriveForDistance
import ca.warp7.frc2018.v5.actions.DriveForTime
import ca.warp7.frc2018.v5.state.drive.OpenLoopDrive
import ca.warp7.frc2018.v5.subsystems.Drive
import ca.warp7.frckt.mode

@Suppress("unused")
object Autonomous {

    val baseline = {
        DriveForTime(0.5, 0.5, 2.0)
    }

    val baseline2 = {
        DriveForDistance(5.0)
    }

    val example = mode {
        async(
                baseline.invoke(),
                waitFor(5.0),
                await { it.elapsed > 6.0 },
                exec { Drive.state = OpenLoopDrive },
                iterate { print("hi") },
                broadcast("hi")
        )
    }
}