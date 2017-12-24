package ir.edusa.parents.Network.Requests;


import java.util.HashMap;

import ir.edusa.parents.Network.ApiConstants;


public class GetCommandsInQueueRequest extends AbstractRequest {

    private String Device_Code;


    public GetCommandsInQueueRequest(String password, String device_Code) {
        super(password);
        Device_Code = device_Code;
    }

    @Override
    public int getMethod() {
        return ApiConstants.METHOD_GET;
    }

    @Override
    public String getApiUrl() {
        return ApiConstants.API_COMMAND_QUEUE;
    }

    @Override
    public HashMap<String, String> getParameters() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Device_Code", Device_Code + "");
        params.put("Password", getPassword() + "");
        return params;
    }

}
