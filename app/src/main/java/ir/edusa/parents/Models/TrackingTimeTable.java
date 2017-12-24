package ir.edusa.parents.Models;

import java.io.Serializable;

public class TrackingTimeTable implements Serializable {
    private int Day_Code;
    private String Start_Time = "";
    private String Stop_Time = "";

    public TrackingTimeTable(int day_Code, String start_Time, String stop_Time) {
        Day_Code = day_Code;
        Start_Time = start_Time;
        Stop_Time = stop_Time;
    }

    public int getDay_Code() {
        return Day_Code;
    }

    public void setDay_Code(int day_Code) {
        Day_Code = day_Code;
    }

    public String getStart_Time() {
        return Start_Time;
    }

    public void setStart_Time(String start_Time) {
        Start_Time = start_Time;
    }

    public String getStop_Time() {
        return Stop_Time;
    }

    public void setStop_Time(String stop_Time) {
        Stop_Time = stop_Time;
    }
}