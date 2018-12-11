package ca.warp7.frc2018_3;

import ca.warp7.action.IAction;
import ca.warp7.action.impl.ActionMode;
import ca.warp7.frc.PIDValues;
import ca.warp7.frc2018_3.actions.DriveForDistanceAction;
import ca.warp7.frc2018_3.actions.OuttakeCube;

import java.util.function.Supplier;

import static ca.warp7.frc2018_3.Components.drive;
import static ca.warp7.frc2018_3.Components.intake;

@SuppressWarnings("unused")
public class AutoExamples extends ActionMode {

    // TODO merge singleton async/other children
    //  action locks
    //  masters
    //  when to establish parent relationship
    //  when to count children
    //  singleton ActionDelegates
    //  onUpdate/onStop in the API (does nothing when at the head)
    //  delegate to become a resource
    //  use systemProperties
    //  set var in the API

    public IAction getAction() {
        return asyncMaster(
                new Move(5),
                when(t -> drive.isWithinDistanceRange(3, 0.1), new LiftUp()),
                when(t -> drive.isWithinDistanceRange(4, 0.1), new ActuateIntake(true))
        ).queue(
                new OuttakeCube(2, 0.8),
                waitFor(0.5),
                new ActuateIntake(false)
        ).asyncAny(
                new FindCube(),
                waitFor(2.5)
        ).async(
                new Move(0.5),
                queue(
                        new LiftDown(),
                        new ActuateIntake(true)
                )
        );

        //Score first cube
        /*runAction(new ParallelAction(
                Arrays.asList(
                        mStartToSwitch,
                        new SetSuperstructurePosition(SuperstructureConstants.kSwitchHeightBackwards, SuperstructureConstants.kStowedPositionAngle, true),
                        new SeriesAction(
                                Arrays.asList(
                                        new WaitAction(mStartCubeWaitTime),
                                        new ShootCube(AutoConstants.kMediumShootPower)
                                )
                        )
                )
        ));

        // Get second cube
        runAction(new ParallelAction(
                Arrays.asList(
                        mSwitchToPyramidCube,
                        new SetSuperstructurePosition(SuperstructureConstants.kIntakePositionHeight, SuperstructureConstants.kIntakePositionAngle, true),
                        new SetIntaking(false, false)
                )
        ));
        runAction(new WaitAction(AutoConstants.kWaitForCubeTime));

        //Score second cube
        runAction(new ParallelAction(
                Arrays.asList(
                        mPyramidCubeToSwitch,
                        new SeriesAction(
                                Arrays.asList(
                                        new SetIntaking(false, true),
                                        new SetSuperstructurePosition(SuperstructureConstants.kSwitchHeightBackwards, SuperstructureConstants.kStowedPositionAngle, true)
                                )
                        ),
                        new SeriesAction(
                                Arrays.asList(
                                        new WaitAction(mPyramidCubeWaitTime),
                                        new ShootCube(AutoConstants.kMediumShootPower)
                                )
                        )
                )
        ));*/


//        return async( // score first cube
//
//                mStartToSwitch, // drive trajectory
//
//                new SetSuperstructurePosition(SuperstructureConstants.kSwitchHeightBackwards, SuperstructureConstants.kStowedPositionAngle, true),
//
//                queue(
//                        waitFor(mStartCubeWaitTime),
//                        new ShootCube(AutoConstants.kMediumShootPower)
//                )
//
//        ).async( // get second cube
//
//                mSwitchToPyramidCube, // drive trajectory
//
//                new SetSuperstructurePosition(SuperstructureConstants.kIntakePositionHeight, SuperstructureConstants.kIntakePositionAngle, true),
//                new SetIntaking(false, false)
//
//        ).waitFor(
//
//                AutoConstants.kWaitForCubeTime
//
//        ).async( // score second cube
//
//                mPyramidCubeToSwitch, // drive trajectory
//
//                queue(
//                        new SetIntaking(false, true),
//                        new SetSuperstructurePosition(SuperstructureConstants.kSwitchHeightBackwards, SuperstructureConstants.kStowedPositionAngle, true)
//                ),
//
//                queue(
//                        waitFor(mPyramidCubeWaitTime),
//                        new ShootCube(AutoConstants.kMediumShootPower)
//                )
//        );
    }

    private IAction progressEstimator(Supplier<Double> supplier) {
        return null;
    }

    private IAction auto() {
        return asyncOp(AsyncStart.OnDynamicInverse, AsyncStop.OnEachFinished,
                new Move(5),
                new Lift(1.0)
        ).queue(
                new ActuateIntake(true),
                new OuttakeCube(0.5, -0.8)
        );
    }

    private IAction auto2() {
        return async(
                new Move(5),
                queue(
                        await(d -> drive.isWithinDistanceRange(3, 0.5)),
                        new Lift(1.0)
                ),
                queue(
                        await(d -> drive.isWithinDistanceRange(4, 0.5)),
                        new ActuateIntake(true)
                )
        ).queue(
                new OuttakeCube(0.5, -0.8)
        );
    }

    private IAction auto3() {
        return async(
                queue(new Move(1)).broadcast("C", "D"),
                queue(new Lift(1)).broadcast("C", "D"),
                when(triggeredOnce("C"), new Move(2)),
                when(triggeredRepeat("D"), new Move(2))
        );
    }


