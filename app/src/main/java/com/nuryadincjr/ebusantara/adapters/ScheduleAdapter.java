package com.nuryadincjr.ebusantara.adapters;

import static java.lang.Double.NaN;
import static java.lang.String.*;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.ItemDestinationBinding;
import com.nuryadincjr.ebusantara.interfaces.ItemClickListener;
import com.nuryadincjr.ebusantara.pojo.Reviewers;
import com.nuryadincjr.ebusantara.pojo.ReviewersReference;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.pojo.Seats;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ItemClickListener itemClickListener;
    private final List<ScheduleReference> references;
    private final int seat;

    public ScheduleAdapter(List<ScheduleReference> references, int seat) {
        this.references = references;
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
        scheduleViewHolder.setDataToView(references.get(position));
    }

    @Override
    public int getItemCount() {
        return references.size();
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
            String piece = "Rp"+dataToView.getBuses().getPrice();

            binding.tvPOName.setText(dataToView.getBuses().getPoName());
            binding.tvBusNo.setText(dataToView.getBuses().getBusNo());
            binding.tvPiece.setText(piece);
            binding.tvDeparture.setText(dataToView.getDeparture().getCity());
            binding.tvArrival.setText(dataToView.getArrival().getCity());
            binding.tvTerminalDeparture.setText(dataToView.getDeparture().getTerminal());
            binding.tvTerminalArrival.setText(dataToView.getArrival().getTerminal());

            List<Reviewers> reviewersList = new ArrayList<>();
            getTime(dataToView);
            getSeats(dataToView);
            getReviews(dataToView, reviewersList);

            binding.getRoot().setOnLongClickListener(this);
            binding.getRoot().setOnClickListener(this);
            binding.btnBookNow.setOnClickListener(this);
        }

        @SuppressLint("SimpleDateFormat")
        private void getTime(ScheduleReference dataToView) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");

            Date departureDate = new Date();
            Date arrivalDate = new Date();
            try {
                departureDate = format.parse(dataToView.getDepartureTime());
                arrivalDate = format.parse(dataToView.getArrivalTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            binding.tvDepartureTime.setText(formatTime.format(departureDate));
            binding.tvArrivalTime.setText(formatTime.format(arrivalDate));
        }

        private void getReviews(ScheduleReference dataToView, List<Reviewers> reviewersList) {
            db.collection("reviews")
                    .document(dataToView.getId()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    List<Map<String, Object>> reviewer = (List<Map<String, Object>>) document.get("reviewer");
                    double ratings = 0;
                    if(reviewer!=null){
                        for (Map<String, Object> map : reviewer) {
                           Reviewers reviewers = new Reviewers(
                                   valueOf(map.get("uid")),
                                   valueOf(map.get("date")),
                                   valueOf(map.get("content")),
                                   valueOf(map.get("ratings")));

                            ratings += Double.parseDouble(reviewers.getRatings());

                           reviewersList.add(reviewers);
                        }
                        ratings /= reviewer.size();
                    }

                    if(valueOf(ratings).equals("NaN")) ratings = 0.0;
                    String displayRating = ratings +"/5";
                    binding.tvReviews.setText(valueOf(reviewer.size()));
                    binding.tvRatings.setText(displayRating);

                    ReviewersReference reference = new ReviewersReference();
                    reference.setRatingsCount(displayRating);
                    reference.setReviewers(reviewersList);
                    dataToView.setReviewers(reference);
                }
            });
        }

        private void getSeats(ScheduleReference dataToView) {
            db.document("seats/"+ dataToView.getId())
                    .get().addOnCompleteListener(seatTask -> {
                if (seatTask.isSuccessful()) {
                    Seats seatsList = seatTask.getResult().toObject(Seats.class);
                    List<Boolean> seatsA = seatsList.getA();
                    List<Boolean> seatsB = seatsList.getB();
                    List<Boolean> seatsC = seatsList.getC();
                    List<Boolean> seatsD = seatsList.getD();
                    Seats seats = new Seats();

                    int counter = 0;
                    if (seatsA.size() != 0 || seatsB.size() != 0 || seatsC.size() != 0 || seatsD.size() != 0) {
                        counter = getCounter(seatsA, counter);
                        counter = getCounter(seatsB, counter);
                        counter = getCounter(seatsC, counter);
                        counter = getCounter(seatsD, counter);

                        seats.setA(seatsA);
                        seats.setB(seatsB);
                        seats.setC(seatsC);
                        seats.setD(seatsD);
                        dataToView.getBuses().setSeats(seats);
                    }

                    if(scheduleAdapter.seat > counter){
                        int color = itemView.getResources().getColor(R.color.gray_e5);
                        int colorBg = itemView.getResources().getColor(R.color.gray_64);
                        String titleButton = "Sold out";

                        binding.cardView.setCardBackgroundColor(colorBg);
                        binding.tvRatings.setBackgroundColor(color);
                        binding.tvPOName.setTextColor(color);
                        binding.tvPiece.setTextColor(color);
                        binding.tvDepartureTime.setTextColor(color);
                        binding.tvArrivalTime.setTextColor(color);
                        binding.cardView.setEnabled(false);
                        binding.btnBookNow.setEnabled(false);
                        binding.btnBookNow.setText(titleButton);
                    }
                }

            });
        }

        private int getCounter(List<Boolean> booleanA, int counter) {
            for (int i = 0; i < booleanA.size(); i++) {
                boolean bool = booleanA.get(i);
                booleanA.set(i, bool);
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