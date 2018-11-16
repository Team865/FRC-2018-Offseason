package ca.warp7.action;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * An {@link IAction} defines any self contained action that can be executed by the robot.
 * An Action is the unit of basis for autonomous programs. Actions may contain anything,
 * which means we can run sub-actions in various ways, in combination with the start,
 * loo, end, and shouldFinish methods. An entire scheduling API is developed with this
 * interface as the basis
 * </p>
 *
 * <p>
 * {@link IAction} and its inner interfaces create an API framework for scheduling complex
 * action tasks in a variety of ways, especially useful for autonomous programming
 * </p>
 *
 * @author Team 865
 * @version 3.6 (Revision 21 on 11/16/2018)
 * @see Mode
 * @see ITimer
 * @see Consumer
 * @see Predicate
 * @see API
 * @see Delegate
 * @see Resources
 * @see Function
 * @see HeadClass
 * @since 1.0
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@FunctionalInterface
public interface IAction {


    /**
     * A wrapper to create an action that should be used to define auto modes,
     * since a mode may be created for multiple times during runtime
     *
     * @since 1.0
     */
    @FunctionalInterface
    interface Mode {

        /**
         * Fetches the main action of the mode to be run
         *
         * @return the action
         */
        IAction getAction();
    }


    /**
     * An internal interface that keep track of time.
     * This makes the Action API independent of WPILib's timer api
     *
     * @since 2.0
     */
    @FunctionalInterface
    interface ITimer {

        /**
         * Gets the time since the Robot is started
         *
         * @return time in seconds
         * @since 2.0
         */
        double getTime();
    }


    /**
     * Represents an operation that accepts a {@link Delegate} and returns no
     * result. Unlike most other functional interfaces, {@code Consumer} is expected
     * to operate via side-effects.
     *
     * @since 2.0
     */
    @FunctionalInterface
    interface Consumer {

        /**
         * Accepts an action delegate performs an action with it
         *
         * @since 2.0
         */
        void accept(Delegate delegate);
    }


    /**
     * Represents a predicate (boolean-valued function) of a delegate
     *
     * @since 2.0
     */
    @FunctionalInterface
    interface Predicate {

        /**
         * Returns a decision based on the referred action
         *
         * @since 2.0
         */
        boolean test(Delegate delegate);
    }


    /**
     * Run code once when the action is started, usually for set up.
     * This method must be called first before shouldFinish is called.
     * <p>
     * This method is the only non-default one in the {@link IAction}
     * interface, making it a functional interface that can be used to
     * create singleton actions
     *
     * @since 1.0
     */
    void onStart();


    /**
     * Returns whether or not the code has finished execution.
     * <b>IMPORTANT:</b> We must make sure the changes in onStart
     * actually get applied to subsystems because updateState
     * will not run on the first call of this method after onStart
     *
     * @return boolean
     * @since 1.0
     */
    default boolean shouldFinish() {
        return true;
    }


    /**
     * Periodically updates the action
     *
     * @since 1.0
     */
    default void onUpdate() {
    }


    /**
     * Run code once when the action finishes, usually for clean up
     *
     * @since 1.0
     */
    default void onStop() {
    }


    /**
     * {@link API} defines the general syntax for expressing complex actions,
     * and defines the following properties:
     * <ul>
     * <li><b>Chain-able:</b> All methods of the API object returns the API object itself</li>
     * <li><b>Hierarchical:</b> All API objects are actions themselves </li>
     * <li><b>List-based: </b> Most methods of the API accepts a vararg list of a actions as arguments</li>
     * </ul>
     *
     * @since 2.0
     */
    interface API extends IAction {

        /**
         * Starts a list of action in parallel, and finish when all of the actions
         * are finished and stops
         *
         * @param actions A list of actions to run
         * @return The API state after the method operation has been queued to the previous state
         * @since 2.0
         */
        API asyncAll(IAction... actions);

