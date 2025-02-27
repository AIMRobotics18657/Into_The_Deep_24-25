package org.firstinspires.ftc.teamcode.subsystems;

import com.aimrobotics.aimlib.gamepad.AIMPad;
import com.aimrobotics.aimlib.util.Mechanism;
import com.aimrobotics.aimlib.subsystems.sds.StateDrivenServo;
import com.aimrobotics.aimlib.subsystems.sds.ServoState;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.settings.ConfigurationInfo;

public class Stick extends Mechanism {

    public StateDrivenServo stick;

    ServoState STK_OUT = new ServoState(0.6);
    ServoState STK_IN = new ServoState( 1);

    public void init(HardwareMap hwMap) {
        stick = new StateDrivenServo(new ServoState[]{STK_OUT,STK_IN}, STK_IN, ConfigurationInfo.stick.getDeviceName());
        stick.init(hwMap);
    }

    @Override
    public void loop(AIMPad aimpad) {
        stick.loop(aimpad);
    }

    @Override
    public void telemetry(Telemetry telemetry) {
        stick.telemetry(telemetry);
    }

    public void stickIn() {
        stick.setActiveTargetState(STK_IN);
    }

    public void stickOut() {
        stick.setActiveTargetState(STK_OUT);
    }
}
