package ir.edusa.parents.Network.Requests;


import ir.edusa.parents.Network.ApiConstants;

import java.util.HashMap;

public class GetDriverRouteRequest extends AbstractRequest {

    private String Driver_Code;
    private String Device_Code;
    private String Date;
    private String Time_From;
    private String Time_To;


    public GetDriverRouteRequest(String device_Code, String driver_Code, String date, String time_From, String time_To, String password) {
        super(password);
        Date = date;
        Driver_Code = driver_Code;
        Device_Code = device_Code;
        Time_From = time_From;
        Time_To = time_To;
    }

    @Override
    public int getMethod() {
        return ApiConstants.METHOD_GET;
    }

    @Override
    public String getApiUrl() {
        return ApiConstants.API_GET_ROUTE;
    }

    @Override
    public HashMap<String, String> getParameters() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Date", Date + "");
        params.put("Driver_Code", Driver_Code + "");
        params.put("Device_Code", Device_Code + "");
        params.put("Time_To", Time_To + "");
        params.put("Time_From", Time_From + "");
        params.put("Password", getPassword() + "");
        return params;
    }

}
