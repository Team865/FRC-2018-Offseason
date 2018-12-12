package ca.warp7.frc2018_5.input

import ca.warp7.frc.next.InputSystem
import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.wpilibj.SPI

object NavX : InputSystem {
    private val mAHRS = AHRS(SPI.Port.kMXP)

    var yaw = 0.0
    var calibrated = false

    override fun onMeasure() {
        if (!calibrated && !mAHRS.isCalibrating) {
            calibrated = true
        }
        yaw = Math.toRadians(mAHRS.fusedHeading.toDouble())
    }
}