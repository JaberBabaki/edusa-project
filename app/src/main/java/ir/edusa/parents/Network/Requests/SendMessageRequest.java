package ir.edusa.parents.Network.Requests;


import ir.edusa.parents.Network.ApiConstants;

import java.util.HashMap;

public class SendMessageRequest extends AbstractRequest {

    public static final String MESSAGE_CODE_DEVICE_LOCATION_SERVICE_IS_OFF = "1";
    public static final String MESSAGE_CODE_MESSAGE_2_ADMIN_PANEL = "0";

    private String Device_Code;
    private String Device_Owner_Code;
    private String Date;
    private String Time;
    private String Message_Code;
    private String Param1;
    private String Param2;
    private String Relating_Driver_Code;
    private String Organization_Code;
    private String Device_Type = "1";

    public SendMessageRequest(String password, String device_Code, String device_Owner_Code,
                              String date, String time, String message_Code, String param1, String param2, String relating_Driver_Code, String organization_Code) {
        super(password);
        Device_Code = device_Code;
        Device_Owner_Code = device_Owner_Code;
        Date = date;
        Time = time;
        Message_Code = message_Code;
        Param1 = param1;
        Param2 = param2;
        Relating_Driver_Code = relating_Driver_Code;
        Organization_Code = organization_Code;
    }

    @Override
    public int getMethod() {
        return ApiConstants.METHOD_GET;
    }

    @Override
    public String getApiUrl() {
        return ApiConstants.API_SEND_MESSAGE_POINT;
    }

    @Override
    public HashMap<String, String> getParameters() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Password", getPassword() + "");
        params.put("Device_Code", Device_Code + "");
        params.put("Device_Owner_Code", Device_Owner_Code + "");
        params.put("Date", Date + "");
        params.put("Time", Time + "");
        params.put("Message_Code", Message_Code + "");
        params.put("Device_Type", Device_Type + "");
        params.put("Param1", Param1 + "");
        params.put("Param2", Param2 + "");
        params.put("Relating_Driver_Code", Relating_Driver_Code + "");
        params.put("Organization_Code", Organization_Code + "");
        return params;
    }

}
