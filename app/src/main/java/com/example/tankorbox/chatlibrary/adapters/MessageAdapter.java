package com.example.tankorbox.chatlibrary.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.chatmodule.model.layout.EmojiTextView;
import com.example.tankorbox.chatlibrary.R;
import com.example.tankorbox.chatlibrary.helpers.TimestampUtil;
import com.example.tankorbox.chatlibrary.models.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int INCOME = 1;
    private static final int OUTGOING = 2;

    private List<Message> mDataset;

    public MessageAdapter(List<Message> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public int getItemViewType(int position) {
        switch (this.mDataset.get(position).getType()) {
            case INCOME:
                return INCOME;
            case OUTGOING:
                return OUTGOING;
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater;
        View view = null;
        switch (viewType) {
            case INCOME:
                inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.rsc_chat_canvas_msg_income, parent, false);
                break;
            case OUTGOING:
                inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.rsc_chat_canvas_msg_outgoing, parent, false);
                break;
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.content.setText(this.mDataset.get(position).getContent());
        viewHolder.content.setUseSystemDefault(Boolean.FALSE);
        viewHolder.timestamp.setText(TimestampUtil.formatTimestamp(this.mDataset.get(position).getTimestamp(), "HH:mm"));
    }

    @Override
    public int getItemCount() {
        return this.mDataset.size();
    }

    // GETTERS AND SETTERS
    public List<Message> getDataset() {
        return mDataset;
    }

    public void setDataset(List<Message> mDataset) {
        this.mDataset = mDataset;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private EmojiTextView content;
        private TextView timestamp;

        public ViewHolder(View v) {
            super(v);
            this.content = v.findViewById(R.id.content);
            this.timestamp = v.findViewById(R.id.timestamp);
        }
    }
}
