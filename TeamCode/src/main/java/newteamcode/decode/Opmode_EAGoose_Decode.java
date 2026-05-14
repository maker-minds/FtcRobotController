package newteamcode.decode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Opmode_EAGoose_Decode extends OpMode {

    //---Objects---\\

    private DriveTrain driveTrain = new DriveTrain();
    private Revolver Revolver = new Revolver();
    private  DcMotor intake = null;
    private DcMotorEx outtake = null;


    //---Functions---\\

    @Override
    public void init() {
        driveTrain.init(hardwareMap);
        driveTrain.setMode(DriveTrain.modes.RUN_WITHOUT_ENCODER);

        Revolver.init(hardwareMap);

        intake = hardwareMap.get(DcMotor.class, "Intake");
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setDirection(DcMotor.Direction.REVERSE);

    }

    @Override
    public void loop() {

        //---tasks---\\

        intake.setPower(1);


        //---driving---\\

        double driveSpeed = gamepad1.left_stick_y;
        double turnSpeed = gamepad1.right_stick_x;
        double sideSpeed = gamepad1.left_trigger - gamepad1.right_trigger;
        driveTrain.calculatePower(driveSpeed, turnSpeed, sideSpeed);
    }
}
