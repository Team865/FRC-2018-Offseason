package ca.warp7.frc.commons.core;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Keeps track of state reporting and sending
 */
class StateManager {

    private static final int kMaxPrintLength = 255;

    private final List<StateObserver> mStateObservers = new ArrayList<>();
    private final PrintStream mPrintStream = new PrintStream(System.out, false);

    private int mPrintCounter;
    private String mLoggedRobotState;
    private double mOldRobotStateTimeStamp;
    private NetworkTable mSubsystemsTable;
    private Robot mAttachedRobot;

    void attachRobotInstance(Robot robot){
        mAttachedRobot = robot;
        mSubsystemsTable = NetworkTableInstance.getDefault().getTable("Subsystems");
        mPrintCounter = 0;
        mLoggedRobotState = "";
        mOldRobotStateTimeStamp = Timer.getFPGATimestamp();
        mAttachedRobot = null;
    }

    private void logRobotState(String state) {
        assertRobotAttached();
        if (state.equals(mLoggedRobotState)) {
            return;
        }
        String oldState = mLoggedRobotState;
        mLoggedRobotState = state;
        SmartDashboard.putString("Robot State", state);
        double newTime = Timer.getFPGATimestamp();
        double dt = newTime - mOldRobotStateTimeStamp;
        mOldRobotStateTimeStamp = newTime;
        System.out.print(getClass().getSimpleName() + " - ");
        System.out.print("Robot State: " + mLoggedRobotState);
        if (!oldState.isEmpty()) {
            System.out.print(String.format(", %.0f seconds after %s began", dt, oldState));
        }
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
        assertRobotAttached();
        String name = (owner != null) ? ((owner instanceof String) ?
                owner.toString() : owner.getClass().getSimpleName()) : "UnclassifiedOwner";
        report0(name, stateType, o);
    }

    private void println(String prefix, Object value) {
        String stringValue = String.valueOf(value);
        if (mPrintCounter <= kMaxPrintLength) {
            mPrintCounter += stringValue.length();
            mPrintStream.println(prefix + stringValue);
        }
    }

    private void reflectComponent(String prefix, Object state) {
        boolean foundCachedObserver = false;
        for (StateObserver observer : mStateObservers) {
            if (observer.isSameAs(state)) {
                observer.updateData();
                foundCachedObserver = true;
            }
        }
        if (!foundCachedObserver) {
            NetworkTable subTable = mSubsystemsTable.getSubTable(prefix);
            StateObserver observer = new StateObserver(subTable, state);
            observer.updateData();
            mStateObservers.add(observer);
        }
    }

    private void assertRobotAttached(){
        if (mAttachedRobot == null){
            System.out.println("ERROR no Robot instance attached!!!");
        }
    }

    private void report0(String name, StateType type, Object o){
        switch (type) {
            case COMPONENT_STATE:
                reflectComponent(name, o);
                break;
            case COMPONENT_INPUT:
                reflectComponent(name + ".in", o);
                break;
            case PRINTLN:
                println("", o);
                break;
            case WARNING:
                println("WARNING ", o);
                break;
            case ERROR:
                println("ERROR ", o);
                mPrintStream.flush();
                break;
        }
    }

    synchronized void sendAll() {
        mStateObservers.forEach(StateObserver::updateNetworkTables);
        if (mPrintCounter > kMaxPrintLength) {
            System.out.println("ERROR Printing has exceeded the limit");
        }
        mPrintCounter = 0;
        mPrintStream.flush();
    }
}
