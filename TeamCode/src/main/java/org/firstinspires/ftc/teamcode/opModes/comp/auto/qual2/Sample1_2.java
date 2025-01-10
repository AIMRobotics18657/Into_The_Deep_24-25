package org.firstinspires.ftc.teamcode.opModes.comp.auto.qual2;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.opModes.comp.auto.qual2.Qual2AutoConstants;
import org.firstinspires.ftc.teamcode.subsystems.v1.Robot;

//@Autonomous(name = "CloseBlueAwayPark", group = "AAA_COMP", preselectTeleOp="BlueTeleOp")
public final class Sample1_2 extends LinearOpMode{
    Robot robot = new Robot(false, Qual2AutoConstants.RED_STARTING_POSITION);

    boolean hasSpecimenDropped = true;
    boolean isAtSampleIntake = true;
    boolean isIntakeReset = true;
    boolean isAtSampleOuttake = true;
    boolean isOuttakeReset = true;
    boolean hasSampleDropped = true;

    public void runOpmode {
        robot.init(hardwareMap);
        Vector2d preDrop;
        Pose2d pickUp1;
        Pose2d pickUp2;
        Pose2d bucketDrop;


        preDrop = Qual2AutoConstants.RED_PRELOAD_DROP;
        pickUp1 = Qual2AutoConstants.RED_PICKUP_ONE;
        pickUp2 = Qual2AutoConstants.RED_PICKUP_TWO;
        bucketDrop = Qual2AutoConstants.RED_HIGH_BASKET;



        Action driveToPreDrop = robot.drivebase.drive.actionBuilder(preDrop)
                .strafeTo(Qual2AutoConstants.RED_PRELOAD_DROP)
                .build();

        Action driveToPickUp1 = robot.drivebase.drive.actionBuilder(pickUp1)
                .splineToLinearHeading(Qual2AutoConstants.RED_POST_DROP.position, Qual2AutoConstants.RED_POST_DROP_TANGENT.heading);
                .splineToLinearHeading(Qual2AutoConstants.RED_PICKUP_ONE.position, Qual2AutoConstants.RED_PICKUP_ONE_TANGENT.heading);
                .build();

        Action driveToPickUp2 = robot.drivebase.drive.actionBuilder(pickUp2)
                .splineToLinearHeading(Qual2AutoConstants.RED_PICKUP_TWO.position, Qual2AutoConstants.RED_PICKUP_TWO_TANGENT.heading);
                .build();

        Action driveToBucket = robot.drivebase.drive.actionBuilder(bucketDrop)
                .splineToLinearHeading(Qual2AutoConstants.RED_HIGH_BASKET.position, Qual2AutoConstants.RED_HIGH_BASKET_TANGENT.heading);
                .build();


        Actions.runBlocking(
                new SequentialAction(
                        driveToPreDrop,
                        (telemetryPacket) -> { // drive and drop preload
                        robot.scoringSystem.specimenGrabber.release();
                        return hasSpecimenDropped;
                        },
                        new SleepAction(1.0),
                        (telemetryPacket) -> { // Drop Preload
                            return false;
                        },
                        driveToPickUp1,
                        new ParallelAction(),
                        (telemetryPacket) -> { // Drive to PickUp1 and Extend Intake Out
                            // BRISTLES IN
                            robot.scoringSystem.intakeSystem.pivotDownState();
                            robot.scoringSystem.intakeSystem.mediumState();
                            return isAtSampleIntake;
                        },
                        new ParallelAction(),
                        driveToBucket,
                        (telemetryPacket) -> { // Reset Intake
                            robot.scoringSystem.intakeSystem.resetState();
                            // BRISTLES OFF
                        return isIntakeReset;
                        },
                        new SequentialAction(),
                        (telemetryPacket) -> { // Transition Sample to Outtake
                            // BRISTLES IN
                            robot.scoringSystem.intakeSystem.pivotUpState();
                            // BRISTLES OFF
                            return isAtSampleOuttake;
                        },
                        new SequentialAction(),
                        (telemetryPacket) -> { // Score1
                            robot.scoringSystem.outtakeSystem.specimenHighState();
                            // BUCKET DROP
                            robot.scoringSystem.outtakeSystem.resetState();
                            return hasSampleDropped;
                        }

                // ADD DRIVE TO PICKUP2, Pick up SAMPLE 2, SCORE SAMPLE 2

                )


        );




    }
}