        /**
         * Starts a list of action in parallel, and finish when any of the actions
         * are finished and stops
         *
         * @param actions A list of actions to run
         * @return The API state after the method operation has been queued to the previous state
         * @since 2.0
         */
        API asyncAny(IAction... actions);

        /**
         * Attempts to schedule actions such that they finish at the same time
         *
         * @param actions A list of actions to run
         * @return The API state after the method operation has been queued to the previous state
         * @since 2.0
         */
        API asyncInverse(IAction... actions);


        /**
         * Schedules a list of parallel actions according to the timing of a master.
         * This means slaves end when the master ends
         *
         * @param master the master action to sync to
         * @param slaves the slaves to run
         * @return The API state after the method operation has been queued to the previous state
         * @since 2.0
         */
        API asyncMaster(IAction master, IAction... slaves);

        /**
         * Waits the queue (do nothing) until a predicate is met
         *
         * @param predicate the predicate to test for
         * @return The API state after the method operation has been queued to the previous state
         * @since 2.0
         */
        API await(Predicate predicate);

        /**
         * Execute a function in reference to an action
         *
         * @param consumer the action delegate to consume
         * @return The API state after the method operation has been queued to the previous state
         * @since 2.0
         */
        API exec(Consumer consumer);

        /**
         * Iterate a function periodically in reference to an action
         *
         * @param consumer the action delegate to consume
         * @return The API state after the method operation has been queued to the previous state
         * @since 2.0
         */
        API iterate(Consumer consumer);

        /**
         * Run one of two actions depending on a condition
         *
         * @param predicate  the predicate to test for
         * @param ifAction   the action to run if the predicate is true
         * @param elseAction the action to run if the predicate is false
         * @return The API state after the method operation has been queued to the previous state
         * @since 2.0
         */
        API runIf(Predicate predicate, IAction ifAction, IAction elseAction);

        /**
         * Runs some actions in sequential order (i.e. the next action starts when the first
         * one finishes)
         *
         * @param actions A list of actions to run
         * @return The API state after the method operation has been queued to the previous state
         * @since 2.0
         */
        API queue(IAction... actions);

        /**
         * Starts a list of action in parallel, and finish when all of the actions
         * are finished and stops
         *
         * @param actions A list of actions to run
         * @return The API state after the method operation has been queued to the previous state
         * @since 2.0
         */
        default API async(IAction... actions) {
            return asyncAll(actions);
        }

        /**
         * Starts a list of action in parallel, and finish when a condition has been met
         *
         * @param actions A list of actions to run
         * @return The API state after the method operation has been queued to the previous state
         * @since 2.0
         */
        default API asyncUntil(Predicate predicate, IAction... actions) {
            return asyncMaster(await(predicate), actions);
        }

        /**
         * Broadcasts string triggers that can be received anywhere in the action tree
         *
         * @param triggers the triggers to broadcast
         * @return The API state after the method operation has been queued to the previous state
         * @since 2.0
         */
        default API broadcast(String... triggers) {
            return exec(Function.broadcastAll(triggers));
        }

        /**
         * Broadcasts string triggers that can be received anywhere in the action tree,
         * when a certain condition is met
         *
         * @param predicate the predicate to test for
         * @param triggers  the triggers to broadcast
         * @return The API state after the method operation has been queued to the previous state
         * @since 2.0
         */
        default API broadcastWhen(Predicate predicate, String... triggers) {
            return await(predicate).broadcast(triggers);
        }

        /**
         * Runs an action only if the condition is true
         *
         * @param predicate the predicate to test for
         * @param ifAction  the action to run if the predicate is true
         * @return The API state after the method operation has been queued to the previous state
         * @since 2.0
         */
        default API onlyIf(Predicate predicate, IAction ifAction) {
            return runIf(predicate, ifAction, null);
        }

