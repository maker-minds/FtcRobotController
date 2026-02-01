package mechanics;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DriveTrain {

    public DcMotor leftfront;
    public DcMotor rightfront;
    public DcMotor leftback;
    public DcMotor rightback;

    Telemetry telemetry;

    public DriveTrain(Telemetry telemetry) {

        this.telemetry = telemetry;
    }

    public void init(HardwareMap hardwareMap) {
        leftfront = hardwareMap.get(DcMotor.class, "left_front");
        leftback = hardwareMap.get(DcMotor.class, "left_back");
        rightfront = hardwareMap.get(DcMotor.class, "right_front");
        rightback = hardwareMap.get(DcMotor.class, "right_back");

        leftfront.setDirection(DcMotor.Direction.FORWARD);
        leftback.setDirection(DcMotor.Direction.REVERSE);
        rightfront.setDirection(DcMotor.Direction.REVERSE);
        rightback.setDirection(DcMotor.Direction.FORWARD);
    }

    public void setMode(String Mode){
        if (Mode.equals("RUN_WITHOUT_ENCODER")){

            leftfront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            leftback.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightfront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightback.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }else if(Mode.equals("RUN_USING_ENCODER")) {

            leftfront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftback.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightfront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightback.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            leftfront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftback.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightfront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightback.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }else if(Mode.equals("ZERO_POWER_BEHAVIOR_BRAKE")) {
            leftfront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            leftback.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightfront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightback.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }else{
            telemetry.addData("error: ",Mode);
        }
    }

    public void setPowerleft(double Power){
        leftfront.setPower(Power);
        leftback.setPower(Power);
    }

    public void setPowerright(double Power){
        rightfront.setPower(Power);
        rightback.setPower(Power);
    }
}
