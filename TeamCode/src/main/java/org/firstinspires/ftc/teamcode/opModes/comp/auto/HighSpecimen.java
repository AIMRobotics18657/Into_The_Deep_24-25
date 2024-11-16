package org.firstinspires.ftc.teamcode.opModes.comp.auto;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.aimrobotics.aimlib.gamepad.AIMPad;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.gb.pinpoint.driver.Pose2D;
import org.firstinspires.ftc.teamcode.subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.subsystems.OuttakeSystem;
import org.firstinspires.ftc.teamcode.subsystems.Robot;
import org.firstinspires.ftc.teamcode.subsystems.ScoringSystem;

@Autonomous(name = "HighSpecimen", group = "AAA_COMP")
public final class HighSpecimen extends LinearOpMode {
    Robot robot = new Robot(true, AutoConstants.STARTING_POSITION);
    ScoringSystem scoringSystem = new ScoringSystem(true);

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        robot.drivebase.setActiveDriveMode(Drivebase.DriveMode.TO_POSITION);
        robot.scoringSystem.setActiveScoringState(ScoringSystem.ScoringState.AUTO_PERIOD);

        AIMPad aimPad1 = new AIMPad(gamepad1);
        AIMPad aimPad2 = new AIMPad(gamepad2);

        while (opModeIsActive()) {

            //DRIVE TO DROP PIXEL
            Actions.runBlocking(
                    new SequentialAction(
                            new ParallelAction(
                                    (telemetryPacket) -> { // Drive to High Drop
                                        robot.drivebase.setTargetPose(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0));
                                        robot.drivebase.loop(aimPad1, aimPad2);
                                        return !robot.drivebase.isAtTargetPosition();
                                    },
                                    (telemetryPacket) -> { // Raise Lifts
                                        scoringSystem.outtakeSystem.setAutoSlidesPosition(OuttakeSystem.AutoSlidesPosition.SPECIMEN_HIGH);
                                        return !scoringSystem.outtakeSystem.outtakeSlides.isAtTargetPosition();
                                    }
                            ),
                            (telemetryPacket) -> { // Score Specimen
                                scoringSystem.outtakeSystem.setAutoSlidesPosition(OuttakeSystem.AutoSlidesPosition.SPECIMEN_HIGH_DROP);
                                return !scoringSystem.outtakeSystem.outtakeSlides.isAtTargetPosition();
                            },
                            (telemetryPacket) -> { // Drive to Park
                                robot.drivebase.setTargetPose(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0));
                                robot.drivebase.loop(aimPad1, aimPad2);
                                return !robot.drivebase.isAtTargetPosition();
                            }
                    )
            );
            break;
        }
    }
}
