package newteamcode.decode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import mechanics.MagnetSwitch;

public class Revolver {

    //---Objects---\\

    public DcMotor revolverMotor = null;
    public MagnetSwitch magnetSwitch;


    //---Constants---\\

    private final byte INTAKE = 0, OUTTAKE = 1;
    //private int[] INTAKE_POSITIONS;
    //private int[] OUTTAKE_POSITIONS;
    private final int[][] ENCODER_POSITIONS = {{1, 2, 3}, {4, 5, 6}};


    //---Variables---\\

    private byte currentState = 0;
    private byte index;
    private int targetPosition;
    private boolean inMovement;


    //---Functions---\\

    public void init(HardwareMap hardwareMap) {
        revolverMotor = hardwareMap.get(DcMotor.class, "revolverMotor");
        revolverMotor.setDirection(DcMotor.Direction.REVERSE);
        revolverMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        revolverMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        revolverMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        magnetSwitch.init(hardwareMap);
    }

    public void positionRevolver() {
        if (!revolverMotor.isBusy()) {
            revolverMotor.setPower(0);
            return;
        }
        revolverMotor.setTargetPosition(targetPosition);
        revolverMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        revolverMotor.setPower(1);
    }

    public void calculateTargetPosition(byte direction) {
        if(inMovement)
            return;
        else if (direction == 0)
            return;
        else if(Math.abs(direction) > 1)
            return;
        else {
            int newIndex = (index + direction) % 3;
            int indexError = revolverMotor.getCurrentPosition() % ENCODER_POSITIONS[currentState][index];
            if (indexError > 186)
                indexError -= ENCODER_POSITIONS[currentState][index];
            targetPosition = revolverMotor.getCurrentPosition() + 373 * direction - indexError;
            //targetPosition = ENCODER_POSITIONS[currentState][(index + direction) % 3];
            inMovement = true;
        }
    }

    public void calculateIndex() {

    }


}
