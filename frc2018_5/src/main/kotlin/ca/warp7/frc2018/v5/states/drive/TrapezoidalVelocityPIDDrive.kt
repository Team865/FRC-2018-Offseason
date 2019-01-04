package ca.warp7.frc2018.v5.states.drive

import ca.warp7.action.IAction
import ca.warp7.frc2018.v5.constants.DriveConstants.kMaximumAcceleration
import ca.warp7.frc2018.v5.constants.DriveConstants.kMaximumVelocity
import ca.warp7.frc2018.v5.inputs.DriveInput
import com.stormbots.MiniPID
import edu.wpi.first.wpilibj.Timer
import kotlin.math.pow
import kotlin.math.sqrt

object TrapezoidalVelocityPIDDrive: IAction {

    private val linearPID = MiniPID(0.0, 0.0, 0.0)
    private var targetVelocity = 0.0
    var velocityOutput = 0.0
    private var reset = false

    // from Trapezoidal Velocity Drive
    private var currentTargetVelocity = 0.0
    private var startTime = 0.0
    private var timeSinceStart = 0.0
    private var startAndEndVelocity = 0.0
    private var targetLinearChange = 0.0
    private var maxAccel = 0.0
    private var isTriange = false
    private var totalTimeEstimate = 0.0
    private var timeUntilMaxVelocityReachedEstimate = 0.0
    private var maxReachedVelocity = 0.0


    private var running = false


    fun setSetpoint(velocity: Double) {
        if (targetVelocity != velocity){
            reset = true
            targetVelocity = velocity
        }
    }

    override fun start() {
        reset = false

        // from Trapezoidal Velocity Drive
        running = true
        currentTargetVelocity = (DriveInput.leftRate + DriveInput.rightRate) / 2
        startTime = Timer.getFPGATimestamp()
        timeSinceStart = 0.0
    }

    // from Trapezoidal Velocity Drive
    fun setTarget(targetx: Double, targetStartAndEndVelocity: Double, maxAcceleration: Double = kMaximumAcceleration){
        targetLinearChange = targetx
        startAndEndVelocity = targetStartAndEndVelocity
        maxAccel = maxAcceleration
        val maxPhysicalPotentialVelocity = kMaximumVelocity
        generateTrajectory(maxPhysicalPotentialVelocity)
    }

    // from Trapezoidal Velocity Drive
    fun generateTrajectory(maxPhysicalVelocity: Double){
        val linearChangeAtMaxTheoreticalVelocity = targetLinearChange / 2
        val maximumTheorecticallyReachableVelocity = sqrt((startAndEndVelocity.pow(2)) + 2 * maxAccel * linearChangeAtMaxTheoreticalVelocity)
        if (maxPhysicalVelocity >= maximumTheorecticallyReachableVelocity){
            isTriange = true
            maxReachedVelocity = maximumTheorecticallyReachableVelocity
        }
        else {
            isTriange = false
            maxReachedVelocity = maxPhysicalVelocity
        }

        timeUntilMaxVelocityReachedEstimate = (maxReachedVelocity - startAndEndVelocity) / maxAccel

        if (isTriange){
            totalTimeEstimate = 2 * timeUntilMaxVelocityReachedEstimate
        }
        else{
            val dxtomaxV = (startAndEndVelocity + maxReachedVelocity) / 2 * timeUntilMaxVelocityReachedEstimate
            val dxatcruiseV = targetLinearChange - dxtomaxV * 2
            val dtatcruiseV = dxatcruiseV / maxReachedVelocity
            val tAcandDc = 2 * timeUntilMaxVelocityReachedEstimate
            totalTimeEstimate = dtatcruiseV + tAcandDc
        }
    }

    override fun update() {
        // from Trapezoidal Velocity Drive
        timeSinceStart = Timer.getFPGATimestamp() - startTime
        if (timeSinceStart > totalTimeEstimate){
            stop()
        }
        if (isTriange){
            if (timeSinceStart <= timeUntilMaxVelocityReachedEstimate){
                currentTargetVelocity = startAndEndVelocity + timeSinceStart * maxAccel
            }
            else{
                currentTargetVelocity = maxReachedVelocity - (timeSinceStart - timeUntilMaxVelocityReachedEstimate) * maxAccel
            }
        }
        else{
            if (timeSinceStart <= timeUntilMaxVelocityReachedEstimate){
                currentTargetVelocity = startAndEndVelocity + timeSinceStart * maxAccel
            }
            else if (timeSinceStart >= totalTimeEstimate - timeUntilMaxVelocityReachedEstimate){
                currentTargetVelocity = startAndEndVelocity + (totalTimeEstimate - timeSinceStart) * maxAccel
            }
            else{
                currentTargetVelocity = maxReachedVelocity
            }
        }

        // not
        if (reset) {
            linearPID.setSetpoint(this.targetVelocity)
        }
        velocityOutput = linearPID.getOutput(velocityOutput)
    }


}