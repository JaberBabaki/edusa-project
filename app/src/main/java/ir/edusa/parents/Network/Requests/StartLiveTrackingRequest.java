package ir.edusa.parents.Network.Requests;


import ir.edusa.parents.Network.ApiConstants;

import java.util.HashMap;

public class StartLiveTrackingRequest extends AbstractRequest {

    private int Duration_Of_Online_Tracking_Per_Minute;
    private String Driver_Code;
    private String Device_Code;


    public StartLiveTrackingRequest( String device_Code,String driver_Code,int duration_Of_Online_Tracking_Per_Minute, String password) {
        super(password);
        Duration_Of_Online_Tracking_Per_Minute = duration_Of_Online_Tracking_Per_Minute;
        Driver_Code = driver_Code;
        Device_Code = device_Code;
    }

    @Override
    public int getMethod() {
        return ApiConstants.METHOD_GET;
    }

    @Override
    public String getApiUrl() {
        return ApiConstants.API_START_LIVE_TRACKING;
    }

    @Override
    public HashMap<String, String> getParameters() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Duration_Of_Online_Tracking_Per_Minute", Duration_Of_Online_Tracking_Per_Minute + "");
        params.put("Driver_Code", Driver_Code + "");
        params.put("Device_Code", Device_Code + "");
        params.put("Password", getPassword() + "");
        return params;
    }

}
