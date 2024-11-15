package org.firstinspires.ftc.teamcode.subsystems;

import com.aimrobotics.aimlib.gamepad.AIMPad;
import com.aimrobotics.aimlib.util.Mechanism;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.settings.GamepadSettings;
import org.firstinspires.ftc.teamcode.util.InputModification;

public class ScoringSystem extends Mechanism {

    IntakeSystem intakeSystem = new IntakeSystem();
    OuttakeSystem outtakeSystem = new OuttakeSystem();
    SpecimenGrabber specimenGrabber = new SpecimenGrabber();

    boolean isRed;
    String targetBlockColor;

    boolean isBucketMode = true;

    public ScoringSystem(boolean isRed) {
        this.isRed = isRed;
        if (isRed) {
            targetBlockColor = "RED";
        } else {
            targetBlockColor = "BLUE";
        }
    }

    enum ScoringState {
        RESETTING, SEARCHING, TRANSITIONING1, TRANSITIONING2, SLIDES_POSITIONING, SPECIMEN_POSITIONING, SPECIMEN_SCORE
    }

    ScoringState activeScoringState = ScoringState.RESETTING;


    @Override
    public void init(HardwareMap hwMap) {
        intakeSystem.init(hwMap);
        outtakeSystem.init(hwMap);
        specimenGrabber.init(hwMap);
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
            case SPECIMEN_POSITIONING:
                specimenPositioningState(aimpad, aimpad2);
                break;
            case SPECIMEN_SCORE:
                specimenScoreState(aimpad, aimpad2);
                break;
        }
        intakeSystem.loop(aimpad);
        outtakeSystem.loop(aimpad);
        specimenGrabber.loop(aimpad);
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
        specimenGrabber.setGrabberState(SpecimenGrabber.GrabberState.RELEASE);
        if (intakeSystem.intakeSlides.isAtTargetPosition() && outtakeSystem.outtakeSlides.isAtTargetPosition()){
            setActiveScoringState(ScoringState.SEARCHING);
        }
    }
    public void searchingState(AIMPad aimpad, AIMPad aimpad2) {
        intakeSystem.intake.setActiveHingeState(Intake.HingeState.NEUTRAL);

        // IntakeSlides control
        if (aimpad.isRightBumperPressed()) {
            intakeSystem.setAutoSlidesPosition(intakeSystem.getNextSlidePosition(intakeSystem.activeAutoSlidesPosition));
        } else if (aimpad.isLeftBumperPressed()) {
            intakeSystem.setAutoSlidesPosition(intakeSystem.getPreviousSlidePosition(intakeSystem.activeAutoSlidesPosition));
        }

        // Intake hinge control
        if (aimpad.isAHeld()) {
            intakeSystem.intake.setActiveHingeState(Intake.HingeState.DOWN);
        }

        // Bristle Control and AIMPad 2 manual override
        if (aimpad2.getLeftTrigger() > GamepadSettings.GP1_TRIGGER_DEADZONE) {
            intakeSystem.intake.bristlesAtPower(-InputModification.poweredInput(aimpad2.getLeftTrigger(), 3));
        } else if (aimpad2.getRightTrigger() > GamepadSettings.GP1_TRIGGER_DEADZONE) {
            intakeSystem.intake.bristlesAtPower(InputModification.poweredInput(aimpad2.getRightTrigger(), 3));
        } else {
            intakeSystem.intake.bristlesIn();
        }

        if (aimpad2.isDPadUpPressed() && aimpad2.isYPressed()) {
            specimenGrabber.setGrabberState(SpecimenGrabber.GrabberState.GRAB);
            setActiveScoringState(ScoringState.SPECIMEN_POSITIONING);
        }

        // Block detection
        if (intakeSystem.intake.getBlockColor().equals("YELLOW")  || intakeSystem.intake.getBlockColor().equals(targetBlockColor) || aimpad2.isAPressed()) {
            intakeSystem.intake.bristlesOff();
            if (isBucketMode) {
                setActiveScoringState(ScoringState.TRANSITIONING1);
            }
        }
    }

    public void transitioning1State(AIMPad aimpad, AIMPad aimpad2) {
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
        intakeSystem.intake.bristlesAtPower(.7);
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

    public void specimenPositioningState(AIMPad aimpad, AIMPad aimpad2) {
        intakeSystem.setAutoSlidesPosition(IntakeSystem.AutoSlidesPosition.RESET);
        intakeSystem.intake.setActiveHingeState(Intake.HingeState.UP);
        intakeSystem.intake.bristlesOff();
        if (aimpad.isLeftBumperPressed()) {
            outtakeSystem.setAutoSlidesPosition(OuttakeSystem.AutoSlidesPosition.SPECIMEN_HIGH);
        } else if (aimpad.isRightBumperPressed()) {
            outtakeSystem.setAutoSlidesPosition(OuttakeSystem.AutoSlidesPosition.SPECIMEN_LOW);
        }

        if (aimpad2.isAPressed()) {
            setActiveScoringState(ScoringState.SPECIMEN_SCORE);
        }
    }

    public void specimenScoreState(AIMPad aimpad, AIMPad aimpad2) {
        if (outtakeSystem.activeAutoSlidesPosition == OuttakeSystem.AutoSlidesPosition.SPECIMEN_HIGH) {
            outtakeSystem.setAutoSlidesPosition(OuttakeSystem.AutoSlidesPosition.SPECIMEN_HIGH_DROP);
        } else {
            outtakeSystem.setAutoSlidesPosition(OuttakeSystem.AutoSlidesPosition.SPECIMEN_LOW_DROP);
        }

        if (outtakeSystem.outtakeSlides.isAtTargetPosition() && aimpad2.isAPressed()) {
            setActiveScoringState(ScoringState.RESETTING);
        }

    }

    @Override
    public void telemetry(Telemetry telemetry) {
        telemetry.addData("Automation State", activeScoringState);
    }
}

