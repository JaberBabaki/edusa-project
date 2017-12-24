package ir.edusa.parents.Network.Requests;


import ir.edusa.parents.Network.ApiConstants;

import java.util.HashMap;

public class SetAddressLocationRequest extends AbstractRequest {

    private String Passenger_Code;
    private String Device_Code;
    private String Driver_Code;
    private double Lat;
    private double Lon;

    public SetAddressLocationRequest(String password, String driver_Code, String passenger_Code, String device_Code, double lat, double lon) {
        super(password);
        Passenger_Code = passenger_Code;
        Device_Code = device_Code;
        Driver_Code = driver_Code;
        Lat = lat;
        Lon = lon;
    }

    @Override
    public int getMethod() {
        return ApiConstants.METHOD_GET;
    }

    @Override
    public String getApiUrl() {
        return ApiConstants.API_SET_ADDRESS_LOCATION;
    }

    @Override
    public HashMap<String, String> getParameters() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Password", getPassword() + "");
        params.put("Device_Code", Device_Code + "");
        params.put("Passenger_Code", Passenger_Code + "");
        params.put("Lat", Lat + "");
        params.put("Lon", Lon + "");
        params.put("Driver_Codes", Driver_Code + "");
        return params;
    }

}
