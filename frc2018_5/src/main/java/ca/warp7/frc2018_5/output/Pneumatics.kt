package ca.warp7.frc2018_5.output

import ca.warp7.frc.next.OutputSystem
import edu.wpi.first.wpilibj.Compressor

object Pneumatics : OutputSystem {
    private val compressor get() = Compressor(0)

    var compressorOn = false

    override fun onDisabled() {
        compressor.closedLoopControl = false
    }

    override fun onOutput() {
        compressor.closedLoopControl = compressorOn
    }
}