        /**
         * Wait (do nothing) for a specified number of seconds
         *
         * @return The API state after the method operation has been queued to the previous state
         * @since 2.0
         */
        default API waitFor(double seconds) {
            return await(Function.elapsed(seconds));
        }

        /**
         * Queues some action the moment a condition becomes true
         *
         * @param predicate the predicate to test for
         * @param actions   A list of actions to run when the condition is true
         * @return The API state after the method operation has been queued to the previous state
         * @since 2.0
         */
        default API when(Predicate predicate, IAction... actions) {
            return await(predicate).queue(actions);
        }
    }


    /**
     * A {@link} Delegate represents an actions running state, which includes tracking its time
     * and managing its resources
     *
     * @since 2.0
     */
    interface Delegate {

        /**
         * Gets the length of time (in seconds) since this action started
         *
         * @return the elapsed time
         * @since 2.0
         */
        double getElapsed();

        /**
         * Gets the thread level of the action tree
         *
         * @return the depth of threads
         * @since 2.0
         */
        int getDetachDepth();

        /**
         * Gets the parent of the action
         *
         * @return the parent delegate object
         * @since 2.0
         */
        Delegate getParent();

        /**
         * Sends a stop signal immediately
         *
         * @since 2.0
         */
        void interrupt();

        /**
         * Gets the resources object shared with the action, or create one if
         * none can currently be found
         *
         * @return the resources object associated with the delegate
         * @since 2.0
         */
        Resources getResources();

        /**
         * Sets the name of the action
         *
         * @since 3.4
         */
        void setName(String name);

        /**
         * Gets the name of the action, if any
         *
         * @return name of the action
         * @since 3.4
         */
        String getName();

        /**
         * Gets a string that represents the action
         *
         * @return The string containing the name, class, and parent
         * @since 3.4
         */
        default String getActionSummary() {
            return String.format("Name: %s |Class: %s |Parent: %s",
                    getName(),
                    getClass().getSimpleName(),
                    getParent().getClass().getSimpleName());
        }

        /**
         * @since 2.0
         */
        default double totalElapsed() {
            return getResources().getTotalElapsed();
        }

        /**
         * @since 2.0
         */
        default boolean hasRemainingTime() {
            return false;
        }

        /**
         * @since 2.0
         */
        default double getRemainingTime() {
            return 0;
        }

        /**
         * @since 2.0
         */
        default boolean hasProgressState() {
            return false;
        }

        /**
         * @since 2.0
         */
        default double getPercentProgress() {
            return 0;
        }

        /**
         * @since 2.0
         */
        default double getNumericalProgress() {
            return 0;
        }

        /**
         * @since 2.0
         */
        default boolean hasParent() {
            return getParent() != null;
        }

        /**
         * @since 3.6
         */
        default List<IAction> getQueue() {
            return null;
        }
    }


    /**
     * Manages the resources of an action or an action tree, which includes
     * timers, variables, and broadcasts
     *
     * @since 2.0
     */
    interface Resources {

        /**
         * Associates the specified value with the specified key in this map
         * (optional operation).  If the map previously contained a mapping for
         * the key, the old value is replaced by the specified value.
         *
         * @param name  name with which the specified value is to be associated
         * @param value value to be associated with the specified key
         * @since 2.0
         */
        void put(String name, Object value);

        /**
         * Returns the value to which the specified key is mapped, or
         * {@code defaultValue} if this map contains no mapping for the key.
         *
         * @param name       the key whose associated value is to be returned
         * @param defaultValue the default mapping of the key
         * @since 2.0
         */
        Object get(String name, Object defaultValue);

        /**
         * @since 2.0
         */
        int getBroadcastCount(String trigger);

        /**
         * @since 2.0
         */
        int getBroadcastSources(String trigger);

        /**
         * @since 2.0
         */
        void addBroadcastSources(String... trigger);

        /**
         * @since 2.0
         */
        String broadcastName(String trigger);

