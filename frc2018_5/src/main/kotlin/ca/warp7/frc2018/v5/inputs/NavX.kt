package ca.warp7.frc2018.v5.inputs

import ca.warp7.frc.Input
import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.wpilibj.SPI

object NavX : Input {
    private val ahrs = AHRS(SPI.Port.kMXP)

    var yaw = 0.0
    var calibrated = false

    @Synchronized
    override fun onMeasure(dt: Double) {
        if (!calibrated && !ahrs.isCalibrating) calibrated = true
        if (calibrated) yaw = Math.toRadians(ahrs.fusedHeading.toDouble())
    }
}