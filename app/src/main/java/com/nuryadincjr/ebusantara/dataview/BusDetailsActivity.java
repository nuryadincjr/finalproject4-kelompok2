package com.nuryadincjr.ebusantara.dataview;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;
import static java.lang.Double.parseDouble;
import static java.text.DecimalFormat.getCurrencyInstance;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.adapters.ReviewersAdapter;
import com.nuryadincjr.ebusantara.databinding.ActivityBusDetailsBinding;
import com.nuryadincjr.ebusantara.interfaces.ItemClickListener;
import com.nuryadincjr.ebusantara.pojo.Buses;
import com.nuryadincjr.ebusantara.pojo.Cities;
import com.nuryadincjr.ebusantara.pojo.ReviewersReference;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.pojo.Seats;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BusDetailsActivity extends AppCompatActivity {

    private ActivityBusDetailsBinding binding;
    private ScheduleReference schedule;
    private Calendar calendar;
    private String passengers;
    private Buses buses;
    private Cities departureCity;
    private Cities arrivalCity;
    private ReviewersReference reviewers;
    private Seats seats;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);

        binding = ActivityBusDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        schedule = getIntent().getParcelableExtra("schedule");
        passengers = getIntent().getStringExtra("passengers");
        calendar =  (Calendar)getIntent().getSerializableExtra("date");

        departureCity = schedule.getDeparture();
        arrivalCity = schedule.getArrival();
        reviewers = schedule.getReviewers();
        buses = schedule.getBuses();
        seats = buses.getSeats();


        Calendar calendar =  (Calendar) getIntent().getSerializableExtra("date");
        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy");
        binding.layoutBookATrip.tvBookingDate.setText(format.format(calendar.getTime()));

        format = new SimpleDateFormat("d MMM yyyy");
        binding.layoutBookATrip.tvDate.setText(format.format(calendar.getTime()));
        binding.layoutBookATrip.tvPOName.setText(buses.getPoName());
        binding.layoutBookATrip.tvDeparture.setText(departureCity.getCity());
        binding.layoutBookATrip.tvArrival.setText(arrivalCity.getCity());
        binding.layoutBookATrip.tvDepartureTime.setText(schedule.getDepartureTime());
        binding.layoutBookATrip.tvArrivalTime.setText(schedule.getArrivalTime());
        binding.layoutBookATrip.tvBusNo.setText(buses.getBusNo());
        binding.layoutBookATrip.tvClass.setText(buses.getClassType());
        binding.layoutBookATrip.tvRatings.setText(reviewers.getRatingsCount());

        getSeats();
        getImage();
        getFacilities();
        getEstimatedTimes();
        getPieces();
        getReviewers();

        binding.ivBackArrow.setOnClickListener(v -> onBackPressed());
        binding.btnBookNow.setOnClickListener(v -> {
            startActivity(new Intent(this,
                    SeatChooserActivity.class)
                    .putExtra("schedule", schedule)
                    .putExtra("date", calendar)
                    .putExtra("passengers", passengers));
        });
    }

    private void getSeats() {
        boolean[] A = seats.getA();
        boolean[] B = seats.getB();
        boolean[] C = seats.getC();
        boolean[] D = seats.getD();

        if (A != null || B != null || C != null || D != null) {
            int counter = 0;
            counter = getCounter(A, counter);
            counter = getCounter(B, counter);
            counter = getCounter(C, counter);
            counter = getCounter(D, counter);

            String displaySeats = counter + " Seat are available";
            binding.layoutBookATrip.tvSeatAvailable.setText(displaySeats);
        }
    }

    private static int getCounter(boolean[] a, int counter) {
        for (boolean b : a) {
            if (b) {
                counter += 1;
            }
        }
        return counter;
    }

    private void getPieces() {
        String piece = buses.getPrice();
        double subTotal = parseDouble(passengers) * parseDouble(piece);

        DecimalFormat format = (DecimalFormat) getCurrencyInstance();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setCurrencySymbol("Rp");
        symbols.setMonetaryDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        format.setDecimalFormatSymbols(symbols);

        String displaySubTotal = passengers+"x"+format.format(parseDouble(piece));
        binding.tvSubTotals.setText(displaySubTotal);
        binding.tvTotals.setText(format.format(subTotal));
    }

    private void getImage() {
        Glide.with(this)
                .load(buses.getImageUrl())
                .centerCrop()
                .placeholder(R.drawable.ic_brand)
                .into(binding.layoutBookATrip.ivBus);
    }

    private void getFacilities() {
        List<String> facilityList = buses.getFacility();
        for(String facility:facilityList){
            switch (facility){
                case "Toilet":
                    binding.layoutBookATrip.tvToiletFacility
                            .setVisibility(View.VISIBLE);
                    break;
                case "Rest stop":
                    binding.layoutBookATrip.tvRestFacility
                            .setVisibility(View.VISIBLE);
                    break;
                case "Luggage":
                    binding.layoutBookATrip.tvLuggageFacility
                            .setVisibility(View.VISIBLE);
                    break;
                case "AC":
                    binding.layoutBookATrip.tvACFacility
                            .setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void getEstimatedTimes() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String departureTime = schedule.getDepartureTime();
        String arrivalTime = schedule.getArrivalTime();

        Date firstTime = null;
        Date secondTime = null;
        try {
            firstTime = format.parse(departureTime);
            secondTime = format.parse(arrivalTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long millisTime = secondTime.getTime() - firstTime.getTime();
        if(firstTime.getTime() > secondTime.getTime()){
            millisTime = firstTime.getTime() - secondTime.getTime();
        }

        long minutes = TimeUnit.MILLISECONDS.toMinutes(millisTime);
        long hours = TimeUnit.MILLISECONDS.toHours(millisTime);

        String estimatedTime = hours+"h"+minutes+"m";
        binding.layoutBookATrip.tvEstimation.setText(estimatedTime);
    }

    private void getReviewers() {
        ReviewersAdapter reviewersAdapter = new ReviewersAdapter(reviewers.getReviewers());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, HORIZONTAL, false);
        binding.layoutBookATrip.rvReviews.setLayoutManager(layoutManager);
        binding.layoutBookATrip.rvReviews.setAdapter(reviewersAdapter);

        onListener(reviewersAdapter, reviewersAdapter);
    }

    private void onListener(ReviewersAdapter productsAdapter, ReviewersAdapter schedules) {
        productsAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
    }
}