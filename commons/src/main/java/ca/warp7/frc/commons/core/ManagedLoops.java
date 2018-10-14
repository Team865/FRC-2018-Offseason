package ca.warp7.frc.commons.core;

class ManagedLoops {

    private static final double kObservationLooperDelta = 0.5;
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

    void setSource(SubsystemsManager manager, Runnable sendStates, Runnable OIUpdater) {
        mStateReportingLoop = new NamedLoop("State Reporting", manager::reportAll);
        mStateSenderLoop = new NamedLoop("State Sender", sendStates);

        mMeasuringLoop = new NamedLoop("System Measuring", manager::measureAll);
        mControllerInputLoop = new NamedLoop("Controller Input", OIUpdater);

        mSystemOutputLoop = new NamedLoop("System Output", manager::outputAll);
        mStateUpdaterLoop = new NamedLoop("State Updater", manager::updateAll);

        registerInitialLoops();
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

    private void registerInitialLoops() {
        mStateObservationLooper.registerStartLoop(mStateReportingLoop);
        mStateObservationLooper.registerLoop(mStateSenderLoop);
        mStateChangeLooper.registerLoop(mStateUpdaterLoop);
        mInputLooper.registerStartLoop(mMeasuringLoop);
        mInputLooper.registerFinalLoop(mControllerInputLoop);
        mStateChangeLooper.registerFinalLoop(mSystemOutputLoop);
    }
}
