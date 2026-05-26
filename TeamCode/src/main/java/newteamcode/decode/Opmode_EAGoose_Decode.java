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

        byte direction = 0;
        if(gamepad2.right_bumper || gamepad2.right_stick_x > 0) {
            direction = Revolver.RIGHT;
        } else if (gamepad2.left_bumper || gamepad2.right_stick_x < 0) {
            direction = Revolver.LEFT;
        } else if(gamepad2.b) {
            direction = Revolver.STOP;
        } else if (gamepad2.a) {
            direction = Revolver.AUTO_ALIGN;
        } else if(gamepad2.left_stick_x > 0) {
            direction = Revolver.MANUAL_RIGHT;
        } else if(gamepad2.left_stick_x < 0) {
            direction = Revolver.MANUAL_LEFT;
        }
        Revolver.positionRevolver(direction);


        //---driving---\\

        double driveSpeed = gamepad1.left_stick_y;
        double turnSpeed = gamepad1.right_stick_x;
        double sideSpeed = gamepad1.left_trigger - gamepad1.right_trigger;
        driveTrain.calculatePower(driveSpeed, turnSpeed, sideSpeed);
    }
}
