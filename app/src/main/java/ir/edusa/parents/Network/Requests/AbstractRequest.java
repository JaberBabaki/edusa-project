package ir.edusa.parents.Network.Requests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ir.edusa.parents.MyApplication;
import ir.edusa.parents.Utils.Log;


/**
 * Created by pouya on 8/25/15.
 */
abstract public class AbstractRequest {
    private String Password = "";
    private boolean isInLoginLoop = false;

    public AbstractRequest(String password) {
        Password = password;
    }
    public AbstractRequest() {
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    abstract public int getMethod();

    abstract public String getApiUrl();


    abstract public HashMap<String, String> getParameters();

    public HashMap<String, String> getParametersWithGlobals() {
        HashMap<String, String> paramsMap = getParameters();
        paramsMap.put("UDID", MyApplication.UDID);
        return paramsMap;
    }

    public HashMap<String, JSONArray> getArrayParameters() {
        return new HashMap<>();
    }

    public String getParametersJsonFormat() {
        HashMap<String, String> paramsMap = getParametersWithGlobals();
        String paramsString = "";

        Iterator<Map.Entry<String, String>> mapIterator = paramsMap.entrySet().iterator();
        while (mapIterator.hasNext()) {
            Map.Entry<String, String> it = mapIterator.next();
            paramsString = paramsString + it.getKey() + "=" + it.getValue();
            if (mapIterator.hasNext()) {
                paramsString = paramsString + "&";
            }
        }
        Log.i("ttt", "" + paramsString);
        return paramsString;

    }

    public JSONObject jsonObjectFrom(Object o) {
        try {
            return new JSONObject(MyApplication.gson.toJson(o));
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public boolean isInLoginLoop() {
        return isInLoginLoop;
    }

    public void setInLoginLoop(boolean inLoginLoop) {
        isInLoginLoop = inLoginLoop;
    }
}
