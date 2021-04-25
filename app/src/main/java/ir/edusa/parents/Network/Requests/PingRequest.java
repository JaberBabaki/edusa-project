package ir.edusa.parents.Network.Requests;


import ir.edusa.parents.Network.ApiConstants;

import java.util.HashMap;

public class PingRequest extends AbstractRequest {

    private boolean Reset_Private_Auth_Key;
    private boolean Show_Device_Info;
    private String Simcard_No;
    private String Device_Code;
    private String Device_Type = "1";

    public PingRequest(String password, boolean reset_Private_Auth_Key, boolean show_Device_Info,
                       String simcard_No, String device_Code) {
        super(password);
        Reset_Private_Auth_Key = reset_Private_Auth_Key;
        Show_Device_Info = show_Device_Info;
        Simcard_No = simcard_No;
        Device_Code = device_Code;
    }

    @Override
    public int getMethod() {
        return ApiConstants.METHOD_POST;
    }

    @Override
    public String getApiUrl() {
        return ApiConstants.API_PING;
    }

    @Override
    public HashMap<String, String> getParameters() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Device_Code", Device_Code + "");
        params.put("Reset_Private_Auth_Key", Reset_Private_Auth_Key + "");
        params.put("Show_Device_Info", Show_Device_Info + "");
        params.put("Simcard_No", Simcard_No + "");
        params.put("Device_Type", Device_Type);
        params.put("Password", getPassword() + "");
        return params;
    }

}
