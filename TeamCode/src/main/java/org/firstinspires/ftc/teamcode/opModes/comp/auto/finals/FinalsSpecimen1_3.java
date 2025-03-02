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

@Autonomous(name = "Specimen1_3", group = "AAA_COMP", preselectTeleOp="RedTeleOp")
public class FinalsSpecimen1_3 extends LinearOpMode {
    Robot_V2 robot = new Robot_V2(FinalsAutoConstants.STARTING_POSITION_SPECIMEN, true);

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

        Action preHang = robot.drivebase.drive.actionBuilder(robot.drivebase.drive.localizer.getPose())
                .strafeTo(FinalsAutoConstants.PRELOAD_DROP.position)
                .build();

        Action pushBlockOne = robot.drivebase.drive.actionBuilder(FinalsAutoConstants.PRELOAD_DROP)
                .setTangent(FinalsAutoConstants.PUSH_SET_TANGENT)
                .splineToSplineHeading(FinalsAutoConstants.PUSH_ONE_A, FinalsAutoConstants.PUSH_ONE_A_B_TANGENT)
                .splineToLinearHeading(FinalsAutoConstants.PUSH_ONE_B, FinalsAutoConstants.PUSH_ONE_A_B_TANGENT)
                .splineToSplineHeading(FinalsAutoConstants.PUSH_ONE_C, FinalsAutoConstants.PUSH_ONE_C_TANGENT)
                .build();

        Action pushBlockTwo = robot.drivebase.drive.actionBuilder(FinalsAutoConstants.PUSH_ONE_C)
                .splineToLinearHeading(FinalsAutoConstants.PUSH_TWO_A, FinalsAutoConstants.PUSH_TWO_A_TANGENT)
                .splineToSplineHeading(FinalsAutoConstants.PUSH_TWO_B, FinalsAutoConstants.PUSH_TWO_B_TANGENT)
                .build();

        Action pushBlockThree = robot.drivebase.drive.actionBuilder(FinalsAutoConstants.PUSH_TWO_B)
                .splineToLinearHeading(FinalsAutoConstants.PUSH_THREE_A, FinalsAutoConstants.PUSH_THREE_A_TANGENT)
                .splineToSplineHeading(FinalsAutoConstants.PUSH_THREE_B, FinalsAutoConstants.PUSH_THREE_B_C_TANGENT)
                .splineToLinearHeading(FinalsAutoConstants.PUSH_THREE_C, FinalsAutoConstants.PUSH_THREE_B_C_TANGENT)
                .build();

        Action hangOne = robot.drivebase.drive.actionBuilder(FinalsAutoConstants.PUSHED_RELOCALIZE_POSE)
                .setTangent(FinalsAutoConstants.HANG_A_SET_TANGENT)
                .splineToLinearHeading(FinalsAutoConstants.HANG_ONE_A, FinalsAutoConstants.HANG_ONE_A_TANGENT)
                .build();

        Action grabTwo = robot.drivebase.drive.actionBuilder(FinalsAutoConstants.HANG_ONE_A)
                .setTangent(FinalsAutoConstants.HANG_B_SET_TANGENT)
                .splineToLinearHeading(FinalsAutoConstants.HANG_ONE_B, FinalsAutoConstants.HANG_ONE_B_TANGENT)
                .build();

        Action hangTwo = robot.drivebase.drive.actionBuilder(FinalsAutoConstants.PUSHED_RELOCALIZE_POSE)
                .setTangent(FinalsAutoConstants.HANG_A_SET_TANGENT)
                .splineToLinearHeading(FinalsAutoConstants.HANG_TWO_A, FinalsAutoConstants.HANG_TWO_A_TANGENT)
                .build();

        Action grabThree = robot.drivebase.drive.actionBuilder(FinalsAutoConstants.HANG_TWO_A)
                .setTangent(FinalsAutoConstants.HANG_B_SET_TANGENT)
                .splineToLinearHeading(FinalsAutoConstants.HANG_TWO_B, FinalsAutoConstants.HANG_TWO_B_TANGENT)
                .build();

        Action hangThree = robot.drivebase.drive.actionBuilder(FinalsAutoConstants.PUSHED_RELOCALIZE_POSE)
                .setTangent(FinalsAutoConstants.HANG_A_SET_TANGENT)
                .splineToLinearHeading(FinalsAutoConstants.HANG_THREE_A, FinalsAutoConstants.HANG_THREE_A_TANGENT)
                .build();

