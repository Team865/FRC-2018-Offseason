package ca.warp7.frc.commons.core;

import java.util.List;

/**
 * Defines a periodic procedure getting input from the controllers
 */
public interface IControllerLoop {

    List<IController> onCreateControllers();

    void onPeriodic();
}
