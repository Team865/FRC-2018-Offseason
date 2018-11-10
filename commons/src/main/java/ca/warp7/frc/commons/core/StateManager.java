package ca.warp7.frc.commons.core;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ca.warp7.frc.commons.core.IControls.*;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kLeft;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kRight;

class StateManager {

    private static final int kMaxPrintLength = 255;
    private static final double kTriggerDeadBand = 0.5;
    private static final double kStateEpsilon = 0.01;
    private static final int kUpPOV = 0;
    private static final int kRightPOV = 90;
    private static final int kDownPOV = 180;
    private static final int kLeftPOV = 270;

    private final List<StateObserver> mStateObservers = new ArrayList<>();
    private final List<XboxControllerPair> mControllers = new ArrayList<>();
    private final PrintStream mPrintStream = new PrintStream(System.out, false);

    private int mPrintCounter;
    private String mLoggedRobotState;
    private double mOldRobotStateTimeStamp;
    private NetworkTable mSubsystemsTable;

    void attach() {
        mSubsystemsTable = NetworkTableInstance.getDefault().getTable("Subsystems");
        mPrintCounter = 0;
        mLoggedRobotState = "";
        mOldRobotStateTimeStamp = Timer.getFPGATimestamp();
    }

    private void logRobotState(String state) {
        if (state.equals(mLoggedRobotState)) return;
        String oldState = mLoggedRobotState;
        mLoggedRobotState = state;
        double newTime = Timer.getFPGATimestamp();
        double dt = newTime - mOldRobotStateTimeStamp;
        mOldRobotStateTimeStamp = newTime;
        System.out.print(getClass().getSimpleName() + " - ");
        System.out.print("Robot State: " + mLoggedRobotState);
        if (!oldState.isEmpty()) System.out.print(String.format(", %.0f seconds after %s began", dt, oldState));
        System.out.println();
    }

    void logInit() {
        logRobotState("Initializing");
    }

    void logDisabled() {
        logRobotState("Disabled");
    }

    void logAutonomous() {
        logRobotState("Autonomous");
    }

    void logTeleop() {
        logRobotState("Teleop");
    }

    void logTest() {
        logRobotState("Test");
    }

    /**
     * Reports a state object
     *
     * @param owner     the owner of the state object, which can modify it
     * @param stateType The state type. See {@link StateType}
     * @param o         The object to report
     */
    synchronized void report(Object owner, StateType stateType, Object o) {
        String name = (owner != null) ? ((owner instanceof String) ?
                owner.toString() : owner.getClass().getSimpleName()) : "UnclassifiedOwner";
        report0(name, stateType, o);
    }

    XboxControlsState createXboxController(int port) {
        for (XboxControllerPair pair : mControllers) if (pair.port == port) return pair.state;
        XboxControllerPair newPair = new XboxControllerPair(port);
        mControllers.add(newPair);
        resetControllerData(newPair.state);
        return newPair.state;
    }

    private void println0(String prefix, Object value) {
        String stringValue = String.valueOf(value);
        if (mPrintCounter <= kMaxPrintLength) {
            mPrintCounter += stringValue.length();
            mPrintStream.println(prefix + stringValue);
        }
    }

    private void report0(String prefix, Object componentState) {
        boolean foundCachedObserver = false;
        for (StateObserver observer : mStateObservers) {
            if (observer.object == componentState) {
                updateObserverData(observer);
                foundCachedObserver = true;
            }
        }
        if (!foundCachedObserver) {
            NetworkTable subTable = mSubsystemsTable.getSubTable(prefix);
            StateObserver observer = new StateObserver(subTable, componentState);
            for (Field field : observer.fields) if (!field.isAccessible()) field.setAccessible(true);
            updateObserverData(observer);
            mStateObservers.add(observer);
        }
    }

