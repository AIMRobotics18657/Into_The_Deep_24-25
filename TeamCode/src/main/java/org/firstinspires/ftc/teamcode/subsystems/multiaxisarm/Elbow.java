package org.firstinspires.ftc.teamcode.subsystems.multiaxisarm;

import com.aimrobotics.aimlib.gamepad.AIMPad;
import com.aimrobotics.aimlib.util.Mechanism;
import com.aimrobotics.aimlib.subsystems.sds.StateDrivenServo;
import com.aimrobotics.aimlib.subsystems.sds.ServoState;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.settings.ConfigurationInfo;

public class Elbow extends Mechanism {
    public StateDrivenServo leftElbow;
    public StateDrivenServo rightElbow;

    ServoState DOWN = new ServoState(.87);
    ServoState HANG = new ServoState(.7);
    ServoState IN_LINE = new ServoState(.33);
    ServoState PICKUP_PLUS = new ServoState(.38);
    ServoState SCORE = new ServoState(.32);
    ServoState PREP_SPECIMEN = new ServoState(0.09);
    ServoState SCORE_SPECIMEN = new ServoState(0.02);
    ServoState UP = new ServoState(0);

    @Override
    public void init(HardwareMap hwMap) {
        leftElbow = new StateDrivenServo(new ServoState[]{DOWN, HANG, UP, SCORE, PICKUP_PLUS, PREP_SPECIMEN, SCORE_SPECIMEN, IN_LINE}, UP, ConfigurationInfo.leftElbow.getDeviceName());
        rightElbow = new StateDrivenServo(new ServoState[]{DOWN, HANG, UP, SCORE, PICKUP_PLUS, PREP_SPECIMEN, SCORE_SPECIMEN, IN_LINE}, UP, ConfigurationInfo.rightElbow.getDeviceName(), Servo.Direction.REVERSE);

        leftElbow.init(hwMap);
        rightElbow.init(hwMap);
    }

    @Override
    public void loop(AIMPad aimpad) {
        leftElbow.loop(aimpad);
        rightElbow.loop(aimpad);
    }

    @Override
    public void telemetry(Telemetry telemetry) {
        leftElbow.telemetry(telemetry);
        rightElbow.telemetry(telemetry);
    }

    public void forward() {
        leftElbow.setActiveTargetState(DOWN);
        rightElbow.setActiveTargetState(DOWN);
    }

    public void inLine() {
        leftElbow.setActiveTargetState(IN_LINE);
        rightElbow.setActiveTargetState(IN_LINE);
    }

    public void score() {
        leftElbow.setActiveTargetState(SCORE);
        rightElbow.setActiveTargetState(SCORE);
    }

    public void prepSpecimen() {
        leftElbow.setActiveTargetState(PREP_SPECIMEN);
        rightElbow.setActiveTargetState(PREP_SPECIMEN);
    }

    public void scoreSpecimen() {
        leftElbow.setActiveTargetState(SCORE_SPECIMEN);
        rightElbow.setActiveTargetState(SCORE_SPECIMEN);
    }

    public void hang() {
        leftElbow.setActiveTargetState(HANG);
        rightElbow.setActiveTargetState(HANG);
    }

    public void pickupPlus() {
        leftElbow.setActiveTargetState(PICKUP_PLUS);
        rightElbow.setActiveTargetState(PICKUP_PLUS);
    }

    public void toggleSpecimen() {
        if (leftElbow.getActiveTargetState() == PREP_SPECIMEN) {
            scoreSpecimen();
        } else {
            prepSpecimen();
        }
    }

    public void toggleSample() {
        if (leftElbow.getActiveTargetState() == IN_LINE) {
            pickupPlus();
        } else {
            inLine();
        }
    }

    public void custom(double position) {
        leftElbow.setActiveStateCustom(position);
        rightElbow.setActiveStateCustom(position);
    }
}
