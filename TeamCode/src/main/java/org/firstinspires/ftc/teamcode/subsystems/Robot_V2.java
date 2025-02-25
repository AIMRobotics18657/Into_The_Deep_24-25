package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.roadrunner.Pose2d;
import com.aimrobotics.aimlib.gamepad.AIMPad;
import com.aimrobotics.aimlib.util.Mechanism;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.settings.InputHandler;
import org.firstinspires.ftc.teamcode.subsystems.multiaxisarm.Hand;

public class Robot_V2 extends Mechanism {

    boolean isAuto;
    int isRed;
    boolean canFlipBack = false;
    double lastHeight = 0;

    final double RELEASE_MS = 200;

    public Drivebase drivebase;
    public ScoringAssembly scoringAssembly = new ScoringAssembly();
//    public Hubs hubs = new Hubs();

    InputHandler inputHandler = new InputHandler();

    ElapsedTime automationTimer = new ElapsedTime();

    enum RobotState {
        RESETTING,
        SEARCHING,
        PREP_SCORING,
        SCORING,
        DROP_SLIDES,
        HANGING,
        TOTAL_FIX
    }

    enum HangState {
        START,
        LOW_EXTEND,
        LOW_CLIP,
        LOW_RETRACT,
        FINAL
    }

    enum ScoringMethod {
        SAMPLE,
        SPECIMEN,
        DUMPING
    }

    ScoringMethod activeScoringMethodType = ScoringMethod.SPECIMEN;

    RobotState activeState = RobotState.RESETTING;

    HangState hangState = HangState.START;

    Pose2d startingPose;

    public Robot_V2(Pose2d startingPose, boolean isAuto, int isRed) {
        this.startingPose = startingPose;
        this.isAuto = isAuto;
        this.isRed = isRed;
        drivebase = new Drivebase(startingPose);
    }

    @Override
    public void init(HardwareMap hwMap) {
        drivebase.init(hwMap);
        scoringAssembly.init(hwMap);
//        hubs.init(hwMap);
    }

    @Override
    public void loop(AIMPad aimpad, AIMPad aimpad2) {
        scoringAssembly.loop(aimpad, aimpad2);
        if (!isAuto) {
            inputHandler.updateInputs(aimpad, aimpad2);
            drivebase.loop(aimpad);
            switch (activeState) {
                case RESETTING:
                    resettingState();
                    break;
                case SEARCHING:
                    searchingState();
                    break;
                case PREP_SCORING:
                    prepScoringState();
                    break;
                case SCORING:
                    scoringState();
                    break;
                case DROP_SLIDES:
                    dropSlides();
                    break;
                case HANGING:
                    hanging();
                    break;
                case TOTAL_FIX:
                    totalFix();
            }
        }

        if (inputHandler.MANUAL_OVERRIDE) {
            activeState = RobotState.TOTAL_FIX;
        }
    }

    @Override
    public void telemetry(Telemetry telemetry) {
        scoringAssembly.telemetry(telemetry);
//        hubs.telemetry(telemetry);
        telemetry.addData("Current State:", activeState);
        telemetry.addData("Current Element:", activeScoringMethodType);
    }

    private void resettingState() {
        switch (activeScoringMethodType) {
            case SAMPLE:
            case DUMPING:
                scoringAssembly.setPickupReset();
                break;
            case SPECIMEN:
                scoringAssembly.reset();
                break;
        }
        if (scoringAssembly.areMotorsAtTargetPresets()) {
            if (activeScoringMethodType == ScoringMethod.SPECIMEN) {
                scoringAssembly.resetSpecimen();
            }
            activeState = RobotState.SEARCHING;
        }
    }

