package org.firstinspires.ftc.teamcode.subsystems.v2;

import com.aimrobotics.aimlib.gamepad.AIMPad;
import com.aimrobotics.aimlib.util.Mechanism;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.settings.ConfigurationInfo;
import org.firstinspires.ftc.teamcode.subsystems.v1.Outtake;


public class Elbow extends Mechanism{
    Servo elbow;

    final double ELBOW_IN_POSITION = 0; //elbow is parallel to the ground
//    final double ELBOW_NEUTRAL_POSITION = 0.5; //elbow is perpendicular to the ground
    final double ELBOW_OUT_POSITION = 1; //elbow goes to outtake position

    public enum ElbowState {
        ELBOWIN, ELBOWOUT, CUSTOM
    }
    Elbow.ElbowState activeElbowState = ElbowState.ELBOWIN; // sets initial active elbow state to intake position
    double elbowTargetPosition = ELBOW_IN_POSITION;


    public void init(HardwareMap hwMap) {
        elbow = hwMap.get(Servo.class, ConfigurationInfo.elbow.getDeviceName());
        elbow.setDirection(Servo.Direction.REVERSE);
    }

    public void loop(AIMPad aimpad) {
        switch(activeElbowState) {
            case ELBOWIN:
                elbowIn();
                break;
            case ELBOWOUT:
                elbowOut();
                break;
            case CUSTOM:
                break;
        }
        elbowToPosition(elbowTargetPosition);
    }

    public void setActiveElbowState(Elbow.ElbowState activeElbowState) {
        this.activeElbowState = activeElbowState;
    }

    public void setElbowStateCustom(double elbowPosition) {
        setActiveElbowState(Elbow.ElbowState.CUSTOM);
        elbowTargetPosition = elbowPosition;
    }

    public void elbowIn() {
        elbowTargetPosition = ELBOW_IN_POSITION;
    }

    public void elbowOut() {
        elbowTargetPosition = ELBOW_OUT_POSITION;
    }

    public void elbowToPosition(double elbowPosition) {
        elbow.setPosition(elbowPosition);
    }


    //DO I NEED TELEMTRY STUFF??


    public void systemsCheck(AIMPad aimpad, Telemetry telemetry) {
        loop(aimpad);
        if (aimpad.isAPressed()) {
            setActiveElbowState(ElbowState.ELBOWIN);
        } else if (aimpad.isBPressed()) {
            setActiveElbowState(Elbow.ElbowState.ELBOWOUT);
        } else if (aimpad.isRightStickMovementEngaged()) {
            setElbowStateCustom(aimpad.getRightStickX());
        }
    }

}
