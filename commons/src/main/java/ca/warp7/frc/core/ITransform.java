package ca.warp7.frc.core;

@FunctionalInterface
public
interface ITransform<T, R> {
    R apply(T t, T other);
}
