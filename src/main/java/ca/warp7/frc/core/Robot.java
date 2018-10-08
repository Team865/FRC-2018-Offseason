package ca.warp7.frc.core;

import ca.warp7.frc.comms.ReportType;
import ca.warp7.frc.wpi_wrapper.IterativeRobotWrapper;

/**
 * Base class for managing the robot's general lifecycle,
 * as well as keeping track of the system's state
 * <p>
 * This class is an extension of IterativeRobot
 */

public abstract class Robot extends IterativeRobotWrapper {

	/**
	 * Contains an array of subsystems.
	 * See {@link SubsystemsManager} for details
	 */
	private SubsystemsManager mSubsystemsManager;

	/**
	 * Keeps track of the robot's looper and loops
	 * See {@link ManagedLoops} for details
	 */
	private ManagedLoops mManagedLoops;

	/**
	 * Runs autos. See {@link AutoRunner} for details
	 */
	private AutoRunner mAutoRunner;

	/**
	 * Keep track of state reporting and sending
	 * See {@link StateAccumulator} for details
	 */
	private StateAccumulator mStateAccumulator;

	/**
	 * A procedure passed into the {@link ManagedLoops} runner
	 * called in teleop. Should get input from controllers and
	 * pass them to subsystems
	 */
	private Runnable mOIRunner;

	/**
	 * Class representing all the common constants of the
	 * robot. Subsystems are pulled from this class
	 * reflectively. See {@link RobotMapInspector}
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
	 * Call the onCreate method defined by the subclass
	 * to try to get the mapping class and OI updater.
	 * If successful, call the normal startCompetition
	 * to start everything
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
	 * Performs all actual setup steps
	 * 1. Create an instance accessor for state reporting
	 * 2. Reflect the subsystems in the mapping class and hand it to the manager
	 * 3. Pass references to the managed loops so that loops may be defined and registered
	 * 4. Start the observation loop that runs both when enabled and disabled
	 */
	@Override
	public final void robotInit() {
		super.robotInit();
		sAccessor = new InstanceAccessor();
		mSubsystemsManager.setSubsystems(RobotMapInspector.getSubsystems(mMappingClass));
		mSubsystemsManager.constructAll();
		mSubsystemsManager.zeroAllSensors();
		mManagedLoops.setSource(mSubsystemsManager, mStateAccumulator::sendAll, mOIRunner);
		mManagedLoops.startObservers();
	}

	/**
	 * Disables the auto thread, managed loops and resets the subsystems
	 * when the robot should disable
	 */
	@Override
	public final void disabledInit() {
		super.disabledInit();
		mAutoRunner.onStop();
		mManagedLoops.disable();
		mSubsystemsManager.disableAll();
	}

	/**
	 * Enables the managed loops and tries to start auto
	 * Prints error if there isn't an auto mod available
	 */
	@Override
	public final void autonomousInit() {
		super.autonomousInit();
		mSubsystemsManager.onAutonomousInit();
		mManagedLoops.enable();
		try {
			mAutoRunner.onStart();
		} catch (NoAutoException e) {
			System.err.println(e.getMessage());
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
		mManagedLoops.enable();
	}

	/**
	 * Method should call the following three methods
	 * to set the robot's subsystems, the OI runner, and the
	 * auto mode
	 */
	protected abstract void onCreate();

	/**
	 * Sets the Operator Input runner.
	 *
	 * @param OIRunner The runner that is called periodically during Teleop.
	 *                 It should get input from controllers and
	 *                 pass them to subsystems
	 */
	protected final void setOIRunner(Runnable OIRunner) {
		mOIRunner = OIRunner;
	}

	/**
	 * Sets the auto mode for the robot
	 *
	 * @param mode the mode to run. See {@link IAutoMode}
	 */
	@SuppressWarnings("SameParameterValue")
	protected final void setAutoMode(IAutoMode mode) {
		mAutoRunner.setAutoMode(mode);
	}

	/**
	 * Sets the mapping class of the robot
	 *
	 * @param mappingClass Class representing all the common constants of the robot.
	 */
	@SuppressWarnings("WeakerAccess")
	protected final void setMapping(Class<?> mappingClass) {
		mMappingClass = mappingClass;
	}

	/**
	 * Similar to {@link java.io.PrintStream#print(Object)}
	 *
	 * @param object the object to print
	 */
	@SuppressWarnings({"unused", "WeakerAccess"})
	public static void print(Object object) {
		sAccessor.print(object);
	}

	/**
	 * Similar to {@link java.io.PrintStream#println(Object)}
	 *
	 * @param object the object to print
	 */
	@SuppressWarnings({"unused", "WeakerAccess"})
	public static void println(Object object) {
		sAccessor.println(object);
	}

	/**
	 * Similar to {@link java.io.PrintStream#println(Object)},
	 * except with the robot name as prefix
	 *
	 * @param object the object to print
	 */
	public static void prefixedPrintln(Object object) {
		sAccessor.prefixedPrintln(object);
	}

	/**
	 * Reports a state object
	 *
	 * @param owner      the owner of the state object, which can modify it
	 * @param reportType Either STATE_INPUT or STATE_CURRENT
	 * @param state      The state object to be reflected
	 */
	public static void reportState(Object owner, ReportType reportType, Object state) {
		sAccessor.reportState(owner, reportType, state);
	}

	/**
	 * A static accessor that refers to the Robot instance
	 */
	private static InstanceAccessor sAccessor;

	/**
	 * Accessor class, containing relay methods to the Robot and its variables
	 */
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

		private void reportState(Object owner, ReportType reportType, Object state) {
			mStateAccumulator.reportState(owner, reportType, state);
		}
	}
}
