package ir.edusa.parents.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;


import ir.edusa.parents.R;

import fontdroid.TextView;


public class WaitingDialog extends Dialog {
    Context ctx;
    private TextView txtErrorDialogText;
    private String text;

    public WaitingDialog(Context context, String text) {
        super(context);
        ctx = context;
        this.text = text;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_waiting);
        txtErrorDialogText = (TextView) findViewById(R.id.txtErrorDialogText);
        txtErrorDialogText.setText(text);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
