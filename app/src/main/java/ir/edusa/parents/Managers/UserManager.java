package ir.edusa.parents.Managers;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ir.edusa.parents.Helpers.ValidatorHelper;
import ir.edusa.parents.Models.AlarmLocationPoint;
import ir.edusa.parents.Models.Message;
import ir.edusa.parents.Models.Passenger;
import ir.edusa.parents.MyApplication;


/**
 * Created by pouya on 5/14/17.
 */

public class UserManager {

//    final static Type productType = new TypeToken<ArrayList<FProduct>>() {
//    }.getType();

    public static String getDeterminLocation() {
        String determinLocation= MyApplication.securePreferences.getString("setLocation");
        return determinLocation == null ? "0" : determinLocation;
    }

    public static void setDeterminLocation(String state) {
        MyApplication.securePreferences.put("setLocation", state);
    }
    public static String getWellcome() {
        String welcome= MyApplication.securePreferences.getString("setWellcome");
        return welcome == null ? "0" : welcome;
    }

    public static void setWellcome(String state) {
        MyApplication.securePreferences.put("setWellcome", state);
    }
    public static String getCellphone() {
        return MyApplication.securePreferences.getString("bGetCellphone");
    }

    public static void setCellphone(String cellphone) {
        MyApplication.securePreferences.put("bGetCellphone", cellphone);
    }


    public static String getToken() {
        return MyApplication.securePreferences.getString("bGetToken");
    }

    public static void setToken(String token) {
        MyApplication.securePreferences.put("bGetToken", token);
    }

    public static String getLastFCMToken() {
        return MyApplication.sharedPreferences.getString("bGetLastFCMToken", "");
    }

    public static void setLastFCMToken(String token) {
        MyApplication.editor.putString("bGetLastFCMToken", token);
        MyApplication.editor.apply();
    }

    public static String getPrivateApiKey() {
        return MyApplication.securePreferences.getString("bGetPrivateApiKey");
    }

    public static void setPrivateApiKey(String key) {
        MyApplication.securePreferences.put("bGetPrivateApiKey", key);
    }

    public static String getDeviceCode() {
        return MyApplication.securePreferences.getString("bGetDeviceCode");
    }

    public static void setDeviceCode(String code) {
        MyApplication.securePreferences.put("bGetDeviceCode", code);
    }

    public static Passenger getPassenger() {
        String temp = MyApplication.sharedPreferences.getString("bGetPassenger", "");
        if (ValidatorHelper.isValidString(temp)) {
            return MyApplication.gson.fromJson(temp, Passenger.class);
        }
        return new Passenger();
    }

    public static void setPassenger(Passenger passenger) {
        MyApplication.editor.putString("bGetPassenger", MyApplication.gson.toJson(passenger));
        MyApplication.editor.apply();
    }

    public static void setAlarmPoints(ArrayList<AlarmLocationPoint> points) {
        Passenger passenger = getPassenger();
        passenger.setAlarm_Location_Points(points);
        setPassenger(passenger);
    }

    public static void setSubscribedTopics(ArrayList<String> topics) {
        MyApplication.editor.putString("bGetSubscribedTopics", MyApplication.gson.toJson(topics));
        MyApplication.editor.apply();
    }

    public static ArrayList<String> getSubscribedTopics() {
        Type stringType = new TypeToken<ArrayList<String>>() {
        }.getType();
        String temp = MyApplication.sharedPreferences.getString("bGetSubscribedTopics", "");
        if (ValidatorHelper.isValidString(temp)) {
            return MyApplication.gson.fromJson(temp, stringType);
        }
        return new ArrayList<>();
    }

    public static ArrayList<Message> getMessages() {
        Type messageType = new TypeToken<ArrayList<Message>>() {
        }.getType();
        String temp = MyApplication.sharedPreferences.getString("bGetMessages", "");
        if (ValidatorHelper.isValidString(temp)) {
            return MyApplication.gson.fromJson(temp, messageType);
        }
        return new ArrayList<>();
    }

    private static void setMessages(ArrayList<Message> passengerAbsences) {
        MyApplication.editor.putString("bGetMessages", MyApplication.gson.toJson(passengerAbsences));
        MyApplication.editor.apply();
    }

    public static void addMessage(Message message) {
        ArrayList<Message> temp = getMessages();
        temp.add(message);
        setMessages(temp);

    }
}
