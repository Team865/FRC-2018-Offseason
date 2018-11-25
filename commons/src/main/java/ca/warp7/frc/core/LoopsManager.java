package ca.warp7.frc.core;

import ca.warp7.action.IAction;

/**
 * Keeps track of the robot's looper and main loops
 */
class LoopsManager {

    private static final double kObservationLooperDelta = 0.02;
    private final ActionLooper mStateObservationLooper = new ActionLooper(kObservationLooperDelta);
    
    private static final double kInputLooperDelta = 0.02;
    private final ActionLooper mInputLooper = new ActionLooper(kInputLooperDelta);

    private static final double kMainLooperDelta = 0.02;
    private final ActionLooper mMainLooper = new ActionLooper(kMainLooperDelta);

    void setComponentsSource(Components components) {
        /*
          Loop that sends data to the driver station
         */
        Loop stateSenderLoop = Robot.getState()::sendAll;
        mStateObservationLooper.registerLoop(stateSenderLoop);

        /*
          Loop asking each system to report its state
         */
        Loop stateReportingLoop = components::onReportState;
        mStateObservationLooper.registerLoop(stateReportingLoop);

        /*
          Loop asking each system to read sensor values
         */
        Loop measuringLoop = components::onMeasure;
        mInputLooper.registerLoop(measuringLoop);

        /*
          Updates the controllers
         */
        Loop collectControllerDataLoop = Robot.getState()::collectControllerData;
        mInputLooper.registerLoop(collectControllerDataLoop);

        /*
          Loop asking the callback to process the controller input
         */
        Loop controllerPeriodicLoop = components::controllerPeriodic;
        mInputLooper.registerLoop(controllerPeriodicLoop);

        /*
          Loop asking each system to modify its current state based on its input
         */
        Loop stateUpdaterLoop = components::onUpdateState;
        mMainLooper.registerLoop(stateUpdaterLoop);

        /*
          Loop asking each system to perform its output
         */
        Loop systemOutputLoop = components::onOutput;
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

    /**
     * Defines a loop mechanism
     */
    @FunctionalInterface
    public interface Loop extends IAction {

        @Override
        default void start() {
        }

        @Override
        default boolean shouldFinish() {
            return false;
        }

        @Override
        void update();
    }
}
