package ir.edusa.parents.Network.Responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by pouya on 8/26/15.
 */
public class PingResponse extends AbstractResponse implements Serializable {

    public static final int REGISTER_TYPE_SERVER_TO_CLIENT=0;
    public static final int REGISTER_TYPE_CLIENT_TO_SERVER=1;

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
        private String Device_Code;
        private String Device_Registered;
        private String Register_Date;
        private String Register_Time;
        private int App_Nonforce_Version;
        private int App_Force_Version;
        private int Register_Type;

        public int getRegister_Type() {
            return Register_Type;
        }

        public void setRegister_Type(int register_Type) {
            Register_Type = register_Type;
        }

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

        public String getDevice_Code() {
            return Device_Code;
        }

        public void setDevice_Code(String device_Code) {
            Device_Code = device_Code;
        }

        public String getDevice_Registered() {
            return Device_Registered;
        }

        public void setDevice_Registered(String device_Registered) {
            Device_Registered = device_Registered;
        }

        public String getRegister_Date() {
            return Register_Date;
        }

        public void setRegister_Date(String register_Date) {
            Register_Date = register_Date;
        }

        public String getRegister_Time() {
            return Register_Time;
        }

        public void setRegister_Time(String register_Time) {
            Register_Time = register_Time;
        }
    }
}
