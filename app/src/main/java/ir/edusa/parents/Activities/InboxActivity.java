package ir.edusa.parents.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import fontdroid.TextView;
import ir.edusa.parents.Adapters.InboxAdapter;
import ir.edusa.parents.Dialogs.ErrorDialog;
import ir.edusa.parents.Fragments.TextInputFragment;
import ir.edusa.parents.Helpers.DateHelper;
import ir.edusa.parents.Helpers.HashHelper;
import ir.edusa.parents.Helpers.Toast;
import ir.edusa.parents.Helpers.ValidatorHelper;
import ir.edusa.parents.Interfaces.OnTextEnteredListener;
import ir.edusa.parents.Managers.UserManager;
import ir.edusa.parents.Models.Command;
import ir.edusa.parents.Models.CommandMessage;
import ir.edusa.parents.Models.Message;
import ir.edusa.parents.Models.Passenger;
import ir.edusa.parents.Models.SchoolService;
import ir.edusa.parents.Network.ApiCallerClass;
import ir.edusa.parents.Network.Requests.CommandACKRequest;
import ir.edusa.parents.Network.Requests.GetCommandsInQueueRequest;
import ir.edusa.parents.Network.Requests.SendMessageRequest;
import ir.edusa.parents.Network.Responses.AbstractResponse;
import ir.edusa.parents.Network.Responses.CommandQueueResponse;
import ir.edusa.parents.R;
import ir.edusa.parents.Utils.FCM.MyFirebaseMessagingService;
import ir.edusa.parents.Utils.Log;

public class InboxActivity extends BaseActivity {

  private RecyclerView rclMessages;
  private TextView txtNoResult;
  private ImageView imgNewMessage;
  private ArrayList<Message> messages;
  private InboxAdapter inboxAdapter;

  @Override
  protected void onResume() {
    super.onResume();
    Log.i("MES", "jaber1");
    //Is EventBus Register
    if (!EventBus.getDefault().isRegistered(this)) {
      //Register EventBus
      EventBus.getDefault().register(this);
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    //Un Register EventBus
    EventBus.getDefault().unregister(this);
  }


  @Override
  protected String getPageTitle() {
    return "ارسال/دریافت پشتیبانی";
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.i("MES", "jaber2");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_inbox);
    rclMessages = (RecyclerView) findViewById(R.id.rclMessages);
    txtNoResult = (TextView) findViewById(R.id.txtNoResult);
    imgNewMessage = (ImageView) findViewById(R.id.imgNewMessage);
    Log.i("ABS", "jaber3  " + UserManager.getPassenger().getServices().size());


    imgNewMessage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!(UserManager.getPassenger().getServices().size() == 0)) {
          Toast.makeText(InboxActivity.this, "jaber3", Toast.LENGTH_SHORT).show();
          TextInputFragment textInputFragment = TextInputFragment.newInstance("متن گزارش", "متن پیام خود را در این بخش وارد کنید.",
            "ارسال", new OnTextEnteredListener() {
              @Override
              public void onSubmit(String s, SchoolService service) {
                sendReport(s, service);
              }
            });
          textInputFragment.setMandatory(true);
          textInputFragment.setNeedsServiceChooser(true);
          textInputFragment.show(getSupportFragmentManager(), TextInputFragment.class.getSimpleName());

        } else{
          Toast.makeText(InboxActivity.this, "هنوز سرویسی به شما اختصاص نیافته", Toast.LENGTH_LONG).show();
        }
      }
    });
    checkCommandsInQueue();
  }

  public void showContent() {
    messages = UserManager.getMessages();

    rclMessages.setLayoutManager(new LinearLayoutManager(this));
    inboxAdapter = new InboxAdapter(this, messages);
    rclMessages.setAdapter(inboxAdapter);
    scrollToEnd();
    if (messages.size() > 0) {
      txtNoResult.setVisibility(View.GONE);
    } else {
      txtNoResult.setVisibility(View.VISIBLE);
    }
  }

  public void scrollToEnd() {
    rclMessages.post(new Runnable() {
      @Override
      public void run() {
        rclMessages.scrollToPosition(messages.size() - 1);
      }
    });
  }

  public void sendReport(final String report, SchoolService service) {
    Passenger passenger = UserManager.getPassenger();
    ApiCallerClass.with(this).hasLoading(true).call(new SendMessageRequest(HashHelper.Calculate_Password(UserManager.getPrivateApiKey(), UserManager.getToken()),
      UserManager.getDeviceCode(), passenger.getCode(), DateHelper.formatDate(new Date(), ""),
      DateHelper.formatTime(new Date(), true), SendMessageRequest.MESSAGE_CODE_MESSAGE_2_ADMIN_PANEL, report, "",
      service.getDriver_Code(), passenger.getOrganization().getCode()), new ApiCallerClass.CallerOnResponse() {
      @Override
      public void onResponse(AbstractResponse result, JSONObject rawResponse) {
        ErrorDialog errorDialog = new ErrorDialog(InboxActivity.this, "پیام شما با موفقیت ارسال شد");
        errorDialog.show();
        errorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
          @Override
          public void onDismiss(DialogInterface dialog) {
            Message message = new Message();
            message.setFromPanel(false);
            message.setDate(new Date());
            message.setMessage_Text(report);
            UserManager.addMessage(message);
            messages.add(message);
            inboxAdapter.notifyDataSetChanged();
            txtNoResult.setVisibility(View.GONE);
            scrollToEnd();
          }
        });
      }

      @Override
      public void onError(String message, int code) {
        ErrorDialog errorDialog = new ErrorDialog(InboxActivity.this, message);
        errorDialog.show();
      }
    }, "");
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageReceived(String command) {
    if (command.equals("NEW_MESSAGE")) {
      if (inboxAdapter == null) {
        return;
      }
      messages.clear();
      messages.addAll(UserManager.getMessages());
      inboxAdapter.notifyDataSetChanged();
      scrollToEnd();
    }
  }

  public void checkCommandsInQueue() {
    ApiCallerClass.with(this).hasLoading(true).call(new GetCommandsInQueueRequest(HashHelper.Calculate_Password(UserManager.getPrivateApiKey()
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
              MyFirebaseMessagingService.sendNotificationToMessages(InboxActivity.this
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
        showContent();
      }

      @Override
      public void onError(String message, int code) {
        Log.e("REQUEST_ERROR (GetCommandsInQueueRequest)", message);
        showContent();
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