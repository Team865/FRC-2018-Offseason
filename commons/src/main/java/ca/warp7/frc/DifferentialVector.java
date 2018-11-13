package ca.warp7.frc;

import ca.warp7.frc.core.ICollectiveState;
import ca.warp7.frc.core.ITransform;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Defines a differential wheel state on the drive train.
 * Can be scalars (position, velocity, acceleration or
 * objects (motor controllers)
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DifferentialVector<T> implements ICollectiveState {
    private T mLeft;
    private T mRight;
    private Map<String, Object> collectiveMap;

    public static DifferentialVector<Double> zeroes() {
        return new DifferentialVector<>(0d, 0d);
    }

    public DifferentialVector(T left, T right) {
        this.mLeft = left;
        this.mRight = right;
        collectiveMap = new HashMap<>();
    }

    public DifferentialVector(DifferentialVector<T> other) {
        mLeft = other.mLeft;
        mRight = other.mRight;
        collectiveMap = new HashMap<>();
    }

    public T getLeft() {
        return mLeft;
    }

    public T getRight() {
        return mRight;
    }

    @Override
    public Map<String, Object> getCollection() {
        collectiveMap.put("left", mLeft);
        collectiveMap.put("right", mRight);
        return collectiveMap;
    }

    public void apply(Consumer<? super T> action) {
        action.accept(mLeft);
        action.accept(mRight);
    }

    public <S> DifferentialVector<S> transformed(Function<T, S> f) {
        return new DifferentialVector<>(f.apply(mLeft), f.apply(mRight));
    }

    public void transform(Function<T, T> f) {
        mLeft = f.apply(mLeft);
        mRight = f.apply(mRight);
    }

    public <S> DifferentialVector<S> transformed(DifferentialVector<T> other, ITransform<T, S> f) {
        return new DifferentialVector<>(f.apply(mLeft, other.getLeft()), f.apply(mRight, other.getRight()));
    }

    public void transform(DifferentialVector<T> other, ITransform<T, T> f) {
        mLeft = f.apply(mLeft, other.getLeft());
        mRight = f.apply(mRight, other.getRight());
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

    public void set(DifferentialVector<T> differentialVector) {
        mLeft = differentialVector.getLeft();
        mRight = differentialVector.getRight();
    }
}
