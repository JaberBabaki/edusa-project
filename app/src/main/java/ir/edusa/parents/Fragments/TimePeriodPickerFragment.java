package ir.edusa.parents.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianDateParser;

import java.util.Date;

import ir.edusa.parents.Helpers.DateHelper;
import ir.edusa.parents.Helpers.Toast;
import ir.edusa.parents.Interfaces.OnDriverRouteTimePicked;
import ir.edusa.parents.R;
import ir.edusa.parents.Utils.RTextView;

public class TimePeriodPickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private OnDriverRouteTimePicked onDriverRouteTimePicked;
    private Date date;
    private int timeFromH;
    private int timeFromM;
    private int timeToH;
    private int timeToM;
    private RTextView txtDate;

    public TimePeriodPickerFragment() {
    }

    public static TimePeriodPickerFragment newInstance(Date date, int timeFromH, int timeFromM, int timeToH, int timeToM, OnDriverRouteTimePicked onDriverRouteTimePicked) {
        TimePeriodPickerFragment addressListDialogFragment = new TimePeriodPickerFragment();
        addressListDialogFragment.init(date, timeFromH, timeFromM, timeToH, timeToM, onDriverRouteTimePicked);
        return addressListDialogFragment;
    }

    public void init(Date date, int timeFromH, int timeFromM, int timeToH, int timeToM, OnDriverRouteTimePicked onDriverRouteTimePicked) {
        this.date = new Date(date.getTime());
        this.timeFromH = timeFromH;
        this.timeFromM = timeFromM;
        this.timeToH = timeToH;
        this.timeToM = timeToM;
        this.onDriverRouteTimePicked = onDriverRouteTimePicked;
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
        View view = inflater.inflate(R.layout.fragment_time_period_picker, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup lytDate =(ViewGroup) view.findViewById(R.id.lytDate);
        txtDate =(RTextView) view.findViewById(R.id.txtDate);
        ViewGroup lytTimeFrom =(ViewGroup) view.findViewById(R.id.lytTimeFrom);
        ViewGroup lytTimeTo =(ViewGroup) view.findViewById(R.id.lytTimeTo);
        ViewGroup lytAccept =(ViewGroup) view.findViewById(R.id.lytAccept);
        final RTextView txtTimeTo =(RTextView) view.findViewById(R.id.txtTimeTo);
        final RTextView txtTimeFrom =(RTextView) view.findViewById(R.id.txtTimeFrom);

        PersianCalendar persianCalendar = new PersianCalendar(date.getTime());
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                persianCalendar.getPersianYear(),
                persianCalendar.getPersianMonth(),
                persianCalendar.getPersianDay()
        );
        PersianCalendar minDate = new PersianCalendar(date.getTime());
        minDate.setPersianDate(minDate.getPersianYear(), (minDate.getPersianMonth() - 1), minDate.getPersianDay());
        datePickerDialog.setMinDate(minDate);

//        PersianCalendar maxDate = new PersianCalendar(new Date().getTime());
//        datePickerDialog.setMaxDate(maxDate);
        final TimePickerDialog timePickerFrom = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                timeFromH = hourOfDay;
                timeFromM = minute;
                txtTimeFrom.setText(DateHelper.increaseToTwoDigit(timeFromH + "") + ":" + DateHelper.increaseToTwoDigit(timeFromM + ""));

            }
        }, timeFromH, timeFromM, true);
        final TimePickerDialog timePickerTo = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                timeToH = hourOfDay;
                timeToM = minute;
                txtTimeTo.setText(DateHelper.increaseToTwoDigit(timeToH + "") + ":" + DateHelper.increaseToTwoDigit(timeToM + ""));

            }
        }, timeToH, timeToM, true);

        txtDate.setText(DateHelper.formatDate(date, "/"));
        txtTimeFrom.setText(DateHelper.increaseToTwoDigit(timeFromH + "") + ":" + DateHelper.increaseToTwoDigit(timeFromM + ""));
        txtTimeTo.setText(DateHelper.increaseToTwoDigit(timeToH + "") + ":" + DateHelper.increaseToTwoDigit(timeToM + ""));
        lytTimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerFrom.show(getActivity().getFragmentManager(), "TimePickerFrom");
            }
        });
        lytTimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerTo.show(getActivity().getFragmentManager(), "TimePickerTo");
            }
        });
        lytDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show(getActivity().getFragmentManager(), "DatePickerFragment");
            }
        });
        lytAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedFrom = timeFromH * 60 + timeFromM;
                int selectedTo = timeToH * 60 + timeToM;
                if (selectedTo < selectedFrom) {
                    Toast.makeText(getActivity(), "بازه انتخاب شده معتبر نمی باشد", Toast.LENGTH_SHORT).show();
                    return;
                }
                onDriverRouteTimePicked.onTimePicked(date, timeFromH, timeFromM, timeToH, timeToM);
                dismiss();
            }
        });

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear++;
        PersianCalendar persianCalendar = new PersianDateParser(year + "/" + monthOfYear + "/" + dayOfMonth).getPersianDate();
        date = persianCalendar.getTime();
        txtDate.setText(DateHelper.formatDate(date, "/"));

    }
}
