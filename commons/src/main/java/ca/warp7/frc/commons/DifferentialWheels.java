package ca.warp7.frc.commons;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Defines a differential wheel state on the drive train.
 * Can be scalars (position, velocity, acceleration or
 * objects (motor controllers)
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DifferentialWheels<T> {
    private T mLeft;
    private T mRight;

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

    public void apply(Consumer<? super T> action) {
        action.accept(mLeft);
        action.accept(mRight);
    }

    public <S> DifferentialWheels<S> transform(Function<T, S> function) {
        return new DifferentialWheels<>(function.apply(mLeft), function.apply(mRight));
    }
}
