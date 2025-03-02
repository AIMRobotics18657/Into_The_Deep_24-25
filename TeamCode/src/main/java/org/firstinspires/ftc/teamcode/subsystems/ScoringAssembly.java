package org.firstinspires.ftc.teamcode.subsystems;

import com.aimrobotics.aimlib.gamepad.AIMPad;
import com.aimrobotics.aimlib.util.Mechanism;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.multiaxisarm.MultiAxisArm;

public class ScoringAssembly extends Mechanism {

    boolean disableArm = false;

    public MultiAxisArm multiAxisArm;
    public Pivot pivot;
    public Slides slides;

    @Override
    public void init(HardwareMap hwMap) {
        multiAxisArm = new MultiAxisArm();
        multiAxisArm.init(hwMap);
        slides = new Slides();
        slides.init(hwMap);
        pivot = new Pivot(slides);
        pivot.init(hwMap);
    }

    public void loop(AIMPad aimpad, AIMPad aimpad2) {
        slides.loop(aimpad, aimpad2);
        multiAxisArm.loop(aimpad, aimpad2);
        pivot.loop(aimpad, aimpad2);
    }

    @Override
    public void telemetry(Telemetry telemetry) {
        pivot.telemetry(telemetry);
        slides.telemetry(telemetry);
        multiAxisArm.telemetry(telemetry);
    }

    public void reset() {
        multiAxisArm.resetOpen();
        pivot.setPivotPosition(Pivot.PivotAngle.SCORE);
        slides.setSlidesPosition(Slides.SlidesExtension.RESET);
    }

    public void resetSpecimen() {
        multiAxisArm.specimenPickup();
        pivot.setPivotPosition(Pivot.PivotAngle.SPECIMEN_PICKUP);
        slides.setSlidesPosition(Slides.SlidesExtension.RESET);
    }

    public void resetAuto() {
        multiAxisArm.neutral();
        pivot.setPivotPosition(Pivot.PivotAngle.START_MORE);
        slides.setSlidesPosition(Slides.SlidesExtension.RESET_MORE);
    }

    public void resetAvoid() {
        multiAxisArm.down();
        pivot.setPivotPosition(Pivot.PivotAngle.HIGH_BUCKET_RESET);
        slides.setSlidesPosition(Slides.SlidesExtension.RESET);
    }

    public void setPickupReset() {
        multiAxisArm.down();
        pivot.setPivotPosition(Pivot.PivotAngle.PICKUP);
        slides.setSlidesPosition(Slides.SlidesExtension.RESET);
    }

    public void setScoringResetClamped() {
        multiAxisArm.upClosed();
        pivot.setPivotPosition(Pivot.PivotAngle.NEW_SCORE);
        slides.setSlidesPosition(Slides.SlidesExtension.RESET);
    }

    public void setSampleUpClamped() {
        multiAxisArm.upClosed();
        pivot.setPivotPosition(Pivot.PivotAngle.NEW_SCORE);
        slides.setSlidesPosition(Slides.SlidesExtension.HIGH_BUCKET);
    }

    public void setSpecimenInPosition() {
        multiAxisArm.specimenInPosition();
        pivot.setPivotPosition(Pivot.PivotAngle.SCORE_SPECIMEN);
        slides.setSlidesPosition(Slides.SlidesExtension.HIGH_SPECIMEN);
    }

    public void setSpecimenClampedAUTO() {
        multiAxisArm.specimenInPosition();
        pivot.setPivotPosition(Pivot.PivotAngle.SPECIMEN_PICKUP);
        slides.setSlidesPosition(Slides.SlidesExtension.HIGH_SPECIMEN_AUTO);
    }

    public void totalFix() {
        multiAxisArm.neutral();
        slides.setSlidesPosition(Slides.SlidesExtension.RESET_MORE);
        pivot.setPivotPosition(Pivot.PivotAngle.START_MORE);
    }

    public void samplePark() {
        multiAxisArm.samplePark();
        slides.setSlidesPosition(Slides.SlidesExtension.SAMPLE_PARK);
        pivot.setPivotPosition(Pivot.PivotAngle.SAMPLE_PARK);
    }

    //====================================================================================================
    // Hanging presets
    //====================================================================================================

    public void setHangStart() {
        multiAxisArm.hang();
        pivot.setPivotPosition(Pivot.PivotAngle.HANG);
        slides.setSlidesPosition(Slides.SlidesExtension.RESET);
    }

    public void setLowHangExtended() {
        multiAxisArm.hang();
        pivot.setPivotPosition(Pivot.PivotAngle.HANG);
        slides.setSlidesPosition(Slides.SlidesExtension.LOW_HANG);
    }

    public void setLowHangClip() {
        multiAxisArm.hang();
        pivot.setPivotPosition(Pivot.PivotAngle.HANG);
        slides.setSlidesPosition(Slides.SlidesExtension.LOW_HANG_CLIP);
    }

    public void setLowHangRetracted() {
        multiAxisArm.hang();
        pivot.setPivotPosition(Pivot.PivotAngle.PICKUP);
        slides.setSlidesPosition(Slides.SlidesExtension.RESET_MORE);
    }

    public void setHighHangFinal() {
        multiAxisArm.hang();
        pivot.setPivotPosition(Pivot.PivotAngle.NEW_SCORE);
        slides.setSlidesPosition(Slides.SlidesExtension.RESET_MORE);
    }

    public boolean areMotorsAtTarget() {
        return pivot.isAtTargetAngle() && slides.isAtTargetPosition();
    }

    public boolean areMotorsAtTargetPresets() {
        return pivot.isAtTargetPreset() && slides.isAtTargetPreset();
    }
}
