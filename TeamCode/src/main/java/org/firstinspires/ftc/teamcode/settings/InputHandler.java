package org.firstinspires.ftc.teamcode.settings;

import com.aimrobotics.aimlib.gamepad.AIMPad;

import org.firstinspires.ftc.teamcode.util.InputModification;

public class InputHandler {
    public boolean ADVANCE_AUTOMATION = false;
    public boolean TOGGLE_LOW_HANG = false;
    public boolean ADVANCE_HANG = false;
    public boolean BACKWARD_HANG = false;
    public boolean LOW_HEIGHT = false;
    public boolean HIGH_HEIGHT = false;
    public boolean RELEASE_ELEMENT = false;
    public boolean ROTATE_HORIZONTAL = false;
    public boolean RESET_ROTATION = false;
    public boolean TOGGLE_HAND_ARM = false;
    public double SLIDES_CONTROL = 0;
    public double PIVOT_CONTROL = 0;
    public boolean SET_SAMPLE = false;
    public boolean SET_SPECIMEN = false;
    public boolean SET_DUMP = false;
    public boolean WE_R_COOKED = false;
    public boolean STICK = false;

    public void updateInputs(AIMPad aimpad, AIMPad aimpad2) {
        ADVANCE_AUTOMATION = aimpad2.isDPadUpPressed();
        TOGGLE_LOW_HANG = aimpad2.isDPadRightHeld() && aimpad2.isXPressed();
        ADVANCE_HANG = aimpad2.isRightTriggerPressed();
        BACKWARD_HANG = aimpad2.isLeftTriggerPressed();
        LOW_HEIGHT = aimpad2.isLeftBumperPressed();
        HIGH_HEIGHT = aimpad2.isRightBumperPressed();
        RELEASE_ELEMENT = aimpad2.isRightTriggerPressed();
        ROTATE_HORIZONTAL = Math.abs(aimpad2.getRightStickX()) > 0.6;
        RESET_ROTATION = aimpad2.isAPressed();
        TOGGLE_HAND_ARM = aimpad2.isRightTriggerPressed();
        SLIDES_CONTROL = InputModification.poweredInput(-aimpad2.getLeftStickY(), GamepadSettings.EXPONENT_MODIFIER);
        PIVOT_CONTROL = InputModification.poweredInput(-aimpad2.getRightStickY(), GamepadSettings.EXPONENT_MODIFIER);
        SET_SAMPLE = aimpad.isLeftBumperPressed();
        SET_SPECIMEN = aimpad.isRightBumperPressed();
        SET_DUMP = aimpad.isDPadDownPressed();
        WE_R_COOKED = aimpad2.isStartHeld() && aimpad2.isYPressed();
        STICK = aimpad.isAHeld();
    }
}
