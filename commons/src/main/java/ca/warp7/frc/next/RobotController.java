package ca.warp7.frc.next;

import ca.warp7.frc.core.IControls;
import edu.wpi.first.wpilibj.XboxController;

import static edu.wpi.first.wpilibj.GenericHID.Hand.kLeft;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kRight;

@SuppressWarnings({"unused"})
public class RobotController {

    private static final double kTriggerDeadBand = 0.5;
    private static final int kUpPOV = 0;
    private static final int kRightPOV = 90;
    private static final int kDownPOV = 180;
    private static final int kLeftPOV = 270;

    private int AButton;
    private int BButton;
    private int XButton;
    private int YButton;
    private int leftBumper;
    private int rightBumper;
    private int leftTrigger;
    private int rightTrigger;
    private int leftStickButton;
    private int rightStickButton;
    private int startButton;
    private int backButton;
    private int upDPad;
    private int rightDPad;
    private int downDPad;
    private int leftDPad;
    private double leftTriggerAxis;
    private double rightTriggerAxis;
    private double leftXAxis;
    private double leftYAxis;
    private double rightXAxis;
    private double rightYAxis;

    public int getAButton() {
        return AButton;
    }

    public int getBButton() {
        return BButton;
    }

    public int getXButton() {
        return XButton;
    }

    public int getYButton() {
        return YButton;
    }

    public int getLeftBumper() {
        return leftBumper;
    }

    public int getRightBumper() {
        return rightBumper;
    }

    public int getLeftTrigger() {
        return leftTrigger;
    }

    public int getRightTrigger() {
        return rightTrigger;
    }

    public int getLeftStickButton() {
        return leftStickButton;
    }

    public int getRightStickButton() {
        return rightStickButton;
    }

    public int getStartButton() {
        return startButton;
    }

    public int getBackButton() {
        return backButton;
    }

    public int getUpDPad() {
        return upDPad;
    }

    public int getRightDPad() {
        return rightDPad;
    }

    public int getDownDPad() {
        return downDPad;
    }

    public int getLeftDPad() {
        return leftDPad;
    }

    public double getLeftTriggerAxis() {
        return leftTriggerAxis;
    }

    public double getRightTriggerAxis() {
        return rightTriggerAxis;
    }

    public double getLeftXAxis() {
        return leftXAxis;
    }

    public double getLeftYAxis() {
        return leftYAxis;
    }

    public double getRightXAxis() {
        return rightXAxis;
    }

    public double getRightYAxis() {
        return rightYAxis;
    }

    private static int u(int old, boolean _new) {
        return _new ?
                old == IControls.Pressed || old == IControls.HeldDown ? IControls.HeldDown : IControls.Pressed :
                old == IControls.Released || old == IControls.KeptUp ? IControls.KeptUp : IControls.Released;
    }

    static void collect(RobotController s, XboxController c) {
        int POV = c.getPOV();
        s.leftTriggerAxis = c.getTriggerAxis(kLeft);
        s.rightTriggerAxis = c.getTriggerAxis(kRight);
        s.leftXAxis = c.getX(kLeft);
        s.leftYAxis = c.getY(kLeft);
        s.rightXAxis = c.getX(kRight);
        s.rightYAxis = c.getY(kRight);
        s.AButton = u(s.AButton, c.getAButton());
        s.BButton = u(s.BButton, c.getBButton());
        s.XButton = u(s.XButton, c.getXButton());
        s.YButton = u(s.YButton, c.getYButton());
        s.leftBumper = u(s.leftBumper, c.getBumper(kLeft));
        s.rightBumper = u(s.rightBumper, c.getBumper(kRight));
        s.leftTrigger = u(s.leftTrigger, s.leftTriggerAxis > kTriggerDeadBand);
        s.rightTrigger = u(s.rightTrigger, s.rightTriggerAxis > kTriggerDeadBand);
        s.leftStickButton = u(s.leftStickButton, c.getStickButton(kLeft));
        s.rightStickButton = u(s.rightStickButton, c.getStickButton(kRight));
        s.startButton = u(s.startButton, c.getStartButton());
        s.backButton = u(s.backButton, c.getBackButton());
        s.upDPad = u(s.upDPad, POV == kUpPOV);
        s.rightDPad = u(s.rightDPad, POV == kRightPOV);
        s.downDPad = u(s.downDPad, POV == kDownPOV);
        s.leftDPad = u(s.leftDPad, POV == kLeftPOV);
    }

    static void reset(RobotController s) {
        s.AButton = IControls.KeptUp;
        s.BButton = IControls.KeptUp;
        s.XButton = IControls.KeptUp;
        s.YButton = IControls.KeptUp;
        s.leftBumper = IControls.KeptUp;
        s.rightBumper = IControls.KeptUp;
        s.leftTrigger = IControls.KeptUp;
        s.rightTrigger = IControls.KeptUp;
        s.leftStickButton = IControls.KeptUp;
        s.rightStickButton = IControls.KeptUp;
        s.startButton = IControls.KeptUp;
        s.backButton = IControls.KeptUp;
        s.upDPad = IControls.KeptUp;
        s.rightDPad = IControls.KeptUp;
        s.downDPad = IControls.KeptUp;
        s.leftDPad = IControls.KeptUp;
        s.leftTriggerAxis = 0;
        s.rightTriggerAxis = 0;
        s.leftXAxis = 0;
        s.leftYAxis = 0;
        s.rightXAxis = 0;
        s.rightYAxis = 0;
    }

    static class Pair {
        private final RobotController state;
        private XboxController controller;
        private int port;
        private boolean active;

        Pair(int port) {
            this.port = port;
            this.state = new RobotController();
            if (port >= 0 && port < 6) {
                active = true;
                this.controller = new XboxController(port);
            } else active = false;
        }

        RobotController getState() {
            return state;
        }

        XboxController getController() {
            return controller;
        }

        int getPort() {
            return port;
        }

        boolean isActive() {
            return active;
        }
    }
}