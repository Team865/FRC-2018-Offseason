package ca.warp7.frc.core;

import ca.warp7.frc.loop.ILoop;
import ca.warp7.frc.loop.Looper;
import ca.warp7.frc.loop.SimpleLoop;

class ManagedLoops {

	private static final double kObservationLooperDelta = 0.5;
	private static final double kInputLooperDelta = 0.02;
	private static final double kStateChangeLooperDelta = 0.02;

	private Looper mStateObservationLooper;
	private Looper mInputLooper;
	private Looper mStateChangeLooper;

	private ILoop mStateReportingLoop; // Loop asking each system to report its state
	private ILoop mStateSenderLoop; // Loop that sends data to the driver station
	private ILoop mSystemInputLoop; // Loop asking each system to read sensor values
	private ILoop mControllerInputLoop; // Loop asking the callback to process the controller input
	private ILoop mStateUpdaterLoop; // Look asking each system to modify its current state based on its input
	private ILoop mSystemOutputLoop; // Loop asking each system to perform its output

	ManagedLoops() {
		mStateObservationLooper = new Looper(kObservationLooperDelta);
		mStateChangeLooper = new Looper(kStateChangeLooperDelta);
		mInputLooper = new Looper(kInputLooperDelta);
	}

	void setSource(SubsystemsManager manager, Runnable sendStates, Runnable OIUpdater) {
		mStateReportingLoop = new SimpleLoop("State Reporting", manager::reportAll);
		mStateSenderLoop = new SimpleLoop("State Sender", sendStates);

		mSystemInputLoop = new SimpleLoop("System Input", manager::inputAll);
		mControllerInputLoop = new SimpleLoop("Controller Input", OIUpdater);

		mSystemOutputLoop = new SimpleLoop("System Output", manager::outputAll);
		mStateUpdaterLoop = new SimpleLoop("State Updater", manager::updateAll);

		registerInitialLoops();
	}

	void onStartObservers() {
		mStateObservationLooper.startLoops();
	}

	void onDisable() {
		mStateChangeLooper.stopLoops();
		mInputLooper.stopLoops();
	}

	void onEnable() {
		mStateChangeLooper.startLoops();
		mInputLooper.startLoops();
	}

	private void registerInitialLoops() {
		mStateObservationLooper.registerStartLoop(mStateReportingLoop);
		mStateObservationLooper.registerLoop(mStateSenderLoop);
		mStateChangeLooper.registerLoop(mStateUpdaterLoop);
		mInputLooper.registerStartLoop(mSystemInputLoop);
		mInputLooper.registerLoop(mControllerInputLoop);
		mStateChangeLooper.registerFinalLoop(mSystemOutputLoop);
	}
}
