package ca.warp7.frc.core;

import ca.warp7.frc.loop.ILoop;
import ca.warp7.frc.loop.Looper;
import ca.warp7.frc.loop.SimpleLoop;
import ca.warp7.frc.observer.StateObserver;
import ca.warp7.frc.observer.StateType;
import ca.warp7.frc.wpi_wrapper.IterativeRobotWrapper;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for managing the robot's general lifecycle,
 * as well as keeping track of the system's state
 */

public abstract class Robot extends IterativeRobotWrapper {

	@Override
	public void robotInit() {
		super.robotInit();
		constructSystems();
		mLoopsManager.startObservers();
	}

	@Override
	public void disabledInit() {
		super.disabledInit();
		mAutoRunner.onStop();
		mLoopsManager.onDisable();
		mSubsystemsIterator.resetAll();
	}

	@Override
	public void autonomousInit() {
		super.autonomousInit();
		mLoopsManager.onEnable();
		mAutoRunner.onStart();
	}

	@Override
	public void teleopInit() {
		super.teleopInit();
		mAutoRunner.onStop();
		mLoopsManager.onEnable();
	}

	@Override
	public void startCompetition() {
		preConfigure();
		if (mMappingClass != null) {
			super.startCompetition();
		} else {
			System.err.println("Robot not set up");
		}
	}

	public static Robot.InternalUtils utils;

	@SuppressWarnings({"unused", "WeakerAccess"})
	public class InternalUtils {
		public void print(Object object) {
			mObservationAccumulator.print(object);
		}

		public void println(Object object) {
			mObservationAccumulator.print(object);
			mObservationAccumulator.print("\n");
		}

		public void prefixedPrintln(Object object) {
			mObservationAccumulator.print(getRobotPrefix());
			println(object);
		}

		public PrintStream getPrintStream() {
			return mAccumulatedPrinter;
		}

		public void reportState(Object owner, StateType stateType, Object state) {
			mObservationAccumulator.reportState(owner, stateType, state);
		}
	}

	protected abstract void onCreate();

	protected void setOIUpdater(Runnable OIUpdater) {
		mOIUpdater = OIUpdater;
	}

	@SuppressWarnings("SameParameterValue")
	protected void setAutoMode(IAutoMode mode) {
		mAutoRunner.setAutoMode(mode);
	}

	@SuppressWarnings("WeakerAccess")
	protected void setMapping(Class<?> mappingClass) {
		mMappingClass = mappingClass;
	}

	private static final String kSubsystemsClassName = "Subsystems";
	private static final String kMappingClassPostfix = ".constants.RobotMap";
	private static final double kObservationLooperDelta = 0.5;
	private static final double kInputLooperDelta = 0.02;
	private static final double kStateChangeLooperDelta = 0.02;
	private static final int kMaxPrintLength = 255;

	private int mPrintCounter;
	private InternalUtils mUtils;
	private Runnable mOIUpdater;
	private Class<?> mMappingClass;
	private List<ISubsystem> mSubsystems;
	private AutoRunner mAutoRunner;
	private ObservationAccumulator mObservationAccumulator;
	private PrintStream mAccumulatedPrinter;
	private List<StateObserver> mStateObservers;
	private SubsystemsIterator mSubsystemsIterator;
	private SystemsReflector mSystemsReflector;
	private LoopsManager mLoopsManager;
	private Looper mStateObservationLooper;
	private Looper mInputLooper;
	private Looper mStateChangeLooper;
	private ILoop mStateReportingLoop; // Loop asking each system to report its state
	private ILoop mStateSenderLoop; // Loop that sends data to the driver station
	private ILoop mSystemInputLoop; // Loop asking each system to read sensor values
	private ILoop mControllerInputLoop; // Loop asking the callback to process the controller input
	private ILoop mStateUpdaterLoop; // Look asking each system to modify its current state based on its input
	private ILoop mSystemOutputLoop; // Loop asking each system to perform its output

	protected Robot() {
		super();
		mPrintCounter = 0;
		mUtils = new InternalUtils();
		mObservationAccumulator = new ObservationAccumulator();
		mStateObservers = new ArrayList<>();
		mAutoRunner = new AutoRunner();
		mSubsystems = new ArrayList<>();
		mSubsystemsIterator = new SubsystemsIterator();
		mSystemsReflector = new SystemsReflector();
		mLoopsManager = new LoopsManager();
		mStateObservationLooper = new Looper(kObservationLooperDelta);
		mAccumulatedPrinter = new PrintStream(System.out, false);
		mStateChangeLooper = new Looper(kStateChangeLooperDelta);
		mInputLooper = new Looper(kInputLooperDelta);
		mStateReportingLoop = new SimpleLoop("State Reporting", mSubsystemsIterator::reportAll);
		mStateSenderLoop = new SimpleLoop("State Sender", mObservationAccumulator::sendAllStates);
		mSystemInputLoop = new SimpleLoop("System Input", mSubsystemsIterator::inputAll);
		mControllerInputLoop = new SimpleLoop("Controller Input", this::onUpdateOI);
		mSystemOutputLoop = new SimpleLoop("System Output", mSubsystemsIterator::outputAll);
		mStateUpdaterLoop = new SimpleLoop("State Updater", mSubsystemsIterator::updateAll);
	}

