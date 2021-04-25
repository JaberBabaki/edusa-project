package ir.edusa.parents.Models;

import java.io.Serializable;

public class AlarmLocationPoint implements Serializable {
    private String Point_Code;
    private String Point_Descript;
    private double Lat;
    private double Lon;

    public AlarmLocationPoint(String point_Code, String point_Descript, double lat, double lon) {
        Point_Code = point_Code;
        Point_Descript = point_Descript;
        Lat = lat;
        Lon = lon;
    }

    public String getPoint_Code() {
        return Point_Code;
    }

    public void setPoint_Code(String point_Code) {
        Point_Code = point_Code;
    }

    public String getPoint_Descript() {
        return Point_Descript;
    }

    public void setPoint_Descript(String point_Descript) {
        Point_Descript = point_Descript;
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

    @Override
    public int hashCode() {
        return getPoint_Code().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return getPoint_Code().equals(((AlarmLocationPoint) obj).getPoint_Code());
    }
}