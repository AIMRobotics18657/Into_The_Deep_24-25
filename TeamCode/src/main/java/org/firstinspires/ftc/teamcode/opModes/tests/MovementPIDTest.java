package org.firstinspires.ftc.teamcode.opModes.tests;

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
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.gb.pinpoint.driver.Pose2D;
import org.firstinspires.ftc.teamcode.opModes.comp.auto.AutoConstants;
import org.firstinspires.ftc.teamcode.subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.subsystems.OuttakeSystem;
import org.firstinspires.ftc.teamcode.subsystems.Robot;
import org.firstinspires.ftc.teamcode.subsystems.ScoringSystem;

@TeleOp(name = "MovementPIDTest", group = "TESTING")
public final class MovementPIDTest extends OpMode {
    Robot robot = new Robot(true, new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0));
    ScoringSystem scoringSystem = new ScoringSystem(true);

    AIMPad aimPad1;
    AIMPad aimPad2;

    @Override
    public void init() {
        robot.init(hardwareMap);
        aimPad1 = new AIMPad(gamepad1);
        aimPad2 = new AIMPad(gamepad2);
    }

    @Override
    public void loop() {
        robot.drivebase.setActiveDriveMode(Drivebase.DriveMode.TO_POSITION);
        robot.scoringSystem.setActiveScoringState(ScoringSystem.ScoringState.AUTO_PERIOD);
        robot.loop(aimPad1, aimPad2);
        aimPad1.update(gamepad1);
        aimPad2.update(gamepad2);

        if (aimPad1.isAPressed()) {
            robot.drivebase.setTargetPose(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0));
        } else if (aimPad1.isBPressed()) {
            robot.drivebase.setTargetPose(new Pose2D(DistanceUnit.INCH, 10, 0, AngleUnit.DEGREES, 0));
        } else if (aimPad1.isXPressed()) {
            robot.drivebase.setTargetPose(new Pose2D(DistanceUnit.INCH, 10, 10, AngleUnit.DEGREES, 0));
        } else if (aimPad1.isYPressed()) {
            robot.drivebase.setTargetPose(new Pose2D(DistanceUnit.INCH, 0, 10, AngleUnit.DEGREES, 0));
        }
        if (aimPad1.isDPadUpPressed()) {
            robot.drivebase.setTargetPose(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0));
        } else if (aimPad1.isDPadDownPressed()) {
            robot.drivebase.setTargetPose(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 90));
        } else if (aimPad1.isDPadLeftPressed()) {
            robot.drivebase.setTargetPose(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 180));
        } else if (aimPad1.isDPadRightPressed()) {
            robot.drivebase.setTargetPose(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 270));
        }

        telemetry.addData("Previous State", aimPad1.getPreviousState());
        telemetry.addData("Current State", aimPad1.getCurrentState());
        telemetry.addData("isAtTargetPosition", robot.drivebase.isAtTargetPosition());
        robot.telemetry(telemetry);
    }
}