        /**
         * @since 2.0
         */
        ITimer getActionTimer();

        /**
         * @since 2.0
         */
        void setActionTimer(ITimer timer);

        /**
         * @since 2.0
         */
        double getTime();

        /**
         * @since 2.0
         */
        double getTotalElapsed();

        /**
         * @since 2.0
         */
        void startTimer();

        /**
         * @since 2.0
         */
        default void broadcast(String trigger) {
            String name = broadcastName(trigger);
            put(name, getInt(name, 0) + 1);
        }

        /**
         * @since 2.0
         */
        default double getDouble(String name, double defaultValue) {
            Object var = get(name, null);
            if (var instanceof Double) return (double) var;
            return defaultValue;
        }

        /**
         * @since 2.0
         */
        default int getInt(String name, int defaultValue) {
            Object var = get(name, null);
            if (var instanceof Integer) return (int) var;
            return defaultValue;
        }

        /**
         * @since 2.0
         */
        default String getString(String name, String defaultValue) {
            Object var = get(name, null);
            if (var instanceof String) return (String) var;
            return defaultValue;
        }
    }


    /**
     * Provides a set of convenience creators for functional interfaces
     * that simplify the API
     *
     * @since 2.0
     */
    abstract class Function {

        /**
         * @since 2.0
         */
        protected static Predicate triggeredOnce(String name) {
            return d -> d.getResources().getBroadcastCount(name) == 1;
        }

        /**
         * @since 2.0
         */
        protected static Predicate triggeredRepeat(String name) {
            return d -> d.getResources().getBroadcastCount(name) > 1;
        }

        /**
         * @since 2.0
         */
        protected static Predicate triggeredAll(String name) {
            return d -> d.getResources().getBroadcastCount(name) == d.getResources().getBroadcastSources(name);
        }

        /**
         * @since 2.0
         */
        protected static Predicate triggeredSome(String name, int times) {
            return d -> d.getResources().getBroadcastCount(name) == times;
        }

        /**
         * @since 2.0
         */
        protected static Predicate elapsed(double timeInSeconds) {
            return d -> !d.hasParent() || d.getParent().getElapsed() > timeInSeconds;
        }

        /**
         * @since 2.0
         */
        protected static Consumer broadcastAll(String... triggers) {
            return d -> Arrays.stream(triggers).forEach(trigger -> d.getResources().broadcast(trigger));
        }

        /**
         * @since 2.0
         */
        protected static Predicate atProgress(double progress) {
            return d -> d.hasProgressState() && d.getNumericalProgress() > progress;
        }

        /**
         * @since 2.0
         */
        protected static Predicate atPercent(int percent) {
            int progress = Math.min(0, Math.max(100, percent));
            return d -> d.hasProgressState() && d.getPercentProgress() > progress;
        }
    }


    /**
     * Helper methods that allows creation of the API based on the API functions as a queue head.
     * This class does not implement the queue method to separate the implementation
     *
     * @since 2.0
     */
    abstract class HeadClass extends Function implements API {

        /**
         * {@inheritDoc}
         */
        @Override
        public API asyncAll(IAction... actions) {
            return queue().asyncAll(actions);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public API asyncAny(IAction... actions) {
            return queue().asyncAny(actions);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public API asyncInverse(IAction... actions) {
            return queue().asyncInverse(actions);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public API asyncMaster(IAction master, IAction... slaves) {
            return queue().asyncMaster(master, slaves);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public API await(Predicate predicate) {
            return queue().await(predicate);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public API broadcast(String... triggers) {
            return queue().broadcast(triggers);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public API exec(Consumer consumer) {
            return queue().exec(consumer);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public API iterate(Consumer consumer) {
            return queue().iterate(consumer);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public API runIf(Predicate predicate, IAction ifAction, IAction elseAction) {
            return queue().runIf(predicate, ifAction, elseAction);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onStart() {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public abstract API queue(IAction... actions);
    }
}
