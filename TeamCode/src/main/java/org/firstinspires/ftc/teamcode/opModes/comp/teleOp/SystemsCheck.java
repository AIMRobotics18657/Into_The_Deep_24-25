package org.firstinspires.ftc.teamcode.opModes.comp.teleOp;

import com.aimrobotics.aimlib.gamepad.AIMPad;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.internal.ui.GamepadUser;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSystem;
import org.firstinspires.ftc.teamcode.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.subsystems.OuttakeSystem;
import org.firstinspires.ftc.teamcode.subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.subsystems.Robot;

@TeleOp(name="SystemsCheck", group="AAA_COMPETITION")
public class SystemsCheck extends OpMode {

    Drivebase drivebase = new Drivebase();
    Intake intake = new Intake();
    IntakeSystem intakeSystem = new IntakeSystem();
    Outtake outtake = new Outtake();
    OuttakeSystem outtakeSystem = new OuttakeSystem();

    AIMPad aimPad1;
    AIMPad aimPad2;


    enum TestingState {
        DRIVEBASE, INTAKE, INTAKE_SYSTEM, OUTTAKE, OUTTAKE_SYSTEM
    }

    TestingState activeTestingState = TestingState.DRIVEBASE;

    @Override
    public void init() {
        drivebase.init(hardwareMap);
        intake.init(hardwareMap);
        intakeSystem.init(hardwareMap);
        outtake.init(hardwareMap);
        outtakeSystem.init(hardwareMap);

        aimPad1 = new AIMPad(gamepad1);
        aimPad2 = new AIMPad(gamepad2);
    }

    @Override
    public void loop() {
        aimPad1.update(gamepad1);
        aimPad2.update(gamepad2);

        switch (activeTestingState) {
            case DRIVEBASE:
                drivebaseTest();
                break;
//            case ODOMETRY:
//                odoTest(aimPad1);
//                break;
            case INTAKE_SYSTEM:
                intakeSystemTest();
                break;
            case INTAKE:
                intakeTest();
                break;
            case OUTTAKE:
                outtakeTest();
                break;
            case OUTTAKE_SYSTEM:
                outtakeSystemTest();
                break;
        }
        telemetry.addData("Advance Pressed", aimPad1.isStartPressed());
        telemetry.addData("Advance Released", aimPad1.isStartReleased());
        telemetry.addData("Previous State", aimPad1.getPreviousState());
        telemetry.addData("Current State", aimPad1.getCurrentState());
        telemetry.addData("Current Testing State", activeTestingState);
        telemetry.update();
    }


    public void drivebaseTest() {
        drivebase.systemsCheck(aimPad1);
        if (aimPad1.isStartPressed()) {
            activeTestingState = TestingState.INTAKE;
        }
    }

    public void intakeTest() {
        intake.systemsCheck(aimPad1);
        if (aimPad1.isStartPressed()) {
            activeTestingState = TestingState.INTAKE_SYSTEM;
        }
    }

    public void intakeSystemTest() {
        intakeSystem.systemsCheck(aimPad1, telemetry);
        if (aimPad1.isStartPressed()) {
            activeTestingState = TestingState.OUTTAKE;
        }
    }

    public void outtakeTest() {
        outtake.systemsCheck(aimPad1);
        if (aimPad1.isStartPressed()) {
            activeTestingState = TestingState.OUTTAKE_SYSTEM;
        }
    }

    public void outtakeSystemTest() {
        outtakeSystem.systemsCheck(aimPad1, telemetry);
        if (aimPad1.isStartPressed()) {
            activeTestingState = TestingState.DRIVEBASE;
        }
    }
}