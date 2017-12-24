package ir.edusa.parents.Network.Requests;


import ir.edusa.parents.Network.ApiConstants;

import java.util.HashMap;

public class PassengerAbsenceRequest extends AbstractRequest {
    public static final int DIRECTION_PICKING = 0;
    public static final int DIRECTION_DROPPING = 1;
    public static final int DIRECTION_BOTH = -1;

    private String Device_Code;
    private String Passenger_Code;
    private String Service_Code;
    private String Driver_Code;
    private String Date;
    private int Direction;

    public PassengerAbsenceRequest(String password, String device_Code, String passenger_Code, String service_Code, String driver_Code, String date, int direction) {
        super(password);
        Device_Code = device_Code;
        Passenger_Code = passenger_Code;
        Service_Code = service_Code;
        Date = date;
        Direction = direction;
        Driver_Code = driver_Code;
    }

    @Override
    public int getMethod() {
        return ApiConstants.METHOD_GET;
    }

    @Override
    public String getApiUrl() {
        return ApiConstants.API_SEND_ABSENCE;
    }

    @Override
    public HashMap<String, String> getParameters() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Password", getPassword() + "");
        params.put("Device_Code", Device_Code + "");
        params.put("Passenger_Code", Passenger_Code + "");
        params.put("Service_Code", Service_Code + "");
        params.put("Date", Date + "");
        params.put("Direction", Direction + "");
        params.put("Driver_Code", Driver_Code + "");


        return params;
    }

}
