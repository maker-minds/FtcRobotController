package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import mechanics.DriveTrain;
import mechanics.IMU_Sensor;
import mechanics.PID_Controller;
import mechanics.Revolver;


@Autonomous(name = "Autonomous_EAGoose", group = "autonomous")
public class Autonomous_EAGoose extends LinearOpMode {

    final double COUNTS_PER_MOTOR_REV = 1120;    // eg: TETRIX Motor Encoder
    final double DRIVE_GEAR_REDUCTION = 1.0;     // No External Gearing.
    final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    final double DRIVE_SPEED = 0.9;
    final double TURN_SPEED = 0.7;

    final double orientation = 0;
    int BackLeftTarget, FrontLeftTarget, BackRightTarget, FrontRightTarget;


    private final DriveTrain DriveTrain = new DriveTrain(telemetry);
    PID_Controller pid = new PID_Controller();
    IMU_Sensor imu = new IMU_Sensor();
    Revolver revolver = new Revolver();
    DcMotor outtake;
    DcMotor intake;
    Servo pusher;

    private final ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        DriveTrain.init(hardwareMap);
        DriveTrain.setMode("RUN_USING_ENCODER");
        revolver.init(hardwareMap);
        outtake = hardwareMap.get(DcMotor.class, "Outtake");
        intake = hardwareMap.get(DcMotor.class, "Intake");
        pusher = hardwareMap.get(Servo.class, "pusher");
        imu.init(hardwareMap);

        revolver.mode = 0;
        revolver.index = 0;
        revolver.gotoPosition();
        pusher.setPosition(0.5);
        waitForStart();

        encoderDrive(DRIVE_SPEED, -20, -20, -20, -20, 4);
    }


    public void correctHeading() {

        double power = pid.calculatePower(imu.getHeading(), orientation);

        DriveTrain.setPowerright(-power);
        DriveTrain.setPowerleft(power);
    }

    public void encoderDrive(double speed, double backleftInches, double frontleftinches, double backrightInches, double frontrightinches, double timeout) {

        calculateTargetPosition(backleftInches, frontleftinches, backrightInches, frontrightinches);

        if (opModeIsActive()) {

            DriveTrain.setPowerleft(Math.abs(speed));
            DriveTrain.setPowerright(Math.abs(speed));

            while (opModeIsActive() && (runtime.seconds() < timeout) &&
                    (DriveTrain.leftfront.isBusy() || DriveTrain.leftback.isBusy() || DriveTrain.rightfront.isBusy() || DriveTrain.rightback.isBusy())) {
                telemetry.update();
            }

            DriveTrain.setPowerleft(0);
            DriveTrain.setPowerright(0);

            sleep(150);
        }
    }

    public void directionDrive(double speed, double direction, double distance, double timeout) {
        int frontleftinches, backleftinches, frontrightinches, backrightinches;
        int cosin_add = (int) (distance * (Math.cos(direction) + Math.sin(direction)));
        int cosin_sub = (int) (distance * (Math.cos(direction) - Math.sin(direction)));
        frontleftinches = cosin_sub;
        backleftinches = cosin_add;
        frontrightinches =cosin_add;
        backrightinches =cosin_sub;

        encoderDrive(speed, backleftinches, frontleftinches, backrightinches, frontrightinches, timeout);
    }

    public void calculateTargetPosition(double backleftInches, double frontleftinches, double backrightInches, double frontrightinches) {
        int backleftlastPosition, newfrontleftlastPosition, backrightlastPosition, newfrontrightlastPosition;

        backleftlastPosition = DriveTrain.leftback.getCurrentPosition();
        newfrontleftlastPosition = DriveTrain.leftfront.getCurrentPosition();
        backrightlastPosition = DriveTrain.rightback.getCurrentPosition();
        newfrontrightlastPosition = DriveTrain.rightfront.getCurrentPosition();

        BackLeftTarget = backleftlastPosition + (int) (backleftInches * COUNTS_PER_INCH);
        FrontLeftTarget = newfrontleftlastPosition + (int) (frontleftinches * COUNTS_PER_INCH);
        BackRightTarget = backrightlastPosition + (int) (backrightInches * COUNTS_PER_INCH);
        FrontRightTarget = newfrontrightlastPosition + (int) (frontrightinches * COUNTS_PER_INCH);

        DriveTrain.leftback.setTargetPosition(BackLeftTarget);
        DriveTrain.leftfront.setTargetPosition(FrontLeftTarget);
        DriveTrain.rightback.setTargetPosition(BackRightTarget);
        DriveTrain.rightback.setTargetPosition(FrontRightTarget);
    }
}
