package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import mechanics.DriveTrain;
import mechanics.IMU_Sensor;
import mechanics.PID_Controller;

@Autonomous(name = "Autonomous_EAGoose")
public class Autonomous_EAGoose extends LinearOpMode {

    final double COUNTS_PER_MOTOR_REV = 1120;    // eg: TETRIX Motor Encoder
    final double DRIVE_GEAR_REDUCTION = 1.0;     // No External Gearing.
    final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    final double DRIVE_SPEED = 0.9;
    final double TURN_SPEED = 0.7;

    double orientation = 0;

    private final DriveTrain DriveTrain = new DriveTrain(telemetry);
    PID_Controller pid = new PID_Controller();
    IMU_Sensor imu = new IMU_Sensor();

    private final ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() {

        DriveTrain.init(hardwareMap);
        DriveTrain.setMode("RUN_USING_ENCODER");

        imu.init(hardwareMap);

        waitForStart();

        encoderDrive(DRIVE_SPEED, 10, 10, 4);
    }


    public void correctHeading() {

        double power = pid.calculatePower(imu.getHeading(), orientation);

        DriveTrain.setPowerright(-power);
        DriveTrain.setPowerleft(power);
    }

    public void encoderDrive(double speed, double leftInches, double rightInches, double timeoutS) {

        int newLeftTarget;
        int newRightTarget;

        int leftCurrentPosition;
        int rightCurrentPosition;

        leftCurrentPosition = (DriveTrain.leftback.getCurrentPosition() + DriveTrain.leftfront.getCurrentPosition()) / 2;
        rightCurrentPosition = (DriveTrain.rightback.getCurrentPosition() + DriveTrain.rightfront.getCurrentPosition()) / 2;

        if (opModeIsActive()) {

            newLeftTarget = leftCurrentPosition + (int) (leftInches * COUNTS_PER_INCH);
            newRightTarget = rightCurrentPosition + (int) (rightInches * COUNTS_PER_INCH);
            DriveTrain.setLefttargetPosition(newLeftTarget);
            DriveTrain.setRighttargetPosition(newRightTarget);

            runtime.reset();
            DriveTrain.setPowerleft(Math.abs(speed) / 2);
            DriveTrain.setPowerright(Math.abs(speed) / 2);

            while (opModeIsActive() && (runtime.seconds() < timeoutS) &&
                    (DriveTrain.leftfront.isBusy() || DriveTrain.leftback.isBusy() || DriveTrain.rightfront.isBusy() || DriveTrain.rightback.isBusy())) {

                if (leftCurrentPosition - DriveTrain.leftback.getCurrentPosition() < 100) {
                    DriveTrain.setPowerleft(Math.abs(speed) + 0.01);
                    DriveTrain.setPowerright(Math.abs(speed) + 0.01);
                } else if (newLeftTarget - DriveTrain.leftback.getCurrentPosition() < 100) {
                    DriveTrain.setPowerleft(Math.abs(speed) - 0.01);
                    DriveTrain.setPowerright(Math.abs(speed) - 0.01);
                }
                telemetry.addData("Running to", " %7d :%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Currently at", " at %7d :%7d",
                        DriveTrain.leftback.getCurrentPosition(), DriveTrain.rightback.getCurrentPosition());
                telemetry.update();
            }

            DriveTrain.setPowerleft(0);
            DriveTrain.setPowerright(0);


            sleep(250);
        }
    }
}
