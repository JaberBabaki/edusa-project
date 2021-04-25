package ir.edusa.parents.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.view.View;
import android.view.ViewGroup;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;

import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import fontdroid.EditText;
import fontdroid.TextView;
import ir.edusa.parents.Dialogs.ConfirmDialog;
import ir.edusa.parents.Dialogs.ErrorDialog;
import ir.edusa.parents.Dialogs.PayDialog;
import ir.edusa.parents.Dialogs.WaitingDialog;
import ir.edusa.parents.Fragments.TextInputFragment;
import ir.edusa.parents.Helpers.DateHelper;
import ir.edusa.parents.Helpers.HashHelper;
import ir.edusa.parents.Helpers.IntentHelper;
import ir.edusa.parents.Helpers.Toast;
import ir.edusa.parents.Helpers.ValidatorHelper;
import ir.edusa.parents.Interfaces.OnTextEnteredListener;
import ir.edusa.parents.Managers.UserManager;
import ir.edusa.parents.Models.SchoolService;
import ir.edusa.parents.MyApplication;
import ir.edusa.parents.Network.ApiCallerClass;
import ir.edusa.parents.Network.ApiConstants;
import ir.edusa.parents.Network.Requests.DownStreamRegisterRequest;
import ir.edusa.parents.Network.Requests.LoginRequest;
import ir.edusa.parents.Network.Requests.PassengerInfoRequest;
import ir.edusa.parents.Network.Requests.PingRequest;
import ir.edusa.parents.Network.Requests.RegisterDeviceRequest;
import ir.edusa.parents.Network.Responses.AbstractResponse;
import ir.edusa.parents.Network.Responses.DownStreamRegisterResponse;
import ir.edusa.parents.Network.Responses.LoginResponse;
import ir.edusa.parents.Network.Responses.PingResponse;
import ir.edusa.parents.R;
import ir.edusa.parents.Utils.Log;

public class LoginActivity extends BaseActivity {

  private EditText edtPhoneNumber;
  private EditText edtPassword;
  private TextView txtTitle;
  private ViewGroup lytEnter;
  private ViewGroup lytPassword;
  private WaitingDialog waitingDialog;
  //private PowerManager.WakeLock mWakeLock;
  private String updatePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
  //    private final String updatePath = getApplicationContext().getCacheDir().getPath();
  private boolean hiddenMenu = false;
  private int hiddenMenuCount = 0;

  @Override
  protected void onResume() {
    super.onResume();
    checkForCrashes();
  }

  private void checkForCrashes() {
    CrashManager.register(this, new CrashManagerListener() {
      @Override
      public boolean shouldAutoUploadCrashes() {
        return true;
      }

      @Override
      public String getUserID() {
        String code = UserManager.getPassenger().getCode();
        if (!ValidatorHelper.isValidString(code)) {
          code = "UNKNOWN";
        }
        return code;
      }

      @Override
      public String getContact() {
        String code = UserManager.getDeviceCode();
        if (!ValidatorHelper.isValidString(code)) {
          code = "UNKNOWN";
        }
        return code;
      }
    });
  }

