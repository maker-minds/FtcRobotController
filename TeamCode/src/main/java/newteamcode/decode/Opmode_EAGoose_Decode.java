package newteamcode.decode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Opmode_EAGoose_Decode  extends OpMode {

    //---Objects---\\
    DriveTrain driveTrain = new DriveTrain();
    private DcMotor intake = null;

    @Override
    public void init() {
        driveTrain.init(hardwareMap);
        driveTrain.setMode(DriveTrain.modes.RUN_WITHOUT_ENCODDER);

        intake = hardwareMap.get(DcMotor.class, "Intake");
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setDirection(DcMotor.Direction.REVERSE);

    }

    @Override
    public void loop() {

        //---driving---\\

        double driveSpeed = gamepad1.left_stick_y;
        double turnSpeed = gamepad1.right_stick_x;
        double sideSpeed = gamepad1.left_trigger - gamepad1.right_trigger;
        driveTrain.calculatePower(driveSpeed, turnSpeed, sideSpeed);

        //---tasks---\\

        intake.setPower(1);
    }
}
