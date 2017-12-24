package ir.edusa.parents.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import ir.edusa.parents.R;


public class IntentHelper {

    //Hide Constructor
    private IntentHelper() {

    }

    //Start Activity
    public static void startActivity(Activity currentActivity, Class destinationActivity) {
        Intent intent = new Intent(currentActivity, destinationActivity);
        currentActivity.startActivity(intent);
        currentActivity.overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_right);
    }

    //Start Activity
    public static void startActivityAndFinishThis(Activity currentActivity, Class destinationActivity) {
        Intent intent = new Intent(currentActivity, destinationActivity);
        currentActivity.startActivity(intent);
        currentActivity.overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_right);
        currentActivity.finish();
    }

    //Start Activity
    public static void startActivityAndFinishThis(final Activity currentActivity, final Class destinationActivity, int delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(currentActivity, destinationActivity);
                currentActivity.startActivity(intent);
                currentActivity.overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_right);
                currentActivity.finish();
            }
        }, delay);

    }

    //Start Activity
    public static void startActivityAndFinishThis(Activity currentActivity, Class destinationActivity, Bundle bundle) {
        Intent intent = new Intent(currentActivity, destinationActivity);
        intent.putExtras(bundle);
        currentActivity.startActivity(intent);
        currentActivity.overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_right);
        currentActivity.finish();
    }

    //Start Activity
    public static void startActivity(Activity currentActivity, Class destinationActivity, Bundle bundle) {
        Intent intent = new Intent(currentActivity, destinationActivity);
        intent.putExtras(bundle);
        currentActivity.startActivity(intent);
        currentActivity.overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_right);
    }

    //Start Activity
    public static void startActivity(Activity currentActivity, Class destinationActivity, Bundle bundle, int flag) {
        Intent intent = new Intent(currentActivity, destinationActivity);
        intent.putExtras(bundle);
        intent.setFlags(flag);
        currentActivity.startActivity(intent);
        currentActivity.overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_right);
    }

    //Start Activity
    public static void startActivity(Activity currentActivity, Class destinationActivity, int flag) {
        Intent intent = new Intent(currentActivity, destinationActivity);
        intent.setFlags(flag);
        currentActivity.startActivity(intent);
        currentActivity.overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_right);
    }

    //Start Activity
    public static void startActivityForResult(Activity currentActivity, Class destinationActivity, int requestCode) {
        Intent intent = new Intent(currentActivity, destinationActivity);
        currentActivity.startActivityForResult(intent, requestCode);
        currentActivity.overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_right);
    }

    //Start Activity
    public static void startActivityForResultFromBottom(Activity currentActivity, Class destinationActivity, int requestCode) {
        Intent intent = new Intent(currentActivity, destinationActivity);
        currentActivity.startActivityForResult(intent, requestCode);
        currentActivity.overridePendingTransition(R.anim.slide_in_down_to_up, R.anim.slide_out_up_to_down);
    }

    //Start Activity
    public static void startActivityForResult(Fragment fragment, Class destinationActivity, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), destinationActivity);
        fragment.startActivityForResult(intent, requestCode);
    }

    //Start Activity
    public static void startActivityForResult(Activity currentActivity, Class destinationActivity, int requestCode, Bundle bundle) {
        Intent intent = new Intent(currentActivity, destinationActivity);
        intent.putExtras(bundle);
        currentActivity.startActivityForResult(intent, requestCode);
        currentActivity.overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_right);
    }


    //Finish Activity With Animation
    public static void finishActivityWithAnimation(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.slide_in_right_slow, R.anim.slide_out_right_to_left);
    }

    //Finish Activity Result With Animation
    public static void finishActivityResultWithAnimation(Activity activity) {
        activity.setResult(Activity.RESULT_OK);
        activity.finish();
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    //Finish Activity Result With Animation
    public static void finishActivityResultWithAnimation(Activity activity, Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    //Finish Activity Result With Animation
    public static void finishActivityResultWithAnimation(Activity activity, Intent data) {
        activity.setResult(Activity.RESULT_OK, data);
        activity.finish();
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    //Finish Activity Result With Animation
    public static void finishActivityResultWithAnimationBottom(Activity activity) {
        activity.setResult(Activity.RESULT_OK);
        activity.finish();
        activity.overridePendingTransition(R.anim.slide_in_down_to_up, R.anim.slide_out_up_to_down);
    }

    //Call
    public static void call(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + Uri.encode(phoneNumber.trim())));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public static void sms(Context context, String phoneNumber, String text) {
        Uri sms_uri = Uri.parse("smsto:" + Uri.encode(phoneNumber.trim()));
        Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
        sms_intent.putExtra("sms_body", text);
        context.startActivity(sms_intent);
    }
    //Update Application
    public static void updateApplication(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW
                , Uri.parse("market://details?id=" + "com.zoodfood.android"));

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    //Finish And Clear Top
    public static void startActivityAndFinishThisByClearTop(Activity currentActivity, Class destinationActivity) {
        Intent intent = new Intent(currentActivity, destinationActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        currentActivity.startActivity(intent);
        currentActivity.overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_right);
        currentActivity.finish();
    }

    //Send DeepLink
    public static void sendDeepLink(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.setPackage(context.getPackageName());
        context.startActivity(intent);
    }

    //Send DeepLink
    public static void sendDeepLink(Activity currentActivity, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.setPackage(currentActivity.getPackageName());
        currentActivity.startActivity(intent);
        currentActivity.overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_right);
    }

    //Share
    public static void share(Context context, String body, String subject) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(sharingIntent, "اشتراک گذاری با : "));
    }
}
