package ir.edusa.parents.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class TrackingProgram implements Serializable {
    private String Code;
    private String Descript;
    private ArrayList<TrackingTimeTable> Tracking_TimeTable_Items = new ArrayList<>();

    public TrackingProgram(String code, String descript, ArrayList<TrackingTimeTable> tracking_TimeTable_Items) {
        Code = code;
        Descript = descript;
        Tracking_TimeTable_Items = tracking_TimeTable_Items;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getDescript() {
        return Descript;
    }

    public void setDescript(String descript) {
        Descript = descript;
    }

    public ArrayList<TrackingTimeTable> getTracking_TimeTable_Items() {
        return Tracking_TimeTable_Items;
    }

    public void setTracking_TimeTable_Items(ArrayList<TrackingTimeTable> tracking_TimeTable_Items) {
        Tracking_TimeTable_Items = tracking_TimeTable_Items;
    }
}