package ir.edusa.parents.Network.Requests;


import ir.edusa.parents.Network.ApiConstants;

import java.util.HashMap;

public class DownStreamRegisterRequest extends AbstractRequest {
    public static final int MODE_INIT = 0;
    public static final int MODE_CHECK = 1;
    private String Device_Type = "1";
    private int Mode;
    private String Simcard_No;

    public DownStreamRegisterRequest(String password, int mode, String simcard_No) {
        super(password);
        Mode = mode;
        Simcard_No = simcard_No;
    }

    @Override
    public int getMethod() {
        return ApiConstants.METHOD_POST;
    }

    @Override
    public String getApiUrl() {
        return ApiConstants.API_DOWN_STREAM_REGISTER;
    }

    @Override
    public HashMap<String, String> getParameters() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Password", getPassword() + "");
        params.put("Mode", Mode + "");
        params.put("Device_Type", Device_Type);
        params.put("Simcard_No", Simcard_No + "");
        return params;
    }

}
