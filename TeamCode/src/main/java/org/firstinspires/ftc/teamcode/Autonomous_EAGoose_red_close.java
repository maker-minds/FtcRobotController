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


@Autonomous(name = "Autonomous_EAGoose_red_close", group = "autonomous")
public class Autonomous_EAGoose_red_close extends LinearOpMode {

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
        outtake.setDirection(DcMotor.Direction.REVERSE);
        intake = hardwareMap.get(DcMotor.class, "Intake");
        pusher = hardwareMap.get(Servo.class, "pusher");
        imu.init(hardwareMap);

        revolver.gotoPosition();
        pusher.setPosition(0.5);
        waitForStart();

        outtake.setPower(1);
        encoderDrive(DRIVE_SPEED, -15, -15, -15, -15, 5);
        revolver.togglemode();
        revolver.updateRevolver();
        revolver.gotoPosition();
        while(revolver.revolver.isBusy()) {
            revolver.checktarget();
            revolver.updateRevolver();
        }
        pusher.setPosition(0.2);
        sleep(1000);
        pusher.setPosition(0.5);
        sleep(1500);
        revolver.nextPosition();
        revolver.gotoPosition();
        while(revolver.revolver.isBusy()) {
            revolver.checktarget();
            revolver.updateRevolver();
        }
        pusher.setPosition(0.2);
        sleep(1000);
        pusher.setPosition(0.5);
        sleep(1500);
        revolver.nextPosition();
        revolver.gotoPosition();
        while(revolver.revolver.isBusy()) {
            revolver.checktarget();
            revolver.updateRevolver();
        }
        pusher.setPosition(0.2);
        sleep(1000);
        pusher.setPosition(0.5);
        sleep(1500);
        encoderDrive(TURN_SPEED,-10, -10, 10, 10, 5);
        encoderDrive(DRIVE_SPEED, 5, 5, 5, 5, 5);
        revolver.togglemode();
        revolver.updateRevolver();
        revolver.gotoPosition();
        while(revolver.revolver.isBusy()) {
            revolver.checktarget();
            revolver.updateRevolver();
        }

    }


    public void correctHeading() {

        double power = pid.calculatePower(imu.getHeading(), orientation);

        DriveTrain.setPowerright(-power);
        DriveTrain.setPowerleft(power);
    }

    public void encoderDrive(double speed, double backleftInches, double frontleftinches, double backrightInches, double frontrightinches, double timeout) {

        if (opModeIsActive()) {

            calculateTargetPosition(-backleftInches, -frontleftinches, -backrightInches, -frontrightinches);

            DriveTrain.setPowerleft(Math.abs(speed));
            DriveTrain.setPowerright(Math.abs(speed));

            runtime.reset();

            while (opModeIsActive() && (runtime.seconds() < timeout) &&
                    (DriveTrain.leftfront.isBusy() && DriveTrain.leftback.isBusy() && DriveTrain.rightfront.isBusy() && DriveTrain.rightback.isBusy())) {
                telemetry.update();
                int diference = Math.abs(DriveTrain.leftfront.getTargetPosition() - DriveTrain.leftfront.getCurrentPosition());
                if(diference < 50) {
                    DriveTrain.setPowerleft(map(diference, 1, 50, 0.3, speed));
                    DriveTrain.setPowerright(map(diference, 1, 50, 0.3, speed));
                }
            }

            DriveTrain.setPowerleft(0);
            DriveTrain.setPowerright(0);

            sleep(250);
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
        DriveTrain.rightfront.setTargetPosition(FrontRightTarget);

        DriveTrain.leftback.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        DriveTrain.leftfront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        DriveTrain.rightback.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        DriveTrain.rightfront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public int map(int input, int inMin, int inMax, double outMin, double outMax) {
        return (int) ((input - inMin) * (outMax - outMin) / (inMax - inMin) + outMin);
    }
}
