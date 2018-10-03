package ca.warp7.frc.core;

import ca.warp7.frc.observer.StateObserver;
import ca.warp7.frc.observer.StateType;
import ca.warp7.frc.wpi_wrapper.IterativeRobotWrapper;

import java.io.PrintStream;
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
		mConstructor.initRobotSystem();
		mManagedLoops.onStartObservers();
	}

	@Override
	public void disabledInit() {
		super.disabledInit();
		mAutoRunner.onStop();
		mManagedLoops.onDisable();
		mSubsystemsManager.resetAll();
	}

	@Override
	public void autonomousInit() {
		super.autonomousInit();
		mManagedLoops.onEnable();
		mAutoRunner.onStart();
	}

	@Override
	public void teleopInit() {
		super.teleopInit();
		mAutoRunner.onStop();
		mManagedLoops.onEnable();
	}

	@Override
	public void startCompetition() {
		mConstructor.preConfigure();
		if (mMappingClass != null && mOIUpdater != null) {
			super.startCompetition();
		} else {
			System.err.println("Robot not set up");
		}
	}

	@SuppressWarnings("unused")
	public static void print(Object object) {
		_accessor.print(object);
	}

	@SuppressWarnings("unused")
	public static void println(Object object) {
		_accessor.println(object);
	}

	public static void prefixedPrintln(Object object) {
		_accessor.prefixedPrintln(object);
	}

	public static void reportState(Object owner, StateType stateType, Object state) {
		_accessor.reportState(owner, stateType, state);
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

	private static final int kMaxPrintLength = 255;
	private static InternalAccessor _accessor;

	private int mPrintCounter;
	private InternalAccessor mAccessor;
	private Runnable mOIUpdater;
	private Class<?> mMappingClass;
	private AutoRunner mAutoRunner;
	private ObservationAccumulator mObservationAccumulator;
	private PrintStream mAccumulatedPrinter;
	private List<StateObserver> mStateObservers;
	private SubsystemsManager mSubsystemsManager;
	private Constructor mConstructor;
	private ManagedLoops mManagedLoops;

	protected Robot() {
		super();
		mAccessor = new InternalAccessor();
		mAccumulatedPrinter = new PrintStream(System.out, false);
		mStateObservers = new ArrayList<>();
		mAutoRunner = new AutoRunner();
		mConstructor = new Constructor();
		mSubsystemsManager = new SubsystemsManager();
		mObservationAccumulator = new ObservationAccumulator();
		mManagedLoops = new ManagedLoops();
	}

	private class InternalAccessor {
		private void print(Object object) {
			mObservationAccumulator.print(object);
		}

		private void println(Object object) {
			mObservationAccumulator.print(object);
			mObservationAccumulator.print("\n");
		}

		private void prefixedPrintln(Object object) {
			mObservationAccumulator.print(getRobotPrefix());
			println(object);
		}

		private void reportState(Object owner, StateType stateType, Object state) {
			mObservationAccumulator.reportState(owner, stateType, state);
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

		synchronized void sendAll() {
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

	private class Constructor {
		private void preConfigure() {
			printRobotPrefix();
			Class<?> mappingClass = MapInspector.getMappingClass(getPackageName());
			if (mappingClass != null) {
				setMapping(mappingClass);
			}
			onCreate();
		}

		private void initRobotSystem() {
			_accessor = mAccessor;
			Class<?> subsystemClass = MapInspector.reflectSubsystemsClass(mMappingClass);
			mSubsystemsManager.setSubsystems(MapInspector.createReflectedSubsystems(subsystemClass));
			mSubsystemsManager.constructAll();
			mManagedLoops.addLoopSources(mSubsystemsManager, mObservationAccumulator::sendAll, mOIUpdater);
			mManagedLoops.registerInitialLoops();
		}
	}
}
