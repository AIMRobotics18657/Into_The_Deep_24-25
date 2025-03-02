package org.firstinspires.ftc.teamcode.opModes.comp.auto.finals;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;


public class BackupConstants {
    //in inches
//THIS IS FOR RED SAMPLE SIDE!!!!!!!!!!!!!!!!!
//scoring the pre-load one


    public final double ROBOT_WIDTH = 18;
    public final double ROBOT_LENGTH = 16;
    public static final Pose2d STARTING_POSITION = new Pose2d(-12, 62, Math.toRadians(270));
    public static final Pose2d PRELOAD_DROP = new Pose2d(-8, 32.25, Math.toRadians(270));
    public static final Pose2d CLEARANCE = new Pose2d(-9, 33.5, Math.toRadians(270));

    //PUSH 1
    public static final Pose2d PUSH_ONE_A = new Pose2d(-26, 42, Math.toRadians(270));
    public static final Pose2d PUSH_ONE_B = new Pose2d(-36, 20, Math.toRadians(270));
    public static final Pose2d PUSH_ONE_C = new Pose2d(-38, 10, Math.toRadians(270));
    public static final Pose2d PUSH_ONE_D = new Pose2d(-40, 48, Math.toRadians(270));
    public static final double PUSH_ONE_A_C_TANGENT = Math.toRadians(180);
    public static final double PUSH_ONE_B_D_TANGENT = Math.toRadians(270);

    //PUSH 2
    public static final Pose2d PUSH_TWO_A = new Pose2d(-50, 12, Math.toRadians(270));
    public static final Pose2d PUSH_TWO_B = new Pose2d(-51, 46, Math.toRadians(270));
    public static final double PUSH_TWO_A_TANGENT = Math.toRadians(180);
    public static final double PUSH_TWO_B_TANGENT = Math.toRadians(270);

    public static final Pose2d HANG_ONE_A = new Pose2d(-36, 66, Math.toRadians(270));
    public static final double HANG_ONE_A_TANGENT = Math.toRadians(90);

    public static final Pose2d HANG_A = new Pose2d(-5, 33.25, Math.toRadians(270));
    public static final Pose2d HANG_B = new Pose2d(-3, 33, Math.toRadians(270));
    public static final Pose2d HANG_C = new Pose2d(-36, 66, Math.toRadians(270));
    public static final double HANG_A_TANGENT = Math.toRadians(270);
    public static final double HANG_B_TANGENT = Math.toRadians(0);
    public static final double HANG_C_TANGENT = Math.toRadians(90);



}