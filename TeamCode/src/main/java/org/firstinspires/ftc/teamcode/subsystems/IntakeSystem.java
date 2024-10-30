package org.firstinspires.ftc.teamcode.subsystems;

import com.aimrobotics.aimlib.gamepad.AIMPad;
import com.aimrobotics.aimlib.util.Mechanism;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.settings.ConfigurationInfo;

public class IntakeSystem extends Mechanism {

    Intake intake;
    SlidesBase intakeSlides;


    private final DcMotorSimple.Direction leftMotorDirection = DcMotorSimple.Direction.FORWARD;
    private final DcMotorSimple.Direction rightMotorDirection = DcMotorSimple.Direction.FORWARD;
    private static final double kP = 0.1;
    private static final double kI = 0.0;
    private static final double kD = 0.0;
    private static final double derivativeLowPassGain = 0.0;
    private static final double integralSumMax = 0.0;
    private static final double kV = 0.0;
    private static final double kA = 0.0;
    private static final double kStatic = 0.0;
    private static final double kCos = 0.0;
    private static final double kG = 0.0;
    private static final double lowPassGain = 0.0;

    Servo leftSlidePivot;
    Servo rightSlidePivot;

    public final double RESET_POS = 0;
    public final double SHORT_POS = 200;
    public final double MEDIUM_POS = 400;
    public final double LONG_POS = 800;
    private double pivotTargetPosition = HINGE_UP_POSITION;

    private final static double HINGE_DOWN_POSITION = -0.5;
    private final static double HINGE_UP_POSITION = 0.5;


    public enum PivotState {
        PIVOT_DOWN, PIVOT_UP, PIVOT_CUSTOM
    }
    private PivotState activePivotState = PivotState.PIVOT_DOWN;


    private enum AutoSlidesPosition {
        RESET, SHORT, MEDIUM, LONG
    }
    private AutoSlidesPosition activeAutoSlidesPosition = AutoSlidesPosition.RESET;

    double slidesTargetPosition = RESET_POS;


    private enum SlidesControlState {
        AUTONOMOUS, MANUAL
    }
    private SlidesControlState activeControlState = SlidesControlState.AUTONOMOUS;


    @Override
    public void init(HardwareMap hwMap) {
        intake = new Intake();
        intake.init(hwMap);

        intakeSlides = new SlidesBase(ConfigurationInfo.leftIntakeSlide.getDeviceName(), ConfigurationInfo.rightIntakeSlide.getDeviceName(), leftMotorDirection, rightMotorDirection, kP, kI, kD, derivativeLowPassGain, integralSumMax, kV, kA, kStatic, kCos, kG, lowPassGain);
        intakeSlides.init(hwMap);

        leftSlidePivot = hwMap.get(Servo.class, ConfigurationInfo.leftIntakeSlidePivot.getDeviceName());
        rightSlidePivot = hwMap.get(Servo.class, ConfigurationInfo.rightIntakeSlidePivot.getDeviceName());
    }

    @Override
    public void loop(AIMPad aimpad) {
        switch (activeControlState){
            case AUTONOMOUS:

                //TODO Comment
                switch(activeAutoSlidesPosition) {
                    case RESET:
                        resetState();
                        break;
                    case SHORT:
                        shortState();
                        break;
                    case MEDIUM:
                        mediumState();
                        break;
                    case LONG:
                        longState();
                        break;
                    intakeSlides.update();
                }
                break;
            case MANUAL:
                //TODO Bounds
                if (aimpad.isLeftStickMovementEngaged()) {
                    intakeSlides.setPower(aimpad.getLeftStickY());
                } else {
                    intakeSlides. // TODO HOLD POSITION
                }
                break;
        }

        switch (activePivotState) {
            case PIVOT_DOWN:
                pivotDownState();
                break;
            case PIVOT_UP:
                pivotUpState();
                break;
            case PIVOT_CUSTOM:
                break;
        }

        pivotToPosition(pivotTargetPosition);
    }

    public void setActiveControlState(SlidesControlState activeControlState) {
        this.activeControlState = activeControlState;
    }


    public void resetState() {
        intakeSlides.setTargetPosition(RESET_POS);
    }

    public void shortState() {
        intakeSlides.setTargetPosition(SHORT_POS);
    }

    public void mediumState() {
        intakeSlides.setTargetPosition(MEDIUM_POS);
    }

    public void longState() {
        intakeSlides.setTargetPosition(LONG_POS);
    }

    public void setActivePivotState(PivotState activePivotState) {
        this.activePivotState = activePivotState;
    }

    public void setPivotStateCustom(double position) {
        activePivotState = PivotState.PIVOT_CUSTOM;
        pivotTargetPosition = position;
    }

    public void pivotDownState() {
        pivotTargetPosition = HINGE_DOWN_POSITION;
    }
    public void pivotUpState() {
        pivotTargetPosition = HINGE_UP_POSITION;
    }

    public void pivotToPosition(double pivotPosition) {
        double clampedPivotPosition = Math.max(HINGE_DOWN_POSITION, Math.min(pivotPosition, HINGE_UP_POSITION));
        leftSlidePivot.setPosition(clampedPivotPosition);
        rightSlidePivot.setPosition(clampedPivotPosition);
    }


    public void setAutoSlidesPosition(AutoSlidesPosition activeAutoSlidesPosition) {
        setActiveControlState(SlidesControlState.AUTONOMOUS);
        this.activeAutoSlidesPosition = activeAutoSlidesPosition;
    }

    public void systemsCheck(AIMPad aimpad) {
        if (aimpad.isAPressed()) {
            setActivePivotState(PivotState.PIVOT_UP);
        } else if (aimpad.isBPressed()) {
            setActivePivotState(PivotState.PIVOT_DOWN);
        } else if (aimpad.isLeftStickMovementEngaged()) {
            setPivotStateCustom(aimpad.getLeftStickX());
        }
        if (aimpad.isXPressed()) {
            setAutoSlidesPosition(AutoSlidesPosition.SHORT);
        } else if (aimpad.isYPressed()) {
            setAutoSlidesPosition(AutoSlidesPosition.MEDIUM);
        } else if (aimpad.isLeftStickMovementEngaged()) {
            setSlidesStateCustom(aimpad.getLeftStickX());
        }
        loop(aimpad);
    }

}
