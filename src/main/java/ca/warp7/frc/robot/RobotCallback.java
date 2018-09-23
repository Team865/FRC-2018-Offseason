package ca.warp7.frc.robot;

import ca.warp7.frc.utils.Pins;

public abstract class RobotCallback<C>
		extends BaseRobot<C>
		implements IRobotCallback<C> {

	public RobotCallback() {
		setCallback(this);
	}

	protected Pins pins(int... n){
		return new Pins(n);
	}

	protected Pins channels(int... n){
		return pins(n);
	}

	protected Pins pin(int n){
		return pins(n);
	}

	protected abstract void onInit();

	@Override
	public void onInitWithBaseRobot(BaseRobot<C> robot) {
		onInit();
	}
}