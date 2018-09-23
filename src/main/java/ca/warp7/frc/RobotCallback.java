package ca.warp7.frc;

public abstract class RobotCallback<C> extends Robot<C> implements Robot.ICallback<C> {

	public RobotCallback() {
		super();
		setCallback(this);
	}

	protected abstract void onInit();

	@Override
	public void onInit(Robot<C> robot) {
		onInit();
	}
}