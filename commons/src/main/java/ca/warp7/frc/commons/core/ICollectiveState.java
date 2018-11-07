package ca.warp7.frc.commons.core;

import java.util.Map;

public interface ICollectiveState {

    Map<String, Object> getCollection();

    @FunctionalInterface
    interface TransformFunction<T, R> {
        R apply(T t, T other);
    }
}