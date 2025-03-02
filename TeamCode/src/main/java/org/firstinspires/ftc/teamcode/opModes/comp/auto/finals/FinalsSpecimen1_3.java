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
public class Specimen1_2 extends LinearOpMode {
    Robot_V2 robot = new Robot_V2(BackupConstants.STARTING_POSITION, true);

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
                .strafeTo(BackupConstants.PRELOAD_DROP.position)
                .build();

        Action pushBlockOne = robot.drivebase.drive.actionBuilder(BackupConstants.PRELOAD_DROP)
                .strafeTo(BackupConstants.CLEARANCE.position)
                .splineToLinearHeading(BackupConstants.PUSH_ONE_A, BackupConstants.PUSH_ONE_A_C_TANGENT)
                .splineToLinearHeading(BackupConstants.PUSH_ONE_B, BackupConstants.PUSH_ONE_B_D_TANGENT)
                .splineToLinearHeading(BackupConstants.PUSH_ONE_C, BackupConstants.PUSH_ONE_A_C_TANGENT)
                .splineToLinearHeading(BackupConstants.PUSH_ONE_D, BackupConstants.PUSH_ONE_B_D_TANGENT)
                .build();

        Action pushBlockTwo = robot.drivebase.drive.actionBuilder(BackupConstants.PUSH_ONE_D)
                .splineToLinearHeading(BackupConstants.PUSH_TWO_A, BackupConstants.PUSH_TWO_A_TANGENT)
                .splineToLinearHeading(BackupConstants.PUSH_TWO_B, BackupConstants.PUSH_TWO_B_TANGENT)
                .splineToLinearHeading(BackupConstants.HANG_ONE_A, BackupConstants.HANG_ONE_A_TANGENT)
                .build();

        Action HangOne = robot.drivebase.drive.actionBuilder(BackupConstants.HANG_ONE_A)
                .setTangent(FinalsAutoConstants.HANG_A_SET_TANGENT)
                .splineToLinearHeading(FinalsAutoConstants.HANG_A, FinalsAutoConstants.HANG_A_TANGENT)
                .splineToLinearHeading(FinalsAutoConstants.HANG_B, FinalsAutoConstants.HANG_B_TANGENT)
                .splineToLinearHeading(FinalsAutoConstants.HANG_C, FinalsAutoConstants.HANG_C_TANGENT)
                .build();

        Action HangTwo = robot.drivebase.drive.actionBuilder(BackupConstants.HANG_ONE_A)
                .setTangent(FinalsAutoConstants.HANG_A_SET_TANGENT)
                .splineToLinearHeading(FinalsAutoConstants.HANG_A, FinalsAutoConstants.HANG_A_TANGENT)
                .splineToLinearHeading(FinalsAutoConstants.HANG_B, FinalsAutoConstants.HANG_B_TANGENT)
                .splineToLinearHeading(FinalsAutoConstants.HANG_C, FinalsAutoConstants.HANG_C_TANGENT)
                .build();

        Action HangThree = robot.drivebase.drive.actionBuilder(BackupConstants.HANG_ONE_A)
                .setTangent(FinalsAutoConstants.HANG_A_SET_TANGENT)
                .splineToLinearHeading(FinalsAutoConstants.HANG_A, FinalsAutoConstants.HANG_A_TANGENT)
                .splineToLinearHeading(FinalsAutoConstants.HANG_B, FinalsAutoConstants.HANG_B_TANGENT)
                .splineToLinearHeading(FinalsAutoConstants.HANG_C, FinalsAutoConstants.HANG_C_TANGENT)
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
                                                robot.scoringAssembly.setSpecimenClampedAUTO();
                                                return !robot.scoringAssembly.areMotorsAtTargetPresets();
                                            }
                                    ),
                                    (telemetryPacket) -> { // Clip preload
                                        robot.scoringAssembly.multiAxisArm.toggleSpecimen();
                                        return false;
                                    },
                                    new SleepAction(.5),
                                    (telemetryPacket) -> { // Reset Position
                                        robot.scoringAssembly.resetSpecimen();
                                        return !robot.scoringAssembly.areMotorsAtTargetPresets();
                                    },
                                    pushBlockOne,
                                    pushBlockTwo,
                                    (telemetryPacket) -> { // Grab
                                        robot.scoringAssembly.multiAxisArm.hand.close();
                                        return false;
                                    },

                                    new SleepAction(0.25),
                                    new ParallelAction(
                                            HangOne,
                                            (telemetryPacket) -> { // Raise slide to drop
                                                robot.scoringAssembly.setSpecimenClampedAUTO();
                                                return !robot.scoringAssembly.areMotorsAtTargetPresets();
                                            }
                                    ),
                                    (telemetryPacket) -> { // Clip specimen
                                        robot.scoringAssembly.multiAxisArm.toggleSpecimen();
                                        return false;
                                    },
                                    new SleepAction(.5),
                                    (telemetryPacket) -> { // Reset Position
                                        robot.scoringAssembly.resetSpecimen();
                                        return !robot.scoringAssembly.areMotorsAtTargetPresets();
                                    },

                                    //SPECIMEN 3
                                    (telemetryPacket) -> { // Grab
                                        robot.scoringAssembly.multiAxisArm.hand.close();
                                        return false;
                                    },
                                    new SleepAction(0.25),
                                    new ParallelAction(
                                            HangTwo,
                                            (telemetryPacket) -> { // Raise slide to drop
                                                robot.scoringAssembly.setSpecimenClampedAUTO();
                                                return !robot.scoringAssembly.areMotorsAtTargetPresets();
                                            }
                                    ),
                                    (telemetryPacket) -> { // Clip specimen
                                        robot.scoringAssembly.multiAxisArm.toggleSpecimen();
                                        return false;
                                    },
                                    new SleepAction(.5),
                                    (telemetryPacket) -> { // Reset Position
                                        robot.scoringAssembly.resetSpecimen();
                                        robot.scoringAssembly.resetAuto();
                                        return !robot.scoringAssembly.areMotorsAtTargetPresets();
                                    },

                                    (telemetryPacket) -> { // Grab
                                        robot.scoringAssembly.multiAxisArm.hand.close();
                                        return false;
                                    },
                                    new SleepAction(0.25),
                                    new ParallelAction(
                                            HangThree,
                                            (telemetryPacket) -> { // Raise slide to drop
                                                robot.scoringAssembly.setSpecimenClampedAUTO();
                                                return !robot.scoringAssembly.areMotorsAtTargetPresets();
                                            }
                                    ),
                                    (telemetryPacket) -> { // Clip specimen
                                        robot.scoringAssembly.multiAxisArm.toggleSpecimen();
                                        return false;
                                    },
                                    new SleepAction(.5),
                                    (telemetryPacket) -> { // Reset Position
                                        robot.scoringAssembly.resetSpecimen();
                                        robot.scoringAssembly.resetAuto();
                                        return !robot.scoringAssembly.areMotorsAtTargetPresets();
                                    },

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
