package ir.edusa.parents.Fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioGroup;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.ArrayList;

import fontdroid.TextView;
import ir.edusa.parents.Helpers.DateHelper;
import ir.edusa.parents.Helpers.Toast;
import ir.edusa.parents.Helpers.ValidatorHelper;
import ir.edusa.parents.Interfaces.OnAbsenceDatePickedListener;
import ir.edusa.parents.Interfaces.OnServiceSelected;
import ir.edusa.parents.Managers.UserManager;
import ir.edusa.parents.Models.SchoolService;
import ir.edusa.parents.Network.Requests.PassengerAbsenceRequest;
import ir.edusa.parents.R;

public class AbsenceAnnouncementFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private OnAbsenceDatePickedListener onDatePickedListener;
    private TextView txtDate;
    private TextView txtDriverName;
    private String selectedDate;
    private SchoolService selectedService;

    public static AbsenceAnnouncementFragment newInstance(OnAbsenceDatePickedListener onDatePickedListener) {
        AbsenceAnnouncementFragment addressListDialogFragment = new AbsenceAnnouncementFragment();
        addressListDialogFragment.init(onDatePickedListener);
        return addressListDialogFragment;
    }

    public void init(OnAbsenceDatePickedListener onDatePickedListener) {

        this.onDatePickedListener = onDatePickedListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        getDialog().getWindow().setLayout(
//                getResources().getDisplayMetrics().widthPixels,
//                getResources().getDisplayMetrics().heightPixels
//        );
        View view = inflater.inflate(R.layout.fragment_absence_announcement, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup lytAccept =(ViewGroup) view.findViewById(R.id.lytAccept);
        final RadioGroup radioGroup =(RadioGroup) view.findViewById(R.id.radioGroup);
        txtDate =(TextView) view.findViewById(R.id.txtDate);
        txtDriverName =(TextView) view.findViewById(R.id.txtDriverName);


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
        PersianCalendar persianCalendar = new PersianCalendar();
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                AbsenceAnnouncementFragment.this,
                persianCalendar.getPersianYear(),
                persianCalendar.getPersianMonth(),
                persianCalendar.getPersianDay()
        );
        datePickerDialog.setMinDate(persianCalendar);
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show(getActivity().getFragmentManager(), "DatePickerFragment");
            }
        });
        lytAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ValidatorHelper.isValidString(selectedDate)) {
                    Toast.makeText(getContext(), "لطفا تاریخ مورد نظر خود را انتخاب نمایید", Toast.LENGTH_SHORT).show();
                    return;
                }
                int direction = -10;
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rdiBoth:
                        direction = PassengerAbsenceRequest.DIRECTION_BOTH;
                        break;
                    case R.id.rdiFirst:
                        direction = PassengerAbsenceRequest.DIRECTION_PICKING;
                        break;
                    case R.id.rdiSecond:
                        direction = PassengerAbsenceRequest.DIRECTION_DROPPING;
                        break;
                    default:
                        Toast.makeText(getContext(), "لطفا مسیر مورد نظر خود را انتخاب نمایید", Toast.LENGTH_SHORT).show();
                        return;
                }
                onDatePickedListener.onPick(selectedDate, direction, selectedService);
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear++;
        txtDate.setText(year + "/" + DateHelper.increaseToTwoDigit(monthOfYear + "") + "/" + DateHelper.increaseToTwoDigit(dayOfMonth + ""));
        selectedDate = year + "" + DateHelper.increaseToTwoDigit(monthOfYear + "") + DateHelper.increaseToTwoDigit(dayOfMonth + "");
    }
}
