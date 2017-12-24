package ir.edusa.parents.Fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.ArrayList;

import fontdroid.TextView;
import ir.edusa.parents.Adapters.ServicesAdapter;
import ir.edusa.parents.Interfaces.OnServiceSelected;
import ir.edusa.parents.Models.SchoolService;
import ir.edusa.parents.R;

public class ServiceChooserFragment extends DialogFragment {

    private OnServiceSelected onServiceSelected;
    private ArrayList<SchoolService> schoolServices;

    public static ServiceChooserFragment newInstance(OnServiceSelected onServiceSelected, ArrayList<SchoolService> schoolServices) {
        ServiceChooserFragment serviceChooserFragment = new ServiceChooserFragment();
        serviceChooserFragment.init(onServiceSelected, schoolServices);
        return serviceChooserFragment;
    }

    public void init(OnServiceSelected onServiceSelected, ArrayList<SchoolService> schoolServices) {
        this.onServiceSelected = onServiceSelected;
        this.schoolServices = schoolServices;
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
        View view = inflater.inflate(R.layout.fragment_service_picker, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView txtTitle =(TextView) getView().findViewById(R.id.txtTitle);
        ViewGroup lytRightButton =(ViewGroup) getView().findViewById(R.id.lytRightButton);
        RecyclerView rclServices =(RecyclerView) getView().findViewById(R.id.rclServices);
        rclServices.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rclServices.setAdapter(new ServicesAdapter(getContext(), schoolServices, new OnServiceSelected() {
            @Override
            public void onItemSelect(SchoolService service) {
                onServiceSelected.onItemSelect(service);
                dismiss();
            }
        }));
        txtTitle.setText("انتخاب سرویس");
        lytRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
