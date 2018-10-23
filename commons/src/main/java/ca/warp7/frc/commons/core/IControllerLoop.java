package ca.warp7.frc.commons.core;

/**
 * Defines a periodic procedure getting input from the controllers
 */
public interface IControllerLoop {
    void onRegister(Components components);

    void onPeriodic();
}
