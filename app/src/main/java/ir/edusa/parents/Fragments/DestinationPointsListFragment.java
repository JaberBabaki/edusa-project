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

import java.util.ArrayList;

import fontdroid.TextView;
import ir.edusa.parents.Adapters.DestinationPointsAdapter;
import ir.edusa.parents.Interfaces.OnDestinationPointClickListener;
import ir.edusa.parents.Models.DestinationPoint;
import ir.edusa.parents.R;


/**
 * Created by pouya on 6/30/17.
 */

public class DestinationPointsListFragment extends DialogFragment {

    private TextView txtTitle;
    private ViewGroup lytRightButton;

    private OnDestinationPointClickListener onDestinationPointClickListener;
    private ArrayList<DestinationPoint> points;

    public DestinationPointsListFragment() {
    }

    public static DestinationPointsListFragment newInstance(ArrayList<DestinationPoint> points, OnDestinationPointClickListener onDestinationPointClickListener) {
        DestinationPointsListFragment addressListDialogFragment = new DestinationPointsListFragment();
        addressListDialogFragment.init(points, onDestinationPointClickListener);
        return addressListDialogFragment;
    }

    public void init(ArrayList<DestinationPoint> points, OnDestinationPointClickListener onDestinationPointClickListener) {
        this.points = points;
        this.onDestinationPointClickListener = onDestinationPointClickListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setLayout(
                getResources().getDisplayMetrics().widthPixels,
                getResources().getDisplayMetrics().heightPixels
        );
        View view = inflater.inflate(R.layout.fragment_destination_points_list, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rclPoints =(RecyclerView) view.findViewById(R.id.rclPoints);

        txtTitle =(TextView) getView().findViewById(R.id.txtTitle);
        lytRightButton =(ViewGroup) getView().findViewById(R.id.lytRightButton);

        txtTitle.setText("لیست مقصد ها");
        lytRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        rclPoints.setLayoutManager(new LinearLayoutManager(getActivity()));
        rclPoints.setAdapter(new DestinationPointsAdapter(getActivity(), points, new OnDestinationPointClickListener() {
            @Override
            public void onItemSelect(DestinationPoint destinationPoint) {
                onDestinationPointClickListener.onItemSelect(destinationPoint);
                dismiss();
            }
        }));
    }
}
