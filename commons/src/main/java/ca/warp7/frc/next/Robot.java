package ca.warp7.frc.next;

import ca.warp7.action.IAction;
import ca.warp7.frc.core.IControls;
import ca.warp7.frc.core.XboxControlsState;

@SuppressWarnings("unused")
public interface Robot {

    interface System {

    }

    interface InputSystem extends System {
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
    }

    interface OutputSystem extends System {

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
         * <p>Called periodically for the subsystem to send outputs to its output device.
         * This method is called from the State Change Looper.</p>
         *
         * <p>This method is guaranteed to not be called when the robot is disabled.
         * Any output limits should be applied here for safety reasons.</p>
         */
        default void onOutput() {
        }
    }

    /**
     * Defines a periodic procedure getting input from the controllers
     */

    @FunctionalInterface
    interface Controls {

        int Pressed = 9;
        int HeldDown = 19;
        int Released = 10;
        int KeptUp = 0;

        void mainPeriodic();

        default void testPeriodic() {
        }
    }

    static void runAction(IAction action) {
    }

    static void runAction(System system, IAction action) {
    }

    static XboxControlsState getController(int port, boolean isActive) {
        return null;
    }

    static void initSystems(System... systems) {
    }

    static void disable() {
    }

    static void initAutonomousMode(IAction.Mode mode, double timeout) {
    }

    static void initTeleop(IControls teleopControls) {
    }

    static void initTest(IControls testControls) {
    }

    static double limit(double val, double lim) {
        lim = Math.abs(lim);
        return Math.max(-lim, Math.min(val, lim));
    }
}
