package ca.warp7.frc;

/**
 * Defines a periodic procedure getting input from the controllers
 */
public interface ControlLoop {

    int HeldDown = 3;
    int Pressed = 2;
    int Released = 1;
    int KeptUp = 0;

    void setup();

    void periodic();
}