  @Override
  protected String getPageTitle() {
    return "ورود";
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    Log.i("LOG", "jaber1");

    edtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
    edtPassword = (EditText) findViewById(R.id.edtPassword);
    lytPassword = (ViewGroup) findViewById(R.id.lytPassword);
    txtTitle = (TextView) findViewById(R.id.txtTitle);
    lytEnter = (ViewGroup) findViewById(R.id.lytEnter);
    lytPassword.setVisibility(View.GONE);
    updatePath += "/parentsApp " + DateHelper.formatDate(new Date(), "") + DateHelper.formatTime(new Date(), false) + ".apk";
    txtTitle.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!hiddenMenu) {
          hiddenMenu = true;
          hiddenMenuCount = 0;
          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              hiddenMenu = false;
              hiddenMenuCount = 0;
            }
          }, 1000);
        } else {
          hiddenMenuCount++;
          if (hiddenMenuCount == 7) {

            String dialogText = "App Version Name= " + MyApplication.VersionName +
              "\n" + "App Version Code = " + MyApplication.VersionCode +
              "\n" + "Device_Code = " + UserManager.getDeviceCode() +
              "\n" + "Passenger_code = " + UserManager.getPassenger().getCode() +
              "\n" + "UDID = " + MyApplication.UDID +
              "\n" + "Model= " + MyApplication.Model;

            TextInputFragment textInputFragment = TextInputFragment.newInstance(ApiConstants.API_PATH,
              dialogText, "بستن", new OnTextEnteredListener() {
                @Override
                public void onSubmit(String s, SchoolService service) {
                  if (ValidatorHelper.isValidString(s)) {
                    ApiConstants.API_PATH = s;
                  }
                }
              });
            textInputFragment.setCancelable(false);
            textInputFragment.setHintAsText(true);
            textInputFragment.show(getSupportFragmentManager(), TextInputFragment.class.getSimpleName());
          }
        }
      }
    });

    String token = UserManager.getToken();
    String deviceCode = UserManager.getDeviceCode();
    String privateKey = UserManager.getPrivateApiKey();
    String cellphone = UserManager.getCellphone();
    if (ValidatorHelper.isValidString(cellphone)) {
      edtPhoneNumber.setText(cellphone);
    }
    if (ValidatorHelper.isValidString(privateKey) && ValidatorHelper.isValidString(deviceCode)) {
      if (ValidatorHelper.isValidString(token) & ValidatorHelper.isValidString(deviceCode)) {
        Log.i("PAY", "jaber1");
        sendPing(token, privateKey, deviceCode);
      } else {
        Log.i("PAY", "jaber2");
        loginUser(deviceCode, privateKey);
      }
    }

    lytEnter.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String enteredNumber = edtPhoneNumber.getText().toString();
        if (!ValidatorHelper.isValidString(enteredNumber)) {
          Toast.makeText(LoginActivity.this, "لطفا شماره موبایل خود را وارد کنید", Toast.LENGTH_SHORT).show();
          return;
        }
        if (!ValidatorHelper.isValidPhoneNumber(enteredNumber)) {
          Toast.makeText(LoginActivity.this, "لطفا شماره موبایل خود را به درستی وارد کنید", Toast.LENGTH_SHORT).show();
          return;
        }
        validateNumber(ValidatorHelper.standardizePhone(edtPhoneNumber.getText().toString()));
      }
    });


  }

  public void registerUser(String phone, final String deviceCode) {
    ApiCallerClass.with(this).hasLoading(true).call(new PingRequest(HashHelper.Calculate_Password(phone, ApiConstants.DEFAULT_PRIVATE_KEY)
        , true, true, phone, "-1")
      , new ApiCallerClass.CallerOnResponse() {
        @Override
        public void onResponse(AbstractResponse result, JSONObject rawResponse) {
          lytPassword.setVisibility(View.VISIBLE);
          edtPhoneNumber.setEnabled(false);
          ErrorDialog errorDialog = new ErrorDialog(LoginActivity.this, "یک کد امنیتی برای شما اس ام اس شد.\n لطفا این کد را وارد نمایید");
          errorDialog.show();

          lytEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String enteredPass = edtPassword.getText().toString();
              if (!ValidatorHelper.isValidString(enteredPass)) {
                Toast.makeText(LoginActivity.this, "لطفا کد امنیتی خود را وارد کنید", Toast.LENGTH_SHORT).show();
                return;
              }
              registerDevice(edtPassword.getText().toString(), deviceCode);
            }
          });
        }

        @Override
        public void onError(String message, int code) {
          ErrorDialog errorDialog = new ErrorDialog(LoginActivity.this, message);
          errorDialog.show();
        }
      }, "");
  }

  public void registerDevice(final String privateKey, final String deviceCode) {
    //Log.i("PAY", "jaber3 " + MyApplication.code);
    ApiCallerClass.with(this).hasLoading(true).call(
      new RegisterDeviceRequest(HashHelper.Calculate_Password(privateKey, ApiConstants.DEFAULT_PRIVATE_KEY),
        privateKey, deviceCode, MyApplication.Model, MyApplication.API_VERSION, MyApplication.VersionCode + "")
      , new ApiCallerClass.CallerOnResponse() {
        @Override
        public void onResponse(AbstractResponse result, JSONObject rawResponse) {
          loginUser(deviceCode, privateKey);
          UserManager.setPrivateApiKey(privateKey);
          UserManager.setLastFCMToken("");
        }

        @Override
        public void onError(String message, int code) {
          ErrorDialog errorDialog = new ErrorDialog(LoginActivity.this, message);
          errorDialog.show();
        }
      }, "");
  }

  public void validateNumber(final String phone) {
    //UserManager.setWellcome("0");
    //UserManager.setDeterminLocation("0");

    ApiCallerClass.with(this).hasLoading(true).call(new PingRequest(HashHelper.Calculate_Password(phone, ApiConstants.DEFAULT_PRIVATE_KEY)
        , false, true, phone, "-1")
      , new ApiCallerClass.CallerOnResponse() {
        @Override
        public void onResponse(AbstractResponse result, JSONObject rawResponse) {
          PingResponse pingResponse = (PingResponse) result;
          if (pingResponse.getData().getRegister_Type() == PingResponse.REGISTER_TYPE_SERVER_TO_CLIENT) {

            if (pingResponse.getData() != null && ValidatorHelper.isValidString(pingResponse.getData().getDevice_Code())) {
              UserManager.setDeviceCode(pingResponse.getData().getDevice_Code());
              registerUser(phone, pingResponse.getData().getDevice_Code());
            }
          } else if (pingResponse.getData().getRegister_Type() == PingResponse.REGISTER_TYPE_CLIENT_TO_SERVER) {
            initDownStreamRegister(phone, DownStreamRegisterRequest.MODE_INIT);
          }
        }

        @Override
        public void onError(String message, int code) {
          ErrorDialog errorDialog = new ErrorDialog(LoginActivity.this, message);
          errorDialog.show();
        }
      }, "");
  }

  public void initDownStreamRegister(final String cellphone, final int mode) {
    ApiCallerClass.with(this).hasLoading(true).call(new DownStreamRegisterRequest(HashHelper.Calculate_Password(cellphone, ApiConstants.DEFAULT_PRIVATE_KEY)
      , mode, cellphone), new ApiCallerClass.CallerOnResponse() {
      @Override
      public void onResponse(AbstractResponse result, JSONObject rawResponse) {
        final DownStreamRegisterResponse downStreamRegisterResponse = (DownStreamRegisterResponse) result;
        if (mode == DownStreamRegisterRequest.MODE_INIT) {
          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              initDownStreamRegister(cellphone, DownStreamRegisterRequest.MODE_CHECK);
            }
          }, downStreamRegisterResponse.getData().getInterval_2_Call_Back());

          UserManager.setDeviceCode(downStreamRegisterResponse.getData().getDevice_Code());
          ErrorDialog errorDialog = new ErrorDialog(LoginActivity.this, "لطفا یک SMS به شماره " +
            downStreamRegisterResponse.getData().getSMS_Center_No() + " ارسال کنید", "ارسال");

          errorDialog.show();
          errorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
              IntentHelper.sms(LoginActivity.this, downStreamRegisterResponse.getData().getSMS_Center_No(),
                "اعتبار سنجی");

              waitingDialog = new WaitingDialog(LoginActivity.this
                , "لظفا تا اعلام نتایج منتظر بمانید. \n این مرحله ممکن است تا چند دقیقه به طول بیانجامد");
              waitingDialog.show();
            }
          });
        } else if (mode == DownStreamRegisterRequest.MODE_CHECK) {
          if (downStreamRegisterResponse.getData().getPrivate_Auth_Key() > 0) {
            registerDevice(downStreamRegisterResponse.getData().getPrivate_Auth_Key() + "", UserManager.getDeviceCode());
          } else if (downStreamRegisterResponse.getData().getInterval_2_Call_Back() > 0) {
            new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                initDownStreamRegister(cellphone, DownStreamRegisterRequest.MODE_CHECK);
              }
            }, downStreamRegisterResponse.getData().getInterval_2_Call_Back());
          } else {
            if (waitingDialog != null) {
              waitingDialog.dismiss();
            }
            ErrorDialog errorDialog = new ErrorDialog(LoginActivity.this, "خطا\n در خواست شما منقضی شده است");
            errorDialog.show();
          }
        }
      }

      @Override
      public void onError(String message, int code) {
        ErrorDialog errorDialog = new ErrorDialog(LoginActivity.this, message);
        errorDialog.show();
        errorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
          @Override
          public void onDismiss(DialogInterface dialog) {
            if (waitingDialog != null) {
              waitingDialog.dismiss();
            }
          }
        });
      }
    }, "");
  }


  public void sendPing(final String token, final String privateKey, final String deviceCode) {
    ApiCallerClass.with(this).hasLoading(true).call(new PingRequest(HashHelper.Calculate_Password(privateKey, token)
        , false, true, "", deviceCode)
      , new ApiCallerClass.CallerOnResponse() {
        @Override
        public void onResponse(AbstractResponse result, JSONObject rawResponse) {
          PingResponse pingResponse = (PingResponse) result;
          if (checkVersion(pingResponse.getData().getApp_Force_Version(), pingResponse.getData().getApp_Nonforce_Version(), ApiConstants.API_UPDATE_LINK + "?Password=" + HashHelper.Calculate_Password(privateKey, token) + "&Device_Type=1&Device_Code=" + deviceCode, new Runnable() {
              @Override
              public void run() {
                getUserInfo(privateKey, deviceCode, token);
              }
            })) {
            getUserInfo(privateKey, deviceCode, token);
          }
        }

        @Override
        public void onError(String message, int code) {
          ErrorDialog errorDialog = new ErrorDialog(LoginActivity.this, message);
          errorDialog.show();
        }
      }, "");
  }

  public void loginUser(final String deviceCode, final String privateKey) {
    //Log.i("PAY", "jaber4 " + MyApplication.code);
    ApiCallerClass.with(this).hasLoading(true).call(new LoginRequest(HashHelper.Calculate_Password(privateKey, ApiConstants.DEFAULT_PRIVATE_KEY), deviceCode)
      , new ApiCallerClass.CallerOnResponse() {
        @Override
        public void onResponse(AbstractResponse result, JSONObject rawResponse) {
          final LoginResponse loginResponse = (LoginResponse) result;
          if (loginResponse.getData().getDevice_Type() != 1) {
            ErrorDialog errorDialog = new ErrorDialog(LoginActivity.this, "حساب کاربری شما مجاز به استفاده از این اپلیکیشن نمی باشد.");
            errorDialog.show();
            errorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
              @Override
              public void onDismiss(DialogInterface dialog) {
                finishWithAnimation();
              }
            });
            return;
          }

          UserManager.setToken(loginResponse.getData().getAuth_Key());
          UserManager.setDeviceCode(loginResponse.getData().getDevice_Code() + "");

    //Log.i("PAY", "jaber99 " + loginResponse.getData().getAuth_Key());

          if (checkVersion(loginResponse.getData().getApp_Force_Version(), loginResponse.getData().getApp_Nonforce_Version(), ApiConstants.API_UPDATE_LINK + "?Password=" + HashHelper.Calculate_Password(privateKey, loginResponse.getData().getAuth_Key()) + "&Device_Type=1&Device_Code=" + deviceCode, new Runnable() {
              @Override
              public void run() {
                getUserInfo(privateKey, deviceCode, loginResponse.getData().getAuth_Key());
              }
            })) {
            getUserInfo(privateKey, deviceCode, loginResponse.getData().getAuth_Key());
          }

        }


        @Override
        public void onError(String message, int code) {
          //Log.i("PAY", "jaber 5 " + MyApplication.code);
          if(code==-11) {
            final PayDialog  errorDialog = new PayDialog(LoginActivity.this, "والدین گرامی برای استفاده از امکانات برنامه ادوسا، می بایست ابتدا نسبت به عملیات پرداخت اقدام نمایید.");
            errorDialog.show();
            errorDialog.btnErrorDialog.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                String url = MyApplication.url;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                errorDialog.dismiss();
                //android.os.Process.killProcess(android.os.Process.myPid());
                //finish();
              }
            });
          }else{
            UserManager.setCellphone("");
            UserManager.setPrivateApiKey("");
            UserManager.setDeviceCode("");
            ErrorDialog errorDialog = new ErrorDialog(LoginActivity.this, message);
            errorDialog.show();
            errorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
              @Override
              public void onDismiss(DialogInterface dialog) {

                finishWithAnimation();
              }
            });
          }

          /*
          ErrorDialog errorDialog = new ErrorDialog(LoginActivity.this, message);
          errorDialog.show();

          errorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

              finishWithAnimation();
            }
          });
          */

        }
      }, "");

  }

  public void getUserInfo(String privateKey, String deviceCode, String authKey) {
    ApiCallerClass.with(this).hasLoading(true).call(new PassengerInfoRequest(deviceCode, HashHelper.Calculate_Password(privateKey, authKey))
      , new ApiCallerClass.CallerOnResponse() {
        @Override
        public void onResponse(AbstractResponse result, JSONObject rawResponse) {
          IntentHelper.startActivityAndFinishThis(LoginActivity.this, MainActivity.class);
        }

        @Override
        public void onError(String message, int code) {
          if (code == ApiConstants.ERROR_CODE_GENERAL) {
            ErrorDialog errorDialog = new ErrorDialog(LoginActivity.this, "اطلاعات کاربری شما ناقص است.\n لطفا با پشتیبانی تماس بگیرید");
            errorDialog.show();
          } else {
            ErrorDialog errorDialog = new ErrorDialog(LoginActivity.this, message);
            errorDialog.show();
          }
        }
      }, "");
  }

  private boolean checkVersion(int minForce, int currentVersion, final String url, final Runnable runnable) {
    boolean hasUpdate = MyApplication.VersionCode < currentVersion;
    boolean forceUpdate = MyApplication.VersionCode < minForce;
    if (hasUpdate) {
      if (forceUpdate) {
        downloadUpdate(url);
      } else {
        final ConfirmDialog confirmDialog = new ConfirmDialog(this, "نصب",
          "باشه", "بروزرسانی \n نسخه جدید برنامه منتشر شد.", new ConfirmDialog.Function() {
          @Override
          public void onYes() {
            downloadUpdate(url);
          }

          @Override
          public void onNo() {
            runnable.run();
          }
        });
        confirmDialog.show();
      }
      return false;
    }
    return true;
  }

  private void downloadUpdate(final String url) {
    Log.i("DON", url);
    ErrorDialog errorDialog = new ErrorDialog(LoginActivity.this, "نسخه جديدي از اپليکشن هم اکنون قابل دانلود است، لطفا بعد از نصب برنامه را بسته و دوباره باز کنید" , "دريافت نسخه جديد");
    errorDialog.show();
    errorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
      @Override
      public void onDismiss(DialogInterface dialog) {
       /* Log.i("DON", url);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);*/

        final Context context = LoginActivity.this;
        final ProgressDialog mProgressDialog;
        // instantiate it within the onCreate method
        mProgressDialog = new ProgressDialog(LoginActivity.this);
        mProgressDialog.setMessage("در حال دریافت بروزرسانی");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        final AsyncTask<String, Integer, String> downloadTask = new AsyncTask<String, Integer, String>() {
          private PowerManager.WakeLock mWakeLock;
          @Override
          protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
              URL url = new URL(sUrl[0]);
              connection = (HttpURLConnection) url.openConnection();
              connection.setInstanceFollowRedirects(true);
              connection.connect();

              // expect HTTP 200 OK, so we don't mistakenly save error report
              // instead of the file
              int status = connection.getResponseCode();
              if (status != HttpURLConnection.HTTP_OK) {
                if (status == HttpURLConnection.HTTP_MOVED_TEMP
                  || status == HttpURLConnection.HTTP_MOVED_PERM
                  || status == HttpURLConnection.HTTP_SEE_OTHER) {
                  String newUrl = connection.getHeaderField("Location");
                  if (ValidatorHelper.isValidString(newUrl)) {
                    return "PredirectP" + newUrl;
                  }
                }

                return "Server returned HTTP " + connection.getResponseCode()
                  + " " + connection.getResponseMessage();
              }

              // this will be useful to display download percentage
              // might be -1: server did not report the length
              connection.setReadTimeout(120000);
              int fileLength = connection.getContentLength();

              // download the file
              input = connection.getInputStream();
              output = new FileOutputStream(updatePath);

              byte data[] = new byte[4096];
              long total = 0;
              int count;
              while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                  input.close();
                  return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                  publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
              }
            } catch (Exception e) {
              return e.toString();
            } finally {
              try {
                if (output != null)
                  output.close();
                if (input != null)
                  input.close();
              } catch (IOException ignored) {
              }

              if (connection != null)
                connection.disconnect();
            }
            return null;
          }

          @Override
          protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
              getClass().getName());
            mWakeLock.acquire();
            runOnUiThread(new Runnable() {
              @Override
              public void run() {
                mProgressDialog.show();
              }
            });
          }

          @Override
          protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);

          }

          @Override
          protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();

            if (result != null) {
              if (result.startsWith("PredirectP")) {
                downloadUpdate(result.replace("PredirectP", ""));
                return;
              }
              ErrorDialog errorDialog = new ErrorDialog(context, "خطا هنگام دانلود نسخه جدید");
              errorDialog.show();
              errorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                  downloadUpdate(url);
                }
              });
            } else {
              android.widget.Toast.makeText(context, "با موفقیت دانلود شد", android.widget.Toast.LENGTH_SHORT).show();
              Intent promptInstall = new Intent(Intent.ACTION_VIEW)
                .setDataAndType(Uri.parse("file:///" + updatePath),
                  "application/vnd.android.package-archive");
              startActivityForResult(promptInstall, 999);
            }
          }
        };
        downloadTask.execute(url);


      }
    });


  }




}
