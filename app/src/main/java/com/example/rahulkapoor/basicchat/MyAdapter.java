package com.example.rahulkapoor.basicchat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<String> messageList = new ArrayList<>();

    public MyAdapter(final ArrayList<String> list) {
        this.messageList = list;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdapter.MyViewHolder holder, final int position) {

        String[] messageData = messageList.get(position).split(",");//split on comma with user, time, text;
        holder.tvUser.setText(messageData[0]);
        holder.tvTime.setText(messageData[1]);
        holder.tvText.setText(messageData[1]);

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvText, tvTime, tvUser;

        public MyViewHolder(final View itemView) {
            super(itemView);

            tvText = (TextView) itemView.findViewById(R.id.message_text);
            tvTime = (TextView) itemView.findViewById(R.id.message_time);
            tvUser = (TextView) itemView.findViewById(R.id.message_user);

        }
    }
}
