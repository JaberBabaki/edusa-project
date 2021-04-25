package ir.edusa.parents.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.Date;

import ir.edusa.parents.Dialogs.ContactDialog;
import ir.edusa.parents.Dialogs.ErrorDialog;
import ir.edusa.parents.Dialogs.SelectDialog;
import ir.edusa.parents.Fragments.AbsenceAnnouncementFragment;
import ir.edusa.parents.Helpers.HashHelper;
import ir.edusa.parents.Helpers.IntentHelper;
import ir.edusa.parents.Helpers.Toast;
import ir.edusa.parents.Helpers.ValidatorHelper;
import ir.edusa.parents.Interfaces.OnAbsenceDatePickedListener;
import ir.edusa.parents.Managers.UserManager;
import ir.edusa.parents.Models.Command;
import ir.edusa.parents.Models.CommandMessage;
import ir.edusa.parents.Models.Message;
import ir.edusa.parents.Models.Passenger;
import ir.edusa.parents.Models.SchoolService;
import ir.edusa.parents.Network.ApiCallerClass;
import ir.edusa.parents.Network.Requests.CommandACKRequest;
import ir.edusa.parents.Network.Requests.GetCommandsInQueueRequest;
import ir.edusa.parents.Network.Requests.PassengerAbsenceRequest;
import ir.edusa.parents.Network.Requests.SetFCMTokenRequest;
import ir.edusa.parents.Network.Responses.AbstractResponse;
import ir.edusa.parents.Network.Responses.CommandQueueResponse;
import ir.edusa.parents.R;
import ir.edusa.parents.Utils.FCM.MyFirebaseMessagingService;
import ir.edusa.parents.Utils.Log;

public class MainActivity extends BaseActivity {

  private ViewGroup circleProfile;
  private ViewGroup circleMap;
  private ViewGroup circleAlarmPints;
  private ViewGroup circleAnnounceAbsence;
  private ViewGroup circleSendMessage;
  private ImageView contactUs;

