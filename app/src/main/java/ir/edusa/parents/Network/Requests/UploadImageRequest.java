package ir.edusa.parents.Network.Requests;


import java.util.HashMap;

import ir.edusa.parents.Network.ApiConstants;

public class UploadImageRequest extends AbstractRequest {

    private String device_Code;
    private String device_Type;
    private String device_Owner_Code;


    public UploadImageRequest(String password, String device_Code, String device_Type, String device_Owner_Code) {
        super(password);
        this.device_Code = device_Code;
        this.device_Type = device_Type;
        this.device_Owner_Code = device_Owner_Code;
    }

    @Override
    public int getMethod() {
        return ApiConstants.METHOD_POST;
    }

    @Override
    public String getApiUrl() {
        return ApiConstants.API_UPLOAD_IMAGE;
    }

    @Override
    public HashMap<String, String> getParameters() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Password", getPassword() + "");
        params.put("Device_Code", device_Code + "");
        params.put("device_Type", device_Type + "");
        params.put("device_Owner_Code", device_Owner_Code + "");
        return params;
    }

}
