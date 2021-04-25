package ir.edusa.parents.Network.Responses;

import com.google.gson.annotations.SerializedName;

import ir.edusa.parents.Models.TrackingPoint;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pouya on 8/26/15.
 */
public class DriverRouteResponse extends AbstractResponse implements Serializable {
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

        private ArrayList<TrackingPoint> Points = new ArrayList<>();

        public ArrayList<TrackingPoint> getPoints() {
            return Points;
        }

        public void setPoints(ArrayList<TrackingPoint> points) {
            Points = points;
        }
    }
}
