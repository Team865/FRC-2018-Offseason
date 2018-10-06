package ca.warp7.frc.core;

/**
 * This class defines a major component of the robot.
 *
 * <p> A good example of a subsystem is the drive train, or a claw if the robot has one. </p>
 *
 * <p> All motors should be a part of a subsystem. For instance, all the wheel motors should be a
 * part of some kind of "Drive train" subsystem. </p>
 *
 * <p>This interface defines all the callbacks a subsystem should have.
 * It is managed by the {@link SubsystemsManager}</p>
 *
 * <p>It is very important that implementations of these methods are synchronized
 * because they are most often called from different threads. It's also important
 * that the periodic functions are not blocking operations</p>
 *
 * <p>A good implementation strategy is define specific object classes that holds
 * the input state and current state of the subsystem respectively</p>
 */

public interface ISubsystem {

	/**
	 * Called when constructing the subsystem
	 *
	 * This method should connect any hardware components
	 * such as motors, gyros, and encoders and perform any
	 * initial settings such as their direction
	 */
	void onConstruct();

	/**
	 * Called when the robot is disabled
	 *
	 * Should reset everything having to do with output
	 */
	void onDisabled();

	/**
	 * Called periodically for the subsystem to receive
	 * inputs. This is called from the Input Looper.
	 * <p>
	 * All sensor reading should be done in this method.
	 * Note that this method may still be called while
	 * the robot is disabled, so extra care should be made
	 * that it performs no outputting
	 */
	void onInputLoop();

	/**
	 * Called periodically for the subsystem to send outputs.
	 * This is called from the State Change Looper.
	 *
	 * This function is guaranteed to not be called when the
	 * robot is disabled
	 */
	void onOutputLoop();

	/**
	 * Called periodically for the subsystem to convert its
	 * inputs into the current state of the subsystem,
	 * including performing necessary calculations.
	 * This is called from the State Change Looper.
	 *
	 * This function is guaranteed to not be called when the
	 * robot is disabled. It is possible that this method
	 * is not the only state-updating runner for this subsystem
	 * in the State Change Looper, as other runners may have
	 * specific tasks worth separating out and are non-conflicting
	 */
	void onUpdateState();

	/**
	 * Called periodically for the subsystem to report its state,
	 * which could involve printing or sending to the SmartDashboard.
	 *
	 * This runs at a slower rate than the other periodic methods.
	 * See {@link ManagedLoops}. It also runs regardless of whether the
	 * Robot is enabled or disabled
	 */
	void onReportState();
}
