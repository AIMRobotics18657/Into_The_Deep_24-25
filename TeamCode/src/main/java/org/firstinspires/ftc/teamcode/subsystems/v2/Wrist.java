package org.firstinspires.ftc.teamcode.subsystems.v2;

import com.aimrobotics.aimlib.gamepad.AIMPad;
import com.aimrobotics.aimlib.util.Mechanism;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.settings.ConfigurationInfo;

public class Wrist extends Mechanism {

    public Servo rotator;
    public Servo flexor;

    private static final double FLX_UP_POSITION = 0;
    private static final double FLX_NEUTRAL_POSITION = .5;
    private static final double FLX_DOWN_POSITION = 1;

    private static final double ROT_LEFT_POSITION = 0;
    private static final double ROT_CENTER_POSITION = .5;
    private static final double ROT_RIGHT_POSITION = 1;

    public enum FlexorState {
        UP, NEUTRAL, DOWN, CUSTOM
    }

    public enum RotatorState {
        LEFT, CENTER, RIGHT, CUSTOM
    }

    private FlexorState activeFlexorState = FlexorState.NEUTRAL;
    private RotatorState activeRotatorState = RotatorState.CENTER;

    private double flexorTargetPosition = FLX_NEUTRAL_POSITION;
    private double rotatorTargetPosition = ROT_CENTER_POSITION;


    @Override
    public void init(HardwareMap hwMap) {
        rotator = hwMap.get(Servo.class, ConfigurationInfo.rotator.getDeviceName());
        flexor = hwMap.get(Servo.class, ConfigurationInfo.flexor.getDeviceName());
    }

    @Override
    public void loop(AIMPad aimpad) {
        switch (activeFlexorState) {
            case UP:
                flexorUp();
            case NEUTRAL:
                flexorNeutral();
            case DOWN:
                flexorDown();
            case CUSTOM:
                break;
        }
        switch (activeRotatorState) {
            case LEFT:
                rotatorLeft();
            case CENTER:
                rotatorCenter();
            case RIGHT:
                rotatorRight();
            case CUSTOM:
                break;
        }
        servosToTargetPositions(flexorTargetPosition, rotatorTargetPosition);
    }

    public void setActiveFlexorState(FlexorState flexorState) {
        activeFlexorState = flexorState;
    }

    public void setActiveRotatorState(RotatorState rotatorState) {
        activeRotatorState = rotatorState;
    }

    public void setFlexorStateCustom(double customPosition) {
        if (!activeFlexorState.equals(FlexorState.CUSTOM)) {
            activeFlexorState = FlexorState.CUSTOM;
        }
        flexorTargetPosition = customPosition;
    }

    public void setRotatorStateCustom(double customPosition) {
        if (!activeRotatorState.equals(RotatorState.CUSTOM)) {
            activeRotatorState = RotatorState.CUSTOM;
        }
        rotatorTargetPosition = customPosition;
    }

    private void servosToTargetPositions(double flexorPosition, double rotatorPosition) {
        flexor.setPosition(flexorPosition);
        rotator.setPosition(rotatorPosition);
    }

    private void flexorUp() {
        flexorTargetPosition = FLX_UP_POSITION;
    }

    private void flexorNeutral() {
        flexorTargetPosition = FLX_NEUTRAL_POSITION;
    }

    private void flexorDown() {
        flexorTargetPosition = FLX_DOWN_POSITION;
    }

    private void rotatorLeft() {
        rotatorTargetPosition = ROT_LEFT_POSITION;
    }

    private void rotatorCenter() {
        rotatorTargetPosition = ROT_CENTER_POSITION;
    }

    private void rotatorRight() {
        rotatorTargetPosition = ROT_RIGHT_POSITION;
    }
}
