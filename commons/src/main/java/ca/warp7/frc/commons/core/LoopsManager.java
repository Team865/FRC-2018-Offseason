package ca.warp7.frc.commons.core;

import ca.warp7.frc.commons.IUnit;
import ca.warp7.frc.commons.Unit;

/**
 * Keeps track of the robot's looper and main loops
 */
class LoopsManager {

    @IUnit.Seconds
    private static final double kObservationLooperDelta = Unit.Hertz.toSeconds(20);
    private final Looper mStateObservationLooper = new Looper(kObservationLooperDelta);

    @IUnit.Seconds
    private static final double kInputLooperDelta = Unit.Hertz.toSeconds(50);
    private final Looper mInputLooper = new Looper(kInputLooperDelta);

    @IUnit.Seconds
    private static final double kMainLooperDelta = Unit.Hertz.toSeconds(50);
    private final Looper mMainLooper = new Looper(kMainLooperDelta);

    void setComponentsSource(Components components) {
        /*
          Loop that sends data to the driver station
         */
        ILoop stateSenderLoop = Robot.state::sendAll;
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
        mMainLooper.registerLoop(stateUpdaterLoop);
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
