package ir.edusa.parents.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fontdroid.TextView;
import ir.edusa.parents.Interfaces.OnServiceSelected;
import ir.edusa.parents.Models.SchoolService;
import ir.edusa.parents.R;


public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<SchoolService> services;
    private OnServiceSelected onServiceSelected;

    public ServicesAdapter(Context context, ArrayList<SchoolService> services, OnServiceSelected onServiceSelected) {
        this.context = context;
        this.services = services;
        this.onServiceSelected = onServiceSelected;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewGroup lytMain;
        ImageView imgPic;
        TextView txtName;


        public ViewHolder(View view) {
            super(view);
            lytMain =(ViewGroup) view.findViewById(R.id.lytMain);
            txtName = (TextView) view.findViewById(R.id.txtName);
            imgPic = (ImageView) view.findViewById(R.id.imgPic);

        }
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_services_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final SchoolService service = services.get(position);
        Picasso.with(context).load(service.getDriver_Image_Url()).into(holder.imgPic);
        String name = service.getDriverFullName();
        for (SchoolService schoolService : services) {
            if (!schoolService.equals(service)) {
                if (schoolService.getDriver_Code().equals(service.getDriver_Code())) {
                    name = " (" + service.getService_Code() + ")";
                }
            }
        }
        holder.txtName.setText(name);
        holder.lytMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onServiceSelected.onItemSelect(service);
            }
        });
    }
}
