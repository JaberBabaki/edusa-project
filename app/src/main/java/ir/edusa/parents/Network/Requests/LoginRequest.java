package ir.edusa.parents.Network.Requests;


import java.util.HashMap;

import ir.edusa.parents.Network.ApiConstants;

public class LoginRequest extends AbstractRequest {


    private String Device_Code = "-1";

    public LoginRequest(String password, String device_Code) {
        super(password);
        Device_Code = device_Code;
    }

    @Override
    public int getMethod() {
        return ApiConstants.METHOD_POST;
    }

    @Override
    public String getApiUrl() {
        return ApiConstants.API_LOGIN;
    }

    @Override
    public HashMap<String, String> getParameters() {
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("Password", getPassword() + "");
        params.put("Device_Code", Device_Code + "");

        return params;
    }

}
