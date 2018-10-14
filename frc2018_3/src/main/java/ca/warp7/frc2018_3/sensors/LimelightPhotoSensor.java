package ca.warp7.frc2018_3.sensors;

public class LimelightPhotoSensor {
    private Limelight limelight;

    private boolean found = false;
    private int pipeline;

    public LimelightPhotoSensor(Limelight limelight, int pipeline) {
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
