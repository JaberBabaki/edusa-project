package ir.edusa.parents.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import ir.edusa.parents.Adapters.AlarmPointsListAdapter;
import ir.edusa.parents.Interfaces.OnAlarmPointClickListener;
import ir.edusa.parents.Models.AlarmLocationPoint;
import ir.edusa.parents.R;

import java.util.ArrayList;

import fontdroid.TextView;

public class AlarmPointsListFragment extends DialogFragment {

    private OnAlarmPointClickListener onAlarmPointClickListener;
    private ArrayList<AlarmLocationPoint> alarmLocationPoints;
    private TextView txtNoResult;

    public AlarmPointsListFragment() {
    }


    public static AlarmPointsListFragment newInstance(ArrayList<AlarmLocationPoint> alarmLocationPoints, OnAlarmPointClickListener onAlarmPointClickListener) {
        AlarmPointsListFragment addressListDialogFragment = new AlarmPointsListFragment();
        addressListDialogFragment.init(alarmLocationPoints, onAlarmPointClickListener);
        return addressListDialogFragment;
    }

    public void init(ArrayList<AlarmLocationPoint> alarmLocationPoints, OnAlarmPointClickListener onAlarmPointClickListener) {
        this.onAlarmPointClickListener = onAlarmPointClickListener;
        this.alarmLocationPoints = alarmLocationPoints;
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
        View view = inflater.inflate(R.layout.fragment_alarm_points_list, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rclPoints = (RecyclerView) getView().findViewById(R.id.rclPoints);
        ViewGroup lytMain = (ViewGroup) getView().findViewById(R.id.lytMain);
        lytMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        txtNoResult = (TextView) getView().findViewById(R.id.txtNoResult);
        rclPoints.setLayoutManager(new LinearLayoutManager(getContext()));
        rclPoints.setAdapter(new AlarmPointsListAdapter(getActivity(), alarmLocationPoints, new OnAlarmPointClickListener() {
            @Override
            public void onItemSelect(AlarmLocationPoint point) {
                onAlarmPointClickListener.onItemSelect(point);
                dismiss();
            }

            @Override
            public void onItemDelete(AlarmLocationPoint point) {
                onAlarmPointClickListener.onItemDelete(point);
                checkPointsCount();
            }
        }));

    }

    private void checkPointsCount() {
        if (alarmLocationPoints==null||alarmLocationPoints.size()==0){
            txtNoResult.setVisibility(View.VISIBLE);
        }else {
            txtNoResult.setVisibility(View.GONE);
        }
    }


}
