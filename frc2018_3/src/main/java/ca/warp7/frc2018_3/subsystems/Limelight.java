package ca.warp7.frc2018_3.subsystems;

import ca.warp7.frc.core.IComponent;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

@SuppressWarnings("FieldCanBeLocal")
public class Limelight implements IComponent {

    private NetworkTable table;

//    private double targetD;
//    private boolean hasTarget;
    private double xOffset;
    private double yOffset;
    private double area;
    private double skew;
    private double LEDMode;
    private double camMode;
    private int pipeline;
    private int pipelineNumber = 2;

    @Override
    public void onConstruct() {
        table = NetworkTableInstance.getDefault().getTable("limelight");
    }

    public double getXOffset() {
        xOffset = table.getEntry("tx").getDouble(0);
        return xOffset;
    }

    public double getYOffset() {
        yOffset = table.getEntry("ty").getDouble(0);
        return yOffset;
    }

    public double getArea() {
        area = table.getEntry("ta").getDouble(0);
        return area;
    }

    public double getSkew() {
        skew = table.getEntry("ts").getDouble(0);
        return skew;
    }

    private boolean foundObject() {
        int found = (int) table.getEntry("tv").getDouble(0);
        return found == 1;
    }

    private double getLEDMode() {
        LEDMode = table.getEntry("ledMode").getDouble(1);
        return LEDMode;
    }

    private double getCamMode() {
        camMode = table.getEntry("camMode").getDouble(0);
        return camMode;
    }

    private int getNetworkPipeline() {
        return (int) table.getEntry("pipeline").getDouble(0);
    }

    int getPipeline() {
        return pipeline;
    }

    public void switchLED() {
        if (getLEDMode() == 0) {
            table.getEntry("ledMode").setDouble(1);
            SmartDashboard.putString("LED Mode", "Off");
        } else if (getLEDMode() == 1) {
            table.getEntry("ledMode").setDouble(0);
            SmartDashboard.putString("LED Mode", "On");
        } else if (getLEDMode() == 2) {
            table.getEntry("ledMode").setDouble(1);
            SmartDashboard.putString("LED Mode", "Off");
        }
    }

    public void switchCamera() {
        if (getCamMode() == 0) {
            table.getEntry("camMode").setDouble(1);
            SmartDashboard.putString("Camera Mode", "Camera");
        } else if (getCamMode() == 1) {
            table.getEntry("camMode").setDouble(0);
            SmartDashboard.putString("Camera Mode", "Vision");
        }
    }

    private void setPipeline(int pipeline) {
        table.getEntry("pipeline").setDouble(pipeline);
        this.pipeline = pipeline;
        SmartDashboard.putNumber("Camera Mode", pipeline);
    }

    public void multiPipeline() {
        if (!foundObject())
            setPipeline((getNetworkPipeline() + 1) % pipelineNumber);
    }
}