package ca.warp7.frc_commons.scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Composite action, running all sub-actions at the same time All actions are started then updated until all actions
 * report being done. {@link ActionSeries}
 *
 * @author Team 254, modified by Team 865
 */

public class ParallelActions implements IAction {

	private final List<IAction> mActions;

	public ParallelActions(List<IAction> actions) {
		mActions = new ArrayList<>(actions);
	}

	public ParallelActions(IAction... actions) {
		mActions = Arrays.asList(actions);
	}

	@Override
	public void onStart() {
		for (IAction action : mActions) {
			action.onStart();
		}
	}

	@Override
	public boolean shouldFinish() {
		for (IAction action : mActions) {
			if (!action.shouldFinish()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void onUpdate() {
		for (IAction action : mActions) {
			action.onUpdate();
		}
	}

	@Override
	public void onStop() {
		for (IAction action : mActions) {
			action.onStop();
		}
	}
}