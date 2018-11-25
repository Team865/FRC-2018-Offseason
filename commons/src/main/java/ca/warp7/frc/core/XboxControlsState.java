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

    private static int u(int old, boolean _new) {
        return _new ?
                old == IControls.Pressed || old == IControls.HeldDown ? IControls.HeldDown : IControls.Pressed :
                old == IControls.Released || old == IControls.KeptUp ? IControls.KeptUp : IControls.Released;
    }

    static void collect(XboxControlsState s, XboxController c) {
        int POV = c.getPOV();
        s.LeftTriggerAxis = c.getTriggerAxis(kLeft);
        s.RightTriggerAxis = c.getTriggerAxis(kRight);
        s.LeftXAxis = c.getX(kLeft);
        s.LeftYAxis = c.getY(kLeft);
        s.RightXAxis = c.getX(kRight);
        s.RightYAxis = c.getY(kRight);
        s.AButton = u(s.AButton, c.getAButton());
        s.BButton = u(s.BButton, c.getBButton());
        s.XButton = u(s.XButton, c.getXButton());
        s.YButton = u(s.YButton, c.getYButton());
        s.LeftBumper = u(s.LeftBumper, c.getBumper(kLeft));
        s.RightBumper = u(s.RightBumper, c.getBumper(kRight));
        s.LeftTrigger = u(s.LeftTrigger, s.LeftTriggerAxis > kTriggerDeadBand);
        s.RightTrigger = u(s.RightTrigger, s.RightTriggerAxis > kTriggerDeadBand);
        s.LeftStickButton = u(s.LeftStickButton, c.getStickButton(kLeft));
        s.RightStickButton = u(s.RightStickButton, c.getStickButton(kRight));
        s.StartButton = u(s.StartButton, c.getStartButton());
        s.BackButton = u(s.BackButton, c.getBackButton());
        s.UpDPad = u(s.UpDPad, POV == kUpPOV);
        s.RightDPad = u(s.RightDPad, POV == kRightPOV);
        s.DownDPad = u(s.DownDPad, POV == kDownPOV);
        s.LeftDPad = u(s.LeftDPad, POV == kLeftPOV);
    }

    static void reset(XboxControlsState s) {
        s.AButton = IControls.KeptUp;
        s.BButton = IControls.KeptUp;
        s.XButton = IControls.KeptUp;
        s.YButton = IControls.KeptUp;
        s.LeftBumper = IControls.KeptUp;
        s.RightBumper = IControls.KeptUp;
        s.LeftTrigger = IControls.KeptUp;
        s.RightTrigger = IControls.KeptUp;
        s.LeftStickButton = IControls.KeptUp;
        s.RightStickButton = IControls.KeptUp;
        s.StartButton = IControls.KeptUp;
        s.BackButton = IControls.KeptUp;
        s.UpDPad = IControls.KeptUp;
        s.RightDPad = IControls.KeptUp;
        s.DownDPad = IControls.KeptUp;
        s.LeftDPad = IControls.KeptUp;
        s.LeftTriggerAxis = 0;
        s.RightTriggerAxis = 0;
        s.LeftXAxis = 0;
        s.LeftYAxis = 0;
        s.RightXAxis = 0;
        s.RightYAxis = 0;
    }

    static class Pair {
        private final XboxControlsState state;
        private XboxController controller;
        private int port;
        private boolean active;

        Pair(int port) {
            this.port = port;
            this.state = new XboxControlsState();
            if (port >= 0 && port < 6) {
                active = true;
                this.controller = new XboxController(port);
            } else active = false;
        }

        public XboxControlsState getState() {
            return state;
        }

        public XboxController getController() {
            return controller;
        }

        public int getPort() {
            return port;
        }

        public boolean isActive() {
            return active;
        }
    }
}