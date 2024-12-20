package org.firstinspires.ftc.teamcode.subsystems.v2;

import com.aimrobotics.aimlib.gamepad.AIMPad;
import com.aimrobotics.aimlib.util.Mechanism;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.settings.ConfigurationInfo;

public class Wrist extends Mechanism {

    public Servo rotator;
    public Servo flexor;

    public enum FlexorState {
        UP, DOWN, NEUTRAL, CUSTOM
    }

    public enum RotatorState {
        LEFT, CENTER, RIGHT, CUSTOM
    }


    @Override
    public void init(HardwareMap hwMap) {
        rotator = hwMap.get(Servo.class, ConfigurationInfo.rotator.getDeviceName());
        flexor = hwMap.get(Servo.class, ConfigurationInfo.flexor.getDeviceName());
    }

    @Override
    public void loop(AIMPad aimpad) {

    }
}
