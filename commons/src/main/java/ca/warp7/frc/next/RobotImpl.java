package ca.warp7.frc.next;

class RobotImpl {


    // TODO and GOALS for 2019
    //  - capture IO streams
    //  - robot will subclass TimedRobot and call static methods
    //  - Ability to run actions from Teleop
    //  - Reduce IoC for clarity

    // TODO Commons Goals:
    //  - To report to DS
    //  - To facilitate action scheduling
    //  - Get data from controllers

    static Class getCaller() {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[3];
        try {
            return Class.forName(caller.getClassName());
        } catch (ClassNotFoundException e) {
            return RobotImpl.class;
        }
    }
}
