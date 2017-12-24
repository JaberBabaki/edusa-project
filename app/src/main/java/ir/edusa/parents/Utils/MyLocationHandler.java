package ir.edusa.parents.Utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class MyLocationHandler {
    public static boolean gps_enabled = false;
    LocationManager lm;
    LocationResult locationResult;
    boolean network_enabled = false;
    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //timer1.cancel();
            locationResult.gotLocation(location);

            //  lm.removeUpdates(this);
            //lm.removeUpdates(locationListenerNetwork);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    public boolean getLocation(Context context, LocationResult result) {
        //I use LocationResult callback class to pass location value from MyLocation to user code.
        locationResult = result;
        if (lm == null)
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //exceptions will be thrown if provider is not permitted.
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //don't start listeners if no provider is enabled
//        if(!gps_enabled && !network_enabled) {
//            return false;
//        }

//        lm.requestSingleUpdate(new Criteria(), locationListener,context.getMainLooper());
        if (gps_enabled) {
            lm.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, context.getMainLooper());
//            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        if (network_enabled) {
            lm.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, context.getMainLooper());
//            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
        }

        //timer1=new Timer();
        //timer1.schedule(new GetLastLocation(), 20000);
        GetLastLocation();
        return true;
    }

//    LocationListener locationListenerNetwork = new LocationListener()
//    {
//        public void onLocationChanged(Location location)
//        {
//            //timer1.cancel();
//            locationResult.gotLocation(location);
//            // lm.removeUpdates(this);
//            //   lm.removeUpdates(locationListener);
//        }
//        public void onProviderDisabled(String provider) {}
//        public void onProviderEnabled(String provider) {}
//        public void onStatusChanged(String provider, int status, Bundle extras) {}
//    };

    void GetLastLocation() {


        Location net_loc = null, gps_loc = null;
        if (gps_enabled) {
            gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (network_enabled) {
            net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        //if there are both values use the latest one
        if (gps_loc != null && net_loc != null) {
            if (gps_loc.getTime() > net_loc.getTime()) {
                locationResult.gotLocation(gps_loc);
            } else {
                locationResult.gotLocation(net_loc);
            }
            return;

        }

        if (gps_loc != null) {
            locationResult.gotLocation(gps_loc);
            return;
        }
        if (net_loc != null) {
            locationResult.gotLocation(net_loc);
            return;
        }
//        locationResult.gotLocation(null);

    }

    public static abstract class LocationResult {
        public abstract void gotLocation(Location location);
    }

}