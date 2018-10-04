package ca.warp7.frc.core;

import ca.warp7.frc.observer.StateType;
import ca.warp7.frc.wpi_wrapper.IterativeRobotWrapper;

/**
 * Base class for managing the robot's general lifecycle,
 * as well as keeping track of the system's state
 */
public abstract class Robot extends IterativeRobotWrapper {

	private Runnable mOIUpdater;
	private Class<?> mMappingClass;
	private AutoRunner mAutoRunner;
	private StateAccumulator mStateAccumulator;
	private SubsystemsManager mSubsystemsManager;
	private ManagedLoops mManagedLoops;
	private InternalAccessor mInternalAccessor;

	protected Robot() {
		super();
		mAutoRunner = new AutoRunner();
		mSubsystemsManager = new SubsystemsManager();
		mStateAccumulator = new StateAccumulator();
		mManagedLoops = new ManagedLoops();
		mInternalAccessor = new InternalAccessor();
	}

	@Override
	public void robotInit() {
		super.robotInit();
		this.initRobotSystem();
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
		try {
			mAutoRunner.onStart();
		} catch (NoAutoException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void teleopInit() {
		super.teleopInit();
		mAutoRunner.onStop();
		mManagedLoops.onEnable();
	}

	@Override
	public void startCompetition() {
		this.configureStartCompetition();
		if (mMappingClass != null && mOIUpdater != null) {
			super.startCompetition();
		} else {
			System.err.println("Robot not set up");
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

	private void configureStartCompetition() {
		printRobotPrefix();
		Class<?> mappingClass = RobotMapInspector.getMappingClass(getPackageName());
		if (mappingClass != null) setMapping(mappingClass);
		onCreate();
	}

	private void initRobotSystem() {
		_accessor = mInternalAccessor;
		Class<?> subsystemClass = RobotMapInspector.reflectSubsystemsClass(mMappingClass);
		mSubsystemsManager.setSubsystems(RobotMapInspector.createReflectedSubsystems(subsystemClass));
		mSubsystemsManager.constructAll();
		mManagedLoops.addLoopSources(mSubsystemsManager, mStateAccumulator::sendAll, mOIUpdater);
		mManagedLoops.registerInitialLoops();
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

	private static InternalAccessor _accessor;

	private class InternalAccessor {
		private void print(Object object) {
			mStateAccumulator.print(object);
		}

		private void println(Object object) {
			mStateAccumulator.print(object);
			mStateAccumulator.print("\n");
		}

		private void prefixedPrintln(Object object) {
			mStateAccumulator.print(getRobotPrefix());
			println(object);
		}

		private void reportState(Object owner, StateType stateType, Object state) {
			mStateAccumulator.reportState(owner, stateType, state);
		}
	}
}
