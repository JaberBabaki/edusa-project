package ir.edusa.parents.Network.Requests;


import java.util.HashMap;

import ir.edusa.parents.Network.ApiConstants;

public class RegisterDeviceRequest extends AbstractRequest {

    private String Private_Auth_Key;
    private String Device_Code;
    private String Device_Model_No = "";
    private String Android_Version = "";
    private String Application_Version = "";
    private String Device_Type = "1";


    public RegisterDeviceRequest(String password, String private_Auth_Key, String device_Code,
                                 String device_Model_No, String android_Version, String application_Version) {
        super(password);
        Private_Auth_Key = private_Auth_Key;
        Device_Code = device_Code;
        Device_Model_No = device_Model_No;
        Android_Version = android_Version;
        Application_Version = application_Version;
    }

    @Override
    public int getMethod() {
        return ApiConstants.METHOD_POST;
    }

    @Override
    public String getApiUrl() {
        return ApiConstants.API_REGISTER_DEVICE;
    }

    @Override
    public HashMap<String, String> getParameters() {
        HashMap<String, String> params = new HashMap<>();
        params.put("Device_Code", Device_Code + "");
        params.put("Private_Auth_Key", Private_Auth_Key + "");
        params.put("Device_Type", Device_Type);
        params.put("Device_Model_No", Device_Model_No);
        params.put("Android_Version", Android_Version);
        params.put("Application_Version", Application_Version);
        params.put("Password", getPassword() + "");
        return params;
    }

}
