package ir.edusa.parents.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


import fontdroid.Button;
import fontdroid.TextView;
import ir.edusa.parents.R;


public class ConfirmDialog extends Dialog {

    private String positiveButton = "";
    private String negativeButton = "";
    private String dialogText;
    private TextView txtErrorDialogText;
    private Button btnNo;
    private Button btnYes;
    private Function function;

    public ConfirmDialog(Context context, String positiveButton, String negativeButton, String dialogText, Function function) {
        super(context);
        this.positiveButton = positiveButton;
        this.negativeButton = negativeButton;
        this.dialogText = dialogText;
        this.function = function;
    }


    public ConfirmDialog(Activity context, String dialogText, Function function) {
        super(context);
        this.dialogText = dialogText;
        this.function = function;
    }

    public interface Function {
        void onYes();

        void onNo();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.confirm_dialog);
        setCancelable(false);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtErrorDialogText = (TextView) findViewById(R.id.txtErrorDialogText);
        btnNo = (Button) findViewById(R.id.btnNo);
        btnYes = (Button) findViewById(R.id.btnYes);

        if (positiveButton.length() > 0 || negativeButton.length() > 0) {
            btnYes.setText(positiveButton);
            btnNo.setText(negativeButton);
        }
        txtErrorDialogText.setText(dialogText);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                function.onNo();
                dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                function.onYes();
                dismiss();
            }
        });
    }
}
