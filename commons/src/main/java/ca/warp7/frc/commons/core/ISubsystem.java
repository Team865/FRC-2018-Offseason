package ca.warp7.frc.commons.core;

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
 * class should be part of a components class so that it could be reflectively found by the
 * {@link Components}</p>
 *
 * <p>This interface defines all the callbacks a subsystem should have.
 * It is managed by the {@link SubsystemsManager} and called periodically by the
 * {@link LoopsManager} class during different phases of robot runtime</p>
 *
 * <p>All the methods except onConstruct are empty default methods. Choose the appropriate one to
 * implement. They will be called by the {@link SubsystemsManager} regardless which are implemented</p>
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


@SuppressWarnings("EmptyMethod")
public interface ISubsystem {

    /**
     * <p>Called when constructing the subsystem</p>
     *
     * <p>This method should connect any hardware components such as motors, gyros,
     * and encoders and perform any initial settings such as their direction.
     * This method should be used instead of class constructors since subsystems are often
     * created statically and we want to initialize in the proper order. In other words, this
     * is a "deferred" constructor</p>
     */
    void onConstruct();

    /**
     * <p>Called when the robot is disabled</p>
     *
     * <p>This method should reset everything having to do with output so as to put
     * the subsystem in a disabled state</p>
     */
    default void onDisabled(){
    }

    /**
     * <p>Called at the start of auto for initial setup</p>
     *
     * <p>Auto loops don't start until this method returns, therefore the implementation
     * must execute quickly</p>
     */
    default void onAutonomousInit() {
    }

    /**
     * <p>Called at the start of Teleop for initial setup</p>
     *
     * <p>Teleop loops don't start until this method returns, therefore the implementation
     * must execute quickly</p>
     */
    default void onTeleopInit() {
    }

    /**
     * <p>Called periodically for the subsystem to get measurements from its input devices.
     * This method is called from the Input Looper. All sensor reading should be done
     * in this method.</p>
     *
     * <p>When using input/current states, the measured values here should change
     * the subsystem's current state</p>
     *
     * <p>Note that this method may still be called while the robot is disabled, so
     * extra care should be made that it performs no outputting</p>
     */
    default void onMeasure(){
    }

    /**
     * <p>Called at the start for the subsystem to zero its sensors.
     * This method may by called by autonomous actions otherwise</p>
     */
    default void onZeroSensors(){
    }

    /**
     * <p>Called periodically for the subsystem to send outputs to its output device.
     * This method is called from the State Change Looper.</p>
     *
     * <p>This method is guaranteed to not be called when the robot is disabled.
     * Any output limits should be applied here for safety reasons.</p>
     */
    default void onOutput(){
    }

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
    default void onUpdateState(){
    }

    /**
     * <p>Called periodically for the subsystem to report its state, which could involve
     * printing or sending to the SmartDashboard.</p>
     *
     * <p>This runs at a slower rate than the other periodic methods.See {@link LoopsManager}.
     * It also runs regardless of whether the Robot is enabled or disabled.</p>
     */
    default void onReportState(){
    }

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

    /**
     * <p>Marks a the subsystems and components class of a robot</p>
     */
    @Target(ElementType.TYPE)
    @interface RobotComponentsPool {
    }
}
