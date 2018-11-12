package ca.warp7.frc.core;

/**
 * Defines a periodic procedure getting input from the controllers
 */

@FunctionalInterface
public interface IControls {

    int Pressed = 9;
    int HeldDown = 19;
    int Released = 10;
    int KeptUp = 0;

    void mainPeriodic();

    default void testPeriodic() {
    }
}
