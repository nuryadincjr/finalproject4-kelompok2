package com.nuryadincjr.ebusantara.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.ItemReviewsBinding;
import com.nuryadincjr.ebusantara.interfaces.ItemClickListener;
import com.nuryadincjr.ebusantara.pojo.Reviewers;
import com.nuryadincjr.ebusantara.util.MainViewModel;

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
        scheduleViewHolder.setDataToView(reviewersList.get(position), position);
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

        public void setDataToView(Reviewers reviewers, int position) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );

            if(position == 0){
                params.setMarginStart(40);
                params.setMarginEnd(10);
                binding.getRoot().setLayoutParams(params);
            }

            if(position == (scheduleAdapter.getItemCount()-1)){
                params.setMarginStart(10);
                params.setMarginEnd(40);
                binding.getRoot().setLayoutParams(params);
            }

            MainViewModel mainViewModel = new ViewModelProvider((ViewModelStoreOwner) itemView.getContext())
                    .get(MainViewModel.class);
            mainViewModel.getUsers(reviewers).observe((LifecycleOwner) itemView.getContext(), users -> {
                binding.tvName.setText(users.getName());

                Glide.with(itemView)
                        .load(users.getPhotoUrl())
                        .centerCrop()
                        .placeholder(R.drawable.ic_brand)
                        .into(binding.ivProfile);
            });

            String rating = reviewers.getRatings();
            if(rating.equals("NaN")) rating = "0";
            binding.tvReviewsContent.setText(reviewers.getContent());
            String displayRating = rating+"/5";
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

