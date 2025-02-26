package org.firstinspires.ftc.teamcode.settings;

import com.aimrobotics.aimlib.util.HardwareInterface;

public class ConfigurationInfo {

    //
    // v2 CONFIGURATION INFORMATION
    //

    // SERVOS
    public static HardwareInterface hand = new HardwareInterface("HND", true, 0);
    public static HardwareInterface leftElbow = new HardwareInterface("LE", true, 0);
    public static HardwareInterface rightElbow = new HardwareInterface("RE", true, 0);
    public static HardwareInterface rotator = new HardwareInterface("ROT", true, 0);
    public static HardwareInterface flexor = new HardwareInterface("FLX", true, 0);
    public static HardwareInterface stick = new HardwareInterface("STK", true, 0);

    // MOTORS
    public static HardwareInterface leftSlide = new HardwareInterface("LS", true, 0);
    public static HardwareInterface rightSlide = new HardwareInterface("RS", true, 0);
    public static HardwareInterface pivot = new HardwareInterface("PVT", true, 0);

    // DRIVEBASE
    public static HardwareInterface leftFront = new HardwareInterface("FL", true, 0);
    public static HardwareInterface rightFront = new HardwareInterface("FR", false, 0);
    public static HardwareInterface leftBack = new HardwareInterface("BL", true, 1);
    public static HardwareInterface rightBack = new HardwareInterface("BR", true, 1);

    // CAMERA
    public static HardwareInterface camera = new HardwareInterface("Ray", true, 0);

    // IMU
    public static HardwareInterface imu = new HardwareInterface("imu", false, 0);
}
