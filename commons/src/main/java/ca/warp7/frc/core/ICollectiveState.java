package ca.warp7.frc.core;

import java.util.Map;

@FunctionalInterface
public interface ICollectiveState {

    Map<String, Object> getCollection();

    @FunctionalInterface
    interface TransformFunction<T, R> {
        R apply(T t, T other);
    }
}
