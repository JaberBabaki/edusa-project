package ir.edusa.parents.Utils.FCM;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import ir.edusa.parents.Helpers.HashHelper;
import ir.edusa.parents.Helpers.ValidatorHelper;
import ir.edusa.parents.Managers.UserManager;
import ir.edusa.parents.Network.ApiCallerClass;
import ir.edusa.parents.Network.Requests.SetFCMTokenRequest;
import ir.edusa.parents.Network.Responses.AbstractResponse;
import ir.edusa.parents.Utils.Log;

import org.json.JSONObject;


/**
 * Created by TempAdmin1 on 7/17/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    private void sendRegistrationToServer(final String token) {
        String deviceCode = UserManager.getDeviceCode();
        if (!ValidatorHelper.isValidString(deviceCode)) {
            return;
        }
        ApiCallerClass.with(MyFirebaseInstanceIDService.this).call(new SetFCMTokenRequest(deviceCode,
                        token, HashHelper.Calculate_Password(UserManager.getPrivateApiKey(), UserManager.getToken())),
                new ApiCallerClass.CallerOnResponse() {
                    @Override
                    public void onResponse(AbstractResponse result, JSONObject rawResponse) {
                        Log.e("FCM Registration", "Success");
                        UserManager.getPassenger().refreshTopics();
                    }

                    @Override
                    public void onError(String message, int code) {
                        Log.e("FCM ERROR", message);
                    }
                }, "");
    }
}

