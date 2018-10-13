package ca.warp7.frc.commons.core;

import ca.warp7.frc.commons.wpi_wrapper.IterativeRobotWrapper;

/**
 * Base class for managing all the robot's stuff
 */

public abstract class Robot extends IterativeRobotWrapper {

	/**
	 * Contains an array of subsystems. See {@link SubsystemsManager} for details
	 */
	private final SubsystemsManager mSubsystemsManager;

	/**
	 * Keeps track of the robot's looper and loops. See {@link ManagedLoops} for details
	 */
	private final ManagedLoops mManagedLoops;

	/**
	 * Runs autos. See {@link AutoRunner} for details
	 */
	private final AutoRunner mAutoRunner;

	/**
	 * Keep track of state reporting and sending. See {@link StateAccumulator} for details
	 */
	private final StateAccumulator mStateAccumulator;

	/**
	 * A procedure passed into the {@link ManagedLoops} runner called in teleop
	 */
	private Runnable mOIRunner;

	/**
	 * Class representing all the common constants of the robot. Subsystems are pulled from
	 * this class reflectively. See {@link RobotMapInspector}
	 */
	private Class<?> mMappingClass;

	protected Robot() {
		super();
		mAutoRunner = new AutoRunner();
		mSubsystemsManager = new SubsystemsManager();
		mStateAccumulator = new StateAccumulator();
		mManagedLoops = new ManagedLoops();
	}

	/**
	 * Call the onCreate method defined by the subclass to try to get the mapping class and OI updater.
	 * If successful, call the normal startCompetition to start everything
	 */
	@Override
	public final void startCompetition() {
		setMapping(RobotMapInspector.getMappingClass(getPackageName()));
		printRobotPrefix();
		onCreate();
		if (mMappingClass != null && mOIRunner != null) {
			super.startCompetition();
		} else {
			System.err.println("Robot not set up");
		}
	}

	/**
	 * Performs all actual setup steps for the robot
	 */
	@Override
	public final void robotInit() {
		super.robotInit();
		sAccessor = new InstanceAccessor();
		mSubsystemsManager.setSubsystems(RobotMapInspector.getSubsystems(mMappingClass));
		mManagedLoops.setSource(mSubsystemsManager, mStateAccumulator::sendAll, mOIRunner);
		mSubsystemsManager.constructAll();
		mSubsystemsManager.zeroAllSensors();
		mSubsystemsManager.reportAll();
		mManagedLoops.startObservers();
	}

	/**
	 * Disables the auto thread, managed loops and resets the subsystems when the robot should disable
	 */
	@Override
	public final void disabledInit() {
		super.disabledInit();
		mAutoRunner.onStop();
		mManagedLoops.disable();
		mSubsystemsManager.disableAll();
	}

	/**
	 * Enables the managed loops and tries to start auto. Prints error if there isn't an auto mod available
	 */
	@Override
	public final void autonomousInit() {
		super.autonomousInit();
		mSubsystemsManager.onAutonomousInit();
		mManagedLoops.disableController();
		mManagedLoops.enable();
		try {
			mAutoRunner.onStart();
		} catch (final AutoRunner.NoAutoException e) {
			printError("The auto mode will do nothing!");
		}
	}

	/**
	 * Stops any autos and makes sure the loops are enabled
	 */
	@Override
	public final void teleopInit() {
		super.teleopInit();
		mAutoRunner.onStop();
		mSubsystemsManager.onTeleopInit();
		mManagedLoops.enableController();
		mManagedLoops.enable();
	}

	/**
	 * Nothing in the testing state
	 */
	@SuppressWarnings("EmptyMethod")
	@Override
	public void testInit() {
		super.testInit();
	}

	/**
	 * Method should call the following three methods to set the robot's subsystems, the OI
	 * runner, and the auto mode
	 */
	protected abstract void onCreate();

	/**
	 * Sets the Operator Input runner, which should get input from controllers and
	 * pass them to subsystems
	 */
	protected final void setOIRunner(Runnable OIRunner) {
		mOIRunner = OIRunner;
	}

	/**
	 * Sets the auto mode for the robot. See {@link IAutoMode}.
	 */
	@SuppressWarnings("SameParameterValue")
	protected final void setAutoMode(IAutoMode mode) {
		mAutoRunner.setAutoMode(mode);
	}

	/**
	 * Sets the class representing all the common constants of the robot.
	 */
	@SuppressWarnings("WeakerAccess")
	protected final void setMapping(Class<?> mappingClass) {
		mMappingClass = mappingClass;
	}

	/**
	 * Prints an object to System.out
	 */
	@SuppressWarnings("WeakerAccess")
	public static void printLine(Object object) {
		sAccessor.reportState(null, ReportType.PRINT_LINE, object);
	}

	/**
	 * Prints an error to System.err
	 */
	@SuppressWarnings({"unused", "WeakerAccess"})
	public static void printError(Object object) {
		sAccessor.reportState(null, ReportType.ERROR_PRINT_LINE, object);
	}

	/**
	 * See {@link StateAccumulator#reportState(Object, ReportType, Object)}
	 */
	public static void reportState(Object owner, ReportType reportType, Object state) {
		sAccessor.reportState(owner, reportType, state);
	}

	/**
	 * A static accessor that refers to the Robot instance
	 */
	private static InstanceAccessor sAccessor;

	private class InstanceAccessor {
		private void reportState(Object owner, ReportType reportType, Object state) {
			mStateAccumulator.reportState(owner, reportType, state);
		}
	}
}