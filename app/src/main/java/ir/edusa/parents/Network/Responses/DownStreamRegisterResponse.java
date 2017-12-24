package ir.edusa.parents.Network.Responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by pouya on 8/26/15.
 */
public class DownStreamRegisterResponse extends AbstractResponse implements Serializable {
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
        private String SMS_Center_No;
        private int Interval_2_Call_Back;
        private String Initial_Key;
        private int Private_Auth_Key;

        public String getDevice_Code() {
            return Device_Code;
        }

        public void setDevice_Code(String device_Code) {
            Device_Code = device_Code;
        }

        public String getSMS_Center_No() {
            return SMS_Center_No;
        }

        public void setSMS_Center_No(String SMS_Center_No) {
            this.SMS_Center_No = SMS_Center_No;
        }

        public int getInterval_2_Call_Back() {
            return Interval_2_Call_Back*1000;
        }

        public void setInterval_2_Call_Back(int interval_2_Call_Back) {
            Interval_2_Call_Back = interval_2_Call_Back;
        }

        public String getInitial_Key() {
            return Initial_Key;
        }

        public void setInitial_Key(String initial_Key) {
            Initial_Key = initial_Key;
        }

        public int getPrivate_Auth_Key() {
            return Private_Auth_Key;
        }

        public void setPrivate_Auth_Key(int private_Auth_Key) {
            Private_Auth_Key = private_Auth_Key;
        }
    }
}
