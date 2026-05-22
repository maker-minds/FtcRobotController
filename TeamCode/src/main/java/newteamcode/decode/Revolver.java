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
    private final int[][] ENCODER_POSITIONS = {{373, 746, 1019}, {883, 236, 510}};


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
        if (inMovement)
            return;
        else if (direction == 0)
            return;
        else if (Math.abs(direction) > 1)
            return;
        else {
            int indexError = revolverMotor.getCurrentPosition() % ENCODER_POSITIONS[currentState][index];
            if (indexError > 186)
                indexError -= ENCODER_POSITIONS[currentState][index];
            targetPosition = revolverMotor.getCurrentPosition() + 373 * direction - indexError;
            //targetPosition = ENCODER_POSITIONS[currentState][(index + direction) % 3];
            inMovement = true;
        }
    }

    public int calculateIndex() {
        if (currentState == INTAKE) {
            for (int i = 2; i >= 0; i--) {
                int mod = revolverMotor.getCurrentPosition() % ENCODER_POSITIONS[INTAKE][i];
                if (mod > 343 || mod < 30)
                    return i;
            }
        } else if (currentState == OUTTAKE) {
            for (int i = 0; i >= 0; i = (i - 1) % 3) {
                int mod = revolverMotor.getCurrentPosition() % ENCODER_POSITIONS[INTAKE][i];
                if (mod > 343 || mod < 30)
                    return i;
            }
        }
        return 9;
    }


}
