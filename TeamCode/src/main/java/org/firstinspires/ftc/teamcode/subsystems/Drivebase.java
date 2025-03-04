package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.aimrobotics.aimlib.gamepad.AIMPad;
import com.aimrobotics.aimlib.util.Mechanism;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.settings.GamepadSettings;
import org.firstinspires.ftc.teamcode.util.InputModification;

public class Drivebase extends Mechanism {

    public MecanumDrive drive;

    private Pose2d startingPose;

    public Drivebase(Pose2d startingPose) {
        this.startingPose = startingPose;
    }

    @Override
    public void init(HardwareMap hwMap) {
        drive = new MecanumDrive(hwMap, startingPose);
        setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior behavior) {
        drive.leftFront.setZeroPowerBehavior(behavior);
        drive.leftBack.setZeroPowerBehavior(behavior);
        drive.rightFront.setZeroPowerBehavior(behavior);
        drive.leftFront.setZeroPowerBehavior(behavior);
    }

    public void setMode(DcMotorEx.RunMode mode) {
        drive.leftFront.setMode(mode);
        drive.leftBack.setMode(mode);
        drive.rightFront.setMode(mode);
        drive.leftFront.setMode(mode);
    }

    @Override
    public void loop(AIMPad gamepad1) {
        manualDrive(gamepad1, false);
    }

    private void manualDrive(AIMPad gamepad, boolean isFieldCentric) {
        double y = InputModification.poweredInput(deadzonedStickInput(-gamepad.getLeftStickY()), GamepadSettings.EXPONENT_MODIFIER);
        double x = InputModification.poweredInput(deadzonedStickInput(-gamepad.getLeftStickX() * 1.1), GamepadSettings.EXPONENT_MODIFIER);
        double rx = InputModification.poweredInput(deadzonedStickInput(-gamepad.getRightStickX()), GamepadSettings.EXPONENT_MODIFIER);

        // Create left stick vector
        Vector2d leftStick = new Vector2d(y, x);

        // Rotate left stick vector by -heading if in fieldcentric mode
        if (isFieldCentric) {
            leftStick = rotateVector(-drive.lazyImu.get().getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS), leftStick);
        }

        // Set drive powers
        drive.setDrivePowers(new PoseVelocity2d(leftStick, rx));
    }

    /**
     * Rotates a vector by a given angle
     * @param angle angle by which to rotate the vector (RADIANS)
     * @param inputVector vector to rotate
     * @return rotated vector
     */
    public Vector2d rotateVector(double angle, Vector2d inputVector) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new Vector2d(cos * inputVector.x - sin * inputVector.y, sin * inputVector.x + cos * inputVector.y);
    }

    private double deadzonedStickInput(double input) {
        if (Math.abs(input) > GamepadSettings.GP1_STICK_DEADZONE) {
            return input;
        } else {
            return 0;
        }
    }

    @Override
    public void telemetry(Telemetry telemetry) {
        telemetry.addData("Angle:", drive.lazyImu.get().getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
    }

    public void systemsCheck(AIMPad gamepad, Telemetry telemetry) {
        loop(gamepad);
    }
}
