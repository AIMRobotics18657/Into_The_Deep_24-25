package org.firstinspires.ftc.teamcode.subsystems;

import com.aimrobotics.aimlib.util.Mechanism;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.settings.ConfigurationInfo;

public class outake extends Mechanism {

    Servo fingers;
    Servo wrist;
    Servo elbow ;
    final double wristUp = 0.5;
    final double elbowUp = 0.5;
    final double wristDown = 0.1;
    final double elbowDown = 0.1;




    @Override
    public void init(HardwareMap hwMap) {
        fingers = hwMap.get(Servo.class, ConfigurationInfo.fingers.getDeviceName());
        wrist = hwMap.get(Servo.class, ConfigurationInfo.wrist.getDeviceName());
        elbow = hwMap.get(Servo.class, ConfigurationInfo.elbow.getDeviceName());
    }

    public void hingeUpOuttake() {
        wrist.setPosition(wristUp);
        elbow.setPosition(elbowUp);
    }

    public void hingeDownOuttake() {
        wrist.setPosition(wristDown);
        elbow.setPosition(elbowDown);
    }

    public void wristPosition(double position) {
        wrist.setPosition(position);
    }

    public void elbowPosition(double position) {
        elbow.setPosition(position);
    }



    public void clamp() {
        fingers.setPower(1);
    }

    public void release() {
        fingers.setPower(-1);
    }

}
