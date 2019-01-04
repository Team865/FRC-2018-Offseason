package ca.warp7.frc2018.v5.states.drive

import ca.warp7.action.IAction
import ca.warp7.frc2018.v5.constants.DriveConstants.kMaximumAcceleration
import ca.warp7.frc2018.v5.constants.DriveConstants.kMaximumVelocity
import ca.warp7.frc2018.v5.inputs.DriveInput
import com.stormbots.MiniPID
import kotlin.math.pow
import kotlin.math.sqrt

object TrapezoidalVelocityDrive: IAction {
    private val mPID = MiniPID(0.0,0.0,0.0)
    private var currentTargetVelocity = 0.0
    private var timeSinceStart = 0.0
    private var startAndEndVelocity = 0.0
    private var targetLinearChange = 0.0
    private var maxAccel = 0.0
    private var isTriange = false
    private var totalTimeEstimate = 0.0
    private var timeUntilMaxVelocityReachedEstimate = 0.0
    private var maxReachedVelocity = 0.0


    private var running = false

    override fun start() {
        running = true
        currentTargetVelocity = (DriveInput.leftRate + DriveInput.rightRate) / 2
        timeSinceStart = 0.0

    }

    fun setTarget(targetx: Double, targetStartAndEndVelocity: Double, maxAcceleration: Double = kMaximumAcceleration){
        targetLinearChange = targetx
        startAndEndVelocity = targetStartAndEndVelocity
        maxAccel = maxAcceleration
        var maxPhysicalPotentialVelocity = kMaximumVelocity
        generateTrajectory(maxPhysicalPotentialVelocity)
    }
    fun generateTrajectory(maxPhysicalVelocity: Double){
        var linearChangeAtMaxTheoreticalVelocity = targetLinearChange / 2
        var maximumTheorecticallyReachableVelocity = sqrt((startAndEndVelocity.pow(2)) + 2 * maxAccel * linearChangeAtMaxTheoreticalVelocity)
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
            var dxtomaxV = (startAndEndVelocity + maxReachedVelocity) / 2 * timeUntilMaxVelocityReachedEstimate
            var dxatcruiseV = targetLinearChange - dxtomaxV * 2
            var dtatcruiseV = dxatcruiseV / maxReachedVelocity
            var tAcandDc = 2 * timeUntilMaxVelocityReachedEstimate
            totalTimeEstimate = dtatcruiseV + tAcandDc
        }
    }

    fun update(dt: Double){
        timeSinceStart += dt

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

    }
}