	private class LoopsManager {
		void addInitialLoops() {
			mStateObservationLooper.registerStartLoop(mStateReportingLoop);
			mStateObservationLooper.registerLoop(mStateSenderLoop);
			mInputLooper.registerStartLoop(mSystemInputLoop);
			mInputLooper.registerLoop(mControllerInputLoop);
			mStateChangeLooper.registerLoop(mStateUpdaterLoop);
			mStateChangeLooper.registerFinalLoop(mSystemOutputLoop);
		}

		void startObservers() {
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
	}

	private class ObservationAccumulator {
		synchronized void reportState(Object owner, StateType stateType, Object state) {
			String prefix = resolveStatePrefix(owner, stateType);
			boolean foundCachedObserver = false;
			for (StateObserver observer : mStateObservers) {
				if (observer.isSameAs(state)) {
					observer.updateData();
					foundCachedObserver = true;
				}
			}
			if (!foundCachedObserver) {
				StateObserver observer = new StateObserver(prefix, state);
				observer.updateData();
				mStateObservers.add(observer);
			}
		}

		synchronized void print(Object object) {
			String value = String.valueOf(object);
			if (mPrintCounter <= kMaxPrintLength) {
				mPrintCounter += value.length();
				mAccumulatedPrinter.print(value);
			}
		}

		synchronized void sendAllStates() {
			mStateObservers.forEach(StateObserver::updateSmartDashboard);
			if (mPrintCounter > kMaxPrintLength) {
				System.err.println("Printing has exceeded the limit");
			}
			mPrintCounter = 0;
			mAccumulatedPrinter.flush();
		}

		private String resolveStatePrefix(Object owner, StateType stateType) {
			String prefix = owner.getClass().getSimpleName();
			if (stateType == StateType.INPUT) {
				return prefix.concat(".in");
			}
			return prefix;
		}
	}

	private class SystemsReflector {
		private Class<?> getMappingClass(String packageName) {
			String mappingClassName = packageName + kMappingClassPostfix;
			try {
				return Class.forName(mappingClassName);
			} catch (ClassNotFoundException e) {
				System.out.println("Mapping class not found");
				return null;
			}
		}

		private Class<?> reflectSubsystemsClass() {
			for (Class mappingSubclass : mMappingClass.getClasses()) {
				if (mappingSubclass.getSimpleName().equals(kSubsystemsClassName)) {
					return mappingSubclass;
				}
			}
			return null;
		}

		private void createReflectedSubsystems(Class<?> subsystemsClass) {
			mSubsystems.clear();
			if (subsystemsClass != null) {
				Field[] subsystemsClassFields = subsystemsClass.getFields();
				for (Field subsystemField : subsystemsClassFields) {
					if (ISubsystem.class.isAssignableFrom(subsystemField.getType())) {
						try {
							ISubsystem currentValue = (ISubsystem) subsystemField.get(null);
							if (currentValue == null) {
								Class subsystemType = subsystemField.getType();
								ISubsystem instance = (ISubsystem) subsystemType.newInstance();
								subsystemField.set(null, instance);
								mSubsystems.add(instance);
							} else {
								mSubsystems.add(currentValue);
							}
						} catch (IllegalAccessException | InstantiationException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	private class SubsystemsIterator {
		void constructAll() {
			mSubsystems.forEach(ISubsystem::onConstruct);
		}

		void resetAll() {
			mSubsystems.forEach(ISubsystem::onDisabledReset);
		}

		void outputAll() {
			mSubsystems.forEach(ISubsystem::onOutputLoop);
		}

		void inputAll() {
			mSubsystems.forEach(ISubsystem::onInputLoop);
		}

		void reportAll() {
			mSubsystems.forEach(ISubsystem::onReportState);
		}

		void updateAll() {
			mSubsystems.forEach(ISubsystem::onUpdateState);
		}
	}


	private void onUpdateOI() {
		mOIUpdater.run();
	}

	private void preConfigure() {
		printRobotPrefix();
		Class<?> mappingClass = mSystemsReflector.getMappingClass(getPackageName());
		if (mappingClass != null) {
			setMapping(mappingClass);
		}
		onCreate();
	}

	private void constructSystems() {
		utils = mUtils;
		Class<?> subsystemClass = mSystemsReflector.reflectSubsystemsClass();
		mSystemsReflector.createReflectedSubsystems(subsystemClass);
		mSubsystemsIterator.constructAll();
		mLoopsManager.addInitialLoops();
	}

}
