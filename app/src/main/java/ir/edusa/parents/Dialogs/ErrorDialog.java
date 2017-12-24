package ir.edusa.parents.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


import fontdroid.Button;
import fontdroid.TextView;
import ir.edusa.parents.R;


public class ErrorDialog extends Dialog {

    private String dialogText;
    private String buttonLabel;
    private fontdroid.TextView txtErrorDialogText;
    private fontdroid.Button btnErrorDialog;

    public ErrorDialog(Context context, String dialogText) {
        super(context);
        this.dialogText = dialogText;
        buttonLabel = "تایید";
    }

    public ErrorDialog(Context context, String dialogText, String buttonLabel) {
        super(context);
        this.dialogText = dialogText;
        this.buttonLabel = buttonLabel;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_error);
        setCancelable(false);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtErrorDialogText = (TextView) findViewById(R.id.txtErrorDialogText);
        btnErrorDialog = (Button) findViewById(R.id.btnErrorDialog);
        btnErrorDialog.setText(buttonLabel);

        txtErrorDialogText.setText(dialogText);
        btnErrorDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
