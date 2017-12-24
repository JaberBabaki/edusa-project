package ir.edusa.parents.Models;

import java.io.Serializable;

public class Organization implements Serializable {
    private String Address = "نام مجموعه";
    private String Code = "";
    private String Descript = "";
    private int Radius_From_Center;
    private double Lat;
    private double Lon;

    public Organization(String address, String code, String descript, int radius_From_Center, double lat, double lon) {
        Address = address;
        Code = code;
        Descript = descript;
        Radius_From_Center = radius_From_Center;
        Lat = lat;
        Lon = lon;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
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

    public int getRadius_From_Center() {
        return Radius_From_Center;
    }

    public void setRadius_From_Center(int radius_From_Center) {
        Radius_From_Center = radius_From_Center;
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
    public boolean equals(Object obj) {
        return obj.equals(getCode());
    }

    @Override
    public int hashCode() {
        return getCode().hashCode();
    }
}