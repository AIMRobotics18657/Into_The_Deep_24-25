package org.firstinspires.ftc.teamcode.subsystems.multiaxisarm;

import com.aimrobotics.aimlib.gamepad.AIMPad;
import com.aimrobotics.aimlib.util.Mechanism;
import com.aimrobotics.aimlib.subsystems.sds.StateDrivenServo;
import com.aimrobotics.aimlib.subsystems.sds.ServoState;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.settings.ConfigurationInfo;

public class Wrist extends Mechanism {

    public StateDrivenServo rotator;
    public StateDrivenServo flexor;

    ServoState FLX_NEUTRAL = new ServoState(.8);
    ServoState FLX_SCORE = new ServoState(0.61);
    ServoState FLX_SCORE_SPECIMEN = new ServoState(.71);
    ServoState FLX_PREP_SPECIMEN = new ServoState(.86);
    ServoState FLX_DOWN = new ServoState(.97);

    ServoState ROT_IN_LINE = new ServoState(1);
    ServoState ROT_HORIZONTAL = new ServoState(.5);
    ServoState ROT_FULL_FLIP = new ServoState(0);


    @Override
    public void init(HardwareMap hwMap) {
        flexor = new StateDrivenServo(new ServoState[]{FLX_NEUTRAL, FLX_SCORE, FLX_SCORE_SPECIMEN, FLX_PREP_SPECIMEN, FLX_DOWN}, FLX_NEUTRAL, ConfigurationInfo.flexor.getDeviceName());
        rotator = new StateDrivenServo(new ServoState[]{ROT_IN_LINE, ROT_HORIZONTAL, ROT_FULL_FLIP}, ROT_HORIZONTAL, ConfigurationInfo.rotator.getDeviceName());
        flexor.init(hwMap);
        rotator.init(hwMap);
    }

    @Override
    public void loop(AIMPad aimpad) {
        rotator.loop(aimpad);
        flexor.loop(aimpad);
    }

    @Override
    public void telemetry(Telemetry telemetry) {
        flexor.telemetry(telemetry);
        rotator.telemetry(telemetry);
    }

    public void flexNeutral() {
        flexor.setActiveTargetState(FLX_NEUTRAL);
    }

    public void flexScore() {
        flexor.setActiveTargetState(FLX_SCORE);
    }

    public void flexScoreSpecimen() {
        flexor.setActiveTargetState(FLX_SCORE_SPECIMEN);
    }

    public void flexPrepSpecimen() {
        flexor.setActiveTargetState(FLX_PREP_SPECIMEN);
    }

    public void toggleSpecimen() {
        if (flexor.getActiveTargetState() == FLX_PREP_SPECIMEN) {
            flexScoreSpecimen();
        } else {
            flexPrepSpecimen();
        }
    }

    public void flexDown() {
        flexor.setActiveTargetState(FLX_DOWN);
    }

    public void flexCustom(double position) {
        flexor.setActiveStateCustom(position);
    }

    public void rotateInLine() {
        rotator.setActiveTargetState(ROT_IN_LINE);
    }

    public void rotateHorizontal() {
        rotator.setActiveTargetState(ROT_HORIZONTAL);
    }

    public void rotateFullFlip() {
        rotator.setActiveTargetState(ROT_FULL_FLIP);
    }

    public void rotateCustom(double position) {
        rotator.setActiveStateCustom(position);
    }
}
