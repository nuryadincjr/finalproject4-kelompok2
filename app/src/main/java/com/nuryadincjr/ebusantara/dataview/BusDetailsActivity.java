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
    private ReviewersReference reviewers;
    private ScheduleReference schedule;
    private String passengers;
    private Buses buses;
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
        Cities departureCity = schedule.getDeparture();
        Cities arrivalCity = schedule.getArrival();
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
        binding.layoutBookATrip.tvBusNo.setText(buses.getBusNo());
        binding.layoutBookATrip.tvClass.setText(buses.getClassType());
        binding.layoutBookATrip.tvRatings.setText(reviewers.getRatingsCount());

        getTime();
        getSeats();
        getImage();
        getFacilities();
        getEstimatedTimes();
        getPieces();
        getReviewers();

        binding.ivBackArrow.setOnClickListener(v -> onBackPressed());
        binding.btnBookNow.setOnClickListener(v -> onStartActivity(calendar));
    }

    @SuppressLint("SimpleDateFormat")
    private void getTime() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");

        Date departureDate = new Date();
        Date arrivalDate = new Date();
        try {
            departureDate = format.parse(schedule.getDepartureTime());
            arrivalDate = format.parse(schedule.getArrivalTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        binding.layoutBookATrip.tvDepartureTime.setText(formatTime.format(departureDate));
        binding.layoutBookATrip.tvArrivalTime.setText(formatTime.format(arrivalDate));
    }

    private void onStartActivity(Calendar calendar) {
        startActivity(new Intent(this,
                SeatChooserActivity.class)
                .putExtra("schedule", schedule)
                .putExtra("date", calendar)
                .putExtra("passengers", passengers));
    }

    private void getSeats() {
        if(seats!=null){
            List<Boolean> seatsA = seats.getA();
            List<Boolean> seatsB = seats.getB();
            List<Boolean> seatsC = seats.getC();
            List<Boolean> seatsD = seats.getD();

            if (seatsA != null || seatsB != null || seatsC != null || seatsD != null) {
                int counter = 0;
                counter = getCounter(seatsA, counter);
                counter = getCounter(seatsB, counter);
                counter = getCounter(seatsC, counter);
                counter = getCounter(seatsD, counter);

                String displaySeats = counter + " Seat are available";
                binding.layoutBookATrip.tvSeatAvailable.setText(displaySeats);
            }
        }
    }

    private static int getCounter( List<Boolean> booleanList, int counter) {
        for (boolean booleanItem : booleanList) {
            if (booleanItem) {
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
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Date departureDate = new Date();
        Date arrivalDate = new Date();
        try {
            departureDate = format.parse(schedule.getDepartureTime());
            arrivalDate = format.parse(schedule.getArrivalTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long millisTime = arrivalDate.getTime() - departureDate.getTime();
        if(departureDate.getTime() > arrivalDate.getTime()){
            millisTime = departureDate.getTime() - arrivalDate.getTime();
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
    }
}