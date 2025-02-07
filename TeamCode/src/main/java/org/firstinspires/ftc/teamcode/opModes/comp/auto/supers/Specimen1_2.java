package org.firstinspires.ftc.teamcode.opModes.comp.auto.supers;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;

import org.firstinspires.ftc.teamcode.subsystems.Robot_V2;

@Autonomous(name = "Specimen1_2", group = "AAA_COMP", preselectTeleOp="RedTeleOp")
public class Specimen1_2 extends LinearOpMode {
    Robot_V2 robot = new Robot_V2(SupersAutoConstants.STARTING_POSITION);

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        Action preHang = robot.drivebase.drive.actionBuilder(robot.drivebase.drive.localizer.getPose())
                .strafeTo(SupersAutoConstants.PRELOAD_DROP.position)
                .build();

        Action pushBlockOne = robot.drivebase.drive.actionBuilder(SupersAutoConstants.PRELOAD_DROP)
                .splineToLinearHeading(SupersAutoConstants.PUSH_ONE_A, SupersAutoConstants.PUSH_ONE_A_C_TANGENT)
                .splineToLinearHeading(SupersAutoConstants.PUSH_ONE_B, SupersAutoConstants.PUSH_ONE_B_D_TANGENT)
                .splineToLinearHeading(SupersAutoConstants.PUSH_ONE_C, SupersAutoConstants.PUSH_ONE_A_C_TANGENT)
                .splineToLinearHeading(SupersAutoConstants.PUSH_ONE_D, SupersAutoConstants.PUSH_ONE_B_D_TANGENT)
                .build();

            Action pushBlockTwo = robot.drivebase.drive.actionBuilder(SupersAutoConstants.PUSH_ONE_D)
                .splineToLinearHeading(SupersAutoConstants.PUSH_TWO_A, SupersAutoConstants.PUSH_TWO_A_TANGENT)
                .splineToLinearHeading(SupersAutoConstants.PUSH_TWO_B, SupersAutoConstants.PUSH_TWO_B_TANGENT)
                .build();

            Action pushBlockThree = robot.drivebase.drive.actionBuilder(SupersAutoConstants.PUSH_TWO_B)
                .splineToLinearHeading(SupersAutoConstants.PUSH_THREE_A, SupersAutoConstants.PUSH_THREE_A_TANGENT)
                .strafeTo(SupersAutoConstants.PUSH_THREE_B.position)
                .build();

            Action hangOne = robot.drivebase.drive.actionBuilder(SupersAutoConstants.PUSH_THREE_B)
                .strafeTo(SupersAutoConstants.HANG_ONE_A.position)
                .waitSeconds(1)
                .strafeTo(SupersAutoConstants.HANG_ONE_B.position)
                .waitSeconds(2)
                .build();

            Action hangTwo = robot.drivebase.drive.actionBuilder(SupersAutoConstants.HANG_ONE_B)
                .strafeTo(SupersAutoConstants.HANG_TWO_A.position)
                .waitSeconds(1)
                .strafeTo(SupersAutoConstants.HANG_TWO_B.position)
                .waitSeconds(2)
                .build();

            Action hangThree = robot.drivebase.drive.actionBuilder(SupersAutoConstants.HANG_TWO_B)
                .strafeTo(SupersAutoConstants.HANG_THREE_A.position)
                .waitSeconds(1)
                .strafeTo(SupersAutoConstants.HANG_THREE_B.position)
                .waitSeconds(2)
                .build();

        waitForStart();
        while (opModeIsActive()){
            Actions.runBlocking(
                    new SequentialAction(
                        preHang,
                        new SleepAction(1),
                        pushBlockOne,
                        pushBlockTwo,
                        pushBlockThree,
                        hangOne,
                        hangTwo,
                        hangThree
                    )
            );
            break;
        }
    }
}