    private void searchingState() {
        switch (activeScoringMethodType) {
            case SAMPLE:
                scoringAssembly.slides.setSlidesAtPower(inputHandler.SLIDES_CONTROL);

                if (inputHandler.RESET_ROTATION) {
                    scoringAssembly.multiAxisArm.wrist.rotateInLine();
                } else if (inputHandler.ROTATE_HORIZONTAL) {
                    scoringAssembly.multiAxisArm.wrist.rotateHorizontal();
                }

                if (inputHandler.TOGGLE_HAND_ARM) {
                    scoringAssembly.multiAxisArm.toggleSample();
                }

                if (inputHandler.SET_SPECIMEN) {
                    activeScoringMethodType = ScoringMethod.SPECIMEN;
                    activeState = RobotState.RESETTING;
                } else if (inputHandler.SET_DUMP) {
                    activeScoringMethodType = ScoringMethod.DUMPING;
                }
                break;
            case DUMPING:
                scoringAssembly.slides.setSlidesAtPower(inputHandler.SLIDES_CONTROL);

                if (inputHandler.RESET_ROTATION) {
                    scoringAssembly.multiAxisArm.wrist.rotateInLine();
                } else if (inputHandler.ROTATE_HORIZONTAL) {
                    scoringAssembly.multiAxisArm.wrist.rotateHorizontal();
                }

                if (inputHandler.TOGGLE_HAND_ARM) {
                    scoringAssembly.multiAxisArm.elbow.toggleSample();
                    scoringAssembly.multiAxisArm.hand.toggle();
                }

                if (inputHandler.SET_SPECIMEN) {
                    activeScoringMethodType = ScoringMethod.SPECIMEN;
                    activeState = RobotState.RESETTING;
                } else if (inputHandler.SET_SAMPLE) {
                    activeScoringMethodType = ScoringMethod.SAMPLE;
                }
                break;
            case SPECIMEN:
                if (inputHandler.TOGGLE_HAND_ARM) {
                    scoringAssembly.multiAxisArm.hand.toggle();
                    activeState = RobotState.PREP_SCORING;
                }

                if (inputHandler.SET_SAMPLE) {
                    activeScoringMethodType = ScoringMethod.SAMPLE;
                    activeState = RobotState.RESETTING;
                } else if (inputHandler.SET_DUMP) {
                    activeScoringMethodType = ScoringMethod.DUMPING;
                    activeState = RobotState.RESETTING;
                }
                break;
        }

        if (inputHandler.ADVANCE_AUTOMATION && scoringAssembly.multiAxisArm.hand.activeHandState == Hand.HandState.CLOSED) {
            activeState = RobotState.PREP_SCORING;
        }

        if (inputHandler.TOGGLE_LOW_HANG) {
            scoringAssembly.setHangStart();
            activeState = RobotState.HANGING;
        }
    }

    private void prepScoringState() {
        switch (activeScoringMethodType) {
            case SPECIMEN:
                scoringAssembly.setSpecimenInPosition();
                break;
            case SAMPLE:
                scoringAssembly.setScoringResetClamped();
                break;
            case DUMPING:
                scoringAssembly.multiAxisArm.neutralClosed();
                activeState = RobotState.SCORING;
                break;
        }

        if (scoringAssembly.areMotorsAtTargetPresets()) {
            if (activeScoringMethodType == ScoringMethod.SPECIMEN) {
                if (lastHeight != 0) {
                    scoringAssembly.slides.setTargetExtension(lastHeight);
                }
            }
            activeState = RobotState.SCORING;
        }

        if (scoringAssembly.pivot.isMovementPrevented()) {
            activeScoringMethodType = ScoringMethod.SAMPLE;
            activeState = RobotState.RESETTING;
        }
    }

