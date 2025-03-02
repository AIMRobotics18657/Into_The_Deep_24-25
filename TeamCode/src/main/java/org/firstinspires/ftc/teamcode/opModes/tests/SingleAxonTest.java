package org.firstinspires.ftc.teamcode.opModes.tests;

import com.aimrobotics.aimlib.gamepad.AIMPad;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.settings.InputHandler;
import org.firstinspires.ftc.teamcode.subsystems.multiaxisarm.Hand;
import org.firstinspires.ftc.teamcode.subsystems.multiaxisarm.MultiAxisArm;
import org.firstinspires.ftc.teamcode.subsystems.Slides;

@TeleOp(name="AxonTest", group="AAA_COMPETITION")
@Disabled
public class SingleAxonTest extends OpMode {

    Hand hand = new Hand();
    InputHandler inputHandler = new InputHandler();

    AIMPad aimPad1;
    AIMPad aimPad2;
    @Override
    public void init() {
        hand.init(hardwareMap);

        aimPad1 = new AIMPad(gamepad1);
        aimPad2 = new AIMPad(gamepad2);
    }

    @Override
    public void loop() {
        aimPad1.update(gamepad1);
        aimPad2.update(gamepad2);
        inputHandler.updateInputs(aimPad1, aimPad2);

        hand.hand.systemsCheck(aimPad1, telemetry);
        telemetry.addData("Advance Pressed", aimPad1.isStartPressed());
        telemetry.addData("Advance Released", aimPad1.isStartReleased());
        telemetry.addData("Previous State", aimPad1.getPreviousState());
        telemetry.addData("Current State", aimPad1.getCurrentState());
        telemetry.update();
    }
}