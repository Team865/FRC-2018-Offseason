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
    private ILoop mControllerLoop;

    /**
     * Loop asking each system to modify its current state based on its input
     */
    private ILoop mStateUpdaterLoop;

    /**
     * Loop asking each system to perform its output
     */
    private ILoop mSystemOutputLoop;

    void setPeriodicSource(Components components, StateManager stateManager) {
        mStateReportingLoop = components::onReportState;
        mStateSenderLoop = stateManager::sendAll;
        mMeasuringLoop = components::onMeasure;
        mSystemOutputLoop = components::onOutput;
        mStateUpdaterLoop = components::onUpdateState;
        mControllerLoop = components::controllerPeriodic;

        mStateObservationLooper.registerStartLoop(mStateReportingLoop);
        mStateObservationLooper.registerLoop(mStateSenderLoop);
        mStateChangeLooper.registerLoop(mStateUpdaterLoop);
        mInputLooper.registerStartLoop(mMeasuringLoop);
        mInputLooper.registerFinalLoop(mControllerLoop);
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
        mInputLooper.registerFinalLoop(mControllerLoop);
    }

    void disableController() {
        mInputLooper.registerFinalLoop(null);
    }
}
