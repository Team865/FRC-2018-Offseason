package ca.warp7.frc2018_2.misc;

import ca.warp7.frc2018_2.subsystems.Limelight;

public class LimelightPhotosensor {
    private Limelight limelight;

    private boolean found = false;
    private int pipeline;

    public LimelightPhotosensor(Limelight limelight, int pipeline) {
        this.limelight = limelight;
        this.pipeline = pipeline;
    }

    public void update() {
        found = limelight.getPipeline() == pipeline && limelight.foundObject();
    }

    public boolean isTriggered() {
        return found;
    }
}
