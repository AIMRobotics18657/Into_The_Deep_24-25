package org.firstinspires.ftc.teamcode.util;

import com.aimrobotics.aimlib.gamepad.AIMPad;
import com.aimrobotics.aimlib.util.Mechanism;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class StateDrivenServo extends Mechanism {

    Servo servo;

    String name;

    ServoState activeState;
    ServoState[] states;

    public StateDrivenServo(ServoState[] states, ServoState targetState, String name) {
        this.states = states;
        this.activeState = targetState;
        this.name = name;
    }

    @Override
    public void init(HardwareMap hwMap) {
        servo = hwMap.get(Servo.class, name);
    }

    @Override
    public void loop(AIMPad aimPad) {
        for (ServoState state:
             states) {
            if (state == activeState) {
                servo.setPosition(activeState.getPosition());
            }
            break;
        }
    }

    public void setActiveState(ServoState newActiveState) {
        activeState = newActiveState;
    }
}
