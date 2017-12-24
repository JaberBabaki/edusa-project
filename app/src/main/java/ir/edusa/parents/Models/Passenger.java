package ir.edusa.parents.Models;

import android.net.Uri;

import com.google.firebase.messaging.FirebaseMessaging;

import ir.edusa.parents.Helpers.ValidatorHelper;
import ir.edusa.parents.Managers.UserManager;

import java.io.Serializable;
import java.util.ArrayList;

public class Passenger implements Serializable {
    private String Code = "";
    private String Class = "";
    private String Name = "";
    private String Family = "";
    private String Address = "";
    private double Address_Lat;
    private double Address_Lon;
    private String Image_Url = "";

    private Organization Organization;
    private ArrayList<SchoolService> Services;
    private ArrayList<AlarmLocationPoint> Alarm_Location_Points = new ArrayList<>();

    public Passenger() {
    }

    public Passenger(String code, String name, String family, String address, double address_Lat,
                     double address_Lon, Organization organization, ArrayList<AlarmLocationPoint> alarm_Location_Points) {
        Code = code;
        Name = name;
        Family = family;
        Address = address;
        Address_Lat = address_Lat;
        Address_Lon = address_Lon;
        this.Organization = organization;
        Alarm_Location_Points = alarm_Location_Points;
    }

    public void refreshTopics() {
        unsubscribeFromTopics();
        subscribeToTopics();
    }

    public void unsubscribeFromTopics() {
        ArrayList<String> subscribedTopics = UserManager.getSubscribedTopics();
        for (String s : subscribedTopics) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(s);
        }
        UserManager.setSubscribedTopics(new ArrayList<String>());
    }

    public void subscribeToTopics() {
        ArrayList<String> subscribedTopics = new ArrayList<>();
        subscribedTopics.add("PASSENGER_CITY_TEHRAN");
        subscribedTopics.add("PASSENGER_CITY_TEHRAN_ORGANIZATION_" + getOrganization().getCode());
        subscribedTopics.add("PASSENGER_CITY_TEHRAN_ORGANIZATION_" + getOrganization().getCode() + "_CLASS_" + getClassCode());
        for (String st : subscribedTopics) {
            FirebaseMessaging.getInstance().subscribeToTopic(st);
        }
        UserManager.setSubscribedTopics(subscribedTopics);
    }

    public String getClassCode() {
        return Class;
    }

    public void setClassCode(String aClass) {
        Class = aClass;
    }

    public ArrayList<SchoolService> getServices() {
        return Services;
    }

    public String getImage_Url() {
        if (ValidatorHelper.isValidString(Image_Url)) {
            return Uri.encode(Image_Url, "@#&=*+-_.,:!?()/~'%");
        }
        return "";
    }

    public void setImage_Url(String image_Url) {
        Image_Url = image_Url;
    }

    public void setServices(ArrayList<SchoolService> services) {
        Services = services;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFamily() {
        return Family;
    }

    public void setFamily(String family) {
        Family = family;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public double getAddress_Lat() {
        return Address_Lat;
    }

    public void setAddress_Lat(double address_Lat) {
        Address_Lat = address_Lat;
    }

    public double getAddress_Lon() {
        return Address_Lon;
    }

    public void setAddress_Lon(double address_Lon) {
        Address_Lon = address_Lon;
    }

    public Organization getOrganization() {
        return Organization;
    }

    public void setOrganization(Organization organization) {
        this.Organization = organization;
    }

    public ArrayList<AlarmLocationPoint> getAlarm_Location_Points() {
        return Alarm_Location_Points;
    }

    public void setAlarm_Location_Points(ArrayList<AlarmLocationPoint> alarm_Location_Points) {
        Alarm_Location_Points = alarm_Location_Points;
    }
}