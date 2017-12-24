package ir.edusa.parents.Models;

import android.net.Uri;

import java.io.Serializable;

import ir.edusa.parents.Helpers.ValidatorHelper;

public class SchoolService implements Serializable {
    private TrackingProgram Tracking_Program;
    private String Service_Code;
    private String Driver_Code = "";
    private String Driver_Name = "";
    private String Driver_Family = "";
    private String Driver_Mobile_Number = "";
    private String Driver_Image_Url = "";

    @Override
    public boolean equals(Object obj) {
        return getService_Code().equals(((SchoolService) obj).getService_Code());
    }

    @Override
    public int hashCode() {
        return getService_Code().hashCode();
    }

    public String getDriverFullName() {
        return getDriver_Name() + " " + getDriver_Family();
    }

    public String getDriver_Code() {
        return Driver_Code;
    }

    public void setDriver_Code(String driver_Code) {
        Driver_Code = driver_Code;
    }

    public String getDriver_Name() {
        return Driver_Name;
    }

    public void setDriver_Name(String driver_Name) {
        Driver_Name = driver_Name;
    }

    public String getDriver_Family() {
        return Driver_Family;
    }

    public void setDriver_Family(String driver_Family) {
        Driver_Family = driver_Family;
    }

    public String getDriver_Mobile_Number() {
        return Driver_Mobile_Number;
    }

    public void setDriver_Mobile_Number(String driver_Mobile_Number) {
        Driver_Mobile_Number = driver_Mobile_Number;
    }

    public String getDriver_Image_Url() {
        if (ValidatorHelper.isValidString(Driver_Image_Url)) {
            return Uri.encode(Driver_Image_Url, "@#&=*+-_.,:!?()/~'%");
        }
        return "";
    }

    public void setDriver_Image_Url(String driver_Image_Url) {
        Driver_Image_Url = driver_Image_Url;
    }

    public TrackingProgram getTracking_Program() {
        return Tracking_Program;
    }

    public void setTracking_Program(TrackingProgram tracking_Program) {
        Tracking_Program = tracking_Program;
    }

    public String getService_Code() {
        return Service_Code;
    }

    public void setService_Code(String service_Code) {
        Service_Code = service_Code;
    }
}