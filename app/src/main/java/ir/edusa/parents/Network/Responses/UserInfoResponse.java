package ir.edusa.parents.Network.Responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import ir.edusa.parents.Models.Passenger;

/**
 * Created by pouya on 8/26/15.
 */
public class UserInfoResponse extends AbstractResponse implements Serializable {
    //   JSONObject responseParams;
    @SerializedName("Result_Data")
    private Data data;


    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public class Data extends Passenger {
    }
}
