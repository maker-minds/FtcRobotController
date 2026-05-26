package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import mechanics.DriveTrain;
import mechanics.IMU_Sensor;
import mechanics.PID_Controller;
import mechanics.Revolver;

@TeleOp(name = "OpMode_EAGoose", group = "teleop")
public class OpMode_EAGoose extends OpMode {

    private DriveTrain DriveTrain;
    private DcMotor Intake;
    private DcMotor Outtake;
    private Revolver Revolver;
    private Servo pusher;

    private PID_Controller pidController;
    private IMU_Sensor imu;

    int timer = 0;
    int revolverTimeout = 0;
    int artifactsToShoot = 0;
    double joystick_y;
    double joystick_side;
    double joystick_turn;

    double prev_y, prev_side, prev_turn;
    double leftPower;
    double rightPower;
    double reference = 0;

    boolean a_pressed, b_pressed, x_pressed, y_pressed, left_pressed, correcting;
    boolean pid, a2_pressed, gone_left, gone_right, push_moved, shootingArtifact = false;


    @Override
    public void init() {

        a_pressed = false;

        DriveTrain = new DriveTrain(telemetry);
        DriveTrain.init(hardwareMap);
        DriveTrain.setMode("RUN_WITHOUT_ENCODER");
        DriveTrain.setMode("ZERO_POWER_BEHAVIOR_BRAKE");

        imu = new IMU_Sensor();
        imu.init(hardwareMap);

        pidController = new PID_Controller();

        Intake = hardwareMap.get(DcMotor.class, "Intake");
        Intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Intake.setDirection(DcMotor.Direction.REVERSE);

        Outtake = hardwareMap.get(DcMotor.class, "Outtake");
        Outtake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Outtake.setDirection(DcMotor.Direction.REVERSE);

        Revolver = new Revolver();
        Revolver.init(hardwareMap);

        pusher = hardwareMap.get(Servo.class, "pusher");
        pusher.setPosition(0.5);
    }

    @Override
    public void start() {
        Intake.setPower(-1);
    }

    @Override
    public void loop() {

        updateValues();

        drive();

        getButtons();

        if(!correcting) {
            Revolver.updateRevolver();
            Revolver.moveRevolver();
        } else {
            Revolver.checktarget();
        }
        correcting = false;

        if (timer > 0) {
            timer--;
        } else if (timer < 0) {
            timer++;
        } else {
            if (pusher.getPosition() < 0.3) {
                timer = -25;
                pusher.setPosition(0.5);
                push_moved = true;
            } else if (push_moved) {
                Revolver.previusPosition();
                artifactsToShoot--;
                revolverTimeout = 40;
                push_moved = false;
            } else if (revolverTimeout == 0) {
                shootingArtifact = false;
            }
        }

        if (revolverTimeout > 0) {
            revolverTimeout--;
        }

        telemetry.addData("trigger", "%7f", gamepad1.left_trigger);
        telemetry.update();


    }

    public void updateValues() {

        joystick_y = gamepad2.left_stick_y;
        joystick_side = gamepad2.left_trigger - gamepad2.right_trigger;
        joystick_turn = gamepad2.right_stick_x;

        if(joystick_y == 0 && Math.abs(prev_y) > 0.2) {
            joystick_y *= 0.9;
        }
        if(joystick_side == 0 && Math.abs(prev_side) > 0.2) {
            joystick_side *= 0.9;
        }
        if(joystick_turn == 0 && Math.abs(prev_turn) > 0.2) {
            joystick_turn *= 0.9;
        }
        if(!pid) {
            leftPower = (joystick_y - joystick_turn);
            rightPower = (joystick_y + joystick_turn);
            reference = imu.getHeading();
        } else {
            double power = pidController.calculatePower(imu.getHeading(), reference);
            leftPower = (joystick_y + power);
            rightPower = (joystick_y - power);
        }

        prev_side = joystick_side;
        prev_turn = joystick_turn;
        prev_y = joystick_y;

    }

    public void drive() {

        DriveTrain.leftfront.setPower(Range.clip(leftPower + joystick_side, -1, 1));
        DriveTrain.leftback.setPower(Range.clip(leftPower - joystick_side, -1, 1));
        DriveTrain.rightfront.setPower(Range.clip(rightPower - joystick_side, -1, 1));
        DriveTrain.rightback.setPower(Range.clip(rightPower + joystick_side, -1, 1));
    }

    public void getButtons() {

        if(gamepad2.a && !a2_pressed) {
            pid = !pid;
            a2_pressed = true;
        } else if(!gamepad2.a) {
            a2_pressed = false;
        }

        if (gamepad1.a && !a_pressed) {
            Intake.setPower(0 - Intake.getPower());
            a_pressed = true;
        } else if (!gamepad1.a) {
            a_pressed = false;
        }

        if (gamepad1.b && !b_pressed) {
            Outtake.setPower(1 - Outtake.getPower());
            b_pressed = true;
        } else if (!gamepad1.b) {
            b_pressed = false;
        }

        if (gamepad1.x && !x_pressed) {
            artifactsToShoot = 3;
            x_pressed = true;
        } else if (!gamepad1.x) {
            x_pressed = false;
        }

        if (gamepad1.y && !y_pressed) {
            pusher.setPosition(0.2);
            timer = 50;
            y_pressed = true;
        } else if (!gamepad1.y) {
            y_pressed = false;
        }

        if ((gamepad1.left_bumper || gamepad1.right_stick_x < 0) && !gone_left) {
            Revolver.previusPosition();
            gone_left = true;
        } else if(!(gamepad1.left_bumper || gamepad1.right_stick_x < 0)){
            gone_left = false;
        }
        if ((gamepad1.right_bumper || gamepad1.right_stick_x > 0) && !gone_right) {
            Revolver.nextPosition();
            gone_right = true;
        } else if(!(gamepad1.right_bumper || gamepad1.right_stick_x > 0)){
            gone_right = false;
        }

        if (gamepad1.dpad_left && !left_pressed) {
            Revolver.togglemode();
            Revolver.gotoPosition();
            if (Revolver.mode == 0) {
                Outtake.setPower(0);
            } else {
                Outtake.setPower(1);
            }
            left_pressed = true;
        } else if (!gamepad1.dpad_left) {
            left_pressed = false;
        }

        if (gamepad1.dpad_up) {
            Revolver.revolver.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        if (gamepad1.left_trigger > 0) {
            Revolver.moveLeftSlowly();
            correcting = true;
        } else if(gamepad1.right_trigger > 0) {
            Revolver.moveRightSlowly();
            correcting = true;
        }

        if (artifactsToShoot > 0 && !shootingArtifact) {
            pusher.setPosition(0.2);
            timer = 50;
            shootingArtifact = true;
        }
    }
}
