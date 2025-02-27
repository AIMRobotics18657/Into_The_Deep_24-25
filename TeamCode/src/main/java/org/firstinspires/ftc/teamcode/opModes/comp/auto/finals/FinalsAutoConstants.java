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
    public static final Pose2d PRELOAD_DROP = new Pose2d(0, 30.5, Math.toRadians(270));
    public static final double PUSH_SET_TANGENT = Math.toRadians(180);

    //PUSH 1
//    .setTangent(Math.toRadians(180))
    public static final Pose2d PUSH_ONE_A = new Pose2d(-28, 39, Math.toRadians(260));
    public static final Pose2d PUSH_ONE_B = new Pose2d(-39, 27, Math.toRadians(260));
    public static final Pose2d PUSH_ONE_C = new Pose2d(-38, 51, Math.toRadians(170));
    public static final double PUSH_ONE_A_B_TANGENT = Math.toRadians(180);
    public static final double PUSH_ONE_C_TANGENT = Math.toRadians(90);

    //PUSH 2
    public static final Pose2d PUSH_TWO_A = new Pose2d(-46, 29, Math.toRadians(240));
    public static final Pose2d PUSH_TWO_B = new Pose2d(-46, 51, Math.toRadians(165));
    public static final double PUSH_TWO_A_TANGENT = Math.toRadians(180);
    public static final double PUSH_TWO_B_TANGENT = Math.toRadians(90);

    //PUSH 3
    public static final Pose2d PUSH_THREE_A = new Pose2d(-54.5, 28, Math.toRadians(230));
    public static final Pose2d PUSH_THREE_B = new Pose2d(-50, 51, Math.toRadians(165));
    public static final Pose2d PUSH_THREE_C = new Pose2d(-35, 63, Math.toRadians(290));
    public static final double PUSH_THREE_A_TANGENT = Math.toRadians(180);
    public static final double PUSH_THREE_B_C_TANGENT = Math.toRadians(90);

    public static final Pose2d PUSHED_RELOCALIZE_POSE = new Pose2d(-36.5, 63, Math.toRadians(270));

    //HANG A TANGENT
    public static final double HANG_A_SET_TANGENT = Math.toRadians(270);
    public static final double HANG_B_SET_TANGENT = Math.toRadians(90);

    //HANG 1
    public static final Pose2d HANG_ONE_A = new Pose2d(-2, 20, Math.toRadians(270));
    public static final double HANG_ONE_A_TANGENT = Math.toRadians(270);
    public static final Pose2d HANG_ONE_B = new Pose2d(-36.5, 65, Math.toRadians(270));
    public static final double HANG_ONE_B_TANGENT = Math.toRadians(90);

    //HANG 2
    public static final Pose2d HANG_TWO_A = new Pose2d(-4, 20, Math.toRadians(270));
    public static final double HANG_TWO_A_TANGENT = Math.toRadians(270);
    public static final Pose2d HANG_TWO_B = new Pose2d(-36.5, 65, Math.toRadians(270));
    public static final double HANG_TWO_B_TANGENT = Math.toRadians(90);

    //HANG 3
    public static final Pose2d HANG_THREE_A = new Pose2d(-6, 20, Math.toRadians(270));
    public static final double HANG_THREE_A_TANGENT = Math.toRadians(270);
    public static final Pose2d HANG_THREE_B = new Pose2d(-36.5, 65, Math.toRadians(270));
    public static final double HANG_THREE_B_TANGENT = Math.toRadians(90);

    //HANG 4
    public static final Pose2d HANG_FOUR_A = new Pose2d(-8, 20, Math.toRadians(270));
    public static final double HANG_FOUR_A_TANGENT = Math.toRadians(270);







    //SAMPLE
    public static final Pose2d STARTING_POSITION_SAMPLE = new Pose2d(12, 61.5, Math.toRadians(0));
    public static final Pose2d PRESCORE = new Pose2d(53.75, 53.75, Math.toRadians(47));
    public static final double PRESCORE_TANGENT = Math.toRadians(180);
    public static final double PRESCORE_SET_TANGENT = Math.toRadians(270);

    //PICKUP 1
    public static final Pose2d PICKUP_ONE_A = new Pose2d(48.25, 36.25, Math.toRadians(95));
    public static final Pose2d SCORE_ONE_A = new Pose2d(56, 55.5, Math.toRadians(45));;
    public static final double PICKUP_ONE_A_TANGENT = Math.toRadians(-70);
    public static final double SCORE_ONE_A_TANGENT = Math.toRadians(90);

    //PICKUP 2
    public static final Pose2d PICKUP_TWO_A = new Pose2d(59, 36.25, Math.toRadians(95));
    public static final Pose2d SCORE_TWO_A = new Pose2d(56.5, 56, Math.toRadians(45));
    public static final double PICKUP_TWO_A_TANGENT = Math.toRadians(-90);
    public static final double SCORE_TWO_A_TANGENT = Math.toRadians(90);

    //PICKUP 3
    public static final Pose2d PICKUP_THREE_A = new Pose2d(56.5, 23.5, Math.toRadians(185));
    public static final Pose2d SCORE_THREE_A = new Pose2d(55, 55, Math.toRadians(45));
    public static final double PICKUP_THREE_A_TANGENT = Math.toRadians(-90);
    public static final double SCORE_THREE_A_TANGENT = Math.toRadians(90);

    public static final Vector2d PARK_SAMPLE = new Vector2d(50, 50);
}