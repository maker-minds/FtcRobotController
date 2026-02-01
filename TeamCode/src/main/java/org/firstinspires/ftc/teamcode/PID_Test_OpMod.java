package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import mechanics.DriveTrain;
import mechanics.IMU_Sensor;
import mechanics.PID_Controller;

@TeleOp(name = "PID_Test_OpMod", group = "teleop")
public class PID_Test_OpMod extends OpMode {

    public IMU_Sensor imu;
    public PID_Controller pid;
    public DriveTrain drivetrain;

    public double power;

    @Override
    public void init() {
        imu = new IMU_Sensor();
        pid = new PID_Controller();
        drivetrain = new DriveTrain(telemetry);
        drivetrain.init(hardwareMap);
        drivetrain.setMode("RUN_WITHOUT_ENCODERS");
        imu.init(hardwareMap);
    }

    @Override
    public void loop() {

        power = pid.calculatePower(imu.getHeading(), 0);
        telemetry.addData("heading", "%.2f", imu.getHeading());
        telemetry.addData("power", "%.2f", power);
        telemetry.update();

        drivetrain.setPowerleft(power);
        drivetrain.setPowerright(-power);

    }
}
