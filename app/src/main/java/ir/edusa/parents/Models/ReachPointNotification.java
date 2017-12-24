package ir.edusa.parents.Models;

import ir.edusa.parents.MyApplication;

import java.io.Serializable;

public class ReachPointNotification extends PushNotificationData implements Serializable {

    public ReachPointNotification(String command_Data, int command_Code) {
        super(command_Data, command_Code);
    }

    public int getAlarm_Point_Code() {
        ReachPointData reachPointData = MyApplication.gson.fromJson(getCommand_Data(), ReachPointData.class);
        return reachPointData.getAlarm_Point_Code();
    }

    public int getOrganization_Code() {
        ReachPointData reachPointData = MyApplication.gson.fromJson(getCommand_Data(), ReachPointData.class);
        return reachPointData.getOrganization_Code();
    }

    public int getPassenger_Code() {
        ReachPointData reachPointData = MyApplication.gson.fromJson(getCommand_Data(), ReachPointData.class);
        return reachPointData.getPassenger_Code();
    }

    public class ReachPointData {

        private int Alarm_Point_Code = -1;
        private int Organization_Code = -1;
        private int Passenger_Code = -1;

        public int getAlarm_Point_Code() {
            return Alarm_Point_Code;
        }

        public void setAlarm_Point_Code(int alarm_Point_Code) {
            Alarm_Point_Code = alarm_Point_Code;
        }

        public int getOrganization_Code() {
            return Organization_Code;
        }

        public void setOrganization_Code(int organization_Code) {
            Organization_Code = organization_Code;
        }

        public int getPassenger_Code() {
            return Passenger_Code;
        }

        public void setPassenger_Code(int passenger_Code) {
            Passenger_Code = passenger_Code;
        }
    }
}
