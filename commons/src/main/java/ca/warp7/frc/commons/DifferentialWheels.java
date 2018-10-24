package ca.warp7.frc.commons;

import ca.warp7.frc.commons.core.ICollectiveState;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Defines a differential wheel state on the drive train.
 * Can be scalars (position, velocity, acceleration or
 * objects (motor controllers)
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DifferentialWheels<T> implements ICollectiveState {
    private T mLeft;
    private T mRight;
    private Map<String, Object> collectiveMap;

    public DifferentialWheels(T left, T right) {
        this.mLeft = left;
        this.mRight = right;
    }

    public T getLeft() {
        return mLeft;
    }

    public T getRight() {
        return mRight;
    }

    @Override
    public Map<String, Object> getCollectiveMap() {
        collectiveMap.put("left", mLeft);
        collectiveMap.put("right", mRight);
        return collectiveMap;
    }

    public void apply(Consumer<? super T> action) {
        action.accept(mLeft);
        action.accept(mRight);
    }

    public <S> DifferentialWheels<S> transformed(Function<T, S> function) {
        return new DifferentialWheels<>(function.apply(mLeft), function.apply(mRight));
    }

    public void transform(Function<T, T> function) {
        mLeft = function.apply(mLeft);
        mRight = function.apply(mRight);
    }

    public void transform(DifferentialWheels<T> other, DependantFunction<T, T> function) {
        mLeft = function.apply(mLeft, other.getLeft());
        mRight = function.apply(mRight, other.getRight());
    }

    public void setLeft(T left) {
        mLeft = left;
    }

    public void setRight(T right) {
        mRight = right;
    }

    public void set(T left, T right) {
        mLeft = left;
        mRight = right;
    }

    public void set(DifferentialWheels<T> differentialWheels) {
        mLeft = differentialWheels.getLeft();
        mRight = differentialWheels.getRight();
    }


}
