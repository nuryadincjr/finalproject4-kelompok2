package com.nuryadincjr.ebusantara.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nuryadincjr.ebusantara.databinding.ItemDestinationBinding;
import com.nuryadincjr.ebusantara.interfaces.ItemClickListener;
import com.nuryadincjr.ebusantara.models.Reviewers;
import com.nuryadincjr.ebusantara.models.ScheduleReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ItemClickListener itemClickListener;
    private final List<ScheduleReference> compactScheduleReferenceList;

    public ScheduleAdapter(List<ScheduleReference> compactScheduleReferenceList) {
        this.compactScheduleReferenceList = compactScheduleReferenceList;
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
        scheduleViewHolder.setDataToView(compactScheduleReferenceList.get(position));
    }

    @Override
    public int getItemCount() {
        return compactScheduleReferenceList.size();
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

        public void setDataToView(ScheduleReference citiesList) {
            binding.tvPOName.setText(citiesList.getBuses().getPoName());
            binding.tvBusNo.setText(citiesList.getBuses().getBusNo());
            binding.tvPiece.setText(citiesList.getBuses().getPrice());
            binding.tvDeparture.setText(citiesList.getDeparture().getCity());
            binding.tvArrival.setText(citiesList.getArrival().getCity());
            binding.tvTerminalDeparture.setText(citiesList.getDeparture().getTerminal());
            binding.tvTerminalArrival.setText(citiesList.getArrival().getTerminal());
            binding.tvDepartureTime.setText(citiesList.getDepartureTime());
            binding.tvArrivalTime.setText(citiesList.getArrivalTime());

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            List<Reviewers> reviewersList = new ArrayList<>();
            Reviewers reviewers = new Reviewers();

            db.collection("reviews")
                    .document(citiesList.getId()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<Map<String, Object>> reviewer = (List<Map<String, Object>>) document.get("reviewer");
                        double ratings = 0;
                        for (Map<String, Object> map : reviewer) {
                            reviewers.setId(String.valueOf(map.get("id")));
                            reviewers.setUid(String.valueOf(map.get("uid")));
                            reviewers.setDate(String.valueOf(map.get("date")));
                            reviewers.setContent(String.valueOf(map.get("content")));
                            reviewers.setRatings(String.valueOf(map.get("ratings")));

                            ratings += Double.parseDouble(reviewers.getRatings());
                            reviewersList.add(reviewers);
                        }

                        double finalRating = ratings/reviewer.size();
                        String displayRating = finalRating+"/5";
                        binding.tvReviews.setText(String.valueOf(reviewer.size()));
                        binding.tvRatings.setText(displayRating);
                    }
                }
            });

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

