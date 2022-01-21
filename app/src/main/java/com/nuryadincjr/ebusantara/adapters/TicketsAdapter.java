package com.nuryadincjr.ebusantara.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.nuryadincjr.ebusantara.databinding.ItemOrderBinding;
import com.nuryadincjr.ebusantara.interfaces.ItemClickListener;
import com.nuryadincjr.ebusantara.pojo.TransactionsReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TicketsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ItemClickListener itemClickListener;
    private final List<TransactionsReference> transactionsList;

    public TicketsAdapter(ArrayList<TransactionsReference> transactionsList) {
        this.transactionsList = transactionsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBinding binding = ItemOrderBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ScheduleViewHolder(binding, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ScheduleViewHolder scheduleViewHolder = (ScheduleViewHolder) holder;
        scheduleViewHolder.setDataToView(transactionsList.get(position));
    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        private final ItemOrderBinding binding;
        private final TicketsAdapter scheduleAdapter;
        private final FirebaseFirestore db;

        public ScheduleViewHolder(ItemOrderBinding binding, TicketsAdapter scheduleAdapter) {
            super(binding.getRoot());
            this.binding = binding;
            this.scheduleAdapter = scheduleAdapter;
            this.db = FirebaseFirestore.getInstance();
        }

        public void setDataToView(TransactionsReference dataToView) {
            String bookNo = "Book No. "+dataToView.getTransactions().getBookNo();

            binding.tvDate.setText(dataToView.getTransactions().getDate());
            binding.tvBookNo.setText(bookNo);
            binding.tvStatus.setText(dataToView.getTransactions().getStatus());
            binding.tvPOName.setText(dataToView.getReference().getBuses().getPoName());
            binding.tvBusNo.setText(dataToView.getReference().getBuses().getBusNo());
            binding.tvDeparture.setText(dataToView.getReference().getDeparture().getCity());
            binding.tvTerminalDeparture.setText(dataToView.getReference().getDeparture().getTerminal());

            getTime(dataToView);
            binding.getRoot().setOnClickListener(this);
            binding.getRoot().setOnLongClickListener(this);
        }

        @SuppressLint("SimpleDateFormat")
        private void getTime(TransactionsReference dataToView) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");

            Date departureDate = new Date();
            try {
                departureDate = format.parse(dataToView.getReference().getDepartureTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            binding.tvDepartureTime.setText(formatTime.format(departureDate));
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