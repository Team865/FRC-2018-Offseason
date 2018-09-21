package ca.warp7.frc.robot;

import ca.warp7.frc.utils.Pins;

public abstract class RobotCallback<C>
		extends ConstructRobot<C>
		implements IConstructCallback<C> {

	public RobotCallback() {
		callback(this);
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
	public void onInitWithConstruct(ConstructRobot<C> robot) {
		onInit();
	}
}