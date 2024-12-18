package org.firstinspires.ftc.teamcode.subsystems.v2;

import com.aimrobotics.aimlib.gamepad.AIMPad;
import com.aimrobotics.aimlib.util.Mechanism;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.settings.ConfigurationInfo;
import org.firstinspires.ftc.teamcode.subsystems.v1.Outtake;

public class Hand extends Mechanism {
    public Servo hand;
//    Servo rightBucketHinge;

    private final double CLOSED_POSITION = 0.1; //arm hinges to intake position TODO find positions
    private final double OPEN_POSITION = .85; //arm hinges to outake position


    // declares possible arm states
    public enum HandState {
        OPEN, CLOSED
    }
    private HandState activeHandState = HandState.CLOSED; // set initial active arm state to in
    private double targetPosition = CLOSED_POSITION;

    /**
     * initializes hardware
     * @param hwMap references the robot's hardware map
     */
    @Override
    public void init(HardwareMap hwMap) {
        hand = hwMap.get(Servo.class, ConfigurationInfo.hand.getDeviceName());
    }

    /**
     * creates loop of arm states and bucket states to be called
     * @param aimpad references AIMPad in slot one
     */
    @Override
    public void loop(AIMPad aimpad) {
        switch(activeHandState) {
            case OPEN:
                open();
                break;
            case CLOSED:
                close();
                break;
        }
        goToTargetPosition();
    }

    private void open() {
        targetPosition = OPEN_POSITION;
    }
    private void close() {
        targetPosition = CLOSED_POSITION;
    }

    private void goToTargetPosition() {
        hand.setPosition(targetPosition);
    }

    public void setActiveHandState(HandState activeHandState) {
        this.activeHandState = activeHandState;
    }

    @Override
    public void telemetry(Telemetry telemetry) {
        telemetry.addData("Hand state", activeHandState);
        telemetry.addData("Hand position", hand.getPosition());
    }

    /**
     *
     * Systems Check
     */
    public void systemsCheck(AIMPad aimpad, Telemetry telemetry) {
        loop(aimpad);
        if (aimpad.isAPressed()) {
            setActiveHandState(HandState.OPEN);
        } else if (aimpad.isBPressed()) {
            setActiveHandState(HandState.CLOSED);
        }
    }

}
