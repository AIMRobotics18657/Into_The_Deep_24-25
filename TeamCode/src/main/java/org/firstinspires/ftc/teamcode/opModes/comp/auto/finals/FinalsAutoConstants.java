package org.firstinspires.ftc.teamcode.opModes.comp.auto.finals;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;


public class FinalsAutoConstants {
    //in inches
//THIS IS FOR RED SAMPLE SIDE!!!!!!!!!!!!!!!!!
//scoring the pre-load one


    public final double ROBOT_WIDTH = 18;
    public final double ROBOT_LENGTH = 16;
    public static final Pose2d STARTING_POSITION_SPECIMEN = new Pose2d(-12, 62, Math.toRadians(270));
    public static final Pose2d PRELOAD_DROP = new Pose2d(-2, 31.5, Math.toRadians(270));
    public static final double PUSH_SET_TANGENT = Math.toRadians(150);

    //PUSH 1
//    .setTangent(Math.toRadians(180))
    public static final Pose2d PUSH_ONE_A = new Pose2d(-27, 39, Math.toRadians(260));
    public static final Pose2d PUSH_ONE_B = new Pose2d(-39, 27, Math.toRadians(260));
    public static final Pose2d PUSH_ONE_C = new Pose2d(-38, 53, Math.toRadians(170));
    public static final double PUSH_ONE_A_B_TANGENT = Math.toRadians(180);
    public static final double PUSH_ONE_C_TANGENT = Math.toRadians(90);

    //PUSH 2
    public static final Pose2d PUSH_TWO_A = new Pose2d(-43, 30, Math.toRadians(235));
    public static final Pose2d PUSH_TWO_B = new Pose2d(-45.25, 53, Math.toRadians(160));
    public static final double PUSH_TWO_A_TANGENT = Math.toRadians(180);
    public static final double PUSH_TWO_B_TANGENT = Math.toRadians(90);

    //PUSH 3
    public static final Pose2d PUSH_THREE_A = new Pose2d(-55.5, 28.5, Math.toRadians(230));
    public static final Pose2d PUSH_THREE_B = new Pose2d(-51, 52.5, Math.toRadians(165));
    public static final Pose2d PUSH_THREE_C = new Pose2d(-36, 66, Math.toRadians(290));
    public static final double PUSH_THREE_A_TANGENT = Math.toRadians(180);
    public static final double PUSH_THREE_B_C_TANGENT = Math.toRadians(90);

    public static final Pose2d PUSHED_RELOCALIZE_POSE = new Pose2d(-37, 62.5, Math.toRadians(270));

    //HANG A TANGENT
    public static final double HANG_A_SET_TANGENT = Math.toRadians(270);
    public static final double HANG_B_SET_TANGENT = Math.toRadians(90);

    //HANG 1
    public static final Pose2d HANG_ONE_A = new Pose2d(2, 29, Math.toRadians(270));
    public static final double HANG_ONE_A_TANGENT = Math.toRadians(270);
    public static final Pose2d HANG_ONE_B = new Pose2d(-36.5, 65.5, Math.toRadians(270));
    public static final double HANG_ONE_B_TANGENT = Math.toRadians(90);

    //HANG 2
    public static final Pose2d HANG_TWO_A = new Pose2d(0, 29, Math.toRadians(270));
    public static final double HANG_TWO_A_TANGENT = Math.toRadians(270);
    public static final Pose2d HANG_TWO_B = new Pose2d(-36.5, 65.5, Math.toRadians(270));
    public static final double HANG_TWO_B_TANGENT = Math.toRadians(90);

    //HANG 3
    public static final Pose2d HANG_THREE_A = new Pose2d(-2, 29, Math.toRadians(270));
    public static final double HANG_THREE_A_TANGENT = Math.toRadians(270);
    public static final Pose2d HANG_THREE_B = new Pose2d(-36.5, 65.5, Math.toRadians(270));
    public static final double HANG_THREE_B_TANGENT = Math.toRadians(90);


    public static final Pose2d HANG_A = new Pose2d(-5, 33.25, Math.toRadians(270));
    public static final Pose2d HANG_B = new Pose2d(-3, 33, Math.toRadians(270));
    public static final Pose2d HANG_C = new Pose2d(-36, 66, Math.toRadians(270));
    public static final double HANG_A_TANGENT = Math.toRadians(270);
    public static final double HANG_B_TANGENT = Math.toRadians(0);
    public static final double HANG_C_TANGENT = Math.toRadians(90);









    //SAMPLE
    public static final Pose2d STARTING_POSITION_SAMPLE = new Pose2d(24, 61.5, Math.toRadians(0));
    public static final Pose2d PRESCORE = new Pose2d(50.25, 55.25, Math.toRadians(45));
    public static final double PRESCORE_TANGENT = Math.toRadians(180);
    public static final double PRESCORE_SET_TANGENT = Math.toRadians(270);

    //PICKUP 1
    public static final Pose2d PICKUP_ONE_A = new Pose2d(48.25, 33.5, Math.toRadians(97));
    public static final Pose2d SCORE_ONE_A = new Pose2d(56.7, 56, Math.toRadians(42));;
    public static final double PICKUP_ONE_A_TANGENT = Math.toRadians(-70);
    public static final double SCORE_ONE_A_TANGENT = Math.toRadians(90);

    //PICKUP 2
    public static final Pose2d PICKUP_TWO_A = new Pose2d(58.25, 36, Math.toRadians(97));
    public static final Pose2d SCORE_TWO_A = new Pose2d(56.5, 56, Math.toRadians(42));
    public static final double PICKUP_TWO_A_TANGENT = Math.toRadians(-90);
    public static final double SCORE_TWO_A_TANGENT = Math.toRadians(90);

    //PICKUP 3
    public static final Pose2d PICKUP_THREE_A = new Pose2d(56.25, 23.5, Math.toRadians(185));
    public static final Pose2d SCORE_THREE_A = new Pose2d(55.9, 55.4, Math.toRadians(42));
    public static final double PICKUP_THREE_A_TANGENT = Math.toRadians(-90);
    public static final double SCORE_THREE_A_TANGENT = Math.toRadians(90);

    public static final Pose2d PARK_SAMPLE = new Pose2d(18, 0, Math.toRadians(0));
    public static final double PARK_SAMPLE_TANGENT = Math.toRadians(180);
    public static final double PARK_SAMPLE_SET_TANGENT = Math.toRadians(270);
}