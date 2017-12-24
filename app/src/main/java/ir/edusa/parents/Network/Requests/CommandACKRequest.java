package ir.edusa.parents.Network.Requests;

import java.util.HashMap;

import ir.edusa.parents.Network.ApiConstants;

public class CommandACKRequest extends AbstractRequest {

    private String Device_Code;
    private int Command_Unique_Code = -1;
    private int Command_Code = -1;
    private int Command_Reference_Code = -1;
    private boolean Ack_Only_This_Command = false;

    public CommandACKRequest(String password, String device_Code, int command_Unique_Code) {
        super(password);
        Device_Code = device_Code;
        Command_Unique_Code = command_Unique_Code;
    }

    public CommandACKRequest(String password, String device_Code, int command_Reference_Code, int command_Code, boolean ack_Only_This_Command) {
        super(password);
        Device_Code = device_Code;
        Command_Reference_Code = command_Reference_Code;
        Ack_Only_This_Command = ack_Only_This_Command;
        Command_Code = command_Code;
    }

    @Override
    public int getMethod() {
        return ApiConstants.METHOD_GET;
    }

    @Override
    public String getApiUrl() {
        return ApiConstants.API_COMMAND_ACK;
    }

    @Override
    public HashMap<String, String> getParameters() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Device_Code", Device_Code + "");
        if (Command_Unique_Code > 0) {
            params.put("Command_Unique_Code", Command_Unique_Code + "");
        }
        if (Command_Reference_Code > 0) {
            params.put("Command_Reference_Code", Command_Reference_Code + "");
        }
        if (Command_Code > 0) {
            params.put("Command_Code", Command_Code + "");
        }
        params.put("Ack_Only_This_Command", Ack_Only_This_Command + "");
        params.put("Password", getPassword() + "");
        return params;
    }

}
