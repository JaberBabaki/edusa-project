package ir.edusa.parents.Network;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ir.edusa.parents.Dialogs.LoadingDialog;
import ir.edusa.parents.Helpers.HashHelper;
import ir.edusa.parents.Helpers.ValidatorHelper;
import ir.edusa.parents.Managers.UserManager;
import ir.edusa.parents.Models.Command;
import ir.edusa.parents.Models.CommandMessage;
import ir.edusa.parents.MyApplication;
import ir.edusa.parents.Network.Requests.DriverLastLocationRequest;
import ir.edusa.parents.Network.Requests.GetCommandsInQueueRequest;
import ir.edusa.parents.Network.Requests.GetDriverRouteRequest;
import ir.edusa.parents.Network.Requests.LoginRequest;
import ir.edusa.parents.Network.Requests.PassengerInfoRequest;
import ir.edusa.parents.Network.Requests.PingRequest;
import ir.edusa.parents.Network.Responses.AbstractResponse;
import ir.edusa.parents.Network.Responses.CommandQueueResponse;
import ir.edusa.parents.Network.Responses.DriverLastLocationResponse;
import ir.edusa.parents.Network.Responses.DriverRouteResponse;
import ir.edusa.parents.Network.Responses.LoginResponse;
import ir.edusa.parents.Network.Responses.PingResponse;
import ir.edusa.parents.Network.Responses.UserInfoResponse;
import ir.edusa.parents.Utils.Log;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

import ir.edusa.parents.Network.Requests.AbstractRequest;
import ir.edusa.parents.Network.Requests.DownStreamRegisterRequest;
import ir.edusa.parents.Network.Responses.DownStreamRegisterResponse;

public class ApiCallerClass {
  private CallerOnResponse callerOnResponse;
  private OnCancel onCancel;
  private LoadingDialog loadingDialog;
  private boolean hasLoading = false;
  public static final MediaType JSON
    = MediaType.parse("application/json; charset=utf-8");
  public static String REQUESTS_TAG = "ZOODFOOD_REQUESTS_TAG";
  private Context context;

  //Instantiate Method
  public static synchronized ApiCallerClass with(Context context) {
    return new ApiCallerClass(context);
  }

  public interface CallerOnResponse {
    void onResponse(AbstractResponse result, JSONObject rawResponse);

    void onError(String message, int code);
  }

  public ApiCallerClass(Context context) {
    REQUESTS_TAG = "ZOODFOOD_REQUESTS_TAG";
    this.context = context;
  }

  public interface OnCancel {
    void onCancel();
  }

  public ApiCallerClass hasLoading(boolean loading) {
    this.hasLoading = loading;
    return this;
  }

  public ApiCallerClass onCancel(OnCancel onCancelCallback) {
    this.onCancel = onCancelCallback;
    return this;
  }

