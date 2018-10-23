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

    private int mPrintCounter;
    private final List<StateObserver> mStateObservers;
    private final PrintStream mAccumulatedPrinter;
    private String mLoggedRobotState;
    private double mOldRobotStateTimeStamp;
    private NetworkTable mSubsystemsTable;

    StateManager() {
        mPrintCounter = 0;
        mStateObservers = new ArrayList<>();
        mAccumulatedPrinter = new PrintStream(System.out, false);
        mLoggedRobotState = "";
        mOldRobotStateTimeStamp = Timer.getFPGATimestamp();
        mSubsystemsTable = NetworkTableInstance.getDefault().getTable("Subsystems");
    }

    void logRobotState(String state) {
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

    /**
     * Reports a state object
     *
     * @param owner     the owner of the state object, which can modify it
     * @param stateType The state type. See {@link StateType}
     * @param o         The object to report
     */
    synchronized void report(Object owner, StateType stateType, Object o) {
        String ownerName = (owner != null) ? ((owner instanceof String) ?
                owner.toString() : owner.getClass().getSimpleName()) : "UnclassifiedOwner";
        switch (stateType) {
            case COMPONENT_STATE:
                reflectComponent(ownerName, o);
                break;
            case COMPONENT_INPUT:
                reflectComponent(ownerName + ".in", o);
                break;
            case PRINTLN:
                println("", o);
                break;
            case WARNING_PRINTLN:
                println("WARNING ", o);
                break;
            case ERROR_PRINTLN:
                println("ERROR ", o);
                break;
        }
    }

    private synchronized void println(String prefix, Object value) {
        String stringValue = String.valueOf(value);
        if (mPrintCounter <= kMaxPrintLength) {
            mPrintCounter += stringValue.length();
            mAccumulatedPrinter.println(prefix + stringValue);
        }
    }

    private synchronized void reflectComponent(String prefix, Object state) {
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

    synchronized void sendAll() {
        mStateObservers.forEach(StateObserver::updateNetworkTables);
        if (mPrintCounter > kMaxPrintLength) {
            System.out.println("ERROR Printing has exceeded the limit");
        }
        mPrintCounter = 0;
        mAccumulatedPrinter.flush();
    }
}
