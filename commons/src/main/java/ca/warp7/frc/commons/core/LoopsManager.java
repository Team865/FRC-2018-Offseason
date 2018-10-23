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
        ILoop controllerDataUpdateLoop = components::controllerUpdate;

        /*
          Loop asking the callback to process the controller input
         */
        ILoop controllerPeriodicLoop = components::controllerPeriodic;

        mStateObservationLooper.registerLoop(stateReportingLoop);
        mStateObservationLooper.registerLoop(stateSenderLoop);
        mInputLooper.registerLoop(measuringLoop);
        mInputLooper.registerLoop(controllerDataUpdateLoop);
        mInputLooper.registerLoop(controllerPeriodicLoop);
        mStateChangeLooper.registerLoop(stateUpdaterLoop);
        mStateChangeLooper.registerLoop(systemOutputLoop);
    }

    void startObservers() {
        mStateObservationLooper.startLoops();
    }

    void disable() {
        mStateChangeLooper.stopLoops();
        mInputLooper.stopLoops(); //TODO try removing this?
    }

    void enable() {
        mStateChangeLooper.startLoops();
        mInputLooper.startLoops();
    }
}
