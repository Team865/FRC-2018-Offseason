package ca.warp7.frc2018.v5.states.drive

import ca.warp7.action.IAction
import ca.warp7.frc2018.v5.constants.DriveConstants.kAxisDeadband
import ca.warp7.frc2018.v5.subsystems.Drive

object CheesyDrive : IAction {

    private val calculator = ca.warp7.frc.CheesyDrive(this::setPercent)

    init {
        calculator.disableInternalDeadband()
    }

    private var leftPercent = 0.0
    private var rightPercent = 0.0

    var wheel = 0.0
    var throttle = 0.0
    var quickTurn = false
    var solenoidOnForShifter = false

    private fun linearScaleDeadband(n: Double) = if (Math.abs(n) < kAxisDeadband) 0.0
    else (n - Math.copySign(kAxisDeadband, n)) / (1 - kAxisDeadband)

    @Synchronized
    private fun setPercent(left: Double, right: Double) {
        leftPercent = left
        rightPercent = right
    }

    @Synchronized
    override fun start() = Unit

    @Synchronized
    override fun shouldFinish(): Boolean {
        return false
    }

    @Synchronized
    override fun update() {
        calculator.cheesyDrive(linearScaleDeadband(wheel), linearScaleDeadband(throttle), quickTurn)
        Drive.leftPercentOutput = leftPercent
        Drive.rightPercentOutput = rightPercent
        Drive.solenoidOnForShifter = solenoidOnForShifter
    }
}