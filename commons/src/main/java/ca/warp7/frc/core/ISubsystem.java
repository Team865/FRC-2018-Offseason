package ca.warp7.frc.core;

/**
 * <b> ISubsystem defines a robot subsystem. A good example of a subsystem is the drive train or
 * a claw. ISubsystem defines many important callbacks for making a subsystem</b>
 *
 * <p></p>
 *
 * <p> All motors should be a part of a subsystem. For instance, all the wheel motors should be a
 * part of some kind of "drive train" subsystem. Every motor should also be only in one subsystem,
 * they should also not be used by multiple subsystems.</p>
 *
 * <p>All sensor devices such as encoders and cameras should be in the subsystem where their
 * measured values have the most direct impact on the motors and/or other output devices of
 * the said subsystem, except in the case of classes extending {@link IComponent}, in which
 * components can be used by multiple subsystems</p>
 *
 * <p>Each subsystem should have only one instance. They should be put in a Subsystems class
 * for clarification and should be declared final. No other parts of the code should attempt
 * to create subsystems. This could also be done using a singleton class</p>
 *
 * <p>For working in conjunction with the {@link Robot} class, the above said Subsystems
 * class should be part of a components class so that it could be reflectively found by the
 * {@link Components}. Implementations of this interface should be put into their own package
 * with no other classes in it. If they extend another class that implemented this interface,
 * then the parent class should be abstract</p>
 *
 * <p>This interface defines all the callbacks a subsystem should have, including handlers for
 * input, output, init, disabled, resetting, debugging, and state updating. It is managed by
 * {@link Components} and called periodically by the {@link LoopsManager} class during
 * different phases of robot runtime</p>
 *
 * <p>All the methods except onConstruct are empty default methods. Choose the appropriate one to
 * implement. They will be called properly by {@link Components} regardless which
 * methods are implemented</p>
 *
 * <p>A good implementation strategy is define specific object classes that holds the input state
 * and current state of the subsystem respectively. This interface defines some annotations
 * markers in order to make it clear about usage of this strategy</p>
 *
 * <p></p>
 *
 * <p>Finally, it is very important that implementations of these methods are <b>synchronized</b>
 * because they are most often called from different threads. It's also important
 * that the periodic functions are not blocking operations as to prevent leaking.</p>
 *
 * @see Robot
 * @see Components
 */


@SuppressWarnings("EmptyMethod")
public interface ISubsystem extends IComponent {

    /**
     * <p>This annotation marks a field to hold the current state of the system.
     * The current state is the what the subsystem is currently doing and sensing.
     * For clarity, there should only be one of such fields</p>
     */
    @interface StateField {
    }

    /**
     * <p>This annotation marks a field to hold the input state of the system.
     * The input state is what the robot currently expects the subsystem to do.
     * For clarity, there should only be one of such fields</p>
     */
    @interface InputField {
    }

    /**
     * <p>Marks a method in a subsystem class that directly alters its input state,
     * either as an input from a controller or an autonomous action</p>
     */
    @interface InputModifier {
    }

    /**
     * <p>Called when the robot is disabled</p>
     *
     * <p>This method should reset everything having to do with output so as to put
     * the subsystem in a disabled state</p>
     */
    default void onDisabled() {
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
    default void onMeasure() {
    }

    /**
     * <p>Called at the start for the subsystem to zero its sensors.
     * In addition, this method may by called by autonomous actions</p>
     */
    default void onZeroSensors() {
    }

    /**
     * <p>Called periodically for the subsystem to send outputs to its output device.
     * This method is called from the State Change Looper.</p>
     *
     * <p>This method is guaranteed to not be called when the robot is disabled.
     * Any output limits should be applied here for safety reasons.</p>
     */
    default void onOutput() {
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
    default void onUpdateState() {
    }

    /**
     * <p>Called periodically for the subsystem to report its state, which could involve
     * printing or sending to the SmartDashboard. Any calls to {@link Robot#report(Object, StateType, Object)}</p>
     * should be made inside this method
     *
     * <p>This runs at a slower rate than the other periodic methods.See {@link LoopsManager}.
     * It also runs regardless of whether the Robot is enabled or disabled.</p>
     */
    default void onReportState() {
    }
}
