package ir.edusa.parents.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import fontdroid.Button;
import fontdroid.TextView;
import ir.edusa.parents.R;


public class ContactDialog extends Dialog {

    private String dialogText;
    private String buttonLabel;
    private TextView txtErrorDialogText;
    private Button btnErrorDialog;

    public LinearLayout layCall;
    public LinearLayout layNotific;
    public LinearLayout layAnswer;
    public LinearLayout layEmail;
    public LinearLayout layManger;
    public LinearLayout layTehran;
    public LinearLayout layAmirkabir;

    public ContactDialog(Context context) {
        super(context);

        buttonLabel = "تاييد";
    }

    public ContactDialog(Context context, String dialogText, String buttonLabel) {
        super(context);
        this.dialogText = dialogText;
        this.buttonLabel = buttonLabel;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_contact);
        setCancelable(false);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtErrorDialogText = (TextView) findViewById(R.id.txtErrorDialogText);
        btnErrorDialog = (Button) findViewById(R.id.btnErrorDialog);
        btnErrorDialog.setText(buttonLabel);

        layCall=(LinearLayout)findViewById(R.id.lay_call);
        layNotific=(LinearLayout)findViewById(R.id.lay_notific);
        layAnswer=(LinearLayout)findViewById(R.id.lay_answer);
        layEmail=(LinearLayout)findViewById(R.id.lay_email);
        //layManger=(LinearLayout)findViewById(R.id.lay_manger);
        layTehran=(LinearLayout)findViewById(R.id.lay_tehran_edusa);
       //layAmirkabir=(LinearLayout)findViewById(R.id.lay_amir_kabir);

        //txtErrorDialogText.setText(dialogText);
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
