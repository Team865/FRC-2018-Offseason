package ca.warp7.frc.commons.wpi_wrapper;

class LocalXboxController extends edu.wpi.first.wpilibj.XboxController {
    /**
     * Construct an instance of a joystick. The joystick index is the USB port on the drivers
     * station.
     *
     * @param port The port on the Driver Station that the joystick is plugged into.
     */
    LocalXboxController(int port) {
        super(port);
    }
}
