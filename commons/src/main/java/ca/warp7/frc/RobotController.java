package ca.warp7.frc;

import edu.wpi.first.networktables.NetworkTable;
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
                old == ControlLoop.Pressed || old == ControlLoop.HeldDown ? ControlLoop.HeldDown : ControlLoop.Pressed :
                old == ControlLoop.Released || old == ControlLoop.KeptUp ? ControlLoop.KeptUp : ControlLoop.Released;
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

    private static String stateString(int s) {
        switch (s) {
            case ControlLoop.KeptUp:
                return "KeptUp";
            case ControlLoop.HeldDown:
                return "HeldDown";
            case ControlLoop.Pressed:
                return "Pressed";
            case ControlLoop.Released:
                return "Released";
        }
        return "None";
    }

    static void outputTelemetry(RobotController s, NetworkTable table, int port) {
        NetworkTable t = table.getSubTable(String.format("RobotController [%d]", port));
        t.getEntry("AButton").setString(stateString(s.AButton));
        t.getEntry("BButton").setString(stateString(s.BButton));
        t.getEntry("XButton").setString(stateString(s.XButton));
        t.getEntry("YButton").setString(stateString(s.YButton));
        t.getEntry("leftBumper").setString(stateString(s.leftBumper));
        t.getEntry("rightBumper").setString(stateString(s.rightBumper));
        t.getEntry("leftTrigger").setString(stateString(s.leftTrigger));
        t.getEntry("rightTrigger").setString(stateString(s.rightTrigger));
        t.getEntry("leftStickButton").setString(stateString(s.leftStickButton));
        t.getEntry("rightStickButton").setString(stateString(s.rightStickButton));
        t.getEntry("startButton").setString(stateString(s.startButton));
        t.getEntry("backButton").setString(stateString(s.backButton));
        t.getEntry("leftTriggerAxis").setNumber(s.leftTriggerAxis);
        t.getEntry("rightTriggerAxis").setNumber(s.leftTriggerAxis);
        t.getEntry("leftXAxis").setNumber(s.leftTriggerAxis);
        t.getEntry("leftYAxis").setNumber(s.leftTriggerAxis);
        t.getEntry("rightXAxis").setNumber(s.leftTriggerAxis);
        t.getEntry("rightYAxis").setNumber(s.leftTriggerAxis);
    }

    static void reset(RobotController s) {
        s.AButton = ControlLoop.KeptUp;
        s.BButton = ControlLoop.KeptUp;
        s.XButton = ControlLoop.KeptUp;
        s.YButton = ControlLoop.KeptUp;
        s.leftBumper = ControlLoop.KeptUp;
        s.rightBumper = ControlLoop.KeptUp;
        s.leftTrigger = ControlLoop.KeptUp;
        s.rightTrigger = ControlLoop.KeptUp;
        s.leftStickButton = ControlLoop.KeptUp;
        s.rightStickButton = ControlLoop.KeptUp;
        s.startButton = ControlLoop.KeptUp;
        s.backButton = ControlLoop.KeptUp;
        s.upDPad = ControlLoop.KeptUp;
        s.rightDPad = ControlLoop.KeptUp;
        s.downDPad = ControlLoop.KeptUp;
        s.leftDPad = ControlLoop.KeptUp;
        s.leftTriggerAxis = 0;
        s.rightTriggerAxis = 0;
        s.leftXAxis = 0;
        s.leftYAxis = 0;
        s.rightXAxis = 0;
        s.rightYAxis = 0;
    }

    static class Instance {
        private final RobotController state;
        private XboxController controller;
        private int port;
        private boolean active;

        Instance(int port) {
            this.port = port;
            this.state = new RobotController();
            if (port >= 0 && port < 6) {
                active = true;
                this.controller = new XboxController(port);
                reset(state);
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