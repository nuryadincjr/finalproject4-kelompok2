package com.nuryadincjr.ebusantara.adapters;

import static com.nuryadincjr.ebusantara.util.Constant.getTime;
import static com.nuryadincjr.ebusantara.util.Constant.toUpperCase;
import static java.lang.String.valueOf;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.ItemDestinationBinding;
import com.nuryadincjr.ebusantara.interfaces.ItemClickListener;
import com.nuryadincjr.ebusantara.pojo.Buses;
import com.nuryadincjr.ebusantara.pojo.Cities;
import com.nuryadincjr.ebusantara.pojo.Reviewers;
import com.nuryadincjr.ebusantara.pojo.ReviewersReference;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.pojo.Seats;

import java.util.List;

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

        public void setDataToView(ScheduleReference schedule) {
            Buses buses = schedule.getBuses();
            ReviewersReference reviewers = schedule.getReviewers();
            Cities departureCity = schedule.getDeparture();
            Cities arrivalCity = schedule.getArrival();
            String piece = "Rp"+buses.getPrice();

            binding.tvPOName.setText(buses.getPoName());
            binding.tvBusNo.setText(buses.getBusNo());
            binding.tvPiece.setText(piece);
            binding.tvDeparture.setText(toUpperCase(departureCity.getCity()));
            binding.tvArrival.setText(toUpperCase(arrivalCity.getCity()));
            binding.tvTerminalDeparture.setText(toUpperCase(departureCity.getTerminal()));
            binding.tvTerminalArrival.setText(toUpperCase(arrivalCity.getTerminal()));

            List<Reviewers> reviewersList = reviewers.getReviewers();
            binding.tvReviews.setText(valueOf(reviewersList.size()));
            binding.tvRatings.setText(reviewers.getRatingsCount());
            binding.tvDepartureTime.setText(getTime(schedule).get("departureTime"));
            binding.tvArrivalTime.setText(getTime(schedule).get("arrivalTime"));

            getSeats(schedule);

            binding.getRoot().setOnLongClickListener(this);
            binding.getRoot().setOnClickListener(this);
            binding.btnBookNow.setOnClickListener(this);
        }

        private void getSeats(ScheduleReference dataToView) {
            db.document("seats/"+ dataToView.getId())
                    .get().addOnCompleteListener(seatTask -> {
                if (seatTask.isSuccessful()) {
                    Seats seatsList = seatTask.getResult().toObject(Seats.class);
                    List<Boolean> seatsA = seatsList.getSeatsA();
                    List<Boolean> seatsB = seatsList.getSeatsB();
                    List<Boolean> seatsC = seatsList.getSeatsC();
                    List<Boolean> seatsD = seatsList.getSeatsD();
                    Seats seats = new Seats();

                    int counter = 0;
                    if (seatsA.size() != 0 || seatsB.size() != 0 ||
                            seatsC.size() != 0 || seatsD.size() != 0) {
                        counter = getCounter(seatsA, counter);
                        counter = getCounter(seatsB, counter);
                        counter = getCounter(seatsC, counter);
                        counter = getCounter(seatsD, counter);

                        seats.setSeatsA(seatsA);
                        seats.setSeatsB(seatsB);
                        seats.setSeatsC(seatsC);
                        seats.setSeatsD(seatsD);
                        dataToView.getBuses().setSeats(seats);
                    }

                    if(counter==0){
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
                    } else if(counter < scheduleAdapter.seat){
                        int color = itemView.getResources().getColor(R.color.gray_e5);
                        int colorBg = itemView.getResources().getColor(R.color.gray_64);
                        String titleButton = "seats available "+counter;

                        binding.cardView.setCardBackgroundColor(colorBg);
                        binding.tvRatings.setBackgroundColor(color);
                        binding.tvPOName.setTextColor(color);
                        binding.tvPiece.setTextColor(color);
                        binding.tvDepartureTime.setTextColor(color);
                        binding.tvArrivalTime.setTextColor(color);
                        binding.btnBookNow.setText(titleButton);
                    }
                }
            });
        }

        private int getCounter(List<Boolean> seatsList, int counter) {
            for (int i = 0; i < seatsList.size(); i++) {
                boolean isSeats = seatsList.get(i);
                seatsList.set(i, isSeats);
                if(isSeats) counter += 1;
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