package ir.edusa.parents.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.edusa.parents.Interfaces.OnDestinationPointClickListener;
import ir.edusa.parents.Models.DestinationPoint;
import ir.edusa.parents.R;

import java.util.ArrayList;

import fontdroid.TextView;


public class DestinationPointsAdapter extends RecyclerView.Adapter<DestinationPointsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<DestinationPoint> points;
    private OnDestinationPointClickListener DestinationPointClickListener;

    public DestinationPointsAdapter(Context context, ArrayList<DestinationPoint> points, OnDestinationPointClickListener DestinationPointClickListener) {
        this.context = context;
        this.points = points;
        this.DestinationPointClickListener = DestinationPointClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewGroup lytMain;
        TextView txtTitle;
        TextView txtAddress;


        public ViewHolder(View view) {
            super(view);
            lytMain = (ViewGroup) view.findViewById(R.id.lytMain);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            txtAddress = (TextView) view.findViewById(R.id.txtAddress);

        }
    }

    @Override
    public int getItemCount() {
        return points.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_destination_points_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final DestinationPoint destinationPoint = points.get(position);
        holder.txtTitle.setText(destinationPoint.getTitle());
        holder.txtAddress.setText(destinationPoint.getAddress());
        holder.lytMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DestinationPointClickListener.onItemSelect(destinationPoint);
            }
        });
    }
}
