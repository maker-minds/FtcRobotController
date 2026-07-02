package newteamcode.decode;

import androidx.annotation.NonNull;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Revolver {

    //---Objects---\\

    public DcMotor revolverMotor = null;
    private final MagnetSwitch magnetSwitch = new MagnetSwitch();


    //---Constants---\\

    public final byte LEFT = -1, RIGHT = 1, MANUAL_LEFT = -2, MANUAL_RIGHT = 2, STOP = 4, AUTO_ALIGN = 3;
    private final byte INTAKE = 0, OUTTAKE = 1;
    private final int[][] ENCODER_POSITIONS = {{373, 747, 1019}, {236, 510, 883}};


    //---Variables---\\

    private byte currentState = INTAKE;
    private byte lastState = OUTTAKE;
    private byte indexSetpoint; //index sollwert: der index auf dem wir sein sollten
    private int targetPosition;
    private boolean emergency = false, emergencyCopy = emergency;


    //---Functions---\\

    public void init(@NonNull HardwareMap hardwareMap) {
        revolverMotor = hardwareMap.get(DcMotor.class, "revolverMotor");
        revolverMotor.setDirection(DcMotor.Direction.REVERSE);
        revolverMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        revolverMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        revolverMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        magnetSwitch.init(hardwareMap);
    }

    public void positionRevolver(byte direction) {
        emergency = Math.abs(direction) > 1;
        if(emergency != emergencyCopy) {
            revolverMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            revolverMotor.setTargetPosition(revolverMotor.getCurrentPosition());
            if(!emergency)
                indexSetpoint = calculateIndex();
        }
        emergencyCopy = emergency;
        if (revolverMotor.isBusy()) {
            return;
        }
        revolverMotor.setPower(0);
        //index = calculateIndex();
        calculateTargetPosition(direction);
        revolverMotor.setTargetPosition(targetPosition);
        revolverMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        revolverMotor.setPower(1);
    }

    private void calculateTargetPosition(byte direction) {
        if (direction == 0 && lastState == currentState)
            return;
        else if (Math.abs(direction) > 1) {
            if (Math.abs(direction) == 2)
                targetPosition = revolverMotor.getCurrentPosition() + 5 * direction;
            else if (Math.abs(direction) == 3)
                realign(direction);
            return;
        }
        int positionError = revolverMotor.getCurrentPosition() % ENCODER_POSITIONS[currentState][indexSetpoint];
        if (positionError > 186)
            positionError -= ENCODER_POSITIONS[currentState][indexSetpoint];
        targetPosition = revolverMotor.getCurrentPosition() + 373 * direction - positionError;
        indexSetpoint += direction;
        //targetPosition = ENCODER_POSITIONS[currentState][(index + direction) % 3];
        lastState = currentState;
    }

    private byte calculateIndex() {
        if (currentState == INTAKE) {
            for (int i = 2; i >= 0; i--) {
                int mod = revolverMotor.getCurrentPosition() % ENCODER_POSITIONS[INTAKE][i];
                if (mod > (ENCODER_POSITIONS[INTAKE][i] - 30) || mod < 30)
                    return (byte) i;
            }
        } else if (currentState == OUTTAKE) {
            for (int i = 2; i >= 0; i--) {
                int mod = revolverMotor.getCurrentPosition() % ENCODER_POSITIONS[OUTTAKE][i];
                if (mod > (ENCODER_POSITIONS[OUTTAKE][i] - 30) || mod < 30)
                    return (byte) i;
            }
        }
        return 9;
    }

    private void realign(byte direction) {
        revolverMotor.setPower(Math.min(Math.abs(direction), 0.6));
        while (!magnetSwitch.isNearMagnet()) {
        }
        revolverMotor.setPower(0);
    }
}
