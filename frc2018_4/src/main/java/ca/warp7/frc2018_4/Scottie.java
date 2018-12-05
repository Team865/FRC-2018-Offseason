package ca.warp7.frc2018_4;
import ca.warp7.frc.core.Robot;

public class Scottie extends Robot {
    public Scottie(){
        System.out.println("Hello me is Robit");
        loader.setAutoMode(AutoActions.nothing,15);
        setTeleop(new Controller());
    }

}