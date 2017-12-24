package ir.edusa.parents.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.ArrayList;

import ir.edusa.parents.Helpers.Toast;
import ir.edusa.parents.Helpers.ValidatorHelper;
import ir.edusa.parents.Interfaces.OnServiceSelected;
import ir.edusa.parents.Interfaces.OnTextEnteredListener;
import ir.edusa.parents.Managers.UserManager;
import ir.edusa.parents.Models.SchoolService;
import ir.edusa.parents.R;

import fontdroid.EditText;
import fontdroid.TextView;

public class TextInputFragment extends DialogFragment {

    private OnTextEnteredListener onTextEnteredListener;
    private String hint;
    private String description;
    private String buttonText;
    private SchoolService selectedService;
    private boolean isMandatory = false;
    private boolean hintAsText = false;
    private boolean needsServiceChooser = false;

    public TextInputFragment() {
    }

    public static TextInputFragment newInstance(String hint, String description, String buttonText, OnTextEnteredListener onTextEnteredListener) {
        TextInputFragment addressListDialogFragment = new TextInputFragment();
        addressListDialogFragment.init(hint, description, buttonText, onTextEnteredListener);
        return addressListDialogFragment;
    }

    public void init(String hint, String description, String buttonText, OnTextEnteredListener onTextEnteredListener) {
        this.hint = hint;
        this.buttonText = buttonText;
        this.description = description;
        this.onTextEnteredListener = onTextEnteredListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        getDialog().getWindow().setLayout(
//                getResources().getDisplayMetrics().widthPixels,
//                getResources().getDisplayMetrics().heightPixels
//        );
        View view = inflater.inflate(R.layout.fragment_text_input, null);
        return view;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }

    public boolean isHintAsText() {
        return hintAsText;
    }

    public void setHintAsText(boolean hintAsText) {
        this.hintAsText = hintAsText;
    }

    public boolean isNeedsServiceChooser() {
        return needsServiceChooser;
    }

    public void setNeedsServiceChooser(boolean needsServiceChooser) {
        this.needsServiceChooser = needsServiceChooser;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView txtDescription =(TextView) view.findViewById(R.id.txtDescription);
        TextView txtAccept =(TextView) view.findViewById(R.id.txtAccept);
        final TextView txtDriverName =(TextView) view.findViewById(R.id.txtDriverName);
        final EditText edtText = (EditText)view.findViewById(R.id.edtText);
        ViewGroup lytAccept =(ViewGroup) view.findViewById(R.id.lytAccept);
        if (needsServiceChooser) {
            final ArrayList<SchoolService> services = UserManager.getPassenger().getServices();
            if (services.size() == 0) {
                Toast.makeText(getContext(), "سرویسی به شما اختصاص نیافته است", Toast.LENGTH_SHORT).show();
                dismiss();
                return;
            }

            selectedService = services.get(0);
            if (services.size() > 1) {
                txtDriverName.setVisibility(View.VISIBLE);
                txtDriverName.setText("مربوط به راننده : " + selectedService.getDriverFullName());
                txtDriverName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ServiceChooserFragment chooserFragment = ServiceChooserFragment.newInstance(new OnServiceSelected() {
                            @Override
                            public void onItemSelect(SchoolService service) {
                                selectedService = service;
                                txtDriverName.setText("مربوط به راننده : " + selectedService.getDriverFullName());

                            }
                        }, services);
                        chooserFragment.show(getFragmentManager(), ServiceChooserFragment.class.getSimpleName());
                    }
                });
            } else {
                txtDriverName.setVisibility(View.GONE);
            }
        }
        txtDescription.setText(description);
        edtText.setHint(hint);
        if (hintAsText) {
            edtText.setText(hint);
        }
        txtAccept.setText(buttonText);
        lytAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMandatory && !ValidatorHelper.isValidString(edtText.getText().toString())) {
                    Toast.makeText(getContext(), "لطفا متن مورد نطر را وارد کنید", Toast.LENGTH_SHORT).show();
                    return;
                }
                onTextEnteredListener.onSubmit(edtText.getText().toString(), selectedService);
                dismiss();
            }
        });

    }


}
