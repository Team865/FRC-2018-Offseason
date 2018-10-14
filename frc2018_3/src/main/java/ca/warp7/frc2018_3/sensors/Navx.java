package ca.warp7.frc2018_3.sensors;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

public class Navx {
    private AHRS ahrs;

    public Navx() {
        ahrs = new AHRS(SPI.Port.kMXP);

        if (!ahrs.isConnected()) {
            System.out.println("Navx is not Connected");
        } else if (ahrs.isCalibrating()) {
            System.out.println("Calibrating Navx");
        }
        ahrs.zeroYaw();
    }

    public double getAngle() {
        return ahrs.getAngle();
    }

    public void resetAngle() {
        ahrs.reset();
    }

    public double getPitch() {
        return ahrs.getPitch();
    }
}
