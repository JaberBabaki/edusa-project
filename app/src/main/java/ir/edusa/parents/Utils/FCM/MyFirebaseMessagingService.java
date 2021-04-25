package ir.edusa.parents.Utils.FCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ir.edusa.parents.Activities.InboxActivity;
import ir.edusa.parents.Activities.MainActivity;
import ir.edusa.parents.Helpers.HashHelper;
import ir.edusa.parents.Helpers.ValidatorHelper;
import ir.edusa.parents.Managers.UserManager;
import ir.edusa.parents.Models.AlarmLocationPoint;
import ir.edusa.parents.Models.Command;
import ir.edusa.parents.Models.LivePointNotification;
import ir.edusa.parents.Models.Message;
import ir.edusa.parents.Models.Passenger;
import ir.edusa.parents.Models.PushNotificationData;
import ir.edusa.parents.Models.ReachPointNotification;
import ir.edusa.parents.MyApplication;
import ir.edusa.parents.Network.ApiCallerClass;
import ir.edusa.parents.Network.Requests.CommandACKRequest;
import ir.edusa.parents.Network.Responses.AbstractResponse;
import ir.edusa.parents.R;
import ir.edusa.parents.Utils.Log;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.Date;


/**
 * Created by TempAdmin1 on 7/17/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private final int ALARM_POINT_NOTIFICATION_ID = 100;
    private final int ORGANIZATION_POINT_NOTIFICATION_ID = 200;
    private final int PASSENGER_POINT_NOTIFICATION_ID = 300;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
            if (remoteMessage.getData().containsKey("info")) {
                PushNotificationData data = MyApplication.gson.fromJson(remoteMessage.getData().get("info"), PushNotificationData.class);
                switch (data.getCommand_Code()) {
                    case PushNotificationData.COMMAND_CODE_LIVE_LOCATION:
                        data = MyApplication.gson.fromJson(remoteMessage.getData().get("info"), LivePointNotification.class);
                        onPointReceived((LivePointNotification) data);
                        break;
                    case PushNotificationData.COMMAND_CODE_REACH_POINT_ALARM:
                        data = MyApplication.gson.fromJson(remoteMessage.getData().get("info"), ReachPointNotification.class);
                        handleReachPoint((ReachPointNotification) data);
                        break;
                    case PushNotificationData.COMMAND_CODE_MESSAGE_FROM_PANEL:
                        Message messageFromPanel = MyApplication.gson.fromJson(data.getCommand_Data(), Message.class);
                        handleMessage(messageFromPanel);
                        break;
                }

            }
        }

        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotificationToMainPage(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), 1000 + (int) (Math.random() * 20));
        }

    }

    public void onPointReceived(LivePointNotification livePointNotification) {
        //Post Event
        EventBus.getDefault().post(livePointNotification);
    }

    public void handleMessage(Message message) {
        int commandCode;
        if (message.is_Group_Message()) {
            commandCode = Command.COMMAND_CODE_GROUP_MESSAGE;
        } else {
            commandCode = Command.COMMAND_CODE_MESSAGE;
        }
        sendNotificationToMessages(this, "پیام جدید", message.getMessage_Text(), (int) (Math.random() * 100000));
        message.setDate(new Date());
        message.setFromPanel(true);
        UserManager.addMessage(message);
        sendACK(message.getCommand_Reference_Code(), commandCode);
        EventBus.getDefault().post("NEW_MESSAGE");

    }

    public void handleReachPoint(ReachPointNotification reachPointNotification) {
        if (!ValidatorHelper.isValidString(UserManager.getDeviceCode())) {
            return;
        }
        if (reachPointNotification.getAlarm_Point_Code() > 0) {
            int alarmPointCode = reachPointNotification.getAlarm_Point_Code();
            Passenger passenger = UserManager.getPassenger();
            if (passenger.getAlarm_Location_Points().size() > 0) {
                for (AlarmLocationPoint point : passenger.getAlarm_Location_Points()) {
                    if (point.getPoint_Code().equals(alarmPointCode + "")) {
                        String pointName = ValidatorHelper.isValidString(point.getPoint_Descript()) ?
                                point.getPoint_Descript() : "نقطه هشدار شماره " + point.getPoint_Code();
                        sendNotificationToMainPage("سرویس نزدیک است", "سرویس به " + pointName + " رسید.", ALARM_POINT_NOTIFICATION_ID + Integer.parseInt(point.getPoint_Code()));
                    }
                }
            }
        } else if (reachPointNotification.getOrganization_Code() > 0) {
            if (reachPointNotification.getOrganization_Code() > 0) {
                int organizationCode = reachPointNotification.getOrganization_Code();
                Passenger passenger = UserManager.getPassenger();
                if (passenger.getOrganization() != null && passenger.getOrganization().getCode().equals(organizationCode + "")) {
                    sendNotificationToMainPage("هشدار رسیدن", "سرویس به " + "مدرسه" + " رسید.", ORGANIZATION_POINT_NOTIFICATION_ID);
                }
            }
        } else if (reachPointNotification.getPassenger_Code() > 0) {
            if (reachPointNotification.getPassenger_Code() > 0) {
                int passengerCode = reachPointNotification.getPassenger_Code();
                Passenger passenger = UserManager.getPassenger();
                if (passenger.getCode().equals(passengerCode + "")) {
                    sendNotificationToMainPage("هشدار رسیدن", "سرویس " + " رسید.", PASSENGER_POINT_NOTIFICATION_ID);
                }
            }
        }
    }

    private void sendNotificationToMainPage(String title, String messageBody, int id) {
        Intent intent = new Intent(this, MainActivity.class);
        showPushNotification(this, title, messageBody, id, intent);
    }

    public static void sendNotificationToMessages(Context context, String title, String messageBody, int id) {
        Intent intent = new Intent(context, InboxActivity.class);
        showPushNotification(context, title, messageBody, id, intent);
    }

    public static void showPushNotification(Context context, String title, String messageBody, int id, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.app_icon)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(id, notificationBuilder.build());
    }

    public void sendACK(int ackNum, int commandCode) {
        ApiCallerClass.with(this).call(new CommandACKRequest(HashHelper.Calculate_Password(UserManager.getPrivateApiKey()
                , UserManager.getToken()), UserManager.getDeviceCode(), ackNum, commandCode, true), new ApiCallerClass.CallerOnResponse() {
            @Override
            public void onResponse(AbstractResponse result, JSONObject rawResponse) {

            }

            @Override
            public void onError(String message, int code) {
                Log.e("REQUEST_ERROR (CommandACKRequest)", message);
            }
        }, "");
    }
}