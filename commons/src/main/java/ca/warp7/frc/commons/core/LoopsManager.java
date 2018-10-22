package ca.warp7.frc.commons.core;

/**
 * Keeps track of the robot's looper and main loops
 */
@SuppressWarnings("FieldCanBeLocal")
class LoopsManager {

    private static final double kObservationLooperDelta = 0.05;
    private final Looper mStateObservationLooper = new Looper(kObservationLooperDelta);

    private static final double kInputLooperDelta = 0.02;
    private final Looper mInputLooper = new Looper(kInputLooperDelta);

    private static final double kStateChangeLooperDelta = 0.02;
    private final Looper mStateChangeLooper = new Looper(kStateChangeLooperDelta);

    /**
     * Loop asking each system to report its state
     */
    private ILoop mStateReportingLoop;

    /**
     * Loop that sends data to the driver station
     */
    private ILoop mStateSenderLoop;

    /**
     * Loop asking each system to read sensor values
     */
    private ILoop mMeasuringLoop;

    /**
     * Loop asking the callback to process the controller input
     */
    private ILoop mControllerInputLoop;

    /**
     * Loop asking each system to modify its current state based on its input
     */
    private ILoop mStateUpdaterLoop;

    /**
     * Loop asking each system to perform its output
     */
    private ILoop mSystemOutputLoop;

    /**
     * A procedure passed into the {@link LoopsManager} runner called in teleop
     */
    private Runnable mOIRunner;

    /**
     * Sets the Operator Input runner, which should get input from controllers and
     * pass them to subsystems
     */
    void setOIRunner(Runnable OIRunner) {
        mOIRunner = OIRunner;
    }

    boolean hasOIRunner(){
        return mOIRunner != null;
    }

    void setPeriodicSource(SubsystemsManager subsystemsManager, StateManager stateManager) {
        mStateReportingLoop = subsystemsManager::reportAll;
        mStateSenderLoop = stateManager::sendAll;
        mMeasuringLoop = subsystemsManager::measureAll;
        mControllerInputLoop = mOIRunner::run;
        mSystemOutputLoop = subsystemsManager::outputAll;
        mStateUpdaterLoop = subsystemsManager::updateAll;

        mStateObservationLooper.registerStartLoop(mStateReportingLoop);
        mStateObservationLooper.registerLoop(mStateSenderLoop);
        mStateChangeLooper.registerLoop(mStateUpdaterLoop);
        mInputLooper.registerStartLoop(mMeasuringLoop);
        mInputLooper.registerFinalLoop(mControllerInputLoop);
        mStateChangeLooper.registerFinalLoop(mSystemOutputLoop);
    }

    void startObservers() {
        mStateObservationLooper.startLoops();
    }

    void disable() {
        mStateChangeLooper.stopLoops();
        mInputLooper.stopLoops();
    }

    void enable() {
        mStateChangeLooper.startLoops();
        mInputLooper.startLoops();
    }

    void enableController() {
        mInputLooper.registerFinalLoop(mControllerInputLoop);
    }

    void disableController() {
        mInputLooper.registerFinalLoop(null);
    }
}
