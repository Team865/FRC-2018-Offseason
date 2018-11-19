package ca.warp7.frc2017_2.auto.modes;

import ca.warp7.action.IAction;

public class NothingMode implements IAction.Mode {
    @Override
    public IAction getAction() {
        return new IAction() {
            @Override
            public void start() {
                System.out.println("NothingMode is doing nothing");
            }

            @Override
            public boolean shouldFinish() {
                return false;
            }

            @Override
            public void update() {
            }

            @Override
            public void stop() {
                System.out.println("NothingMode is done");
            }
        };
    }
}
