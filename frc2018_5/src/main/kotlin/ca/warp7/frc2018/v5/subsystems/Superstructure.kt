package ca.warp7.frc2018.v5.subsystems

import ca.warp7.frc.Subsystem
import edu.wpi.first.wpilibj.Compressor

object Superstructure : Subsystem() {
    private val compressor get() = Compressor(0)

    var compressorOn = false

    override fun onDisabled() {
        compressor.stop()
    }

    override fun onIdle() {
        onDisabled()
    }

    override fun onOutput() {
        if (compressorOn) compressor.start()
        else compressor.stop()
    }
}