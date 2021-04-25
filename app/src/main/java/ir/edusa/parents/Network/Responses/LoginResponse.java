package ir.edusa.parents.Network.Responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by pouya on 8/26/15.
 */
public class LoginResponse extends AbstractResponse implements Serializable {
    //   JSONObject responseParams;
    @SerializedName("Result_Data")
    private Data data;


    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public class Data {
        private String Auth_Key;
        private String Server_Latin_Date;
        private String Server_Time;
        private int App_Nonforce_Version;
        private int App_Force_Version;
        private int Device_Application_Version;
        private int Device_Type;
        private int Device_Code;

        public int getApp_Nonforce_Version() {
            return App_Nonforce_Version;
        }

        public void setApp_Nonforce_Version(int app_Nonforce_Version) {
            App_Nonforce_Version = app_Nonforce_Version;
        }

        public int getApp_Force_Version() {
            return App_Force_Version;
        }

        public void setApp_Force_Version(int app_Force_Version) {
            App_Force_Version = app_Force_Version;
        }

        public String getAuth_Key() {
            return Auth_Key;
        }

        public void setAuth_Key(String auth_Key) {
            Auth_Key = auth_Key;
        }

        public String getServer_Latin_Date() {
            return Server_Latin_Date;
        }

        public void setServer_Latin_Date(String server_Latin_Date) {
            Server_Latin_Date = server_Latin_Date;
        }

        public String getServer_Time() {
            return Server_Time;
        }

        public void setServer_Time(String server_Time) {
            Server_Time = server_Time;
        }

        public int getDevice_Application_Version() {
            return Device_Application_Version;
        }

        public void setDevice_Application_Version(int device_Application_Version) {
            Device_Application_Version = device_Application_Version;
        }

        public int getDevice_Type() {
            return Device_Type;
        }

        public void setDevice_Type(int device_Type) {
            Device_Type = device_Type;
        }

        public int getDevice_Code() {
            return Device_Code;
        }

        public void setDevice_Code(int device_Code) {
            Device_Code = device_Code;
        }
    }
}
