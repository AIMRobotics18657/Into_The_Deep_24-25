package org.firstinspires.ftc.teamcode.opModes.comp.auto;

import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.gb.pinpoint.driver.Pose2D;

import org.firstinspires.ftc.teamcode.subsystems.v1.Robot;

public class AutoConstants {

    Robot robot = new Robot(true, AutoConstants.STARTING_POSITION);

    public static final Pose2D HIGH_SPECIMEN_DROP = new Pose2D(DistanceUnit.INCH, 32, 0, AngleUnit.DEGREES, 0);
    public static final Pose2D HIGH_SPECIMEN_DROP_PREP = new Pose2D(DistanceUnit.INCH, 26, 0, AngleUnit.DEGREES, 0);

    public static final Pose2D PARK = new Pose2D(DistanceUnit.INCH, 1, 52, AngleUnit.DEGREES, 0);
    public static final Pose2D STARTING_POSITION = new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0);

    public static final Vector2d SAMPLE_1 = new Vector2d(-48, -26);
//        public void runOpMode() {
//        robot.init(hardwareMap);
//        robot.drivebase.setActiveDriveMode(Drivebase.DriveMode.TO_POSITION);
//        robot.scoringSystem.setActiveScoringState(ScoringSystem.ScoringState.AUTO_PERIOD);
//        AIMPad aimPad1 = new AIMPad(gamepad1);
//        AIMPad aimPad2 = new AIMPad(gamepad2);
//
//        while (!isStarted() && !isStopRequested()) {
//        robot.telemetry(telemetry);
//        telemetry.update();
//            }
//        }





    }
