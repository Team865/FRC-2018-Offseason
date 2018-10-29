package ca.warp7.frc.commons.core;

/**
 * Defines a periodic procedure getting input from the controllers
 */
public interface IControls {

    int Pressed = 0;
    int Released = 1;
    int HeldDown = 2;
    int KeptUp = 3;

    void periodic();
}
