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
 * @version 3.1 (Revision #12) Revised 11/14/2018
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
     */
    void onStart();

    /**
     * Returns whether or not the code has finished execution.
     * <b>IMPORTANT:</b> We must make sure the changes in onStart
     * actually get applied to subsystems because updateState
     * will not run on the first call of this method after onStart
     *
     * @return boolean
     */
    default boolean shouldFinish() {
        return true;
    }

    /**
     * Called by runAction in AutoModeBase iteratively until isFinished returns true.
     * Iterative logic lives in this method. <b>PLEASE NO WHILE LOOPS</b>
     */
    default void onUpdate() {
    }

    /**
     * Run code once when the action finishes, usually for clean up
     */
    default void onStop() {
    }

    interface API extends IAction {

        API asyncAll(IAction... actions);

        API asyncAny(IAction... actions);

        API asyncInverse(IAction... actions);

        API asyncMaster(IAction master, IAction... slaves);

        API await(Predicate predicate);

        API exec(Consumer consumer);

        API iterate(Consumer consumer);

        API runIf(Predicate predicate, IAction ifAction, IAction elseAction);

        API queue(IAction... actions);

        default API async(IAction... actions) {
            return asyncAll(actions);
        }

        default API asyncUntil(Predicate predicate, IAction... actions) {
            return asyncMaster(await(predicate), actions);
        }

        default API broadcast(String... triggers) {
            return exec(Function.broadcastAll(triggers));
        }

        default API broadcastWhen(Predicate predicate, String... triggers) {
            return await(predicate).broadcast(triggers);
        }

        default API onlyIf(Predicate predicate, IAction action) {
            return runIf(predicate, action, null);
        }

        default API waitFor(double seconds) {
            return await(Function.elapsed(seconds));
        }

        default API when(Predicate predicate, IAction... actions) {
            return await(predicate).queue(actions);
        }

    }

    abstract class Function {

        protected static Predicate triggeredOnce(String name) {
            return d -> d.getResources().getBroadcastCount(name) == 1;
        }

        protected static Predicate triggeredRepeat(String name) {
            return d -> d.getResources().getBroadcastCount(name) > 1;
        }

        protected static Predicate triggeredAll(String name) {
            return d -> d.getResources().getBroadcastCount(name) == d.getResources().getBroadcastSources(name);
        }

        protected static Predicate triggeredSome(String name, int times) {
            return d -> d.getResources().getBroadcastCount(name) == times;
        }

        protected static Predicate elapsed(double timeInSeconds) {
            return d -> !d.hasParent() || d.getParent().getDelegate().getElapsed() > timeInSeconds;
        }

        protected static Consumer broadcastAll(String... triggers) {
            return d -> Arrays.stream(triggers).forEach(trigger -> d.getResources().broadcast(trigger));
        }

        protected static Predicate atProgress(double progress) {
            return d -> d.hasProgressState() && d.getNumericalProgress() > progress;
        }

        protected static Predicate atPercent(int percent) {
            int progress = Math.min(0, Math.max(100, percent));
            return d -> d.hasProgressState() && d.getPercentProgress() > progress;
        }
    }

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


    interface Consumer {
        void accept(Delegate delegate);
    }

    interface Predicate {
        boolean test(Delegate delegate);
    }

    /**
     * An internal interface that keep track of time.
     * This makes the Action API independent of WPILib's timer api
     */
    @FunctionalInterface
    interface ITimer {

        /**
         * Gets the time since the Robot is started
         *
         * @return time in seconds
         */
        double getTime();

    }

    @SuppressWarnings("unused")
    interface Delegate {

        double getElapsed();

        int getDetachDepth();

        Parent getParent();

        void interrupt();

        Resources getResources();

        default double totalElapsed() {
            return getResources().getTotalElapsed();
        }

        default boolean hasRemainingTime() {
            return false;
        }

        default double getRemainingTime() {
            return 0;
        }

        default boolean hasProgressState() {
            return false;
        }

        default double getPercentProgress() {
            return 0;
        }

        default double getNumericalProgress() {
            return 0;
        }

        default boolean hasParent() {
            return getParent() != null;
        }
    }

    /**
     * Essentially a wrapper to create an action, no other mechanisms
     * included. There are other classes that help with scheduling
     * mechanisms
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

    interface Parent {

        default List<IAction> getActionQueue() {
            return null;
        }

        default Delegate getDelegate() {
            return null;
        }

        default int size() {
            return 0;
        }
    }

    @SuppressWarnings("unused")
    interface Resources {

        void put(String name, Object value);

        Object get(String name, Object defaultVal);

        int getBroadcastCount(String trigger);

        int getBroadcastSources(String trigger);

        void addBroadcastSources(String... trigger);

        String broadcastName(String trigger);

        ITimer getActionTimer();

        void setActionTimer(ITimer timer);

        double getTime();

        double getTotalElapsed();

        void startTimer();

        default void broadcast(String trigger) {
            String name = broadcastName(trigger);
            put(name, getInt(name, 0) + 1);
        }

        default double getDouble(String name, double defaultVal) {
            Object var = get(name, null);
            if (var instanceof Double) return (double) var;
            return defaultVal;
        }

        default int getInt(String name, int defaultVal) {
            Object var = get(name, null);
            if (var instanceof Integer) return (int) var;
            return defaultVal;
        }

        default String getString(String name, String defaultVal) {
            Object var = get(name, null);
            if (var instanceof String) return (String) var;
            return defaultVal;
        }
    }
}
