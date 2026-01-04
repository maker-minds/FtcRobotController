package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import mechanics.DriveTrain;
import mechanics.Revolver;

@TeleOp(name = "OpMode_EAGoose")
public class OpMode_EAGoose extends OpMode {

    private DriveTrain DriveTrain;
    private DcMotor Intake;
    private DcMotor Outtake;
    private Revolver Revolver;

    double joystick_y;
    double joystick_side;
    double joystick_turn;
    double leftPower;
    double rightPower;
    boolean a_pressed;
    boolean b_pressed;

    @Override
    public void init() {

        a_pressed = false;

        DriveTrain = new DriveTrain(telemetry);
        DriveTrain.init(hardwareMap);
        DriveTrain.setMode("RUN_WITHOUT_ENCODER");

        Intake = hardwareMap.get(DcMotor.class, "Intake");
        Intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Intake.setDirection(DcMotor.Direction.REVERSE);

        Outtake = hardwareMap.get(DcMotor.class, "Outtake");
        Outtake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Outtake.setDirection(DcMotor.Direction.REVERSE);

        Revolver = new Revolver();
        Revolver.init(hardwareMap);


    }

    @Override
    public void loop() {

        updateValues();

        drive();

        performActions();

        Revolver.checktarget();

//        telemetry.addData("RevolverPosition", "%7d", Revolver.revolver.getCurrentPosition());
//        telemetry.addData("error", "%7d", Revolver.revolver.getTargetPosition() - Revolver.revolver.getCurrentPosition());
//        telemetry.addData("power", "%7f", Revolver.revolver.getPower());
//        telemetry.addData("targetPosition", "%7d", Revolver.revolver.getTargetPosition());

    }

    public void updateValues() {

        joystick_y = gamepad2.left_stick_y;
        joystick_side = gamepad2.left_stick_x;
        joystick_turn = gamepad2.right_stick_x;

        leftPower = (joystick_y - joystick_turn);
        rightPower = (joystick_y + joystick_turn);
    }

    public void drive() {

        DriveTrain.leftfront.setPower(Range.clip(leftPower + joystick_side, -1, 1));
        DriveTrain.leftback.setPower(Range.clip(leftPower - joystick_side, -1, 1));
        DriveTrain.rightfront.setPower(Range.clip(rightPower - joystick_side, -1, 1));
        DriveTrain.rightback.setPower(Range.clip(rightPower + joystick_side, -1, 1));
    }

    public void performActions() {

        if (gamepad1.a && !a_pressed) {
            Intake.setPower(1 - Intake.getPower());
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

        if (gamepad1.x) {
            Revolver.nextPosition();
            Revolver.gotoPosition();
        }
        if(gamepad1.y) {
            Revolver.previusPosition();
            Revolver.gotoPosition();
        }

        if (gamepad1.dpad_left) {
            Revolver.togglemode();
            Revolver.gotoPosition();
        }
    }
}
