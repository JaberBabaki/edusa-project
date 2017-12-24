package ir.edusa.parents.Network.Requests;


import ir.edusa.parents.Network.ApiConstants;

import java.util.HashMap;

public class StopLiveTrackingRequest extends AbstractRequest {

    private String Driver_Code;
    private String Device_Code;


    public StopLiveTrackingRequest(String device_Code, String driver_Code, String password) {
        super(password);
        Driver_Code = driver_Code;
        Device_Code = device_Code;
    }

    @Override
    public int getMethod() {
        return ApiConstants.METHOD_GET;
    }

    @Override
    public String getApiUrl() {
        return ApiConstants.API_STOP_LIVE_TRACKING;
    }

    @Override
    public HashMap<String, String> getParameters() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Driver_Code", Driver_Code + "");
        params.put("Device_Code", Device_Code + "");
        params.put("Password", getPassword() + "");
        return params;
    }

}
