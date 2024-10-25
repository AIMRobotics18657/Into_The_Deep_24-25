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


    private static final DcMotorSimple.Direction leftMotorDirection = DcMotorSimple.Direction.FORWARD;
    private static final DcMotorSimple.Direction rightMotorDirection = DcMotorSimple.Direction.FORWARD;
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

    private double pivotTargetPosition = UP_HINGE_POSITION;

    private final static double DOWN_HINGE_POSITION = -0.5;
    private final static double UP_HINGE_POSITION = 0.5;

    private final static double IN_SLIDES_POSITION = -0.5;
    private final static double OUT_SLIDES_POSITION = 0.5;


    private enum PivotState {
        PIVOTDOWN, PIVOTUP, PIVOTCUSTOM
    }
    private PivotState activePivotState = PivotState.PIVOTDOWN;


    enum SlidesState {
        SLIDESIN, SLIDESOUT, SLIDESCUSTOM
    }
    private SlidesState activeSlidesState = SlidesState.SLIDESIN;
    double slidesTargetPosition = IN_SLIDES_POSITION;


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
        switch (activePivotState) {
            case PIVOTDOWN:
                pivotDownState();
                break;
            case PIVOTUP:
                pivotUpState();
                break;
            case PIVOTCUSTOM:
                break;
        }
        switch(activeSlidesState) {
            case SLIDESIN:
                slidesInState();
                break;
            case SLIDESOUT:
                slidesOutState();
                break;
            case SLIDESCUSTOM:
                break;
        }
        pivotToPosition(pivotTargetPosition);
        slidesToPosition(slidesTargetPosition);
    }

    public void setPivotTargetPosition(double pivotTargetPosition) {
        this.pivotTargetPosition = pivotTargetPosition;
    }

    public void setPivotStateCustom(double position) {
        setPivotTargetPosition(PivotState.PIVOTCUSTOM);
        pivotTargetPosition = position;
    }

    public void pivotDownState() {
        pivotTargetPosition = DOWN_HINGE_POSITION;
    }
    public void pivotUpState() {
        pivotTargetPosition = UP_HINGE_POSITION;
    }


    public void setSlidesTargetPosition(double slidesTargetPosition) {
        this.slidesTargetPosition = slidesTargetPosition;
    }

    public void setSlidesStateCustom(double position) {
        setSlidesTargetPosition(SlidesState.SLIDESCUSTOM);
        slidesTargetPosition = position;
    }

    public void slidesInState() {
        slidesTargetPosition = IN_SLIDES_POSITION;
    }
    public void slidesOutState() {
        slidesTargetPosition = OUT_SLIDES_POSITION;
    }


    public void pivotToPosition(double pivotPosition) {
        double clampedPivotPosition = Math.max(DOWN_HINGE_POSITION, Math.min(pivotPosition, UP_HINGE_POSITION));
        leftSlidePivot.setPosition(clampedPivotPosition);
        rightSlidePivot.setPosition(clampedPivotPosition);
    }

    public void slidesToPosition(double slidesPosition) {
        double clampedSlidesPosition = Math.max(IN_SLIDES_POSITION, Math.min(slidesPosition, OUT_SLIDES_POSITION));
        leftSlidePivot.setPosition(clampedSlidesPosition);
        rightSlidePivot.setPosition(clampedSlidesPosition);
    }

    public void systemsCheck(AIMPad aimpad) {
        loop(aimpad);
        if (aimpad.isAPressed()) {
            setPivotTargetPosition(PivotState.PIVOTUP);
        } else if (aimpad.isBPressed()) {
            setPivotTargetPosition(PivotState.PIVOTDOWN);
        } else if (aimpad.isLeftStickMovementEngaged()) {
            setPivotStateCustom(aimpad.getLeftStickX());
        }
        loop(aimpad);
        if (aimpad.isAPressed()) {
            setSlidesTargetPosition(SlidesState.SLIDESIN);
        } else if (aimpad.isBPressed()) {
            setSlidesTargetPosition(SlidesState.SLIDESOUT);
        } else if (aimpad.isLeftStickMovementEngaged()) {
            setSlidesStateCustom(aimpad.getLeftStickX());
        }
    }

}
