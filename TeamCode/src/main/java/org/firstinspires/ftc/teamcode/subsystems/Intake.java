package org.firstinspires.ftc.teamcode.subsystems;

import android.content.res.Configuration;

import com.aimrobotics.aimlib.gamepad.AIMPad;
import com.aimrobotics.aimlib.util.Mechanism;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.settings.ConfigurationInfo;

public class Intake extends Mechanism {

    CRServo bristles;
    Servo leftHinge;
    Servo rightHinge;
    final double DOWN_HINGE_POSITION = 0.09;
    final double NEUTRAL_HINGE_POSITION = 0.35;
    final double UP_HINGE_POSITION = 0.76;

    ColorSensor bottomSensor;
    ColorSensor sideSensor;

    int[] redBlockValues = {0, 0, 0}; // TODO Add in the values
    int[] blueBlockValues = {0, 0, 0};
    int[] yellowBlockValues = {0, 0, 0};

    enum HingeState {
        UP, NEUTRAL, DOWN, CUSTOM
    }

    private HingeState activeHingeState = HingeState.NEUTRAL;
    double hingeTargetPosition = UP_HINGE_POSITION;

    @Override
    public void init(HardwareMap hwMap) {
        bristles = hwMap.get(CRServo.class, ConfigurationInfo.bristles.getDeviceName());
        leftHinge = hwMap.get(Servo.class, ConfigurationInfo.leftHinge.getDeviceName());
        rightHinge = hwMap.get(Servo.class, ConfigurationInfo.rightHinge.getDeviceName());
        rightHinge.setDirection(Servo.Direction.REVERSE);

        bottomSensor = hwMap.get(ColorSensor.class, ConfigurationInfo.bottomSensor.getDeviceName());
        sideSensor = hwMap.get(ColorSensor.class, ConfigurationInfo.sideSensor.getDeviceName());
    }

    @Override
    public void loop(AIMPad aimpad) {
        switch(activeHingeState) {
            case DOWN:
                hingeDownState();
                break;
            case NEUTRAL:
                hingeNeutralState();
                break;
            case UP:
                hingeUpState();
                break;
            case CUSTOM:
                break;
        }
        hingeToPosition(hingeTargetPosition);
    }

    public void setActiveHingeState(HingeState activeHingeState) {
        this.activeHingeState = activeHingeState;
    }

    public void setHingeStateCustom(double position) {
        setActiveHingeState(HingeState.CUSTOM);
        hingeTargetPosition = position;
    }

    public void hingeDownState() {
        hingeTargetPosition = DOWN_HINGE_POSITION;
    }
    public void hingeUpState() {
        hingeTargetPosition = UP_HINGE_POSITION;
    }
    public void hingeNeutralState() {
        hingeTargetPosition = NEUTRAL_HINGE_POSITION;
    }


    public void bristlesIn() {bristles.setPower(1);}

    public void bristlesOut() {bristles.setPower(-1);}

    public void bristlesOff(){
        bristles.setPower(0);
    }

    public void bristlesAtPower(double bristlesPower) {
        bristles.setPower(bristlesPower);
    }

    public void hingeToPosition(double hingePosition) {
        double clampedHingePosition = Math.max(DOWN_HINGE_POSITION, Math.min(hingePosition, UP_HINGE_POSITION));
        leftHinge.setPosition(clampedHingePosition);
        rightHinge.setPosition(clampedHingePosition);
    }

    private boolean matchesColor(ColorSensor sensor, int[] colorValues) {
        return sensor.red() > colorValues[0] && sensor.green() > colorValues[1] && sensor.blue() > colorValues[2];
    }

    public boolean isBlockRed() {
        return matchesColor(bottomSensor, redBlockValues) || matchesColor(sideSensor, redBlockValues);
    }

    public boolean isBlockBlue() {
        return matchesColor(bottomSensor, blueBlockValues) || matchesColor(sideSensor, blueBlockValues);
    }

    public boolean isBlockYellow() {
        return matchesColor(bottomSensor, yellowBlockValues) || matchesColor(sideSensor, yellowBlockValues);
    }

    /**
     * Detects the color of the block in front of the sensors.
     *
     * @return the detected block color as a String
     */
    public String getBlockColor() {
        if (isBlockRed()) return "RED";
        if (isBlockBlue()) return "BLUE";
        if (isBlockYellow()) return "YELLOW";
        return "NONE";
    }

    public void systemsCheck(AIMPad aimpad) {
        if (aimpad.isAPressed()) {
            setActiveHingeState(HingeState.UP);
        } else if (aimpad.isBPressed()) {
            setActiveHingeState(HingeState.DOWN);
        } else if (aimpad.isXPressed()) {
            setActiveHingeState(HingeState.NEUTRAL);
        } else if (aimpad.isYHeld()) {
            setHingeStateCustom(aimpad.getLeftStickX());
        }
        bristlesAtPower(aimpad.getRightStickY());
        loop(aimpad);
    }

}
