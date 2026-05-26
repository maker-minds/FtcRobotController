package newteamcode.decode;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MagnetSwitch {

    //---Objects---\\

    public DigitalChannel channel;

    //---Functions---\\

    public void init(HardwareMap hardwareMap) {
        channel = hardwareMap.get(DigitalChannel.class, "MagnetSwitch");
        channel.setMode(DigitalChannel.Mode.INPUT);
    }

    public boolean isNearMagnet() {
        return !channel.getState();
    }
}
