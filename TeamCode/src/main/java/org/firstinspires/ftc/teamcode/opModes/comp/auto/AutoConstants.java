package org.firstinspires.ftc.teamcode.opModes.comp.auto;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.gb.pinpoint.driver.Pose2D;

public class AutoConstants {

    public static final Pose2D HIGH_SPECIMEN_DROP = new Pose2D(DistanceUnit.INCH, -33, 4.5, AngleUnit.DEGREES, 0);
    public static final Pose2D PARK = new Pose2D(DistanceUnit.INCH, -63, 50, AngleUnit.DEGREES, 0);
    public static final Pose2D STARTING_POSITION = new Pose2D(DistanceUnit.INCH, -63, 4.5, AngleUnit.DEGREES, 0);
}
