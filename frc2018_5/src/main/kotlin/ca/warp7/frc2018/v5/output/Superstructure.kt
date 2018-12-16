package ca.warp7.frc2018.v5.output

import ca.warp7.frc.OutputSystem
import edu.wpi.first.wpilibj.Compressor

object Superstructure : OutputSystem {
    private val compressor get() = Compressor(0)

    var compressorOn = false

    override fun onDisabled() {
        compressor.closedLoopControl = false
    }

    override fun onIdle() {
        onDisabled()
    }

    override fun onOutput() {
        compressor.closedLoopControl = compressorOn
    }
}