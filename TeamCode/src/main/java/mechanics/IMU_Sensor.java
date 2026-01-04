package mechanics;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class IMU_Sensor {

    IMU imu;

    public IMU_Sensor() {

    }

    public void init(HardwareMap hardwareMap) {
        imu = hardwareMap.get(IMU.class, "IMU");

        RevHubOrientationOnRobot imuOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.FORWARD, RevHubOrientationOnRobot.UsbFacingDirection.LEFT);

        imu.initialize(new IMU.Parameters(imuOrientation));
    }

    public double getHeading() {

        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }
}
