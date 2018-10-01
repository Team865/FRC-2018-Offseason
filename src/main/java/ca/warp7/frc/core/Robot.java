package ca.warp7.frc.core;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for managing the robot's general lifecycle and looping mechanism,
 * as well as keeping track of the system's state
 */

public abstract class Robot extends _LocalIterativeRobot {

	@Override
	public void robotInit() {
		logRobotState("Started");
		constructSystems();
		mLoopsManager.startObservationLoops();
	}

	@Override
	public void disabledInit() {
		logRobotState("Disabled");
		mLoopsManager.disable();
		mSubsystemsIterator.resetAll();
	}

	@Override
	public void autonomousInit() {
		logRobotState("Auto");
		mLoopsManager.enable();
	}

	@Override
	public void teleopInit() {
		logRobotState("Teleop");
		mLoopsManager.enable();
		mLoopsManager.registerControllerLoop();
	}

	@Override
	public void startCompetition() {
		preConstructRobot();
		if (mMappingClass != null) {
			super.startCompetition();
		} else {
			System.err.println("Robot not set up");
		}
	}

	public static Robot.InternalUtils utils;

	@SuppressWarnings("unused")
	public class InternalUtils {
		public void print(Object object) {
			mObservationAccumulator.print(object);
		}

		public void println(Object object) {
			mObservationAccumulator.print(object);
			mObservationAccumulator.print("\n");
		}

		public PrintStream getPrintStream() {
			return mAccumulatedPrinter;
		}

		public void reportState(Object owner, StateType stateType, Object state) {
			mObservationAccumulator.reportState(owner, stateType, state);
		}

		public LoopsManager getLoopsManager() {
			return mLoopsManager;
		}
	}

	protected abstract void onConstruct();

	protected void setOperatorInput(Runnable operatorInput) {
		mOIRunnable = operatorInput;
	}

	protected void setAutonomousMode(AutonomousMode mode) {
	}

	@SuppressWarnings("WeakerAccess")
	protected void setMapping(Class<?> mappingClass) {
		mMappingClass = mappingClass;
	}

	private static final String kSubsystemsClassName = "Subsystems";
	private static final String kMappingClassPostfix = ".mapping.Mapping";
	private static final double kObservationLooperDelta = 0.5;
	private static final double kStateChangeLooperDelta = 0.02;
	private static final double kInputLooperDelta = 0.02;
	private static final int kMaxPrintLength = 255;

	private int mPrintCounter;
	private String mLoggedRobotState;
	private InternalUtils mUtils;
	private Runnable mOIRunnable;
	private Class<?> mMappingClass;
	private List<ISubsystem> mSubsystems;
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
	private ILoop mSystemStateUpdateLoop; // Look asking each system to modify its current state based on its input
	private ILoop mSystemOutputLoop; // Loop asking each system to perform its output

	protected Robot() {
		super();
		mPrintCounter = 0;
		mUtils = new InternalUtils();
		mObservationAccumulator = new ObservationAccumulator();
		mStateObservers = new ArrayList<>();
		mSubsystems = new ArrayList<>();
		mSubsystemsIterator = new SubsystemsIterator();
		mSystemsReflector = new SystemsReflector();
		mLoopsManager = new LoopsManager();
		mStateObservationLooper = new Looper(kObservationLooperDelta);
		mAccumulatedPrinter = new PrintStream(System.out, false);
		mStateChangeLooper = new Looper(kStateChangeLooperDelta);
		mInputLooper = new Looper(kInputLooperDelta);
		mStateReportingLoop = new SimpleLoop(mSubsystemsIterator::reportAll);
		mStateSenderLoop = new SimpleLoop(mObservationAccumulator::sendAllStates);
		mSystemInputLoop = new SimpleLoop(mSubsystemsIterator::inputAll);
		mControllerInputLoop = new SimpleLoop(this::onExecuteOI);
		mSystemOutputLoop = new SimpleLoop(mSubsystemsIterator::outputAll);
		mSystemStateUpdateLoop = new SimpleLoop(mSubsystemsIterator::updateAll);
	}

	private class LoopsManager {
		void addInitialLoops() {
			mStateObservationLooper.registerStartLoop(mStateReportingLoop);
			mStateObservationLooper.registerLoop(mStateSenderLoop);
			mInputLooper.registerStartLoop(mSystemInputLoop);
			mStateChangeLooper.registerLoop(mSystemStateUpdateLoop);
			mStateChangeLooper.registerFinalLoop(mSystemOutputLoop);
		}

		void registerControllerLoop() {
			mInputLooper.registerFinalLoop(mControllerInputLoop);
		}

		void startObservationLoops() {
			mStateObservationLooper.startLoops();
		}

		void disable() {
			mInputLooper.registerFinalLoop(null);
			mStateChangeLooper.stopLoops();
		}

		void enable() {
			mStateChangeLooper.startLoops();
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
				DriverStation.reportError("Printing has exceeded the limit", false);
			}
			mPrintCounter = 0;
			mAccumulatedPrinter.flush();
		}

		private String resolveStatePrefix(Object owner, StateType stateType) {
			String prefix = owner.getClass().getSimpleName();
			if (stateType == StateType.INPUT) {
				return prefix.concat(".in");
			}
			return prefix.concat(".");
		}
	}

	private class SystemsReflector {

		private Class<?> getMappingClass() {
			String mappingClassName = getClass().getPackage().getName() + kMappingClassPostfix;
			try {
				return Class.forName(mappingClassName);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
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


	private void printPrefix() {
		System.out.print("(" + getClass().getSimpleName() + ") ");
	}


	private void onExecuteOI() {
		mOIRunnable.run();
	}

	private void preConstructRobot() {
		printPrefix();
		Class<?> mappingClass = mSystemsReflector.getMappingClass();
		if (mappingClass != null) {
			setMapping(mappingClass);
		}
		onConstruct();
	}

	private void constructSystems() {
		utils = mUtils;
		Class<?> subsystemClass = mSystemsReflector.reflectSubsystemsClass();
		mSystemsReflector.createReflectedSubsystems(subsystemClass);
		mSubsystemsIterator.constructAll();
		mLoopsManager.addInitialLoops();
	}

	private void logRobotState(String state) {
		if (state.equals(mLoggedRobotState)) {
			return;
		}
		mLoggedRobotState = state;
		SmartDashboard.putString("Robot State", state);
		printPrefix();
		System.out.println("Robot State: " + state);
	}
}
