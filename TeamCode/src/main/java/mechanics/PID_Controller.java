package mechanics;

import com.qualcomm.robotcore.util.ElapsedTime;

public class PID_Controller {

    public final double Kp = 0.03;
    public final double Ki = 0;
    public final double Kd = 0.005;
    public double diference = 0;
    public double integralSum = 0;
    public double lastDiference = 0;

    ElapsedTime timer = new ElapsedTime();


    public PID_Controller() {

    }

    public double calculatePower(double state, double reference) {
        diference = angleWrap(reference - state);

        integralSum = integralSum + (diference * timer.seconds());

        double derivative = (diference - lastDiference) / timer.seconds();

        timer.reset();

        lastDiference = diference;
        double output = (Kp * diference) + (Ki * integralSum) + (Kd * derivative);

        return output;
    }

    public double angleWrap(double angle) {
        if (angle > 180) {
            angle -= 180;
        }
        if (angle < -180) {
            angle += 180;
        }
        return angle;
    }


}
