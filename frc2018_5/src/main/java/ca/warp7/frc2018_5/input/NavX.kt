package ca.warp7.frc2018_5.input

import ca.warp7.frc.next.Robot
import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.wpilibj.SPI

object NavX : Robot.InputSystem {

    private val mAHRS = AHRS(SPI.Port.kMXP)

    var yaw = 0.0

    override fun onMeasure() {
        yaw = Math.toRadians(mAHRS.fusedHeading.toDouble())
    }
}