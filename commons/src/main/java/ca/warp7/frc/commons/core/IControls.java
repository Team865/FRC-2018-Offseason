package ca.warp7.frc.commons.core;

/**
 * Defines a periodic procedure getting input from the controllers
 */
public interface IControls {

    int Pressed = 9;
    int HeldDown = 19;
    int Released = 10;
    int KeptUp = 0;

    void periodic();
}
