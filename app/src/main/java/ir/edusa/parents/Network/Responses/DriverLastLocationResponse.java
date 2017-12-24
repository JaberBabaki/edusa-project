package ir.edusa.parents.Network.Responses;

import com.google.gson.annotations.SerializedName;
import ir.edusa.parents.Models.TrackingPoint;

import java.io.Serializable;

/**
 * Created by pouya on 8/26/15.
 */
public class DriverLastLocationResponse extends AbstractResponse implements Serializable {
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
        private TrackingPoint Point;

        public TrackingPoint getPoint() {
            return Point;
        }

        public void setPoint(TrackingPoint point) {
            Point = point;
        }
    }
}
