package ir.edusa.parents.Network.Requests;


import ir.edusa.parents.Network.ApiConstants;

import java.util.HashMap;

public class SetAlarmPointRequest extends AbstractRequest {

    private String Device_Code;
    private String Passenger_Code;
    private String Point_Code;
    private String Point_Descript;
    private String Driver_Code;
    private double Lat;
    private double Lon;

    public SetAlarmPointRequest(String password, String driver_Code, String device_Code, String passenger_Code, String point_Code, String point_Descript, double lat, double lon) {
        super(password);
        Device_Code = device_Code;
        Driver_Code = driver_Code;
        Passenger_Code = passenger_Code;
        Point_Code = point_Code;
        Point_Descript = point_Descript;
        Lat = lat;
        Lon = lon;
    }

    @Override
    public int getMethod() {
        return ApiConstants.METHOD_GET;
    }

    @Override
    public String getApiUrl() {
        return ApiConstants.API_SET_ALARM_POINT;
    }

    @Override
    public HashMap<String, String> getParameters() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Device_Code", Device_Code + "");
        params.put("Passenger_Code", Passenger_Code + "");
        params.put("Point_Code", Point_Code + "");
        params.put("Point_Descript", Point_Descript + "");
        params.put("Lat", Lat + "");
        params.put("Lon", Lon + "");
        params.put("Driver_Codes", Driver_Code + "");
        params.put("Password", getPassword() + "");
        return params;
    }

}
