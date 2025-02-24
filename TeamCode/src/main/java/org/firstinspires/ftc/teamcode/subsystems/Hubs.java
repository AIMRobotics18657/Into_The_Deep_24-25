package org.firstinspires.ftc.teamcode.subsystems;

import com.aimrobotics.aimlib.gamepad.AIMPad;
import com.aimrobotics.aimlib.util.Mechanism;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VoltageUnit;

import java.util.List;

public class Hubs extends Mechanism {

    List<LynxModule> allHubs;

    @Override
    public void init(HardwareMap hwMap) {
        allHubs = hwMap.getAll(LynxModule.class);

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
    }

    @Override
    public void loop(AIMPad aimpad) {
//        for (LynxModule hub : allHubs) {
//            hub.clearBulkCache();
//        }
    }

    @Override
    public void telemetry(Telemetry telemetry) {
        for (LynxModule hub : allHubs) {
            telemetry.addData("Hub", hub.getDeviceName());
            telemetry.addData("Voltage", hub.getInputVoltage(VoltageUnit.MILLIVOLTS));
            telemetry.addData("Current", hub.getCurrent(CurrentUnit.MILLIAMPS));
        }
    }
}
