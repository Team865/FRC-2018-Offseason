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
 * <p>
 * Subinterfaces define a declarative, chain-able API syntax for scheduling autos.
 *
 * @author Team 865
 * @version 3.2 (Revision #12) Revised 11/14/2018
 * @since 1.0
 */
@SuppressWarnings("ALL")
@FunctionalInterface
public interface IAction {

    /**
     * Run code once when the action is started, usually for set up.
     * This method must be called first before shouldFinish is called.
     * <p>
     * This method is the only non-default one, making it a functional interface
     * that can be used t create singleton actions
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
     * Called by runAction in AutoModeBase iteratively until isFinished returns true.
     * Iterative logic lives in this method. <b>PLEASE NO WHILE LOOPS</b>
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
     * @since 2.0
     */
    interface API extends IAction {

        /**
         * @since 2.0
         */
        API asyncAll(IAction... actions);

        /**
         * @since 2.0
         */
        API asyncAny(IAction... actions);

        /**
         * @since 2.0
         */
        API asyncInverse(IAction... actions);

        /**
         * @since 2.0
         */
        API asyncMaster(IAction master, IAction... slaves);

        /**
         * @since 2.0
         */
        API await(Predicate predicate);

        /**
         * @since 2.0
         */
        API exec(Consumer consumer);

        /**
         * @since 2.0
         */
        API iterate(Consumer consumer);

        /**
         * @since 2.0
         */
        API runIf(Predicate predicate, IAction ifAction, IAction elseAction);

        /**
         * @since 2.0
         */
        API queue(IAction... actions);

        /**
         * @since 2.0
         */
        default API async(IAction... actions) {
            return asyncAll(actions);
        }

        /**
         * @since 2.0
         */
        default API asyncUntil(Predicate predicate, IAction... actions) {
            return asyncMaster(await(predicate), actions);
        }

        /**
         * @since 2.0
         */
        default API broadcast(String... triggers) {
            return exec(Function.broadcastAll(triggers));
        }

        /**
         * @since 2.0
         */
        default API broadcastWhen(Predicate predicate, String... triggers) {
            return await(predicate).broadcast(triggers);
        }

        /**
         * @since 2.0
         */
        default API onlyIf(Predicate predicate, IAction action) {
            return runIf(predicate, action, null);
        }

        /**
         * @since 2.0
         */
        default API waitFor(double seconds) {
            return await(Function.elapsed(seconds));
        }

        /**
         * @since 2.0
         */
        default API when(Predicate predicate, IAction... actions) {
            return await(predicate).queue(actions);
        }
    }

    /**
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
            return d -> !d.hasParent() || d.getParent().getDelegate().getElapsed() > timeInSeconds;
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
     * @since 2.0
     */

    abstract class Head extends Function implements API {

        @Override
        public API asyncAll(IAction... actions) {
            return queue().asyncAll(actions);
        }

        @Override
        public API asyncAny(IAction... actions) {
            return queue().asyncAny(actions);
        }

        @Override
        public API asyncInverse(IAction... actions) {
            return queue().asyncInverse(actions);
        }

        @Override
        public API asyncMaster(IAction master, IAction... slaves) {
            return queue().asyncMaster(master, slaves);
        }

        @Override
        public API await(Predicate predicate) {
            return queue().await(predicate);
        }

        @Override
        public API broadcast(String... triggers) {
            return queue().broadcast(triggers);
        }

        @Override
        public API exec(Consumer consumer) {
            return queue().exec(consumer);
        }

        @Override
        public API iterate(Consumer consumer) {
            return queue().iterate(consumer);
        }

        @Override
        public API runIf(Predicate predicate, IAction ifAction, IAction elseAction) {
            return queue().runIf(predicate, ifAction, elseAction);
        }

        @Override
        public void onStart() {
        }
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
         * @since 2.0
         */
        boolean test(Delegate delegate);
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
     * @since 2.0
     */
    interface Delegate {

        /**
         * @since 2.0
         */
        double getElapsed();

        /**
         * @since 2.0
         */
        int getDetachDepth();

        /**
         * @since 2.0
         */
        Parent getParent();

        /**
         * @since 2.0
         */
        void interrupt();

        /**
         * @since 2.0
         */
        Resources getResources();

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
    }

    /**
     * Essentially a wrapper to create an action, no other mechanisms
     * included. There are other classes that help with scheduling
     * mechanisms
     *
     * @since 1.0
     */

    @FunctionalInterface
    interface Mode {

        /**
         * Procedure to fetch the main action of the mode
         *
         * @return the action
         */
        IAction getAction();
    }

    /**
     * @since 2.0
     */
    interface Parent {

        /**
         * @since 2.0
         */
        default List<IAction> getActionQueue() {
            return null;
        }

        /**
         * @since 2.0
         */
        default Delegate getDelegate() {
            return null;
        }

        /**
         * @since 2.0
         */
        default int size() {
            return 0;
        }
    }

    /**
     * @since 2.0
     */
    interface Resources {

        /**
         * @since 2.0
         */
        void put(String name, Object value);

        /**
         * @since 2.0
         */
        Object get(String name, Object defaultVal);

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
        default double getDouble(String name, double defaultVal) {
            Object var = get(name, null);
            if (var instanceof Double) return (double) var;
            return defaultVal;
        }

        /**
         * @since 2.0
         */
        default int getInt(String name, int defaultVal) {
            Object var = get(name, null);
            if (var instanceof Integer) return (int) var;
            return defaultVal;
        }

        /**
         * @since 2.0
         */
        default String getString(String name, String defaultVal) {
            Object var = get(name, null);
            if (var instanceof String) return (String) var;
            return defaultVal;
        }
    }
}
