package ir.edusa.parents.Models;

import ir.edusa.parents.Helpers.ValidatorHelper;

import java.io.Serializable;
import java.util.StringTokenizer;

public class LivePointNotification extends PushNotificationData implements Serializable {


    public LivePointNotification(String command_Data, int command_Code) {
        super(command_Data, command_Code);
    }

    public double getLatitude() {
        if (!ValidatorHelper.isValidString(getCommand_Data())) {
            return 0;
        }
        StringTokenizer tokens = new StringTokenizer(getCommand_Data(), ",");
        double lat = Double.parseDouble(tokens.nextToken());
        double lon = Double.parseDouble(tokens.nextToken());
        String date = tokens.nextToken();
        String time = tokens.nextToken();
        return lat;
    }

    public double getLongitude() {
        if (!ValidatorHelper.isValidString(getCommand_Data())) {
            return 0;
        }
        StringTokenizer tokens = new StringTokenizer(getCommand_Data(), ",");
        double lat = Double.parseDouble(tokens.nextToken());
        double lon = Double.parseDouble(tokens.nextToken());
        String date = tokens.nextToken();
        String time = tokens.nextToken();
        return lon;
    }


}
