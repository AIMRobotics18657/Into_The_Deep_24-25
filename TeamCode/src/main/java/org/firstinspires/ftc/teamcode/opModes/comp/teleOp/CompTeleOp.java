package org.firstinspires.ftc.teamcode.opModes.comp.teleOp;

import com.aimrobotics.aimlib.gamepad.AIMPad;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.internal.ui.GamepadUser;
import org.firstinspires.ftc.teamcode.subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSystem;
import org.firstinspires.ftc.teamcode.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.subsystems.OuttakeSystem;
import org.firstinspires.ftc.teamcode.subsystems.Robot;

@TeleOp(name="CompTeleOp", group="AAA_COMP")
public class CompTeleOp extends OpMode {

    Robot robot;
    AIMPad aimPad1;
    AIMPad aimPad2;

    @Override
    public void init() {
        robot = new Robot(true);
        robot.init(hardwareMap);
        aimPad1 = new AIMPad(gamepad1);
        aimPad2 = new AIMPad(gamepad2);
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        robot.loop(aimPad1, aimPad2);
//        gamepad1.setGamepadId(1);
//        gamepad2.setGamepadId(2);
//        gamepad1.setUser(GamepadUser.ONE);
//        gamepad2.setUser(GamepadUser.TWO);
        aimPad1.update(gamepad1);
        aimPad2.update(gamepad2);

        telemetry.addData("Advance Pressed", aimPad1.isStartPressed());
        telemetry.addData("Advance Released", aimPad1.isStartReleased());
        telemetry.addData("Previous State", aimPad1.getPreviousState());
        telemetry.addData("Current State", aimPad1.getCurrentState());
        robot.telemetry(telemetry);
        telemetry.update();
    }
}