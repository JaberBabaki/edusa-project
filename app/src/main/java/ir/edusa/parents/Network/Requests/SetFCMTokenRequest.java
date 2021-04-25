package ir.edusa.parents.Network.Requests;


import ir.edusa.parents.Network.ApiConstants;

import java.util.HashMap;

public class SetFCMTokenRequest extends AbstractRequest {

    private String Device_Code;
    private String FCM_Token;

    public SetFCMTokenRequest(String device_Code, String fCM_Token, String password) {
        super(password);
        Device_Code = device_Code;
        FCM_Token = fCM_Token;
    }

    @Override
    public int getMethod() {
        return ApiConstants.METHOD_GET;
    }

    @Override
    public String getApiUrl() {
        return ApiConstants.API_SET_FCM_TOKEN;
    }

    @Override
    public HashMap<String, String> getParameters() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Device_Code", Device_Code + "");
        params.put("FCM_Token", FCM_Token + "");
        params.put("Password", getPassword() + "");
        return params;
    }

}
