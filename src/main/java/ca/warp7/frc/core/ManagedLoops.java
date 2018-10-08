package ca.warp7.frc.core;

import ca.warp7.frc.loop.ILoop;
import ca.warp7.frc.loop.Looper;
import ca.warp7.frc.loop.SimpleLoop;

class ManagedLoops {

	private static final double kObservationLooperDelta = 0.5;
	private Looper mStateObservationLooper = new Looper(kObservationLooperDelta);

	private static final double kInputLooperDelta = 0.02;
	private Looper mInputLooper = new Looper(kInputLooperDelta);

	private static final double kStateChangeLooperDelta = 0.02;
	private Looper mStateChangeLooper = new Looper(kStateChangeLooperDelta);

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
		mStateReportingLoop = new SimpleLoop("State Reporting", manager::reportAll);
		mStateSenderLoop = new SimpleLoop("State Sender", sendStates);

		mMeasuringLoop = new SimpleLoop("System Measuring", manager::measureAll);
		mControllerInputLoop = new SimpleLoop("Controller Input", OIUpdater);

		mSystemOutputLoop = new SimpleLoop("System Output", manager::outputAll);
		mStateUpdaterLoop = new SimpleLoop("State Updater", manager::updateAll);

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

	private void registerInitialLoops() {
		mStateObservationLooper.registerStartLoop(mStateReportingLoop);
		mStateObservationLooper.registerLoop(mStateSenderLoop);
		mStateChangeLooper.registerLoop(mStateUpdaterLoop);
		mInputLooper.registerStartLoop(mMeasuringLoop);
		mInputLooper.registerLoop(mControllerInputLoop);
		mStateChangeLooper.registerFinalLoop(mSystemOutputLoop);
	}
}
