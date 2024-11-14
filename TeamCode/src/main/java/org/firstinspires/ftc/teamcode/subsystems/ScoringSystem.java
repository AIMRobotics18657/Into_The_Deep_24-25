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
        RESETTING, SEARCHING, TRANSITIONING1, TRANSITIONING2, SLIDES_POSITIONING
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
        intakeSystem.intake.bristlesIn();
        intakeSystem.setAutoSlidesPosition(IntakeSystem.AutoSlidesPosition.SHORT);

        if (aimpad.isRightBumperPressed()) {
            intakeSystem.setAutoSlidesPosition(intakeSystem.getNextSlidePosition(intakeSystem.activeAutoSlidesPosition));
        } else if (aimpad.isLeftBumperPressed()) {
            intakeSystem.setAutoSlidesPosition(intakeSystem.getPreviousSlidePosition(intakeSystem.activeAutoSlidesPosition));
        }

        if (aimpad.isBHeld()) {
            intakeSystem.intake.setActiveHingeState(Intake.HingeState.DOWN);
        }

        if (intakeSystem.intake.getBlockColor().equals("YELLOW")  || intakeSystem.intake.getBlockColor().equals(targetBlockColor) || aimpad.isAPressed()) {
            intakeSystem.intake.bristlesOff();
            setActiveScoringState(ScoringState.TRANSITIONING1);
        }
    }

    public void transitioning1State(AIMPad aimpad, AIMPad aimpad2) {
        intakeSystem.intake.setActiveHingeState(Intake.HingeState.UP);
        intakeSystem.setAutoSlidesPosition(IntakeSystem.AutoSlidesPosition.RESET);
        if (intakeSystem.intakeSlides.isAtTargetPosition()) {
            setActiveScoringState(ScoringState.TRANSITIONING2);
        }
    }

    public void transitioning2State(AIMPad aimpad, AIMPad aimpad2) {
        intakeSystem.intake.bristlesIn();
        if (intakeSystem.intake.getBlockColor().equals("NONE") && aimpad.isAPressed()) {
            outtakeSystem.outtake.setActiveArmState(Outtake.ArmState.ARMOUT);
            intakeSystem.intake.bristlesOff();
            setActiveScoringState(ScoringState.SLIDES_POSITIONING);
        }
    }

    public void slidesPositioningState(AIMPad aimpad, AIMPad aimpad2) {
        if (aimpad.isDPadLeftPressed()) {
            outtakeSystem.setAutoSlidesPosition(OuttakeSystem.AutoSlidesPosition.TALL);
        } else if (aimpad.isDPadRightPressed()) {
            outtakeSystem.setAutoSlidesPosition(OuttakeSystem.AutoSlidesPosition.SHORT);
        } else if (Math.abs(aimpad.getRightStickY()) > GamepadSettings.GP1_STICK_DEADZONE) {
            outtakeSystem.setActiveControlState(OuttakeSystem.SlidesControlState.MANUAL);
        }

        if (aimpad.getLeftTrigger() > GamepadSettings.GP1_TRIGGER_DEADZONE) {
            outtakeSystem.outtake.setActiveBucketState(Outtake.BucketState.BUCKETOUT);
        }

        if (outtakeSystem.outtake.activeBucketState == Outtake.BucketState.BUCKETOUT && aimpad.isAPressed()) {
            setActiveScoringState(ScoringState.RESETTING);
        }
    }

    @Override
    public void telemetry(Telemetry telemetry) {
        telemetry.addData("Automation State", activeScoringState);
    }
}

