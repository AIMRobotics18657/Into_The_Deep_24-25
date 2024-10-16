package org.firstinspires.ftc.teamcode.subsystems;

import com.aimrobotics.aimlib.util.Mechanism;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.settings.ConfigurationInfo;

public class OuttakeSystem extends Mechanism {
    Outake outake;
    SlidesBase outakeSlides;

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

    @Override
    public void init(HardwareMap hwMap) {
        outake = new Outake();
        outake.init(hwMap);

        outakeSlides = new SlidesBase(ConfigurationInfo.leftOutakeSlide.getDeviceName(), ConfigurationInfo.rightOutakeSlide.getDeviceName(), leftMotorDirection, rightMotorDirection, kP, kI, kD, derivativeLowPassGain, integralSumMax, kV, kA, kStatic, kCos, kG, lowPassGain);
        outakeSlides.init(hwMap);
    }
}