  public void call(final AbstractRequest request, CallerOnResponse respons, String tag) {
    REQUESTS_TAG = tag;
    callerOnResponse = respons;
    if (hasLoading) {
      loadingDialog = new LoadingDialog(context);
      loadingDialog.show();
      if (onCancel != null) {
        loadingDialog.setCancelable(true);
        loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
          @Override
          public void onCancel(DialogInterface dialog) {
            onCancel.onCancel();
          }
        });
      }
    }
    CallAction(request);
  }

  public void call(Request request, CallerOnResponse respons) {
    callerOnResponse = respons;
    if (hasLoading) {
      loadingDialog = new LoadingDialog(context);
      loadingDialog.show();
      if (onCancel != null) {
        loadingDialog.setCancelable(true);
        loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
          @Override
          public void onCancel(DialogInterface dialog) {
            onCancel.onCancel();
          }
        });
      }
    }
    executeRequest(request, null);
  }


  private void CallAction(final AbstractRequest request) {
    RequestBody body = null;
    String url = request.getApiUrl();
    if (request.getMethod() == ApiConstants.METHOD_GET) {
      String paramsString = request.getParametersJsonFormat();
      url = url + Uri.encode("?" + paramsString, "@#&=*+-_.,:!?()/~'%");
    } else {
      if (true) {
        JSONObject jsonObject = new JSONObject();
        for (String key : request.getParametersWithGlobals().keySet()) {
          try {
            jsonObject.put(key, request.getParametersWithGlobals().get(key));
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
        for (String key : request.getArrayParameters().keySet()) {
          try {
            jsonObject.put(key, request.getArrayParameters().get(key));
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
        body = RequestBody.create(JSON, jsonObject.toString());
      } else {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : request.getParametersWithGlobals().keySet()) {
          builder.add(key, request.getParametersWithGlobals().get(key));
        }
        body = builder.build();
      }
    }


    final Request.Builder builder = new Request.Builder()
      .url(url)
      .tag(REQUESTS_TAG)
      .method(request.getMethod() == ApiConstants.METHOD_GET ? "GET" : "POST",
        request.getMethod() == ApiConstants.METHOD_GET ? null : body == null ? RequestBody.create(null, new byte[0]) : body);
//        String token = UserManager.getToken();
//        if (ValidatorHelper.isValidString(token)) {
//            builder.addHeader("Authorization", "Bearer " + token);
//        }
    Request okRequest = builder.build();

//        Log.e("api_caller ST", "" + url);
    executeRequest(okRequest, request);
  }

  private void executeRequest(final Request okRequest, final AbstractRequest request) {
    LogRequest(okRequest);

    MyApplication.getOkHttpClient().newCall(okRequest).enqueue(new Callback() {

      @Override
      public void onFailure(final Call call, final IOException e) {
        MyApplication.longLog("TAG TAG TAG", e.getMessage());
        Handler mainHandler = null;
        try {
          mainHandler = new Handler(context.getMainLooper());
        } catch (Exception e10) {
          e10.printStackTrace();
        }

        if (mainHandler == null) {
          return;
        }

        mainHandler.post(new Runnable() {
          @Override
          public void run() {
            try {
              loadingDialog.dismiss();
            } catch (Exception e1) {

            }
            if (!call.isCanceled()) {
              callerOnResponse.onError("خطای ارتباط با اینترنت", ApiConstants.NETWORK_ERROR_CODE);
            }
          }
        });
      }

      @Override
      public void onResponse(final Call call, final Response response) throws IOException {

        Handler mainHandler = null;
        try {
          mainHandler = new Handler(context.getMainLooper());
        } catch (Exception e) {
          e.printStackTrace();

        }

        if (mainHandler == null) {
          return;
        }

        final String responseBody = response.body().string();

        MyApplication.longLog("TAG TAG TAG", responseBody);

        mainHandler.post(new Runnable() {
          @Override
          public void run() {
            try {
              loadingDialog.dismiss();
            } catch (Exception e) {

            }

            if (response.isSuccessful() && !call.isCanceled()) {
              try {
                parseResult(new JSONObject(responseBody), request);
              } catch (JSONException e) {
                e.printStackTrace();
                callerOnResponse.onError("خطای ارتباط با سرور", 1060);
              }
            } else {
              try {
                JSONObject error = new JSONObject(responseBody);
                if (error.has("Message")) {
                  callerOnResponse.onError("خطای داخلی سرور", 1060);

                } else {
                  callerOnResponse.onError("خطای ارتباط با سرور", 1060);
                }
              } catch (JSONException e) {
                e.printStackTrace();
                callerOnResponse.onError("خطای ارتباط با سرور", 1060);
              }
            }
          }
        });
      }
    });
  }

  public void parseResult(JSONObject response, AbstractRequest request) throws JSONException {
    String requestType = "";
    if (request != null) {
      requestType = request.getClass().getSimpleName();
    }
    AbstractResponse abstractResponse = null;
    boolean wasSuccessful = response.getBoolean("Status");
    if (wasSuccessful) {
      if (request == null) {
        callerOnResponse.onResponse(null, response);
        return;
      }

      if (requestType.equals(LoginRequest.class.getSimpleName())) {
        abstractResponse = MyApplication.gson.fromJson(response.toString(), LoginResponse.class);
      }
      if (requestType.equals(PassengerInfoRequest.class.getSimpleName())) {
        UserInfoResponse userInfoResponse = MyApplication.gson.fromJson(response.toString(), UserInfoResponse.class);
        UserManager.setPassenger(userInfoResponse.getData());
        userInfoResponse.getData().refreshTopics();
        abstractResponse = userInfoResponse;
      }
      if (requestType.equals(DriverLastLocationRequest.class.getSimpleName())) {
        abstractResponse = MyApplication.gson.fromJson(response.toString(), DriverLastLocationResponse.class);
      }
      if (requestType.equals(PingRequest.class.getSimpleName())) {
        abstractResponse = MyApplication.gson.fromJson(response.toString(), PingResponse.class);
      }
      if (requestType.equals(GetDriverRouteRequest.class.getSimpleName())) {
        abstractResponse = MyApplication.gson.fromJson(response.toString(), DriverRouteResponse.class);
      }
      if (requestType.equals(DownStreamRegisterRequest.class.getSimpleName())) {
        abstractResponse = MyApplication.gson.fromJson(response.toString(), DownStreamRegisterResponse.class);
      }
      if (requestType.equals(GetCommandsInQueueRequest.class.getSimpleName())) {
        abstractResponse = MyApplication.gson.fromJson(response.toString(), CommandQueueResponse.class);
        ArrayList<Object> commands = new ArrayList<>();
        JSONArray commandArray = response.getJSONObject("Result_Data").getJSONArray("Commands_4_Device");
        for (int i = 0; i < commandArray.length(); i++) {
          int commandCode = commandArray.getJSONObject(i).getInt("Command_Code");
          switch (commandCode) {
            case Command.COMMAND_CODE_GROUP_MESSAGE:
            case Command.COMMAND_CODE_MESSAGE:
              commands.add(MyApplication.gson.fromJson(commandArray.getJSONObject(i).toString(), CommandMessage.class));
              break;
          }
        }
        ((CommandQueueResponse) abstractResponse).getData().setCommands(commands);
      }


      callerOnResponse.onResponse(abstractResponse, response);
    } else {
      try {
        if (response.optJSONObject("Error") != null && ValidatorHelper.isValidString(response.optJSONObject("Error").optString("Message"))) {
          if (response.optJSONObject("Error").optInt("Code", 85) == -1 && !requestType.equals(LoginRequest.class.getSimpleName())) {
            tryLoginUser(request);
            return;
          }
          if (response.optJSONObject("Error").optInt("Code", 85) == -11 && requestType.equals(LoginRequest.class.getSimpleName())) {
           Log.i("PAY"," jab 1"+response.optJSONObject("Error").optInt("Code", 85));
           Log.i("PAY"," jab 2"+response.optJSONObject("Error").getJSONObject("Error_Data").optString("Redirect_Url_4_Payment"));
           // MyApplication.code = "-11";
           // MyApplication.message = response.optJSONObject("Error").optString("Message");
            // MyApplication.fee=response.optJSONObject("Error").getJSONObject("Error_Data").optString("Payment_Fee");
            MyApplication.url=response.optJSONObject("Error").getJSONObject("Error_Data").optString("Redirect_Url_4_Payment");



          }
          callerOnResponse.onError(response.optJSONObject("Error").optString("Message"), response.optJSONObject("Error").optInt("Code"));
        } else {
          callerOnResponse.onError("خطا در ارتباط با سرور", -1);
        }
      } catch (Exception e) {
        e.printStackTrace();
        callerOnResponse.onError("خطای ارتباط با سرور", -1);

      }
    }
  }


  public static void cancel(String tag) {
    OkHttpClient client = MyApplication.getOkHttpClient();
    for (Call call : client.dispatcher().queuedCalls()) {
      if (call.request().tag().equals(tag)) {
        call.cancel();
        Log.e("cancel", "cancel");
      }
    }
    for (Call call : client.dispatcher().runningCalls()) {
      if (call.request().tag().equals(tag)) {
        call.cancel();
        Log.e("cancel", "cancel");
      }
    }
  }

  private static String bodyToString(final Request request) {

    try {
      final Request copy = request.newBuilder().build();
      final Buffer buffer = new Buffer();
      copy.body().writeTo(buffer);
      return buffer.readUtf8();
    } catch (final Exception e) {
      return "did not work";
    }
  }

  public static void LogRequest(Request request) {
    Log.e("URL", request.url().toString());
    Log.e("Method", request.method());
    Log.e("Body", bodyToString(request));
  }

  public void tryLoginUser(final AbstractRequest aRequestََ) {
    String deviceCode = UserManager.getDeviceCode();
    ApiCallerClass.with(context).call(new LoginRequest(HashHelper.Calculate_Password(UserManager.getPrivateApiKey(), ApiConstants.DEFAULT_PRIVATE_KEY), deviceCode)
      , new ApiCallerClass.CallerOnResponse() {
        @Override
        public void onResponse(AbstractResponse result, JSONObject rawResponse) {
          LoginResponse loginResponse = (LoginResponse) result;
          UserManager.setToken(loginResponse.getData().getAuth_Key());
          UserManager.setDeviceCode(loginResponse.getData().getDevice_Code() + "");
          if (aRequestََ.isInLoginLoop()) {
            callerOnResponse.onError("خطا در فرآیند احراض هویت \n با پشتیبانی تماس بگیرید", ApiConstants.RELOGIN_ERROR_CODE);
            return;
          }
          if (ValidatorHelper.isValidString(aRequestََ.getPassword())) {
            aRequestََ.setPassword(HashHelper.Calculate_Password(UserManager.getPrivateApiKey()
              , loginResponse.getData().getAuth_Key()));
          }
          aRequestََ.setInLoginLoop(true);
          ApiCallerClass.with(context).call(aRequestََ, callerOnResponse, REQUESTS_TAG);
        }

        @Override
        public void onError(String message, int code) {
          callerOnResponse.onError("لطفا مجددا به حساب کاربری خود وارد شوید", ApiConstants.RELOGIN_ERROR_CODE);
        }
      }, "");
  }

}
