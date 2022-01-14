package com.nuryadincjr.ebusantara.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nuryadincjr.ebusantara.databinding.ItemDestinationBinding;
import com.nuryadincjr.ebusantara.databinding.ItemDestinationSearchBinding;
import com.nuryadincjr.ebusantara.interfaces.ItemClickListener;
import com.nuryadincjr.ebusantara.models.Schedule;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ItemClickListener itemClickListener;
    private final List<Schedule> scheduleList;

    public ScheduleAdapter(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDestinationBinding binding = ItemDestinationBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ScheduleViewHolder(binding, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ScheduleViewHolder scheduleViewHolder = (ScheduleViewHolder) holder;
        scheduleViewHolder.setDataToView(scheduleList.get(position));
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        private final ItemDestinationBinding binding;
        private ScheduleAdapter scheduleAdapter;

        public ScheduleViewHolder(ItemDestinationBinding binding, ScheduleAdapter scheduleAdapter) {
            super(binding.getRoot());
            this.binding = binding;
            this.scheduleAdapter = scheduleAdapter;
        }

        public void setDataToView(Schedule citiesList) {
            binding.tvPiece.setText(citiesList.getId());

            binding.getRoot().setOnLongClickListener(this);
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(scheduleAdapter.itemClickListener != null){
                scheduleAdapter.itemClickListener.onClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(scheduleAdapter.itemClickListener != null){
                scheduleAdapter.itemClickListener.onLongClick(v, getAdapterPosition());
            }
            return true;
        }
    }
}

