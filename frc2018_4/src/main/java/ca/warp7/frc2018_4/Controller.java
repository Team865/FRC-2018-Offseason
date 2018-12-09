package ca.warp7.frc2018_4;

import ca.warp7.frc.core.IControls;
import ca.warp7.frc.core.RobotLoader;
import ca.warp7.frc.core.XboxControlsState;

import static ca.warp7.frc2018_4.Components.*;
import static ca.warp7.frc2018_4.constants.LiftConstants.kLiftInputExponentialScaleValue;
import static ca.warp7.frc2018_4.constants.LiftConstants.kSetPoint1;
import static ca.warp7.frc2018_4.constants.LiftConstants.kSetPoint2;

public class Controller implements IControls {
    private XboxControlsState Driver = RobotLoader.createXboxController(0, true);
    private XboxControlsState Operator = RobotLoader.createXboxController(1, true);

    @Override
    public void mainPeriodic() {
        // Climbing
        if (Operator.BButton == HeldDown){
            climber.mInputState.mDemandedMotorSpeed = Operator.LeftYAxis;
        }

        // Superstructure

        // Lift
        if (Operator.RightTrigger == HeldDown && (Operator.XButton != HeldDown)){
            lift.mInputState.mDemandedHeight = kSetPoint1;
        }
        else if (Operator.XButton == HeldDown && (Operator.RightTrigger != HeldDown)){
            lift.mInputState.mDemandedHeight = kSetPoint2;
        }
        else {
            if (Operator.AButton == HeldDown) lift.mInputState.mDemandedHeight = Math.pow(Operator.LeftYAxis, kLiftInputExponentialScaleValue);
        }

        // Wrist
        if (Operator.XButton == HeldDown){
            wrist.mInputState.mShouldSlowFall = true;
            wrist.mInputState.mShouldUseTargetAngle = false;
        }
        else if (Operator.LeftBumper == HeldDown) {
            wrist.mInputState.mShouldSlowFall = false;
            wrist.mInputState.mShouldUseTargetAngle = false; //TODO once this is finished change to true
            wrist.mInputState.mDemandedWristAngle = Operator.RightYAxis;
        }
        else {
            wrist.mInputState.mShouldSlowFall = false;
            wrist.mInputState.mShouldUseTargetAngle = false;
            wrist.mInputState.mDemandedWristAngularVelocity = Operator.RightYAxis;
        }

        // Driving
        drive.setShouldSolenoidBeOnForShifter(Driver.RightBumper != HeldDown);
        drive.setReversed(Driver.RightStickButton == HeldDown);
        if (Driver.BButton != HeldDown) {
            drive.cheesyDrive(Driver.RightXAxis, Driver.LeftYAxis, Driver.LeftBumper == HeldDown);
        }
    }
}
