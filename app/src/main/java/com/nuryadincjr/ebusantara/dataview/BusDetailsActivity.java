package com.nuryadincjr.ebusantara.dataview;

import static android.view.View.VISIBLE;
import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;
import static com.nuryadincjr.ebusantara.R.drawable.ic_brand;
import static com.nuryadincjr.ebusantara.R.id.ivViewer;
import static com.nuryadincjr.ebusantara.R.layout.activity_bus_details;
import static com.nuryadincjr.ebusantara.R.layout.layout_image_viewer;
import static com.nuryadincjr.ebusantara.util.Constant.getEstimatedTimes;
import static com.nuryadincjr.ebusantara.util.Constant.getPieces;
import static com.nuryadincjr.ebusantara.util.Constant.getTime;
import static com.nuryadincjr.ebusantara.util.Constant.toUpperCase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nuryadincjr.ebusantara.adapters.ReviewersAdapter;
import com.nuryadincjr.ebusantara.databinding.ActivityBusDetailsBinding;
import com.nuryadincjr.ebusantara.pojo.Buses;
import com.nuryadincjr.ebusantara.pojo.Cities;
import com.nuryadincjr.ebusantara.pojo.ReviewersReference;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.pojo.Seats;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

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
        setContentView(activity_bus_details);

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

        binding.layoutBookATrip.tvPOName.setText(toUpperCase(buses.getPoName()));
        binding.layoutBookATrip.tvDeparture.setText(toUpperCase(departureCity.getCity()));
        binding.layoutBookATrip.tvArrival.setText(toUpperCase(arrivalCity.getCity()));
        binding.layoutBookATrip.tvBusNo.setText(buses.getBusNo());
        binding.layoutBookATrip.tvClass.setText(buses.getClassType());
        binding.layoutBookATrip.tvRatings.setText(reviewers.getRatingsCount());
        binding.layoutBookATrip.tvEstimation.setText(getEstimatedTimes(schedule));
        binding.layoutBookATrip.tvDepartureTime.setText(getTime(schedule).get("departureTime"));
        binding.layoutBookATrip.tvArrivalTime.setText(getTime(schedule).get("arrivalTime"));
        binding.tvSubTotals.setText(getPieces(buses, passengers).get("displaySubTotal"));
        binding.tvTotals.setText(getPieces(buses, passengers).get("subTotal"));

        getSeats();
        getImage();
        getReviewers();
        getFacilities();

        binding.ivBackArrow.setOnClickListener(v -> onBackPressed());
        binding.btnBookNow.setOnClickListener(v -> onStartActivity(calendar));
        binding.layoutBookATrip.tvSeePicture.setOnClickListener(this::onImageShow);
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
            List<Boolean> seatsA = seats.getSeatsA();
            List<Boolean> seatsB = seats.getSeatsB();
            List<Boolean> seatsC = seats.getSeatsC();
            List<Boolean> seatsD = seats.getSeatsD();

            if (seatsA != null || seatsB != null || seatsC != null || seatsD != null) {
                int counter = 0;
                counter = getCounter(seatsA, counter);
                counter = getCounter(seatsB, counter);
                counter = getCounter(seatsC, counter);
                counter = getCounter(seatsD, counter);

                if(counter < Integer.parseInt(passengers)){
                    passengers = String.valueOf(counter);
                }
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

    private void getImage() {
        Glide.with(this)
                .load(buses.getImageUrl())
                .centerCrop()
                .placeholder(ic_brand)
                .into(binding.layoutBookATrip.ivBus);
    }

    private void getFacilities() {
        List<String> facilityList = buses.getFacility();
        for(String facility:facilityList){
            switch (facility){
                case "Toilet":
                    binding.layoutBookATrip.tvToiletFacility
                            .setVisibility(VISIBLE);
                    break;
                case "Rest stop":
                    binding.layoutBookATrip.tvRestFacility
                            .setVisibility(VISIBLE);
                    break;
                case "Luggage":
                    binding.layoutBookATrip.tvLuggageFacility
                            .setVisibility(VISIBLE);
                    break;
                case "AC":
                    binding.layoutBookATrip.tvACFacility
                            .setVisibility(VISIBLE);
                    break;
            }
        }
    }

    private void getReviewers() {
        ReviewersAdapter reviewersAdapter = new ReviewersAdapter(reviewers.getReviewers());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, HORIZONTAL, false);
        binding.layoutBookATrip.rvReviews.setLayoutManager(layoutManager);
        binding.layoutBookATrip.rvReviews.setAdapter(reviewersAdapter);
    }

    private void onImageShow(View v) {
        View inflatedView = getLayoutInflater().inflate(layout_image_viewer, null);
        ImageView imageView = inflatedView.findViewById(ivViewer);
        Glide.with(this)
                .load(buses.getImageUrl())
                .centerCrop()
                .placeholder(ic_brand)
                .into(imageView);
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setView(inflatedView).show();
    }
}