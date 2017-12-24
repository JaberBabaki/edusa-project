package ir.edusa.parents.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ir.edusa.parents.Interfaces.OnPassengerClickListener;
import ir.edusa.parents.Models.Passenger;
import ir.edusa.parents.R;

import java.util.ArrayList;

import fontdroid.TextView;


public class PasserngersListAdapter extends RecyclerView.Adapter<PasserngersListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Passenger> passengers;
    private OnPassengerClickListener passengerClickListener;

    public PasserngersListAdapter(Context context, ArrayList<Passenger> passengers, OnPassengerClickListener passengerClickListener) {
        this.context = context;
        this.passengers = passengers;
        this.passengerClickListener = passengerClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewGroup lytMain;
        ImageView imgPic;
        TextView txtName;


        public ViewHolder(View view) {
            super(view);
            lytMain = (ViewGroup) view.findViewById(R.id.lytMain);
            txtName = (TextView) view.findViewById(R.id.txtName);
            imgPic = (ImageView) view.findViewById(R.id.imgPic);

        }
    }

    @Override
    public int getItemCount() {
        return passengers.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_passengers_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Passenger passenger = passengers.get(position);
        holder.txtName.setText(passenger.getName() + " " + passenger.getFamily());
        holder.lytMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passengerClickListener.onItemSelect(passenger);
            }
        });
    }
}
