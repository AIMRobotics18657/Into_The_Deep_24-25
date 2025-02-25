package org.firstinspires.ftc.teamcode.opModes.comp.auto.finals;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.aimrobotics.aimlib.gamepad.AIMPad;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;

import org.firstinspires.ftc.teamcode.subsystems.Robot_V2;

@Autonomous(name = "Specimen1_2", group = "AAA_COMP", preselectTeleOp="RedTeleOp")
public class FinalsSample extends LinearOpMode {
    Robot_V2 robot = new Robot_V2(FinalsAutoConstants.STARTING_POSITION_SAMPLE, true, 1);

    boolean isDone = false;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        robot.scoringAssembly.multiAxisArm.hand.close();
        robot.scoringAssembly.multiAxisArm.wrist.rotateHorizontal();
        while (!isStarted() && !isStopRequested()) {
            robot.scoringAssembly.multiAxisArm.hand.loop(new AIMPad(gamepad2));
            robot.scoringAssembly.multiAxisArm.wrist.rotator.loop(new AIMPad(gamepad2));
        }

        Action preScore = robot.drivebase.drive.actionBuilder(robot.drivebase.drive.localizer.getPose())
                .setTangent(FinalsAutoConstants.PRESCORE_SET_TANGENT)
                .splineToLinearHeading(FinalsAutoConstants.PRESCORE, FinalsAutoConstants.PRESCORE_TANGENT)
                .build();

        Action pickupBlockOne = robot.drivebase.drive.actionBuilder(FinalsAutoConstants.PRESCORE)
                .splineToLinearHeading(FinalsAutoConstants.PICKUP_ONE_A, FinalsAutoConstants.PICKUP_ONE_A_TANGENT)
                .build();

        Action scoreBlockOne = robot.drivebase.drive.actionBuilder(FinalsAutoConstants.PICKUP_ONE_A)
                .splineToLinearHeading(FinalsAutoConstants.SCORE_ONE_A, FinalsAutoConstants.SCORE_ONE_A_TANGENT)
                .build();

        Action pickupBlockTwo = robot.drivebase.drive.actionBuilder(FinalsAutoConstants.SCORE_ONE_A)
                .splineToLinearHeading(FinalsAutoConstants.PICKUP_TWO_A, FinalsAutoConstants.PICKUP_TWO_A_TANGENT)
                .build();

        Action scoreBlockTwo = robot.drivebase.drive.actionBuilder(FinalsAutoConstants.PICKUP_TWO_A)
                .splineToLinearHeading(FinalsAutoConstants.SCORE_TWO_A, FinalsAutoConstants.SCORE_TWO_A_TANGENT)
                .build();

        Action pickupBlockThree = robot.drivebase.drive.actionBuilder(FinalsAutoConstants.SCORE_TWO_A)
                .splineToLinearHeading(FinalsAutoConstants.PICKUP_THREE_A, FinalsAutoConstants.PICKUP_THREE_A_TANGENT)
                .build();

        Action scoreBlockThree = robot.drivebase.drive.actionBuilder(FinalsAutoConstants.PICKUP_THREE_A)
                .splineToLinearHeading(FinalsAutoConstants.SCORE_THREE_A, FinalsAutoConstants.SCORE_THREE_A_TANGENT)
                .build();


