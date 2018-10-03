package ca.warp7.frc.core;

import ca.warp7.frc.action_graph.IAction;

@SuppressWarnings("WeakerAccess")
public interface IAutoMode {
	IAction getMainAction();
}