    private void updateObserverData(StateObserver observer) {
        for (Field stateField : observer.fields) {
            try {
                String fieldName = stateField.getName();
                if (!fieldName.startsWith("_")) {
                    Object value = stateField.get(observer.object);
                    observer.map.put(fieldName, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void report0(String name, StateType type, Object o) {
        switch (type) {
            case ComponentState:
                report0(name, o);
                break;
            case ComponentInput:
                report0(name + ".in", o);
                break;
            case Println:
                println0("", o);
                break;
            case Warning:
                println0("WARNING ", o);
                break;
            case Error:
                println0("ERROR ", o);
                mPrintStream.flush();
                break;
        }
    }

    synchronized void sendAll() {
        for (StateObserver observer : mStateObservers) {
            for (String entryKey : observer.map.keySet()) {
                Object value = observer.map.get(entryKey);
                if (value instanceof ICollectiveState) ((ICollectiveState) value).getCollection().forEach(
                        (s, object) -> sendNetworkTableValue(observer.table.getEntry(entryKey + "/" + s), object));
                else sendNetworkTableValue(observer.table.getEntry(entryKey), value);
            }
        }
        if (mPrintCounter > kMaxPrintLength) System.out.println("ERROR Printing has exceeded the limit");
        mPrintCounter = 0;
        mPrintStream.flush();
    }

    private static void sendNetworkTableValue(NetworkTableEntry entry, Object value) {
        if (value instanceof Number) {
            double n = ((Number) value).doubleValue();
            if (Math.abs(n) < kStateEpsilon) n = 0;
            entry.setNumber(n);
        } else if (value instanceof Boolean) entry.setBoolean((Boolean) value);
        else if (value instanceof String) entry.setString((String) value);
        else if (value.getClass().isEnum()) entry.setString(value.toString());
        else entry.setString(value.getClass().getSimpleName() + " Object");
    }

    private static int u0(int oldState, boolean newState) {
        return newState ? ((oldState == Pressed || oldState == HeldDown) ? HeldDown : Pressed) :
                ((oldState == Released || oldState == KeptUp) ? KeptUp : Released);
    }

    private static void resetControllerData(XboxControlsState S) {
        S.AButton = KeptUp;
        S.BButton = KeptUp;
        S.XButton = KeptUp;
        S.YButton = KeptUp;
        S.LeftBumper = KeptUp;
        S.RightBumper = KeptUp;
        S.LeftTrigger = KeptUp;
        S.RightTrigger = KeptUp;
        S.LeftStickButton = KeptUp;
        S.RightStickButton = KeptUp;
        S.StartButton = KeptUp;
        S.BackButton = KeptUp;
        S.UpDPad = KeptUp;
        S.RightDPad = KeptUp;
        S.DownDPad = KeptUp;
        S.LeftDPad = KeptUp;
        S.LeftTriggerAxis = 0;
        S.RightTriggerAxis = 0;
        S.LeftXAxis = 0;
        S.LeftYAxis = 0;
        S.RightXAxis = 0;
        S.RightYAxis = 0;
    }

    private static void collectIndividualController(XboxControlsState S, XboxController C) {
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

    synchronized void collectControllerData() {
        for (XboxControllerPair pair : mControllers) {
            if (pair.active) {
                collectIndividualController(pair.state, pair.controller);
                report0(String.format("XboxController[%d]", pair.port), pair.state);
            }
        }
    }

    private static class StateObserver {
        private final Object object;
        private final Field[] fields;
        private final Map<String, Object> map;
        private final NetworkTable table;

        StateObserver(NetworkTable table, Object object) {
            this.object = object;
            fields = this.object.getClass().getDeclaredFields();
            map = new HashMap<>();
            this.table = table;
        }
    }

    private static class XboxControllerPair {
        private final XboxControlsState state;
        private XboxController controller;
        private int port;
        private boolean active;

        private XboxControllerPair(int port) {
            this.port = port;
            this.state = new XboxControlsState();
            if (port >= 0 && port < 6) {
                active = true;
                this.controller = new XboxController(port);
            } else active = false;
        }
    }
}
