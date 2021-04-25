package ir.edusa.parents.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fontdroid.TextView;
import ir.edusa.parents.Helpers.DateHelper;
import ir.edusa.parents.Models.Message;
import ir.edusa.parents.R;


public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder> {

    private static final int SERVER_MESSAGE = 0;
    private static final int USER_MESSAGE = 1;

    private Context context;
    private ArrayList<Message> messages;

    public InboxAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtText;
        TextView txtDate;


        public ViewHolder(View view) {
            super(view);
            txtText =(TextView) view.findViewById(R.id.txtText);
            txtDate = (TextView) view.findViewById(R.id.txtDate);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        if (viewType == SERVER_MESSAGE) {
            view = inflater.inflate(R.layout.item_incomming_chat, parent, false);
        } else {
            view = inflater.inflate(R.layout.item_outgoing_chat, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).isFromPanel()) {
            return SERVER_MESSAGE;
        }
        return USER_MESSAGE;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Message message = messages.get(position);
        holder.txtText.setText(message.getMessage_Text());
        holder.txtDate.setText(DateHelper.formatFullDateTime(message.getDate()));
    }
}
