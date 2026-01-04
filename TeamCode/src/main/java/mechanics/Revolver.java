package mechanics;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Revolver {

    final private int[] inputpos = {373, 746, 1120}; //1, 2 ,3
    final private int[] outputpos = {168, 560, 933}; // 2, 3, 1
    int index = 0;
    String[] load = {"none", "none", "none"};
    boolean mode = true; // true = input ; false = output

    public DcMotor revolver;

    public Revolver() {

    }

    public void init(HardwareMap hardwareMap) {

        revolver = hardwareMap.get(DcMotor.class, "revolver");
        revolver.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        revolver.setDirection(DcMotor.Direction.FORWARD);
        revolver.setTargetPosition(0);

        revolver.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public int searchEmptyPosition() {
        if(!load[0].equals("none") && !load[1].equals("none") && !load[2].equals("none")) {
            return 9;
        }
        for(int i = 0; i < 3; i++) {

            if(load[index].equals("none")) {
                return i;
 //               revolver.setTargetPosition(inputpos[index]);
 //              runtoPosition();
            } else {
                index++;
                if (index == 3) {
                    index = 0;
                }
            }
        }
        return 9;
    }

    public int getindexof(int item, int[] array) {

        for(int i = array.length - 1; i >= 0; i--){

            if(item % array[i] == array[i]){
                return i;
            }
        }
        return 9;
    }

    public void nextPosition() {
        index ++;
        if(index >= 3) {
            index = 0;
        }
    }

    public void previusPosition() {
        index --;
        if(index <= -1) {
            index = 2;
        }
    }

    public void gotoPosition() {
        if(mode) {
            revolver.setTargetPosition(inputpos[index]);
            runtoPosition();
        }else {
            revolver.setTargetPosition(outputpos[index]);
            runtoPosition();
        }
    }

    public void togglemode() {

        mode = !mode;
        if(mode) {
           nextPosition();
        } else {
            previusPosition();
        }
    }

    public void runtoPosition() {

        revolver.setPower(0.5);
        revolver.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void checktarget() {
        if(Math.abs(revolver.getTargetPosition() - revolver.getCurrentPosition()) < 20) {
           revolver.setPower(0.1);
        }
        if(!revolver.isBusy()) {
            revolver.setPower(0);
        }
    }

}
