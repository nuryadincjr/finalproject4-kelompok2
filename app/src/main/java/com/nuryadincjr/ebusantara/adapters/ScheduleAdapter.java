package com.nuryadincjr.ebusantara.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nuryadincjr.ebusantara.databinding.ItemDestinationBinding;
import com.nuryadincjr.ebusantara.interfaces.ItemClickListener;
import com.nuryadincjr.ebusantara.models.Reviewers;
import com.nuryadincjr.ebusantara.models.ReviewersReference;
import com.nuryadincjr.ebusantara.models.ScheduleReference;
import com.nuryadincjr.ebusantara.models.Seats;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ItemClickListener itemClickListener;
    private final List<ScheduleReference> compactScheduleReferenceList;
    private int seat;

    public ScheduleAdapter(List<ScheduleReference> compactScheduleReferenceList, int seat) {
        this.compactScheduleReferenceList = compactScheduleReferenceList;
        this.seat = seat;
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
        private final ScheduleAdapter scheduleAdapter;
        private final FirebaseFirestore db;

        public ScheduleViewHolder(ItemDestinationBinding binding, ScheduleAdapter scheduleAdapter) {
            super(binding.getRoot());
            this.binding = binding;
            this.scheduleAdapter = scheduleAdapter;
            this.db = FirebaseFirestore.getInstance();
        }

        public void setDataToView(ScheduleReference dataToView) {
            binding.tvPOName.setText(dataToView.getBuses().getPoName());
            binding.tvBusNo.setText(dataToView.getBuses().getBusNo());
            binding.tvPiece.setText(dataToView.getBuses().getPrice());
            binding.tvDeparture.setText(dataToView.getDeparture().getCity());
            binding.tvArrival.setText(dataToView.getArrival().getCity());
            binding.tvTerminalDeparture.setText(dataToView.getDeparture().getTerminal());
            binding.tvTerminalArrival.setText(dataToView.getArrival().getTerminal());
            binding.tvDepartureTime.setText(dataToView.getDepartureTime());
            binding.tvArrivalTime.setText(dataToView.getArrivalTime());

            List<Reviewers> reviewersList = new ArrayList<>();
            getSeats(dataToView);
            getReviews(dataToView, reviewersList);

            binding.getRoot().setOnLongClickListener(this);
            binding.getRoot().setOnClickListener(this);
            binding.btnBookNow.setOnClickListener(this);
        }

        private void getReviews(ScheduleReference dataToView, List<Reviewers> reviewersList) {
            db.collection("reviews")
                    .document(dataToView.getId()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<Map<String, Object>> reviewer = (List<Map<String, Object>>) document.get("reviewer");
                        double ratings = 0;
                        for (Map<String, Object> map : reviewer) {
                            Reviewers reviewers = new Reviewers();
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

                        ReviewersReference reference = new ReviewersReference();
                        reference.setRatingsCount(displayRating);
                        reference.setReviewers(reviewersList);

                        dataToView.setReviewers(reference);
                    }
                }
            });
        }

        private void getSeats(ScheduleReference dataToView) {
            db.document("seats/"+ dataToView.getBuses().getId())
                    .get().addOnCompleteListener(seatTask -> {
                if (seatTask.isSuccessful()) {
                    Map<String, Object> seatsList = seatTask.getResult().getData();
                    List<Boolean> seatsA = (List<Boolean>) seatsList.get("A");
                    List<Boolean> seatsB = (List<Boolean>) seatsList.get("B");
                    List<Boolean> seatsC = (List<Boolean>) seatsList.get("C");
                    List<Boolean> seatsD = (List<Boolean>) seatsList.get("D");
                    Seats seats = new Seats();

                    boolean[] arrayA = new boolean[seatsA.size()];
                    boolean[] arrayB = new boolean[seatsB.size()];
                    boolean[] arrayC = new boolean[seatsC.size()];
                    boolean[] arrayD = new boolean[seatsD.size()];

                    int counter = 0;
                    if (arrayA.length != 0 || arrayB.length != 0 || arrayC.length != 0 || arrayD.length != 0) {
                        counter = getCounter(seatsA, arrayA, counter);
                        counter = getCounter(seatsB, arrayB, counter);
                        counter = getCounter(seatsC, arrayC, counter);
                        counter = getCounter(seatsD, arrayD, counter);

                        seats.setA(arrayA);
                        seats.setB(arrayB);
                        seats.setC(arrayC);
                        seats.setD(arrayD);
                        dataToView.getBuses().setSeats(seats);
                    }

                    if(scheduleAdapter.seat > counter){
                        binding.cardView.setEnabled(false);
                        binding.btnBookNow.setText("Sold out");
                        binding.btnBookNow.setEnabled(false);
                    }
                }
            });
        }

        private int getCounter(List<Boolean> booleanA, boolean[] a, int counter) {
            for (int i = 0; i < booleanA.size(); i++) {
                boolean bool = booleanA.get(i);
                a[i] = bool;
                if(bool) counter += 1;
            }
            return counter;
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