        Action park = robot.drivebase.drive.actionBuilder(FinalsAutoConstants.HANG_TWO_A)
                .setTangent(FinalsAutoConstants.HANG_B_SET_TANGENT)
                .splineToLinearHeading(FinalsAutoConstants.HANG_THREE_B, FinalsAutoConstants.HANG_THREE_B_TANGENT)
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
                                            preHang,
                                            (telemetryPacket) -> { // Raise slide to drop
                                                robot.scoringAssembly.setSpecimenInPosition();
                                                return false;
                                            }
                                    ),
                                    new ParallelAction(
                                            new SequentialAction(
                                                    (telemetryPacket) -> { // Clip preload
                                                        robot.scoringAssembly.multiAxisArm.toggleSpecimen();
                                                        return false;
                                                    },
                                                    new SleepAction(0.1),
                                                    (telemetryPacket) -> {
                                                        robot.scoringAssembly.multiAxisArm.specimenPickup();
                                                        return false;
                                                    },
                                                    new SleepAction(1.3),
                                                    (telemetryPacket) -> { // Reset Position
                                                        robot.stick.stickOut();
                                                        robot.scoringAssembly.resetSpecimen();
                                                        return !robot.scoringAssembly.areMotorsAtTargetPresets();
                                                    }
                                            ),
                                            new SequentialAction(
                                                    pushBlockOne,
                                                    pushBlockTwo,
                                                    pushBlockThree
                                            )
                                    ),

                                    // SPEC 1

                                    (telemetryPacket) -> { // Grab
                                        robot.scoringAssembly.multiAxisArm.hand.close();
                                        robot.stick.stickIn();
                                        robot.drivebase.drive.localizer.setPose(FinalsAutoConstants.PUSHED_RELOCALIZE_POSE);
                                        return false;
                                    },
                                    new SleepAction(0.25),
                                    new ParallelAction(
                                            hangOne,
                                            (telemetryPacket) -> { // Raise slide to drop
                                                robot.scoringAssembly.setSpecimenInPosition();
                                                return false;
                                            }
                                    ),
                                    new ParallelAction(
                                        grabTwo,
                                        new SequentialAction(
                                                (telemetryPacket) -> { // Clip preload
                                                    robot.scoringAssembly.multiAxisArm.toggleSpecimen();
                                                    return false;
                                                },
                                                new SleepAction(0.2),
                                                (telemetryPacket) -> { // Reset Position
                                                    robot.scoringAssembly.resetSpecimen();
                                                    return !robot.scoringAssembly.areMotorsAtTargetPresets();
                                                }
                                        )
                                    ),

                                    //SPEC 2

                                    (telemetryPacket) -> { // Grab
                                        robot.scoringAssembly.multiAxisArm.hand.close();
                                        return false;
                                    },
                                    new SleepAction(0.25),
                                    new ParallelAction(
                                            hangTwo,
                                            (telemetryPacket) -> { // Raise slide to drop
                                                robot.scoringAssembly.setSpecimenInPosition();
                                                return false;
                                            }
                                    ),
                                    new ParallelAction(
                                            grabThree,
                                            new SequentialAction(
                                                    (telemetryPacket) -> { // Clip preload
                                                        robot.scoringAssembly.multiAxisArm.toggleSpecimen();
                                                        return false;
                                                    },
                                                    new SleepAction(0.2),
                                                    (telemetryPacket) -> { // Reset Position
                                                        robot.scoringAssembly.resetSpecimen();
                                                        return !robot.scoringAssembly.areMotorsAtTargetPresets();
                                                    }
                                            )
                                    ),

                                    //SPEC 3

                                    (telemetryPacket) -> { // Grab
                                        robot.scoringAssembly.multiAxisArm.hand.close();
                                        return false;
                                    },
                                    new SleepAction(0.25),
                                    new ParallelAction(
                                            hangThree,
                                            (telemetryPacket) -> { // Raise slide to drop
                                                robot.scoringAssembly.setSpecimenInPosition();
                                                return false;
                                            }
                                    ),
                                    new ParallelAction(
                                            park,
                                            new SequentialAction(
                                                    (telemetryPacket) -> { // Clip preload
                                                        robot.scoringAssembly.multiAxisArm.toggleSpecimen();
                                                        return false;
                                                    },
                                                    new SleepAction(0.2),
                                                    (telemetryPacket) -> { // Reset Position
                                                        robot.scoringAssembly.resetSpecimen();
                                                        return !robot.scoringAssembly.areMotorsAtTargetPresets();
                                                    }
                                            )
                                    ),
                                    (telemetryPacket) -> { // End Auto
                                        isDone = true;
                                        return false;
                                    }
                            )
                    )
            );
            break;
        }
    }
}