    private PIDValues PID = new PIDValues(0.018, 0.00001, 0.23);

    private IAction auto4() {
        return broadcast("INIT").async(
                when(triggeredOnce("INIT")).async(
                        () -> drive.setPIDTargetDistance(PID, 5),
                        progressEstimator(drive::getProgress),
                        when(atProgress(3), broadcast("LIFT_UP"))
                ).broadcast("OUTTAKE")
        );
    }

    private IAction auto5() {
        /*
        auto={INIT: [move(5,{3:"A"}),
             lift(1, {0.5:"A"})],
A:print("woah")
}
         */

        return broadcast("INIT").async(
                when(triggeredOnce("INIT")).async(
                        async(
                                new Move(5),
                                progressEstimator(drive::getProgress),
                                broadcastWhen(atProgress(3), "A")
                        ),
                        async(
                                new Lift(1),
                                progressEstimator(null),
                                broadcastWhen(atProgress(0.5), "A")
                        )
                ),
                when(triggeredRepeat("A")).exec(d -> System.out.println("woah"))
        );
    }

    private IAction auto5a() {
        /*
        auto={INIT: [move(5,{3:"A"}),
             lift(1, {0.5:"A"})],
A:print("woah")
}
         */

        return broadcast("INIT").async(
                when(triggeredOnce("INIT")).async(
                        new Move(5), broadcastWhen(d -> drive.getProgress() > 3, "A"),
                        new Lift(1), broadcastWhen(d -> lift.getHeight() > 0.5, "A")
                ),
                when(triggeredRepeat("A")).exec(d -> System.out.println("woah"))
        );
    }


    private IAction auto6() {
        return async(
                new Move(5),
                new Lift(1),
                when(d -> drive.getProgress() == 3 && lift.getHeight() == 0.5).exec(d -> System.out.println("woah"))
        );
    }


    static class B implements IAction {
        @Override
        public void start() {
        }
    }

    static class Move extends B {
        Move(double distance) {
        }
    }

    private static final Lift lift = new Lift(5);


    public IAction getAction2() {
        return queue(
                new DriveForDistanceAction(PID, 103, 5),
                new OuttakeCube(0.5, -0.75)
        );
    }

    private IAction oneSwitch() {
        return asyncUntil(
                drive::isPIDReached,
                () -> drive.setPIDTargetDistance(PID, 103)
        ).queue(
                () -> drive.openLoopDrive(0, 0),

                () -> intake.setSpeed(-0.75),
                waitFor(0.5),
                () -> intake.setSpeed(0)
        );
    }

    private IAction driveDistance2() {
        return queue(
                () -> drive.setPIDTargetDistance(PID, 103),
                await(drive::isPIDReached),
                () -> drive.openLoopDrive(0, 0)
        );
    }

    public IAction getOneSwitch() {
        return queue(
                () -> drive.setPIDTargetDistance(PID, 103),
                await(drive::isPIDReached),
                () -> drive.openLoopDrive(0, 0),

                () -> intake.setSpeed(-0.75),
                waitFor(0.5),
                () -> intake.setSpeed(0)
        );
    }

    public void teleop() {
//        // Limelight
//        if (Driver.XButton == Pressed) limelight.switchCamera();
//
//        // Pneumatics
//        if (Driver.BackButton == Pressed) pneumatics.toggleClosedLoop();
//        pneumatics.setGrapplingHook(Driver.StartButton == HeldDown);
//
//        // Driving
//        drive.setShouldSolenoidBeOnForShifter(Driver.RightBumper != HeldDown);
//        drive.setReversed(Driver.RightStickButton == HeldDown);
//        if (Driver.BButton != HeldDown) {
//            drive.cheesyDrive(Driver.RightXAxis, Driver.LeftYAxis, Driver.LeftBumper == HeldDown);
//        }
//
//        // Intake
//        if (Driver.AButton == Pressed) intake.togglePiston();
//        if (Driver.LeftDPad == HeldDown) intake.setSpeed(Intake.kSlowOuttakePower);
//        else if (Driver.LeftTrigger == HeldDown) intake.setSpeed(Intake.kFastOuttakePower);
//        else if (Driver.RightTrigger == HeldDown) intake.setSpeed(Intake.kIntakePower);
//        else intake.setSpeed(0);
//
//        // Arm lift
//        if (Operator.BButton == HeldDown) armLift.setSpeed(Operator.LeftYAxis);
//        else if (Driver.BButton == HeldDown) armLift.setSpeed(Driver.LeftYAxis);
//        else armLift.setSpeed(0);
//
//        // Climber
//        if (Operator.StartButton == HeldDown) climber.setSpeed(Operator.LeftYAxis);
//        else climber.setSpeed(0);
    }

    static class Lift extends B {
        Lift(double setpoint) {
        }

        double getHeight() {
            return 0;
        }
    }

    class LiftUp extends B {
        LiftUp() {
        }
    }

    class LiftDown extends B {
        LiftDown() {
        }
    }

    class ActuateIntake extends B {
        ActuateIntake(boolean on) {
        }
    }

    class FindCube extends B {
        FindCube() {
        }
    }
}
