package ir.edusa.parents.Utils;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import ir.edusa.parents.Dialogs.LoadingDialog;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by pouya on 10/16/15.
 */
public class GetDirectionsAsyncTask extends AsyncTask<Map<String, String>, Object, ArrayList> {
    public static final String USER_CURRENT_LAT = "user_current_lat";
    public static final String USER_CURRENT_LONG = "user_current_long";
    public static final String DESTINATION_LAT = "destination_lat";
    public static final String DESTINATION_LONG = "destination_long";
    public static final String DIRECTIONS_MODE = "directions_mode";
    private Exception exception;
    private boolean sucsess = false;
    private Context context;
    private int distance;
    private int duration;
    private DrawPollylines drawPollylines;
    private LoadingDialog loadingDialog;

    public interface DrawPollylines {
        void draw(ArrayList<LatLng> directionPoints);
    }

    public GetDirectionsAsyncTask(Context context, DrawPollylines dpl) {
        super();
        this.context = context;
        sucsess = false;
        drawPollylines = dpl;
    }

    public void onPreExecute() {
        loadingDialog = new LoadingDialog(context);
        loadingDialog.show();
    }

    @Override
    public void onPostExecute(ArrayList result) {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        if (exception == null) {
            drawPollylines.draw(result);
        } else {
            processException();
        }
    }

    @Override
    protected ArrayList doInBackground(Map<String, String>... params) {
        Map<String, String> paramMap = params[0];
        try {
            LatLng fromPosition = new LatLng(Double.valueOf(paramMap.get(USER_CURRENT_LAT)), Double.valueOf(paramMap.get(USER_CURRENT_LONG)));
            LatLng toPosition = new LatLng(Double.valueOf(paramMap.get(DESTINATION_LAT)), Double.valueOf(paramMap.get(DESTINATION_LONG)));
            GMapV2Direction md = new GMapV2Direction();
            Document doc = md.getDocument(fromPosition, toPosition, paramMap.get(DIRECTIONS_MODE));
            ArrayList directionPoints = md.getDirection(doc);
            sucsess = true;
            distance = (md.getDistanceValue(doc));
            duration = (md.getDurationValue(doc));
            return directionPoints;
        } catch (Exception e) {
            exception = e;
            sucsess = false;
            return null;
        }
    }

    public int getDistance() {
        if (sucsess) {
            return distance;
        }
        return -1;
    }

    public int getDuration() {
        if (sucsess) {
            return distance;
        }
        return -1;
    }

    private void processException() {
//        Toast.makeText(activity, "ERPPPPd", Toast.LENGTH_SHORT).show();
    }
}