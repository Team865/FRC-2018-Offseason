package ca.warp7.frc.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b> ISubsystem defines a major component system of the robot.
 * A good example of a subsystem is the drive train or a claw.</b>
 *
 * <p></p>
 *
 * <p> All motors should be a part of a subsystem. For instance, all the wheel motors should be a
 * part of some kind of "drive train" subsystem. Every motor should also be only in one subsystem,
 * not multiple ones.</p>
 *
 * <p>All sensor devices such as encoders and cameras should be in the subsystem where their
 * measured values have the most direct impact on the motors and/or other output devices of
 * the said subsystem.</p>
 *
 * <p>Each subsystem should have only one instance. They should be put in a Subsystems class
 * for clarification and should be declared final. No other parts of the code should attempt
 * to create subsystems. This could also be done using a singleton class</p>
 *
 * <p>For working in conjunction with the {@link Robot} class, the above said Subsystems
 * class should be part of a mapping class so that it could be reflectively found by the
 * {@link RobotMapInspector}</p>
 *
 * <p>This interface defines all the callbacks a subsystem should have.
 * It is managed by the {@link SubsystemsManager} and called periodically by the
 * {@link ManagedLoops} class during different phases of robot runtime</p>
 *
 * <p>A good implementation strategy is define specific object classes that holds the input state
 * and current state of the subsystem respectively. This interface defines some annotations in
 * order to make it clear about usage of this strategy</p>
 *
 * <p></p>
 *
 * <p>Finally, it is very important that implementations of these methods are <b>synchronized</b>
 * because they are most often called from different threads. It's also important
 * that the periodic functions are not blocking operations as to prevent leaking.</p>
 */

public interface ISubsystem {

	/**
	 * <p>Called when constructing the subsystem</p>
	 *
	 * <p>This method should connect any hardware components such as motors, gyros,
	 * and encoders and perform any initial settings such as their direction</p>
	 */
	void onConstruct();

	/**
	 * <p>Called when the robot is disabled</p>
	 *
	 * <p>This method should reset everything having to do with output so as to put
	 * the subsystem in a disabled state</p>
	 */
	void onDisabled();

	/**
	 * <p>Called at the start of auto for initial setup</p>
	 *
	 * <p>Auto loops don't start until this method returns, therefore the implementation
	 * must execute quickly</p>
	 */
	void onAutonomousInit();

	/**
	 * <p>Called at the start of Teleop for initial setup</p>
	 *
	 * <p>Teleop loops don't start until this method returns, therefore the implementation
	 * must execute quickly</p>
	 */
	void onTeleopInit();

	/**
	 * <p>Called periodically for the subsystem to receive inputs from its input devices.
	 * This method is called from the Input Looper. All sensor reading should be done
	 * in this method.</p>
	 *
	 * <p>Note that this method may still be called while the robot is disabled, so
	 * extra care should be made that it performs no outputting</p>
	 */
	void onInputLoop();

	/**
	 * <p>Called periodically for the subsystem to send outputs to its output device.
	 * This method is called from the State Change Looper.</p>
	 *
	 * <p>This method is guaranteed to not be called when the robot is disabled.
	 * Any output limits should be applied here for safety reasons.</p>
	 */
	void onOutputLoop();

	/**
	 * <p>Called periodically for the subsystem to convert its inputs into the current
	 * state of the subsystem, including performing necessary calculations.
	 * This is called from the State Change Looper.</p>
	 *
	 * <p>This function is guaranteed to not be called when the robot is disabled.
	 * It is possible that this method is not the only state-updating runner for this
	 * subsystem in the State Change Looper, as other runners may have specific tasks
	 * worth separating out and are non-conflicting. In such case, those runners will
	 * also be stopped when disabled.</p>
	 */
	void onUpdateState();

	/**
	 * <p>Called periodically for the subsystem to report its state, which could involve
	 * printing or sending to the SmartDashboard.</p>
	 *
	 * <p>This runs at a slower rate than the other periodic methods.See {@link ManagedLoops}.
	 * It also runs regardless of whether the Robot is enabled or disabled.</p>
	 */
	void onReportState();

	/**
	 * <p>This annotation marks a field to hold the current state of the system.
	 * For clarity, there should only be one of such fields</p>
	 */
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@interface CurrentStateField {
	}

	/**
	 * <p>This annotation marks a field to hold the input state of the system.
	 * For clarity, there should only be one of such fields</p>
	 */
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@interface InputStateField {
	}

	/**
	 * <p>Marks a method in a subsystem that directly alters its input state</p>
	 */
	@Target(ElementType.METHOD)
	@interface InputStateModifier {
	}
}
