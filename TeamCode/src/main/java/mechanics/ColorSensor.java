package mechanics;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ColorSensor {

    NormalizedColorSensor colorSensor;

    public enum detectedColors {
        GREEN,
        PURPLE,
        UNKNOWN
    }
    public ColorSensor() {

    }

    public void init(HardwareMap hardwareMap) {

        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "colorsensor");
    }

    public detectedColors getColor(Telemetry telemetry) {

        NormalizedRGBA colors = colorSensor.getNormalizedColors();

        float red, green, blue;
        red = colors.red / colors.alpha;
        green = colors.green / colors.alpha;
        blue = colors.blue / colors.alpha;

        telemetry.addData("red", red);
        telemetry.addData("green", green);
        telemetry.addData("blue", blue);

        // TODO find values and write if statements

        return detectedColors.UNKNOWN;
    }
}
