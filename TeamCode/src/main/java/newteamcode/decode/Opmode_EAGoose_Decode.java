package newteamcode.decode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "OpMode_EAGoose_DECODE", group = "teleop")
public class OpMode_EAGoose_Decode extends OpMode {

    //---Objects---\\

    private final DriveTrain driveTrain = new DriveTrain();
    private final Revolver revolver = new Revolver();
    private DcMotor intake = null;
    private final  DcMotorEx outtake = null;


    //---Functions---\\

    @Override
    public void init() {
        driveTrain.init(hardwareMap);
        driveTrain.setMode(DriveTrain.modes.RUN_WITHOUT_ENCODER);

        revolver.init(hardwareMap);

        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setDirection(DcMotor.Direction.REVERSE);

    }

    @Override
    public void loop() {

        //---tasks---\\

        intake.setPower(1);

        byte direction = getDirection();
        revolver.positionRevolver(direction);


        //---driving---\\

        double driveSpeed = gamepad1.left_stick_y;
        double turnSpeed = gamepad1.right_stick_x;
        double sideSpeed = gamepad1.left_trigger - gamepad1.right_trigger;
        driveTrain.calculatePower(driveSpeed, turnSpeed, sideSpeed);
    }

    private byte getDirection() {
        byte direction = 0;
        if(gamepad2.right_bumper || gamepad2.right_stick_x > 0) {
            direction = revolver.RIGHT;
        } else if (gamepad2.left_bumper || gamepad2.right_stick_x < 0) {
            direction = revolver.LEFT;
        } else if(gamepad2.b) {
            direction = revolver.STOP;
        } else if (gamepad2.a) {
            direction = revolver.AUTO_ALIGN;
        } else if(gamepad2.left_stick_x > 0) {
            direction = revolver.MANUAL_RIGHT;
        } else if(gamepad2.left_stick_x < 0) {
            direction = revolver.MANUAL_LEFT;
        }
        return direction;
    }
}
