package ca.warp7.frc.core;

/**
 * Keeps track of the robot's looper and main loops
 */
class LoopsManager {

    private static final double kObservationLooperDelta = 0.02;
    private final Looper mStateObservationLooper = new Looper(kObservationLooperDelta);
    
    private static final double kInputLooperDelta = 0.02;
    private final Looper mInputLooper = new Looper(kInputLooperDelta);

    private static final double kMainLooperDelta = 0.02;
    private final Looper mMainLooper = new Looper(kMainLooperDelta);

    void setComponentsSource(Components components) {
        /*
          Loop that sends data to the driver station
         */
        Looper.Loop stateSenderLoop = Robot.getState()::sendAll;
        mStateObservationLooper.registerLoop(stateSenderLoop);

        /*
          Loop asking each system to report its state
         */
        Looper.Loop stateReportingLoop = components::onReportState;
        mStateObservationLooper.registerLoop(stateReportingLoop);

        /*
          Loop asking each system to read sensor values
         */
        Looper.Loop measuringLoop = components::onMeasure;
        mInputLooper.registerLoop(measuringLoop);

        /*
          Updates the controllers
         */
        Looper.Loop collectControllerDataLoop = Robot.getState()::collectControllerData;
        mInputLooper.registerLoop(collectControllerDataLoop);

        /*
          Loop asking the callback to process the controller input
         */
        Looper.Loop controllerPeriodicLoop = components::controllerPeriodic;
        mInputLooper.registerLoop(controllerPeriodicLoop);

        /*
          Loop asking each system to modify its current state based on its input
         */
        Looper.Loop stateUpdaterLoop = components::onUpdateState;
        mMainLooper.registerLoop(stateUpdaterLoop);

        /*
          Loop asking each system to perform its output
         */
        Looper.Loop systemOutputLoop = components::onOutput;
        mMainLooper.registerLoop(systemOutputLoop);
    }

    void start() {
        mStateObservationLooper.startLoops();
        mInputLooper.startLoops();
    }

    void disable() {
        mMainLooper.stopLoops();
    }

    void enable() {
        mMainLooper.startLoops();
    }
}
