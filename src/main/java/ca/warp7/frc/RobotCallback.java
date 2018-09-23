package ca.warp7.frc;

public abstract class RobotCallback<C> extends BaseRobot<C> implements BaseRobot.ICallback<C> {

	public RobotCallback() {
		super();
		setCallback(this);
	}

	protected abstract void onInit();

	@Override
	public void onInit(BaseRobot<C> robot) {
		onInit();
	}
}