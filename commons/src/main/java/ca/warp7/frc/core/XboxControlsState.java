package ca.warp7.frc.core;

import edu.wpi.first.wpilibj.XboxController;

import static edu.wpi.first.wpilibj.GenericHID.Hand.kLeft;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kRight;

@SuppressWarnings("WeakerAccess")
public class XboxControlsState {

    private static final double kTriggerDeadBand = 0.5;
    private static final int kUpPOV = 0;
    private static final int kRightPOV = 90;
    private static final int kDownPOV = 180;
    private static final int kLeftPOV = 270;

    public int AButton;
    public int BButton;
    public int XButton;
    public int YButton;
    public int LeftBumper;
    public int RightBumper;
    public int LeftTrigger;
    public int RightTrigger;
    public int LeftStickButton;
    public int RightStickButton;
    public int StartButton;
    public int BackButton;
    public int UpDPad;
    public int RightDPad;
    public int DownDPad;
    public int LeftDPad;
    public double LeftTriggerAxis;
    public double RightTriggerAxis;
    public double LeftXAxis;
    public double LeftYAxis;
    public double RightXAxis;
    public double RightYAxis;

    private static int u0(int oldState, boolean newState) {
        return newState ? ((oldState == IControls.Pressed || oldState == IControls.HeldDown) ? IControls.HeldDown : IControls.Pressed) :
                ((oldState == IControls.Released || oldState == IControls.KeptUp) ? IControls.KeptUp : IControls.Released);
    }

    static void collect(XboxControlsState S, XboxController C) {
        int POV = C.getPOV();
        S.LeftTriggerAxis = C.getTriggerAxis(kLeft);
        S.RightTriggerAxis = C.getTriggerAxis(kRight);
        S.LeftXAxis = C.getX(kLeft);
        S.LeftYAxis = C.getY(kLeft);
        S.RightXAxis = C.getX(kRight);
        S.RightYAxis = C.getY(kRight);
        S.AButton = u0(S.AButton, C.getAButton());
        S.BButton = u0(S.BButton, C.getBButton());
        S.XButton = u0(S.XButton, C.getXButton());
        S.YButton = u0(S.YButton, C.getYButton());
        S.LeftBumper = u0(S.LeftBumper, C.getBumper(kLeft));
        S.RightBumper = u0(S.RightBumper, C.getBumper(kRight));
        S.LeftTrigger = u0(S.LeftTrigger, S.LeftTriggerAxis > kTriggerDeadBand);
        S.RightTrigger = u0(S.RightTrigger, S.RightTriggerAxis > kTriggerDeadBand);
        S.LeftStickButton = u0(S.LeftStickButton, C.getStickButton(kLeft));
        S.RightStickButton = u0(S.RightStickButton, C.getStickButton(kRight));
        S.StartButton = u0(S.StartButton, C.getStartButton());
        S.BackButton = u0(S.BackButton, C.getBackButton());
        S.UpDPad = u0(S.UpDPad, POV == kUpPOV);
        S.RightDPad = u0(S.RightDPad, POV == kRightPOV);
        S.DownDPad = u0(S.DownDPad, POV == kDownPOV);
        S.LeftDPad = u0(S.LeftDPad, POV == kLeftPOV);
    }

    static void reset(XboxControlsState S) {
        S.AButton = IControls.KeptUp;
        S.BButton = IControls.KeptUp;
        S.XButton = IControls.KeptUp;
        S.YButton = IControls.KeptUp;
        S.LeftBumper = IControls.KeptUp;
        S.RightBumper = IControls.KeptUp;
        S.LeftTrigger = IControls.KeptUp;
        S.RightTrigger = IControls.KeptUp;
        S.LeftStickButton = IControls.KeptUp;
        S.RightStickButton = IControls.KeptUp;
        S.StartButton = IControls.KeptUp;
        S.BackButton = IControls.KeptUp;
        S.UpDPad = IControls.KeptUp;
        S.RightDPad = IControls.KeptUp;
        S.DownDPad = IControls.KeptUp;
        S.LeftDPad = IControls.KeptUp;
        S.LeftTriggerAxis = 0;
        S.RightTriggerAxis = 0;
        S.LeftXAxis = 0;
        S.LeftYAxis = 0;
        S.RightXAxis = 0;
        S.RightYAxis = 0;
    }
}