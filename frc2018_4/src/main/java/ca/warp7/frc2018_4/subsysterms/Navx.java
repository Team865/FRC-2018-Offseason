package ca.warp7.frc2018_4.subsysterms;

import ca.warp7.frc.core.ISubsystem;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

public class Navx implements ISubsystem {
    private AHRS ahrs;

    @Override
    public void onConstruct() {
        ahrs = new AHRS(SPI.Port.kMXP);
    }

    AHRS getAhrs() {
        return ahrs;
    }
}
