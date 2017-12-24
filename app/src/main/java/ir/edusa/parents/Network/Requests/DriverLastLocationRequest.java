package ir.edusa.parents.Network.Requests;


import ir.edusa.parents.Network.ApiConstants;

import java.util.HashMap;

public class DriverLastLocationRequest extends AbstractRequest {

    private String Device_Code;
    private String Driver_Code;


    public DriverLastLocationRequest(String device_Code, String driver_Code, String password) {
        super(password);
        Device_Code = device_Code;
        Driver_Code = driver_Code;
    }

    @Override
    public int getMethod() {
        return ApiConstants.METHOD_GET;
    }

    @Override
    public String getApiUrl() {
        return ApiConstants.API_GET_DRIVER_LAST_LOCATION;
    }

    @Override
    public HashMap<String, String> getParameters() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Device_Code", Device_Code + "");
        params.put("Driver_Code", Driver_Code + "");
        params.put("Password", getPassword() + "");
        return params;
    }

}
