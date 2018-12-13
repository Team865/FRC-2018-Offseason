package ca.warp7.frc.next;

/**
 * Defines a periodic procedure getting input from the controllers
 */
public interface ControlLoop {

    int Pressed = 9;
    int HeldDown = 19;
    int Released = 10;
    int KeptUp = 0;

    void setup();

    void periodic();
}
