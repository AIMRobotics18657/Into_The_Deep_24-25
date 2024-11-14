package org.firstinspires.ftc.teamcode.subsystems;

import com.aimrobotics.aimlib.gamepad.AIMPad;
import com.aimrobotics.aimlib.util.Mechanism;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.settings.ConfigurationInfo;
import org.firstinspires.ftc.teamcode.subsystems.settings.GamepadSettings;

public class OuttakeSystem extends Mechanism {
    Outtake outtake;
    SlidesBase outtakeSlides;

    private static final DcMotorSimple.Direction leftMotorDirection = DcMotorSimple.Direction.FORWARD;
    private static final DcMotorSimple.Direction rightMotorDirection = DcMotorSimple.Direction.REVERSE;

    private static final double kP = 0.0085;
    private static final double kI = 0.00001;
    private static final double kD = 0.00008;
    private static final double derivativeLowPassGain = 0.15;
    private static final double integralSumMax = 2500;
    private static final double kV = 0.01;
    private static final double kA = 0.0;
    private static final double kStatic = 0.0;
    private static final double kCos = 0.0;
    private static final double kG = 0;
    private static final double lowPassGain = 0.15;

    public static final double RESET_POS = 0;
    public static final double SHORT_POS = 1500;
    public static final double TALL_POS = 3500;

    public enum AutoSlidesPosition {
        RESET, SHORT, TALL
    }
    private OuttakeSystem.AutoSlidesPosition activeAutoSlidesPosition = OuttakeSystem.AutoSlidesPosition.RESET;

    public enum SlidesControlState {
        AUTONOMOUS, MANUAL
    }
    private OuttakeSystem.SlidesControlState activeControlState = OuttakeSystem.SlidesControlState.AUTONOMOUS;


    @Override
    public void init(HardwareMap hwMap) {
        outtake = new Outtake();
        outtake.init(hwMap);

        outtakeSlides = new SlidesBase(ConfigurationInfo.leftOuttakeSlide.getDeviceName(), ConfigurationInfo.rightOuttakeSlide.getDeviceName(), leftMotorDirection, rightMotorDirection, kP, kI, kD, derivativeLowPassGain, integralSumMax, kV, kA, kStatic, kCos, kG, lowPassGain);
        outtakeSlides.init(hwMap);
    }

    @Override
    public void loop(AIMPad aimpad) {
        switch (activeControlState){
            case AUTONOMOUS:
                switch(activeAutoSlidesPosition) {
                    case RESET:
                        resetState();
                        break;
                    case SHORT:
                        shortState();
                        break;
                    case TALL:
                        tallState();
                        break;
                }
                outtakeSlides.update();
                break;
            case MANUAL:
                if (Math.abs(aimpad.getLeftStickY()) > GamepadSettings.GP1_STICK_DEADZONE && !outtakeSlides.currentSpikeDetected()) {
                    outtakeSlides.setPower(-aimpad.getLeftStickY());
                } else {
                    outtakeSlides.holdPosition();
                }
                break;
        }
        outtake.loop(aimpad);
    }

    public void setActiveControlState(OuttakeSystem.SlidesControlState activeControlState) {
        this.activeControlState = activeControlState;
    }

    public void setAutoSlidesPosition(OuttakeSystem.AutoSlidesPosition activeAutoSlidesPosition) {
        setActiveControlState(OuttakeSystem.SlidesControlState.AUTONOMOUS);
        this.activeAutoSlidesPosition = activeAutoSlidesPosition;
    }

    /**
     * Set the slides to the reset position
     */
    public void resetState() {
        outtakeSlides.setTargetPosition(RESET_POS);
    }

    /**
     * Set the slides to the short position
     */
    public void shortState() {
        outtakeSlides.setTargetPosition(SHORT_POS);
    }

    /**
     * Set the slides to the tall position
     */
    public void tallState() {
        outtakeSlides.setTargetPosition(TALL_POS);
    }

    @Override
    public void telemetry(Telemetry telemetry) {
        telemetry.addData("Slides Position", outtakeSlides.getCurrentPosition());
    }

    public void systemsCheck(AIMPad aimpad, Telemetry telemetry) {
        if (aimpad.isDPadDownPressed()) {
            setAutoSlidesPosition(OuttakeSystem.AutoSlidesPosition.RESET);
        } else if (aimpad.isDPadLeftPressed()) {
            setAutoSlidesPosition(OuttakeSystem.AutoSlidesPosition.SHORT);
        } else if (aimpad.isDPadUpPressed()) {
            setAutoSlidesPosition(OuttakeSystem.AutoSlidesPosition.TALL);
        } else if (aimpad.isYPressed()) {
            setActiveControlState(OuttakeSystem.SlidesControlState.MANUAL);
        }
        loop(aimpad);
        telemetry(telemetry);
    }

}
