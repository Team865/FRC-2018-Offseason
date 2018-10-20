package ca.warp7.frc.commons.core;

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
    private final PrintStream mAccumulatedError;

    StateManager() {
        mPrintCounter = 0;
        mStateObservers = new ArrayList<>();
        mAccumulatedPrinter = new PrintStream(System.out, false);
        mAccumulatedError = new PrintStream(System.err, false);
    }

    /**
     * Reports a state object
     *
     * @param owner      the owner of the state object, which can modify it
     * @param stateType The report type. See {@link StateType}
     * @param state      The state object to be reflected
     */
    synchronized void report(Object owner, StateType stateType, Object state) {
        String ownerName, value;
        if (owner != null) {
            ownerName = owner.getClass().getSimpleName();
        } else {
            ownerName = "Robot";
        }
        switch (stateType) {
            case REFLECT_STATE_CURRENT:
                reflectObject(ownerName, state);
                break;
            case REFLECT_STATE_INPUT:
                reflectObject(ownerName + ".in", state);
                break;
            case PRINT_LINE:
                value = String.valueOf(state);
                if (mPrintCounter <= kMaxPrintLength) {
                    mPrintCounter += value.length();
                    mAccumulatedPrinter.println(value);
                }
                break;
            case ERROR_PRINT_LINE:
                value = String.valueOf(state);
                if (mPrintCounter <= kMaxPrintLength) {
                    mPrintCounter += value.length();
                    mAccumulatedError.println(value);
                }
                break;
        }
    }

    private synchronized void reflectObject(String prefix, Object state) {
        boolean foundCachedObserver = false;
        for (StateObserver observer : mStateObservers) {
            if (observer.isSameAs(state)) {
                observer.updateData();
                foundCachedObserver = true;
            }
        }
        if (!foundCachedObserver) {
            StateObserver observer = new StateObserver(prefix, state);
            observer.updateData();
            mStateObservers.add(observer);
        }
    }

    synchronized void sendAll() {
        mStateObservers.forEach(StateObserver::updateSmartDashboard);
        if (mPrintCounter > kMaxPrintLength) {
            System.err.println("Printing has exceeded the limit");
        }
        mPrintCounter = 0;
        mAccumulatedPrinter.flush();
    }
}
