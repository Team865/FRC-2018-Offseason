package ca.warp7.frc.core;

import ca.warp7.frc.comms.StateType;
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
	private InstanceAccessor mInstanceAccessor;

	protected Robot() {
		super();
		mAutoRunner = new AutoRunner();
		mSubsystemsManager = new SubsystemsManager();
		mStateAccumulator = new StateAccumulator();
		mManagedLoops = new ManagedLoops();
		mInstanceAccessor = new InstanceAccessor();
	}

	@Override
	public final void startCompetition() {
		setMapping(RobotMapInspector.getMappingClass(getPackageName()));
		printRobotPrefix();
		onCreate();
		if (mMappingClass != null && mOIUpdater != null) {
			super.startCompetition();
		} else {
			System.err.println("Robot not set up");
		}
	}

	@Override
	public final void robotInit() {
		super.robotInit();
		_accessor = mInstanceAccessor;
		mSubsystemsManager.setSubsystems(RobotMapInspector.getSubsystems(mMappingClass));
		mSubsystemsManager.constructAll();
		mManagedLoops.setSource(mSubsystemsManager, mStateAccumulator::sendAll, mOIUpdater);
		mManagedLoops.onStartObservers();
	}

	@Override
	public final void disabledInit() {
		super.disabledInit();
		mAutoRunner.onStop();
		mManagedLoops.onDisable();
		mSubsystemsManager.resetAll();
	}

	@Override
	public final void autonomousInit() {
		super.autonomousInit();
		mManagedLoops.onEnable();
		try {
			mAutoRunner.onStart();
		} catch (NoAutoException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public final void teleopInit() {
		super.teleopInit();
		mAutoRunner.onStop();
		mManagedLoops.onEnable();
	}

	protected abstract void onCreate();

	protected final void setOIUpdater(Runnable OIUpdater) {
		mOIUpdater = OIUpdater;
	}

	@SuppressWarnings("SameParameterValue")
	protected final void setAutoMode(IAutoMode mode) {
		mAutoRunner.setAutoMode(mode);
	}

	@SuppressWarnings("WeakerAccess")
	protected final void setMapping(Class<?> mappingClass) {
		mMappingClass = mappingClass;
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

	private static InstanceAccessor _accessor;

	private class InstanceAccessor {
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
