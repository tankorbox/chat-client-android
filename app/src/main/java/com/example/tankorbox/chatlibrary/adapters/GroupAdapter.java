package com.example.tankorbox.chatlibrary.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tankorbox.chatlibrary.R;
import com.example.tankorbox.chatlibrary.models.Group;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter {
    private List<Group> groups;
    private AdapterListener mAdapterListener;

    public GroupAdapter(List<Group> groups, AdapterListener listener) {
        this.groups = groups;
        this.mAdapterListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_group, viewGroup, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((GroupViewHolder) viewHolder).setGroupName(groups.get(i).getName());
        ((GroupViewHolder) viewHolder).tvGroupName.setOnClickListener(view -> {
            mAdapterListener.onClick(groups.get(i));
        });
    }

    @Override
    public int getItemCount() {
        if (groups == null || groups.isEmpty()) return 0;
        return groups.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface AdapterListener {
        void onClick(Group group);
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView tvGroupName;

        GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            tvGroupName = view.findViewById(R.id.tvGroupName);
        }

        public void setGroupName(String text) {
            tvGroupName.setText(text);
        }
    }
}
