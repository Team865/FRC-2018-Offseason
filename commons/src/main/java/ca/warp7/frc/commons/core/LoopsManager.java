package ca.warp7.frc.commons.core;

/**
 * Keeps track of the robot's looper and main loops
 */
//@SuppressWarnings("FieldCanBeLocal")
class LoopsManager {

    private static final double kObservationLooperDelta = 0.05;
    private final Looper mStateObservationLooper = new Looper(kObservationLooperDelta);

    private static final double kInputLooperDelta = 0.02;
    private final Looper mInputLooper = new Looper(kInputLooperDelta);

    private static final double kStateChangeLooperDelta = 0.02;
    private final Looper mStateChangeLooper = new Looper(kStateChangeLooperDelta);

    /**
     * Loop asking the callback to process the controller input
     */
    private ILoop mControllerPeriodic;

    void setPeriodicSource(Components components, StateManager stateManager) {
        /*
          Loop that sends data to the driver station
         */
        ILoop stateSenderLoop = stateManager::sendAll;
        /*
          Loop asking each system to report its state
         */
        ILoop stateReportingLoop = components::onReportState;
        /*
          Loop asking each system to read sensor values
         */
        ILoop measuringLoop = components::onMeasure;
        /*
          Loop asking each system to perform its output
         */
        ILoop systemOutputLoop = components::onOutput;
        /*
          Loop asking each system to modify its current state based on its input
         */
        ILoop stateUpdaterLoop = components::onUpdateState;

        /*
          Updates the controllers
         */
        ILoop mControllerUpdate = components::controllerUpdate;

        mControllerPeriodic = components::controllerPeriodic;

        mStateObservationLooper.registerStartLoop(stateReportingLoop);
        mStateObservationLooper.registerLoop(stateSenderLoop);
        
        mInputLooper.registerStartLoop(measuringLoop);
        mInputLooper.registerLoop(mControllerUpdate);
        mInputLooper.registerFinalLoop(mControllerPeriodic);

        mStateChangeLooper.registerLoop(stateUpdaterLoop);
        mStateChangeLooper.registerFinalLoop(systemOutputLoop);
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
        mInputLooper.registerFinalLoop(mControllerPeriodic);
    }

    void disableController() {
        mInputLooper.registerFinalLoop(null);
    }
}