  @Override
  protected String getPageTitle() {
    return "خانه";
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Log.i("WEL", "" + UserManager.getWellcome());
    if ("0".equals(UserManager.getWellcome())) {
      final SelectDialog errorDialog = new SelectDialog(MainActivity.this, "باسلام \n والدين گرامي به اٍدوسا خوش آمديد از اينکه عضوي از خانواده بزرگ Edusa هستيد بسيار خرسنديم. لطفا جهت تاييد مشخصات خود و همچنين تعيين موقعيت مکاني دقيق فرزند دلبندتان به قسمت اطلاعات کاربري که در صفحه اصلي ديده مي شود برويد. همچنين در صورت داشتن هرگونه سوال و ابهام در نحوه استفاده از برنامه مي توانيد به بخش ارتباط با ما مراجعه کنيد. ");
      errorDialog.show();
      errorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
          UserManager.setWellcome("1");
          errorDialog.dismiss();
          IntentHelper.startActivity(MainActivity.this, ProfileActivity.class);
        }
      });
    }
    checkFCMToken();

    circleProfile = (ViewGroup) findViewById(R.id.circleProfile);
    circleAlarmPints = (ViewGroup) findViewById(R.id.circleAlarmPints);
    circleSendMessage = (ViewGroup) findViewById(R.id.circleSendMessage);
    circleAnnounceAbsence = (ViewGroup) findViewById(R.id.circleAnnounceAbsence);
    contactUs = (ImageView) findViewById(R.id.contact_us);
    circleMap = (ViewGroup) findViewById(R.id.circleMap);
    ViewGroup layMain = (ViewGroup) findViewById(R.id.lay_first);
    ViewGroup laySecond = (ViewGroup) findViewById(R.id.lay_ok);
    if (android.os.Build.VERSION.SDK_INT <= 15) {
      layMain.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }else{
      layMain.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
      laySecond.setBackgroundColor(R.drawable.sliding_menu_bg);

    }


    circleProfile.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        IntentHelper.startActivity(MainActivity.this, ProfileActivity.class);
      }
    });
    circleAlarmPints.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        IntentHelper.startActivity(MainActivity.this, AlarmPointsActivity.class);
      }
    });
    contactUs.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final ContactDialog errorDialog = new ContactDialog(MainActivity.this);
        errorDialog.show();
        errorDialog.layCall.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            //String phone = "09385476103";
            //Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            //startActivity(intent);
          }
        });
        errorDialog.layNotific.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/SmartSchoolService"));
            startActivity(intent);
          }
        });
        errorDialog.layAnswer.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/joinchat/BYR2v0wGSmVdbdNAXCowcA"));
            startActivity(intent);
          }
        });
        errorDialog.layEmail.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
              "mailto", "ems.edusa.school@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "پشتيباني");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
          }
        });
        errorDialog.layTehran.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            String url = "https://www.tehran.edusa.ir";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
          }
        });
                /*errorDialog.layAmirkabir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "http://www.aut.ac.ir";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });*/


        errorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
          @Override
          public void onDismiss(DialogInterface dialog) {
            errorDialog.dismiss();
          }
        });
      }
    });
    circleMap.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        IntentHelper.startActivity(MainActivity.this, MapActivity.class);
      }
    });
    circleSendMessage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        IntentHelper.startActivity(MainActivity.this, InboxActivity.class);
      }
    });
    circleAnnounceAbsence.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.i("ABS", "jaber3  " + UserManager.getPassenger().getServices().size());
        if (!(UserManager.getPassenger().getServices().size() == 0)) {
          final AbsenceAnnouncementFragment absenceAnnouncementFragment = AbsenceAnnouncementFragment.newInstance(new OnAbsenceDatePickedListener() {
            @Override
            public void onPick(String date, int direction, SchoolService service) {
              sendAbsenceReport(service, date, direction);
            }
          });
          absenceAnnouncementFragment.show(getSupportFragmentManager(), AbsenceAnnouncementFragment.class.getSimpleName());
        } else {
          Toast.makeText(MainActivity.this,"هنوز سرویسی به شما اختصاص نیافته",Toast.LENGTH_LONG).show();
        }

      }
    });

    checkCommandsInQueue();
  }

  public void sendAbsenceReport(SchoolService service, String date, int direction) {
    Log.i("ABS", "jaber3");
    Passenger passenger = UserManager.getPassenger();
    ApiCallerClass.with(this).hasLoading(true).call(new PassengerAbsenceRequest(HashHelper.Calculate_Password(UserManager.getPrivateApiKey(), UserManager.getToken()),
      UserManager.getDeviceCode(), passenger.getCode(), service.getService_Code(), service.getDriver_Code(), date, direction), new ApiCallerClass.CallerOnResponse() {
      @Override
      public void onResponse(AbstractResponse result, JSONObject rawResponse) {
        Log.i("ABS", "jaber4");
        ErrorDialog errorDialog = new ErrorDialog(MainActivity.this, "اعلام غیبت شما با موفقیت ثبت شد \n و به اطلاع راننده خواهد رسید");
        Log.i("ABS", "jaber5");
        errorDialog.show();
        errorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
          @Override
          public void onDismiss(DialogInterface dialog) {
            Log.i("ABS", "jaber6");
            DialogFragment fragment = (DialogFragment) getSupportFragmentManager().findFragmentByTag(AbsenceAnnouncementFragment.class.getSimpleName());
            if (fragment != null) {
              Log.i("ABS", "jaber7");
              fragment.dismissAllowingStateLoss();
            }
          }
        });
      }

      @Override
      public void onError(String message, int code) {
        ErrorDialog errorDialog = new ErrorDialog(MainActivity.this, message);
        errorDialog.show();
      }
    }, "");
  }


  public void checkFCMToken() {
    String currentToken = FirebaseInstanceId.getInstance().getToken();
    if (!ValidatorHelper.isValidString(currentToken)) {
      return;
    }
    String lastToken = UserManager.getLastFCMToken();
    if (!lastToken.equals(currentToken)) {
      sendRegistrationToServer(currentToken);
    }
  }

  private void sendRegistrationToServer(final String token) {
    String deviceCode = UserManager.getDeviceCode();
    if (!ValidatorHelper.isValidString(deviceCode)) {
      return;
    }
    ApiCallerClass.with(this).call(new SetFCMTokenRequest(deviceCode, token, HashHelper.Calculate_Password(UserManager.getPrivateApiKey(), UserManager.getToken())), new ApiCallerClass.CallerOnResponse() {
      @Override
      public void onResponse(AbstractResponse result, JSONObject rawResponse) {
        Log.e("FCM Registration", "Success");
        UserManager.setLastFCMToken(token);
      }

      @Override
      public void onError(String message, int code) {
        Log.e("FCM ERROR", message);
      }
    }, "");
  }

  public void checkCommandsInQueue() {
    ApiCallerClass.with(this).call(new GetCommandsInQueueRequest(HashHelper.Calculate_Password(UserManager.getPrivateApiKey()
      , UserManager.getToken()), UserManager.getDeviceCode()), new ApiCallerClass.CallerOnResponse() {
      @Override
      public void onResponse(AbstractResponse result, JSONObject rawResponse) {
        CommandQueueResponse queueResponse = (CommandQueueResponse) result;
        int ackNum = -1;
        for (Object object : queueResponse.getData().getCommands()) {
          Command command = (Command) object;
          ackNum = Math.max(ackNum, command.getCommand_Unique_Code());
          switch (command.getCommand_Code()) {
            case Command.COMMAND_CODE_MESSAGE:
            case Command.COMMAND_CODE_GROUP_MESSAGE:
              CommandMessage commandMessage = (CommandMessage) command;
              if (!ValidatorHelper.isValidString(commandMessage.getCommand_Data())) {
                break;
              }
              Message message = new Message();
              message.setMessage_Text(commandMessage.getCommand_Data());
              MyFirebaseMessagingService.sendNotificationToMessages(MainActivity.this
                , "پیام جدید", message.getMessage_Text(), (int) (Math.random() * 100000));
              message.setDate(new Date());
              message.setFromPanel(true);
              UserManager.addMessage(message);
              break;
          }
        }
        if (ackNum >= 0) {
          sendACK(ackNum);
        }
      }

      @Override
      public void onError(String message, int code) {
        Log.e("REQUEST_ERROR (GetCommandsInQueueRequest)", message);
      }
    }, "");
  }

  public void sendACK(int ackNum) {
    ApiCallerClass.with(this).call(new CommandACKRequest(HashHelper.Calculate_Password(UserManager.getPrivateApiKey()
      , UserManager.getToken()), UserManager.getDeviceCode(), ackNum), new ApiCallerClass.CallerOnResponse() {
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