        while (opModeIsActive()){
            Actions.runBlocking(
                    new ParallelAction(
                            (telemetryPacket) -> { // Drop Purple
                                robot.loop(new AIMPad(gamepad1), new AIMPad(gamepad2));
                                return !isDone;
                            },
                            new SequentialAction(
                                    new ParallelAction(
                                            preScore,
                                            (telemetryPacket) -> { // Raise slide to drop
                                                robot.scoringAssembly.setSpecimenClampedAUTO();//TODO GET IN POSITION
                                                return !robot.scoringAssembly.areMotorsAtTargetPresets();
                                            }
                                    ),
                                    (telemetryPacket) -> { // Drop block
                                        robot.scoringAssembly.multiAxisArm.hand.open();
                                        return false;
                                    },
                                    new SleepAction(.25),
                                    new ParallelAction(
                                            pickupBlockOne,
                                            new SequentialAction(
                                                (telemetryPacket) -> { // TODO DROP SLIDES
                                                    robot.scoringAssembly.setSpecimenClampedAUTO();
                                                    return !robot.scoringAssembly.areMotorsAtTargetPresets();
                                                },
                                                (telemetryPacket) -> { // TODO DROP PIVOT
                                                    robot.scoringAssembly.setSpecimenClampedAUTO();
                                                    return !robot.scoringAssembly.areMotorsAtTargetPresets();
                                                }
                                            )
                                    ),
                                    (telemetryPacket) -> { // Grab
                                        robot.scoringAssembly.multiAxisArm.hand.close();
                                        return false;
                                    },
                                    new SleepAction(0.25),
                                    new ParallelAction(
                                            scoreBlockOne,
                                            (telemetryPacket) -> { //TODO GET IN POSITION
                                                robot.scoringAssembly.setSpecimenClampedAUTO();
                                                return !robot.scoringAssembly.areMotorsAtTargetPresets();
                                            }
                                    ),
                                    (telemetryPacket) -> { // Drop block
                                        robot.scoringAssembly.multiAxisArm.hand.open();
                                        return false;
                                    },
                                    new SleepAction(.25),
                                    new ParallelAction(
                                            pickupBlockTwo,
                                            new SequentialAction(
                                                    (telemetryPacket) -> { // TODO DROP SLIDES
                                                        robot.scoringAssembly.setSpecimenClampedAUTO();
                                                        return !robot.scoringAssembly.areMotorsAtTargetPresets();
                                                    },
                                                    (telemetryPacket) -> { // TODO DROP PIVOT
                                                        robot.scoringAssembly.setSpecimenClampedAUTO();
                                                        return !robot.scoringAssembly.areMotorsAtTargetPresets();
                                                    }
                                            )
                                    ),
                                    (telemetryPacket) -> { // Grab
                                        robot.scoringAssembly.multiAxisArm.hand.close();
                                        return false;
                                    },
                                    new SleepAction(0.25),
                                    new ParallelAction(
                                            scoreBlockTwo,
                                            (telemetryPacket) -> { //TODO GET IN POSITION
                                                robot.scoringAssembly.setSpecimenClampedAUTO();
                                                return !robot.scoringAssembly.areMotorsAtTargetPresets();
                                            }
                                    ),
                                    (telemetryPacket) -> { // Drop block
                                        robot.scoringAssembly.multiAxisArm.hand.open();
                                        return false;
                                    },
                                    new SleepAction(.25),
                                    new ParallelAction(
                                            pickupBlockThree,
                                            new SequentialAction(
                                                    (telemetryPacket) -> { // TODO DROP SLIDES
                                                        robot.scoringAssembly.setSpecimenClampedAUTO();
                                                        return !robot.scoringAssembly.areMotorsAtTargetPresets();
                                                    },
                                                    (telemetryPacket) -> { // TODO DROP PIVOT
                                                        robot.scoringAssembly.setSpecimenClampedAUTO();
                                                        return !robot.scoringAssembly.areMotorsAtTargetPresets();
                                                    }
                                            )
                                    ),
                                    (telemetryPacket) -> { // Grab
                                        robot.scoringAssembly.multiAxisArm.hand.close();
                                        return false;
                                    },
                                    new SleepAction(0.25),
                                    new ParallelAction(
                                            scoreBlockThree,
                                            (telemetryPacket) -> { //TODO GET IN POSITION
                                                robot.scoringAssembly.setSpecimenClampedAUTO();
                                                return !robot.scoringAssembly.areMotorsAtTargetPresets();
                                            }
                                    )
                            )
                    )
            );
            break;
        }
    }
}
