package ca.warp7.frc2018.v5.inputs


import ca.warp7.frc.Input
import ca.warp7.frc2018.v5.inputs.AutoInput.Selected.*
import edu.wpi.first.wpilibj.AnalogInput


object AutoInput : Input {

    private val a0 = AnalogInput(0)
    private val a1 = AnalogInput(1)
    private val a2 = AnalogInput(2)
    private val a3 = AnalogInput(3)
    private val a4 = AnalogInput(4)
    private val a5 = AnalogInput(5)

    enum class Selected {
        None,
        LeftScale,
        MiddleSwitch,
        RightScale,
        LeftSwitch,
        RightSwitch
    }

    enum class Side {
        Left, Right
    }

    var selectedMode = None;
    var scalePlates = Side.Left
    var switchPlates = Side.Left

    override fun onMeasure(dt: Double) {
        var voltage = 0.0
        selectedMode = None
        if (a0.averageVoltage > voltage) {
            selectedMode = RightScale
            voltage = a0.averageVoltage
        }
        if (a1.averageVoltage > voltage) {
            selectedMode = None
            voltage = a1.averageVoltage
        }
        if (a2.averageVoltage > voltage) {
            selectedMode = MiddleSwitch
            voltage = a2.averageVoltage
        }
        if (a3.averageVoltage > voltage) {
            selectedMode = LeftScale
            voltage = a3.averageVoltage
        }
        if (a4.averageVoltage > voltage) {
            selectedMode = LeftSwitch
            voltage = a4.averageVoltage
        }
        if (a5.averageVoltage > voltage) {
            selectedMode = RightSwitch
            voltage = a5.averageVoltage
        }
    }
}

