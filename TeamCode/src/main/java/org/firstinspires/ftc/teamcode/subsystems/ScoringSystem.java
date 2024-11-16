package org.firstinspires.ftc.teamcode.subsystems;

import com.aimrobotics.aimlib.gamepad.AIMPad;
import com.aimrobotics.aimlib.util.Mechanism;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.settings.GamepadSettings;
import org.firstinspires.ftc.teamcode.util.InputModification;

public class ScoringSystem extends Mechanism {

    public IntakeSystem intakeSystem = new IntakeSystem();
    public OuttakeSystem outtakeSystem = new OuttakeSystem();
    public SpecimenGrabber specimenGrabber = new SpecimenGrabber();

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

    public enum ScoringState {
        RESETTING, SEARCHING, TRANSITIONING1, TRANSITIONING2, SLIDES_POSITIONING, SPECIMEN_POSITIONING, SPECIMEN_SCORE, REZEROING, AUTO_PERIOD
    }

    ScoringState activeScoringState = ScoringState.RESETTING;

    ScoringState outtakeRezeroingGoBackState = ScoringState.SEARCHING;


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
            case REZEROING:
                rezeroing();
                break;
            case AUTO_PERIOD:
                autoPeriod();
                break;
        }
        intakeSystem.loop(aimpad);
        outtakeSystem.loop(aimpad, aimpad2);
        specimenGrabber.loop(aimpad);
    }

    public void setActiveScoringState(ScoringState activeScoringState) {
        this.activeScoringState = activeScoringState;
    }

    public void setOuttakeSlidesRezeroing(ScoringState goBackState) {
        outtakeRezeroingGoBackState = goBackState;
        activeScoringState = ScoringState.REZEROING;
    }

    public void resettingState() {
        intakeSystem.intake.setActiveHingeState(Intake.HingeState.UP);
        intakeSystem.setActivePivotState(IntakeSystem.PivotState.PIVOT_DOWN);
        outtakeSystem.outtake.setActiveArmState(Outtake.ArmState.ARMIN);
        outtakeSystem.outtake.setActiveBucketState(Outtake.BucketState.BUCKETIN);
        specimenGrabber.setGrabberState(SpecimenGrabber.GrabberState.RELEASE);
        intakeSystem.setAutoSlidesPosition(IntakeSystem.AutoSlidesPosition.RESET);
        outtakeSystem.setAutoSlidesPosition(OuttakeSystem.AutoSlidesPosition.RESET);
        if (intakeSystem.intakeSlides.isAtTargetPosition() && outtakeSystem.outtakeSlides.isAtTargetPosition()){
            setActiveScoringState(ScoringState.SEARCHING);
        }
    }
    public void searchingState(AIMPad aimpad, AIMPad aimpad2) {

        if (aimpad.isAnyDPadPressed()) {
            outtakeRezeroingGoBackState = ScoringState.SEARCHING;
            setActiveScoringState(ScoringState.REZEROING);
        }

        // IntakeSlides control
        if (aimpad.isRightBumperPressed()) {
            intakeSystem.setAutoSlidesPosition(intakeSystem.getNextSlidePosition(intakeSystem.activeAutoSlidesPosition));
        } else if (aimpad.isLeftBumperPressed()) {
            intakeSystem.setAutoSlidesPosition(intakeSystem.getPreviousSlidePosition(intakeSystem.activeAutoSlidesPosition));
        }

        // Intake hinge control
        if (aimpad.isAHeld()) {
            intakeSystem.intake.setActiveHingeState(Intake.HingeState.DOWN);
        } else {
            intakeSystem.intake.setActiveHingeState(Intake.HingeState.NEUTRAL);
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

        String blockColor = intakeSystem.intake.getBlockColor();
        // Block detection
        if (blockColor.equals("YELLOW")  || blockColor.equals(targetBlockColor) || aimpad2.isAPressed()) {
            intakeSystem.intake.bristlesOff();
            if (isBucketMode) {
                intakeSystem.intake.setActiveHingeState(Intake.HingeState.UP);
                intakeSystem.setAutoSlidesPosition(IntakeSystem.AutoSlidesPosition.RESET);
                setActiveScoringState(ScoringState.TRANSITIONING1);
            }
        }
    }

    public void transitioning1State(AIMPad aimpad, AIMPad aimpad2) {
        if (aimpad.isAnyDPadPressed()) {
            outtakeRezeroingGoBackState = ScoringState.TRANSITIONING1;
            setActiveScoringState(ScoringState.REZEROING);
        }

        if (intakeSystem.intakeSlides.isAtTargetPosition()) {
            if (aimpad.isAPressed()) {
                setActiveScoringState(ScoringState.TRANSITIONING2);
            } else if (aimpad2.isYPressed()) {
                setActiveScoringState(ScoringState.SEARCHING);
            }
        }
    }

    public void transitioning2State(AIMPad aimpad, AIMPad aimpad2) {
        if (aimpad.isAnyDPadPressed()) {
            outtakeRezeroingGoBackState = ScoringState.TRANSITIONING2;
            setActiveScoringState(ScoringState.REZEROING);
        }

        intakeSystem.intake.bristlesIn();
        if (!(intakeSystem.intake.getBlockColor().equals("YELLOW")  || intakeSystem.intake.getBlockColor().equals(targetBlockColor))) {
            if (aimpad.getRightTrigger() > GamepadSettings.GP1_TRIGGER_DEADZONE) {
                outtakeSystem.outtake.setActiveArmState(Outtake.ArmState.ARMOUT);
                intakeSystem.intake.bristlesOff();
                setActiveScoringState(ScoringState.SLIDES_POSITIONING);
            } else if (aimpad2.isYPressed()) {
                setActiveScoringState(ScoringState.SEARCHING);
            }
        }
    }

    public void slidesPositioningState(AIMPad aimpad, AIMPad aimpad2) {
        if (aimpad.getRightTrigger() > GamepadSettings.GP1_TRIGGER_DEADZONE) {
            outtakeSystem.setAutoSlidesPosition(OuttakeSystem.AutoSlidesPosition.TALL);
        } else if (aimpad.getLeftTrigger() > GamepadSettings.GP1_TRIGGER_DEADZONE) {
            outtakeSystem.setAutoSlidesPosition(OuttakeSystem.AutoSlidesPosition.SHORT);
        }

        if (aimpad.isLeftBumperPressed() && aimpad.isRightBumperPressed()) {
            outtakeSystem.outtake.setActiveBucketState(Outtake.BucketState.BUCKETOUT);
        }
//        else if (Math.abs(aimpad.getRightStickY()) > GamepadSettings.GP1_STICK_DEADZONE) {
//            outtakeSystem.setActiveControlState(OuttakeSystem.SlidesControlState.MANUAL);
//        }

        if ((outtakeSystem.outtake.activeBucketState == Outtake.BucketState.BUCKETOUT && aimpad.isAPressed())) {
            setActiveScoringState(ScoringState.RESETTING);
        }
    }

    public void specimenPositioningState(AIMPad aimpad, AIMPad aimpad2) {
        intakeSystem.setAutoSlidesPosition(IntakeSystem.AutoSlidesPosition.RESET);
        intakeSystem.intake.setActiveHingeState(Intake.HingeState.UP);
        intakeSystem.intake.bristlesOff();
        if (aimpad2.isLeftBumperPressed()) {
            outtakeSystem.setAutoSlidesPosition(OuttakeSystem.AutoSlidesPosition.SPECIMEN_LOW);
        } else if (aimpad2.isRightBumperPressed()) {
            outtakeSystem.setAutoSlidesPosition(OuttakeSystem.AutoSlidesPosition.SPECIMEN_HIGH);
        }

        if (aimpad2.isAPressed()) {
            if (outtakeSystem.activeAutoSlidesPosition == OuttakeSystem.AutoSlidesPosition.SPECIMEN_HIGH) {
            outtakeSystem.setAutoSlidesPosition(OuttakeSystem.AutoSlidesPosition.SPECIMEN_HIGH_DROP);
            } else {
                outtakeSystem.setAutoSlidesPosition(OuttakeSystem.AutoSlidesPosition.SPECIMEN_LOW_DROP);
            }
            setActiveScoringState(ScoringState.SPECIMEN_SCORE);
        }
    }

    public void specimenScoreState(AIMPad aimpad, AIMPad aimpad2) {
        if (outtakeSystem.outtakeSlides.isAtTargetPosition() && aimpad2.isAPressed()) {
            specimenGrabber.setGrabberState(SpecimenGrabber.GrabberState.RELEASE);
            setActiveScoringState(ScoringState.RESETTING);
        }

    }

    public void rezeroing() {
        outtakeSystem.setManualPower(-0.3);
        outtakeSystem.setActiveControlState(OuttakeSystem.SlidesControlState.MANUAL);
        if (outtakeSystem.outtakeSlides.currentSpikeDetected()) {
            outtakeSystem.setManualPower(0);
            outtakeSystem.outtakeSlides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            outtakeSystem.outtakeSlides.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            setActiveScoringState(outtakeRezeroingGoBackState);
        }
    }

    public void autoPeriod() {
//        intakeSystem.intake.setActiveHingeState(Intake.HingeState.UP);
//        intakeSystem.setActivePivotState(IntakeSystem.PivotState.PIVOT_DOWN);
//        outtakeSystem.outtake.setActiveArmState(Outtake.ArmState.ARMIN);
//        outtakeSystem.outtake.setActiveBucketState(Outtake.BucketState.BUCKETIN);
    }

    @Override
    public void telemetry(Telemetry telemetry) {
        intakeSystem.telemetry(telemetry);
        telemetry.addData("Automation State", activeScoringState);
    }
}

