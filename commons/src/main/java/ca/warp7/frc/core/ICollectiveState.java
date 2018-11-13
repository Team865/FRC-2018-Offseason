package ca.warp7.frc.core;

import java.util.Map;

@FunctionalInterface
public interface ICollectiveState {

    Map<String, Object> getCollection();

}
