package com.nuryadincjr.ebusantara.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.nuryadincjr.ebusantara.databinding.ItemDestinationBinding;
import com.nuryadincjr.ebusantara.databinding.ItemReviewsBinding;
import com.nuryadincjr.ebusantara.interfaces.ItemClickListener;
import com.nuryadincjr.ebusantara.models.Reviewers;
import com.nuryadincjr.ebusantara.models.Users;

import java.util.List;

public class ReviewersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ItemClickListener itemClickListener;
    private final List<Reviewers> reviewersList;

    public ReviewersAdapter(List<Reviewers> reviewersList) {
        this.reviewersList = reviewersList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemReviewsBinding binding = ItemReviewsBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ScheduleViewHolder(binding, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ScheduleViewHolder scheduleViewHolder = (ScheduleViewHolder) holder;
        scheduleViewHolder.setDataToView(reviewersList.get(position));
    }

    @Override
    public int getItemCount() {
        return reviewersList.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        private final ItemReviewsBinding binding;
        private final ReviewersAdapter scheduleAdapter;

        public ScheduleViewHolder(ItemReviewsBinding binding, ReviewersAdapter scheduleAdapter) {
            super(binding.getRoot());
            this.binding = binding;
            this.scheduleAdapter = scheduleAdapter;
        }

        public void setDataToView(Reviewers reviewers) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.document("users/"+reviewers.getUid()).get().addOnCompleteListener(task -> {
               if(task.isSuccessful()){
                   Users users = task.getResult().toObject(Users.class);
                   binding.tvName.setText(users.getName());
               }
            });

            binding.tvReviewsContent.setText(reviewers.getContent());

            String displayRating = reviewers.getRatings()+"/5";
            binding.tvRatings.setText(displayRating);

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

