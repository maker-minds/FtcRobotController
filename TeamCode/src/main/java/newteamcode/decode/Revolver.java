package newteamcode.decode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Revolver {

    //---Objects---\\

    public DcMotor revolverMotor = null;

    //---Variables---\\

    private int[] intakePositions;
    private int[] outtakePositions;

    private boolean outtakeMode = false;

    public void init(HardwareMap hardwareMap) {
        revolverMotor = hardwareMap.get(DcMotor.class, "revolvermotor");
        revolverMotor.setDirection(DcMotor.Direction.REVERSE);
        revolverMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        revolverMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        revolverMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void function() {
        if(!outtakeMode) {

        }
    }
}
