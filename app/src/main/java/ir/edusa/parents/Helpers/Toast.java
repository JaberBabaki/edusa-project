package ir.edusa.parents.Helpers;


import android.content.Context;
import android.support.annotation.IntDef;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import fontdroid.TextView;
import ir.edusa.parents.R;

public class Toast {

    private static Toast toast;
    private Context context;
    private String text;
    @Duration
    private int duration;

    public static final int LENGTH_SHORT = android.widget.Toast.LENGTH_SHORT;
    public static final int LENGTH_LONG = android.widget.Toast.LENGTH_LONG;

    @IntDef({LENGTH_SHORT, LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {
    }

    private Toast(Context context, String text, @Duration int duration) {
        this.context = context;
        this.text = text;
        this.duration = duration;
    }

    public static synchronized Toast makeText(Context context, String text, @Duration int duration) {
        toast = new Toast(context, text, duration);
        return toast;
    }

    //Show
    public void show() {
        //Inflate Layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_smart, null);

        //Initialize Ui Component
        LinearLayout lnlContainer = (LinearLayout) layout.findViewById(R.id.lnlContainer);
        TextView txtLabel = (TextView) layout.findViewById(R.id.txtLabel);

        //Set Label
        txtLabel.setText(text);

        //Initialize Toast
        android.widget.Toast toast = new android.widget.Toast(context);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }
}
