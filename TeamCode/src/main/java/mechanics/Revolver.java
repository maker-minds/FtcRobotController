package mechanics;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Revolver {

    final private int[] inputpos = {373, 746, 0}; //1, 2 ,3
    final private int[] outputpos = {935, 185, 562}; //1, 2, 3
     public int index = 0, target = 0;

    String[] load = {"none", "none", "none"};
    public int mode = 0; // 0 = input ; 1 = output

    public DcMotor revolver;

    public Revolver() {

    }

    public void init(HardwareMap hardwareMap) {

        revolver = hardwareMap.get(DcMotor.class, "rucola");
        revolver.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        revolver.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        revolver.setDirection(DcMotor.Direction.FORWARD);
        revolver.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        index = getindexof(revolver.getCurrentPosition());
        target = 2;
    }

    public void updateRevolver() {
        index = getindexof(revolver.getCurrentPosition());
        if(mode == 0) {
//            target = searchEmptyPosition();
        }
    }

    public void moveRevolver () {
        if(target < 3 && target > -1) {
            gotoPosition();
        }
        checktarget();
    }

    public int getindexof(int item) {

        int[] array;
        if(mode == 0) {
            array = inputpos;
        } else {
            array = outputpos;
        }
        for(int i = array.length - 1; i >= 0; i--){

            if(item > array[i] % 1120 - 20 && item < array[i] % 1120 + 20){
                return i;
            }
        }
        return 9;
    }

    public int searchEmptyPosition() {
        if(!(load[0].equals("none") || load[1].equals("none") || load[2].equals("none"))) {
            return 9;
        }
        for(int i = 0; i < 3; i++) {

            if(load[i].equals("none")) {
                return i;
            } else {
                index++;
                if (index == 3) {
                    index = 0;
                }
            }
        }
        return 9;
    }

    public void gotoPosition() {
        if(revolver.isBusy()) return;
        if(target == index) return;

        if(mode == 0) {
            revolver.setTargetPosition(inputpos[target]);
            runtoPosition();
        }else {
            revolver.setTargetPosition(outputpos[target]);
            runtoPosition();
        }
/*      if(revolver.getCurrentPosition() - revolver.getTargetPosition() > 400) {
            revolver.setTargetPosition(revolver.getTargetPosition() + 1120);
        } else if(revolver.getCurrentPosition() - revolver.getTargetPosition() < -400) {
            revolver.setTargetPosition(revolver.getTargetPosition() - 1120);
        }*/
    }
    public void runtoPosition() {

        revolver.setPower(1);
        revolver.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void checktarget() {
        if(!revolver.isBusy()) {
            revolver.setPower(0);
        }
         else if(Math.abs(revolver.getTargetPosition() - revolver.getCurrentPosition()) < 25) {
            revolver.setPower(0.7);
        }
    }

    public void togglemode() {
        mode = 1 - mode;
    }

    public void nextPosition() {
        if(index > 2) {
            return;
        }
        target = index + 1;
        if(target >= 3) {
            target = 0;
        }
    }

    public void previusPosition() {
        if(index > 2) {
            return;
        }
        target = index - 1;
        if(target <= -1) {
            target = 2;
        }
    }

    public void moveRightSlowly() {
        revolver.setTargetPosition(5);
        revolver.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void moveLeftSlowly() {
        revolver.setTargetPosition(-5);
        revolver.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}
