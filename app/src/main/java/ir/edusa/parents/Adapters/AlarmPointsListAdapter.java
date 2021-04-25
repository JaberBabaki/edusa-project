package ir.edusa.parents.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.edusa.parents.Interfaces.OnAlarmPointClickListener;
import ir.edusa.parents.Models.AlarmLocationPoint;

import ir.edusa.parents.R;

import java.util.ArrayList;

import fontdroid.TextView;


public class AlarmPointsListAdapter extends RecyclerView.Adapter<AlarmPointsListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<AlarmLocationPoint> points;
    private OnAlarmPointClickListener onAlarmPointClickListener;

    public AlarmPointsListAdapter(Context context, ArrayList<AlarmLocationPoint> points, OnAlarmPointClickListener onAlarmPointClickListener) {
        this.context = context;
        this.points = points;
        this.onAlarmPointClickListener = onAlarmPointClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewGroup lytMain;
        TextView txtTitle;
        TextView txtDelete;


        public ViewHolder(View view) {
            super(view);
            lytMain = (ViewGroup) view.findViewById(R.id.lytMain);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            txtDelete = (TextView) view.findViewById(R.id.txtDelete);
        }
    }

    @Override
    public int getItemCount() {
        return points.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_alarm_points_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final AlarmLocationPoint point = points.get(position);
        holder.txtTitle.setText(point.getPoint_Code() + " - " + point.getPoint_Descript());
        holder.lytMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAlarmPointClickListener.onItemSelect(point);
            }
        });
        holder.txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                points.remove(point);
                onAlarmPointClickListener.onItemDelete(point);
                notifyDataSetChanged();
            }
        });
    }
}