    private void scoringState() {
        switch (activeScoringMethodType) {
            case SPECIMEN:
                if (inputHandler.HIGH_HEIGHT) {
                    scoringAssembly.slides.aLittleUp();
                } else if (inputHandler.LOW_HEIGHT) {
                    scoringAssembly.slides.aLittleDown();
                }

                if (inputHandler.TOGGLE_HAND_ARM) {
                    scoringAssembly.toggleSpecimen();
                }

                if (inputHandler.ADVANCE_AUTOMATION) {
                    lastHeight = scoringAssembly.slides.activeTargetExtension;
                    activeScoringMethodType = ScoringMethod.SPECIMEN;
                    activeState = RobotState.RESETTING;
                }
                break;
            case SAMPLE:
                if (inputHandler.HIGH_HEIGHT) {
                    scoringAssembly.slides.setSlidesPosition(Slides.SlidesExtension.HIGH_BUCKET);
                } else if (inputHandler.LOW_HEIGHT) {
                    scoringAssembly.slides.setSlidesPosition(Slides.SlidesExtension.LOW_BUCKET);
                }

                if (inputHandler.RELEASE_ELEMENT && scoringAssembly.multiAxisArm.hand.activeHandState == Hand.HandState.CLOSED) {
                    scoringAssembly.multiAxisArm.hand.open();
                    automationTimer.reset();
                    canFlipBack = true;
                }

                if (automationTimer.milliseconds() > RELEASE_MS && canFlipBack) {
                    scoringAssembly.multiAxisArm.wrist.flexNeutral();
                    scoringAssembly.resetAvoid();
                    canFlipBack = false;
                    activeState = RobotState.DROP_SLIDES;
//                    scoringAssembly.pivot.setPivotPosition(Pivot.PivotAngle.HIGH_BUCKET_RESET);
                }


//                if (scoringAssembly.areMotorsAtTargetPresets() && scoringAssembly.multiAxisArm.hand.activeHandState == Hand.HandState.OPEN && !canFlipBack) {
//                    scoringAssembly.resetAvoid();
//                    activeState = RobotState.DROP_SLIDES;
//                }
                break;
            case DUMPING:
                if (inputHandler.SET_SAMPLE) {
                    activeScoringMethodType = ScoringMethod.SAMPLE;
                    activeState = RobotState.PREP_SCORING;
                }
                scoringAssembly.slides.setSlidesAtPower(inputHandler.SLIDES_CONTROL);
                if (inputHandler.TOGGLE_HAND_ARM) {
                    scoringAssembly.multiAxisArm.hand.toggle();
                    activeState = RobotState.RESETTING;
                }
        }
    }

    private void dropSlides() {
        if (scoringAssembly.areMotorsAtTarget()) {
           activeState =  RobotState.RESETTING;
        }
    }

    private void hanging() {
        switch (hangState) {
            case START:
                scoringAssembly.setHangStart();
                if (inputHandler.ADVANCE_HANG) {
                    hangState = HangState.LOW_EXTEND;
                }
                break;
            case LOW_EXTEND:
                scoringAssembly.setLowHangExtended();
                if (inputHandler.ADVANCE_HANG) {
                    hangState = HangState.LOW_CLIP;
                } else if (inputHandler.BACKWARD_HANG) {
                    hangState = HangState.START;
                }
                break;
            case LOW_CLIP:
                scoringAssembly.setLowHangClip();
                if (inputHandler.ADVANCE_HANG) {
                    hangState = HangState.LOW_RETRACT;
                } else if (inputHandler.BACKWARD_HANG) {
                    hangState = HangState.LOW_EXTEND;
                }
                break;
            case LOW_RETRACT:
                scoringAssembly.setLowHangRetracted();
                if (inputHandler.ADVANCE_HANG) {
                    hangState = HangState.FINAL;
                } else if (inputHandler.BACKWARD_HANG) {
                    hangState = HangState.LOW_CLIP;
                }
                break;
            case FINAL:
                scoringAssembly.setHighHangFinal();
                if (inputHandler.BACKWARD_HANG) {
                    hangState = HangState.LOW_RETRACT;
                }
                break;
        }
        if (inputHandler.TOGGLE_LOW_HANG) {
            activeState = RobotState.RESETTING;
        }
    }

    private void totalFix() { // TODO CURRENT BASED
        scoringAssembly.totalFix();
        if (inputHandler.ADVANCE_AUTOMATION) {
            scoringAssembly.slides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            scoringAssembly.pivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            scoringAssembly.slides.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            scoringAssembly.pivot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            activeState = RobotState.RESETTING;
        }
    }
}
