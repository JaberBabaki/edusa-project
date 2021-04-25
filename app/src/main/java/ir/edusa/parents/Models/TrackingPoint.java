package ir.edusa.parents.Models;

import java.io.Serializable;

public class TrackingPoint implements Serializable {


    private String Point_Code;
    private String Date;
    private String Time;
    private double Lat;
    private double Lon;

    public TrackingPoint(String point_Code, String date, String time, double lat, double lon) {
        Point_Code = point_Code;
        Date = date;
        Time = time;
        Lat = lat;
        Lon = lon;
    }

    public String getPoint_Code() {
        return Point_Code;
    }

    public void setPoint_Code(String point_Code) {
        Point_Code = point_Code;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLon() {
        return Lon;
    }

    public void setLon(double lon) {
        Lon = lon;
    }
}
