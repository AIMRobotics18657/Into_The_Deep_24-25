package org.firstinspires.ftc.teamcode.subsystems.settings;

import com.aimrobotics.aimlib.util.HardwareInterface;

public class ConfigurationInfo {

    // BRISTLES
    public static HardwareInterface bristles = new HardwareInterface("BRI", true, 0);

    //INTAKE HINGE
    public static HardwareInterface leftHinge = new HardwareInterface("LH", false, 0);
    public static HardwareInterface rightHinge = new HardwareInterface("RH", false, 0);

    // OUTAKE
    public static HardwareInterface fingers = new HardwareInterface("FO", true, 0);
    public static HardwareInterface wrist = new HardwareInterface("WO", true, 0);
    public static HardwareInterface elbow = new HardwareInterface("EO", false, 0);

    // DRIVEBASE
    public static HardwareInterface leftFront = new HardwareInterface("FLD", true, 1);
    public static HardwareInterface rightFront = new HardwareInterface("FRD", false, 1);
    public static HardwareInterface leftBack = new HardwareInterface("BLD", true, 0);
    public static HardwareInterface rightBack = new HardwareInterface("BRD", false, 0);

    // PAL
    public static HardwareInterface releaseServo = new HardwareInterface("REL", false, 2);

    // SLIDES
    public static HardwareInterface leftSlide = new HardwareInterface("LL", false, 2);
    public static HardwareInterface rightSlide = new HardwareInterface("RL", true, 2);

    // CAMERA
    public static HardwareInterface camera = new HardwareInterface("Ray", true, 0);

    // IMU
    public static HardwareInterface imu = new HardwareInterface("imu", false, 0);
}
