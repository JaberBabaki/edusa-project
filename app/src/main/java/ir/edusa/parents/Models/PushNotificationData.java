package ir.edusa.parents.Models;

import java.io.Serializable;

public class PushNotificationData implements Serializable {
    public static final int COMMAND_CODE_LIVE_LOCATION = 200;
    public static final int COMMAND_CODE_REACH_POINT_ALARM = 201;
    public static final int COMMAND_CODE_MESSAGE_FROM_PANEL = 206;

    private String Command_Data;
    private int Command_Code;

    public PushNotificationData(String command_Data, int command_Code) {
        Command_Data = command_Data;
        Command_Code = command_Code;
    }

    public String getCommand_Data() {
        return Command_Data;
    }

    public void setCommand_Data(String command_Data) {
        Command_Data = command_Data;
    }

    public int getCommand_Code() {
        return Command_Code;
    }

    public void setCommand_Code(int command_Code) {
        Command_Code = command_Code;
    }
}
