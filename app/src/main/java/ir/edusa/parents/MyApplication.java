package ir.edusa.parents;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import ir.edusa.parents.Helpers.ValidatorHelper;
import ir.edusa.parents.Utils.Log;
import ir.edusa.parents.Utils.SecurePreferences;
import okhttp3.OkHttpClient;

/**
 * Created by pouya on 5/4/17.
 */

public class MyApplication extends Application {
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static String PackageName;
    public static int VersionCode;
    public static String VersionName;
    public static String API_VERSION;
//    public static String IMEI;
    public static String Model;
    public static String UDID;
    private static GsonBuilder gsonBuilder = new GsonBuilder();
    public static Gson gson;
    public static boolean doubleBackToExitPressedOnce = false;
    public static final DisplayMetrics metrics = new DisplayMetrics();
    public static SecurePreferences securePreferences;
    private static OkHttpClient client;

    //public static String code="0";
    //public static String message="0";
    //public static String fee="0";
    public static String url="0";


    @Override
    public void onCreate() {
        super.onCreate();
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

            @Override
            public Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
                    throws JsonParseException {
                if (json.getAsString().length() == 0)
                    return null;
                try {
                    return df.parse(json.getAsString());
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });
        gsonBuilder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            @Override
            public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(df.format(src));
            }
        });
        gson = gsonBuilder.create();

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        if (!BuildConfig.DEBUG) {
            Log.LOG = false;
        }
        String token = FirebaseInstanceId.getInstance().getToken();
        if (ValidatorHelper.isValidString(token)) {
            Log.e("Firebase token : ", token);
        }
        sharedPreferences = this.getSharedPreferences("TrackingSharedPreferences", Context.MODE_PRIVATE);
        securePreferences = new SecurePreferences(this, "TrackingSharedPreferencesS", "kdjhasdgbcuaodhajcbnuah", true);
        editor = sharedPreferences.edit();
//        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        IMEI = telephonyManager.getDeviceId();
        API_VERSION = Build.VERSION.RELEASE;
        PackageManager manager = this.getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
            VersionCode = info.versionCode ;
            VersionName = info.versionName + "";
            PackageName = info.packageName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Model = Build.MODEL;
        UDID = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

//        Log.e("HASH", HashHelper.Calculate_Password("09195176778"));
    }

    public static OkHttpClient getOkHttpClient() {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .readTimeout(30000, TimeUnit.MILLISECONDS)
                    .writeTimeout(30000, TimeUnit.MILLISECONDS)
                    .build();
        }
        return client;
    }

    public static void longLog(String TAG, String veryLongString) {
        if (veryLongString == null) {
            return;
        }
        int maxLogSize = 1000;
        for (int i = 0; i <= veryLongString.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > veryLongString.length() ? veryLongString.length() : end;
            Log.e(TAG, veryLongString.substring(start, end));
        }
    }

}
