package org.firstinspires.ftc.teamcode.subsystems;

import com.aimrobotics.aimlib.gamepad.AIMPad;
import com.aimrobotics.aimlib.util.Mechanism;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.settings.GamepadSettings;

public class ScoringSystem extends Mechanism {

    IntakeSystem intakeSystem = new IntakeSystem();
    OuttakeSystem outtakeSystem = new OuttakeSystem();

    boolean isRed;
    String targetBlockColor;

    public ScoringSystem(boolean isRed) {
        this.isRed = isRed;
        if (isRed) {
            targetBlockColor = "RED";
        } else {
            targetBlockColor = "BLUE";
        }
    }

    enum ScoringState {
        RESETTING, SEARCHING, TRANSITIONING1, TRANSITIONING2, SLIDES_POSITIONING, SPECIMEN
    }

    ScoringState activeScoringState = ScoringState.RESETTING;


    @Override
    public void init(HardwareMap hwMap) {
        intakeSystem.init(hwMap);
        outtakeSystem.init(hwMap);
    }

    @Override
    public void loop(AIMPad aimpad, AIMPad aimpad2) {
        switch(activeScoringState) {
            case RESETTING:
                resettingState();
                break;
            case SEARCHING:
                searchingState(aimpad, aimpad2);
                break;
            case TRANSITIONING1:
                transitioning1State(aimpad, aimpad2);
                break;
            case TRANSITIONING2:
                transitioning2State(aimpad, aimpad2);
                break;
            case SLIDES_POSITIONING:
                slidesPositioningState(aimpad, aimpad2);
                break;
            case SPECIMEN:
                specimenState(aimpad, aimpad2);
                break;
        }
        intakeSystem.loop(aimpad);
        outtakeSystem.loop(aimpad);
    }

    public void setActiveScoringState(ScoringState activeScoringState) {
        this.activeScoringState = activeScoringState;

    }
    public void resettingState() {
        intakeSystem.intake.setActiveHingeState(Intake.HingeState.UP);
        intakeSystem.setActivePivotState(IntakeSystem.PivotState.PIVOT_DOWN);
        intakeSystem.setAutoSlidesPosition(IntakeSystem.AutoSlidesPosition.RESET);
        outtakeSystem.setAutoSlidesPosition(OuttakeSystem.AutoSlidesPosition.RESET);
        outtakeSystem.outtake.setActiveArmState(Outtake.ArmState.ARMIN);
        outtakeSystem.outtake.setActiveBucketState(Outtake.BucketState.BUCKETIN);
        if (intakeSystem.intakeSlides.isAtTargetPosition() && outtakeSystem.outtakeSlides.isAtTargetPosition()){
            setActiveScoringState(ScoringState.SEARCHING);
        }
    }
    public void searchingState(AIMPad aimpad, AIMPad aimpad2) {
        intakeSystem.intake.setActiveHingeState(Intake.HingeState.NEUTRAL);

        if (aimpad.isRightBumperPressed()) {
            intakeSystem.setAutoSlidesPosition(intakeSystem.getNextSlidePosition(intakeSystem.activeAutoSlidesPosition));
        } else if (aimpad.isLeftBumperPressed()) {
            intakeSystem.setAutoSlidesPosition(intakeSystem.getPreviousSlidePosition(intakeSystem.activeAutoSlidesPosition));
        }

        if (aimpad.isAHeld()) {
            intakeSystem.intake.setActiveHingeState(Intake.HingeState.DOWN);
        }

        if (aimpad2.getLeftTrigger() > GamepadSettings.GP1_TRIGGER_DEADZONE) {
            intakeSystem.intake.bristlesAtPower(-aimpad2.getLeftTrigger());
        } else if (aimpad2.getRightTrigger() > GamepadSettings.GP1_TRIGGER_DEADZONE) {
            intakeSystem.intake.bristlesAtPower(aimpad2.getRightTrigger());
        } else {
            intakeSystem.intake.bristlesIn();
        }

        if (intakeSystem.intake.getBlockColor().equals("YELLOW")  || intakeSystem.intake.getBlockColor().equals(targetBlockColor) || aimpad2.isAPressed()) {
            intakeSystem.intake.bristlesOff();
            setActiveScoringState(ScoringState.TRANSITIONING1);
        }
    }

    public void transitioning1State(AIMPad aimpad, AIMPad aimpad2) {
        if (aimpad2.isBHeld()) {
            intakeSystem.intake.bristlesOut();
        }
        intakeSystem.intake.setActiveHingeState(Intake.HingeState.UP);
        intakeSystem.setAutoSlidesPosition(IntakeSystem.AutoSlidesPosition.RESET);
        if (intakeSystem.intakeSlides.isAtTargetPosition()) {
            if (aimpad.isAPressed()) {
                setActiveScoringState(ScoringState.TRANSITIONING2);
            } else if (aimpad2.isYPressed()) {
                setActiveScoringState(ScoringState.SEARCHING);
            }
        }
    }

    public void transitioning2State(AIMPad aimpad, AIMPad aimpad2) {
        if (aimpad2.isBHeld()) {
            intakeSystem.intake.bristlesOut();
        } else {
            intakeSystem.intake.bristlesAtPower(.7);
        }
        if (intakeSystem.intake.getBlockColor().equals("NONE")) {
            if (aimpad.isAPressed()) {
                outtakeSystem.outtake.setActiveArmState(Outtake.ArmState.ARMOUT);
                intakeSystem.intake.bristlesOff();
                setActiveScoringState(ScoringState.SLIDES_POSITIONING);
            } else if (aimpad2.isYPressed()) {
                setActiveScoringState(ScoringState.SEARCHING);
            }
        }
    }

    public void slidesPositioningState(AIMPad aimpad, AIMPad aimpad2) {
        if (aimpad.isLeftBumperPressed()) {
            outtakeSystem.setAutoSlidesPosition(OuttakeSystem.AutoSlidesPosition.TALL);
        } else if (aimpad.isRightBumperPressed()) {
            outtakeSystem.setAutoSlidesPosition(OuttakeSystem.AutoSlidesPosition.SHORT);
        }
//        else if (Math.abs(aimpad.getRightStickY()) > GamepadSettings.GP1_STICK_DEADZONE) {
//            outtakeSystem.setActiveControlState(OuttakeSystem.SlidesControlState.MANUAL);
//        }

        if (aimpad.getRightTrigger() > GamepadSettings.GP1_TRIGGER_DEADZONE) {
            outtakeSystem.outtake.setActiveBucketState(Outtake.BucketState.BUCKETOUT);
        }

        if ((outtakeSystem.outtake.activeBucketState == Outtake.BucketState.BUCKETOUT && aimpad.isAPressed() || aimpad2.isYPressed())) {
            setActiveScoringState(ScoringState.RESETTING);
        }
    }

    public void specimenState(AIMPad aimpad, AIMPad aimpad2) {
        intakeSystem.setAutoSlidesPosition(IntakeSystem.AutoSlidesPosition.RESET);
        intakeSystem.intake.setActiveHingeState(Intake.HingeState.UP);
        intakeSystem.intake.bristlesIn();
        if (aimpad.isAPressed()) {
            setActiveScoringState(ScoringState.RESETTING);
        }
    }

    @Override
    public void telemetry(Telemetry telemetry) {
        telemetry.addData("Automation State", activeScoringState);
    }
}

