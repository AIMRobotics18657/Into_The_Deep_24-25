package org.firstinspires.ftc.teamcode.subsystems.v2;

import com.aimrobotics.aimlib.gamepad.AIMPad;
import com.aimrobotics.aimlib.util.Mechanism;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.settings.ConfigurationInfo;


public class Elbow extends Mechanism{
    Servo elbow;

    final double IN_POSITION = 0; //elbow is parallel to the ground
//    final double ELBOW_NEUTRAL_POSITION = 0.5; //elbow is perpendicular to the ground
    final double OUT_POSITION = 1; //elbow goes to outtake position

    public enum ElbowState {
        IN, OUT, CUSTOM
    }
    Elbow.ElbowState activeElbowState = ElbowState.IN; // sets initial active elbow state to intake position
    double targetPosition = IN_POSITION;


    public void init(HardwareMap hwMap) {
        elbow = hwMap.get(Servo.class, ConfigurationInfo.elbow.getDeviceName());
        elbow.setDirection(Servo.Direction.REVERSE);
    }

    public void loop(AIMPad aimpad) {
        switch(activeElbowState) {
            case IN:
                in();
                break;
            case OUT:
                out();
                break;
            case CUSTOM:
                break;
        }
        goToTargetPosition(targetPosition);
    }

    public void setActiveElbowState(Elbow.ElbowState activeElbowState) {
        this.activeElbowState = activeElbowState;
    }

    public void setElbowStateCustom(double elbowPosition) {
        if (!activeElbowState.equals(ElbowState.CUSTOM)) {
            setActiveElbowState(Elbow.ElbowState.CUSTOM);
        }
        targetPosition = elbowPosition;
    }

    public void in() {
        targetPosition = IN_POSITION;
    }

    public void out() {
        targetPosition = OUT_POSITION;
    }

    public void goToTargetPosition(double position) {
        elbow.setPosition(position);
    }


    public void systemsCheck(AIMPad aimpad, Telemetry telemetry) {
        loop(aimpad);
        if (aimpad.isAPressed()) {
            setActiveElbowState(ElbowState.IN);
        } else if (aimpad.isBPressed()) {
            setActiveElbowState(Elbow.ElbowState.OUT);
        } else if (aimpad.isRightStickMovementEngaged()) {
            setElbowStateCustom(aimpad.getRightStickX());
        }
    }

}
