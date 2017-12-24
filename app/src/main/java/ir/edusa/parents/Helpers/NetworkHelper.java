package ir.edusa.parents.Helpers;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkHelper {

    private static NetworkHelper networkHelper;
    private Context context;

    //Hide Constructor
    private NetworkHelper(Context context) {
        this.context = context;
    }

    //Instantiate Method
    public static synchronized NetworkHelper with(Context context) {
        if (networkHelper == null) {
            networkHelper = new NetworkHelper(context);
        }

        return networkHelper;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}