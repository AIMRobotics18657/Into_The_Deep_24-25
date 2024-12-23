package org.firstinspires.ftc.teamcode.subsystems.v1;

import com.aimrobotics.aimlib.gamepad.AIMPad;
import com.aimrobotics.aimlib.util.Mechanism;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.settings.ConfigurationInfo;

public class Outtake extends Mechanism {

    Servo leftArmHinge;
    Servo rightArmHinge;
    Servo bucketHinge;
//    Servo rightBucketHinge;

    final double ARM_HINGE_IN_POSITION = 0.1; //arm hinges to intake position
    final double ARM_HINGE_OUT_POSITION = .85; //arm hinges to outake position
    final double BUCKET_HINGE_IN_POSITION = 0.1; //bucket hinges to intake position
    final double BUCKET_HINGE_OUT_POSITION = 1; //bucket hinges to outake position

    // declares possible arm states
    public enum ArmState {
        ARMIN, ARMOUT, CUSTOM
    }
    ArmState activeArmState = ArmState.ARMIN; // set initial active arm state to in
    double armTargetPosition = ARM_HINGE_IN_POSITION;

    // declaring possible bucket states
    public enum BucketState {
        BUCKETIN, BUCKETOUT, CUSTOM
    }
    BucketState activeBucketState = BucketState.BUCKETIN; // sets initial bucket state to bucket in
    double bucketTargetPosition = BUCKET_HINGE_IN_POSITION;

    /**
     * initializes hardware
     * @param hwMap references the robot's hardware map
     */
    @Override
    public void init(HardwareMap hwMap) {
        leftArmHinge = hwMap.get(Servo.class, ConfigurationInfo.leftArmHinge.getDeviceName());
        rightArmHinge = hwMap.get(Servo.class, ConfigurationInfo.rightArmHinge.getDeviceName());
        bucketHinge = hwMap.get(Servo.class, ConfigurationInfo.leftBucketHinge.getDeviceName());
        rightArmHinge.setDirection(Servo.Direction.REVERSE);
        bucketHinge.setDirection(Servo.Direction.REVERSE);
//        rightBucketHinge = hwMap.get(Servo.class, ConfigurationInfo.rightBucketHinge.getDeviceName());

    }

    /**
     * creates loop of arm states and bucket states to be called
     * @param aimpad references AIMPad in slot one
     */
    @Override
    public void loop(AIMPad aimpad) {
        switch(activeArmState) {
            case ARMIN:
                armHingeIn();
                break;
            case ARMOUT:
                armHingeOut();
                break;
            case CUSTOM:
                break;
        }
        switch(activeBucketState) {
            case BUCKETIN:
                bucketHingeIn();
                break;
            case BUCKETOUT:
                bucketHingeOut();
                break;
            case CUSTOM:
                break;
        }
        armToPosition(armTargetPosition);
        bucketToPosition(bucketTargetPosition);
    }

    /**
     * setting arm state to arm state fed in
     * @param activeArmState
     */
    public void setActiveArmState(ArmState activeArmState) {
        this.activeArmState = activeArmState;
    }

    /**
     * sets arm target position to the position being fed in
     * @param armPosition
     */
    public void setArmStateCustom(double armPosition) {
        setActiveArmState(ArmState.CUSTOM);
        armTargetPosition = armPosition;
    }

    /**
     *sets bucket state to the state being fed in
     * @param activeBucketState
     */
    public void setActiveBucketState(BucketState activeBucketState) {
        this.activeBucketState = activeBucketState;
    }

    /**
     * sets bucket position to bucket state fed in
     * @param bucketPosition
     */
    public void setBucketStateCustom(double bucketPosition) {
        setActiveBucketState(BucketState.CUSTOM);
        bucketTargetPosition = bucketPosition;
    }


    /**
     * puts arm hinge to intake position
     */
    public void armHingeIn() {
        armTargetPosition = ARM_HINGE_IN_POSITION;

    }

    /**
     * puts arm hinge to outake position
     */
    public void armHingeOut() {
        armTargetPosition = ARM_HINGE_OUT_POSITION;

    }

    /**
     * puts bucket hinge to intake position
     */
    public void bucketHingeIn() {
        bucketTargetPosition = BUCKET_HINGE_IN_POSITION;

    }

    /**
     * puts bucket hinge to outtake position
     */
    public void bucketHingeOut() {
        bucketTargetPosition = BUCKET_HINGE_OUT_POSITION;
    }

    /**
     * sets position of the bucket
     * @param bucketPosition - position bucket goes to
     */
    public void bucketToPosition(double bucketPosition) {
        bucketHinge.setPosition(bucketPosition);
//        rightBucketHinge.setPosition(bucketPosition);
    }

    /**
     * sets position of the arm
     * @param armPosition - position arms go to
     */
    public void armToPosition(double armPosition) {
        leftArmHinge.setPosition(armPosition);
        rightArmHinge.setPosition(armPosition);
    }

    @Override
    public void telemetry(Telemetry telemetry) {
    }

    /**
     *
     * Systems Check
     */
    public void systemsCheck(AIMPad aimpad, Telemetry telemetry) {
        loop(aimpad);
        if (aimpad.isAPressed()) {
            setActiveArmState(ArmState.ARMIN);
        } else if (aimpad.isBPressed()) {
            setActiveArmState(ArmState.ARMOUT);
        } else if (aimpad.isLeftStickMovementEngaged()) {
            setArmStateCustom(aimpad.getLeftStickX());
        }

        if (aimpad.isXPressed()) {
            setActiveBucketState(BucketState.BUCKETIN);
        } else if (aimpad.isYPressed()) {
            setActiveBucketState(BucketState.BUCKETOUT);
        } else if (aimpad.isRightStickMovementEngaged()) {
            setBucketStateCustom(aimpad.getRightStickX());
        }
    }

}

