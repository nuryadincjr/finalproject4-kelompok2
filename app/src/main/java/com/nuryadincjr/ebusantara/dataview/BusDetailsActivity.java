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
import static com.nuryadincjr.ebusantara.util.Constant.getUsers;
import static com.nuryadincjr.ebusantara.util.Constant.toUpperCase;
import static java.lang.String.valueOf;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.adapters.ReviewersAdapter;
import com.nuryadincjr.ebusantara.adapters.ScheduleAdapter;
import com.nuryadincjr.ebusantara.api.ReviewsRepository;
import com.nuryadincjr.ebusantara.databinding.ActivityBusDetailsBinding;
import com.nuryadincjr.ebusantara.databinding.LayoutBookATripBinding;
import com.nuryadincjr.ebusantara.interfaces.ItemClickListener;
import com.nuryadincjr.ebusantara.pojo.Buses;
import com.nuryadincjr.ebusantara.pojo.Cities;
import com.nuryadincjr.ebusantara.pojo.Reviewers;
import com.nuryadincjr.ebusantara.pojo.ReviewersReference;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.pojo.Seats;
import com.nuryadincjr.ebusantara.pojo.Users;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BusDetailsActivity extends AppCompatActivity {
    private ActivityBusDetailsBinding binding;
    private ReviewersReference reviewers;
    private ScheduleReference schedule;
    private String passengers;
    private Buses buses;
    private Seats seats;
    private Users user;
    private LayoutBookATripBinding layoutBookATrip;

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

        layoutBookATrip = binding.layoutBookATrip;
        Calendar calendar =  (Calendar) getIntent().getSerializableExtra("date");
        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy");
        layoutBookATrip.tvBookingDate.setText(format.format(calendar.getTime()));
        format = new SimpleDateFormat("d MMM yyyy");
        layoutBookATrip.tvDate.setText(format.format(calendar.getTime()));

        layoutBookATrip.tvPOName.setText(toUpperCase(buses.getPoName()));
        layoutBookATrip.tvDeparture.setText(toUpperCase(departureCity.getCity()));
        layoutBookATrip.tvArrival.setText(toUpperCase(arrivalCity.getCity()));
        layoutBookATrip.tvBusNo.setText(buses.getBusNo());
        layoutBookATrip.tvClass.setText(buses.getClassType());
        layoutBookATrip.tvRatings.setText(reviewers.getRatingsCount());
        layoutBookATrip.tvEstimation.setText(getEstimatedTimes(schedule));
        layoutBookATrip.tvDepartureTime.setText(getTime(schedule).get("departureTime"));
        layoutBookATrip.tvArrivalTime.setText(getTime(schedule).get("arrivalTime"));
        binding.tvSubTotals.setText(getPieces(buses, passengers).get("displaySubTotal"));
        binding.tvTotals.setText(getPieces(buses, passengers).get("subTotal"));

        user = getUsers(this);
        binding.layoutBookATrip.llRating.setOnClickListener(v ->
                getPopup(schedule, user));

        getSeats();
        getImage();
        getReviewers();
        getFacilities();

        binding.ivBackArrow.setOnClickListener(v -> onBackPressed());
        binding.btnBookNow.setOnClickListener(v -> onStartActivity(calendar));
        layoutBookATrip.tvSeePicture.setOnClickListener(this::onImageShow);
    }

    @SuppressLint("InflateParams")
    public void getPopup(ScheduleReference schedule, Users users) {
        View layoutRating = getLayoutInflater().inflate(R.layout.layout_review, null);
        TextView poName = layoutRating.findViewById(R.id.tvPOName);
        TextView busNo = layoutRating.findViewById(R.id.tvBusNo);
        TextView maxLine = layoutRating.findViewById(R.id.tvMaxLine);
        TextView rate = layoutRating.findViewById(R.id.btnRate);
        EditText content = layoutRating.findViewById(R.id.etContent);
        CheckedTextView star1 = layoutRating.findViewById(R.id.ctvStar1);
        CheckedTextView star2 = layoutRating.findViewById(R.id.ctvStar2);
        CheckedTextView star3 = layoutRating.findViewById(R.id.ctvStar3);
        CheckedTextView star4 = layoutRating.findViewById(R.id.ctvStar4);
        CheckedTextView star5 = layoutRating.findViewById(R.id.ctvStar5);
        String id = schedule.getBuses().getId();

        poName.setText(schedule.getBuses().getPoName());
        busNo.setText(schedule.getBuses().getBusNo());

        ReviewsRepository repository = new ReviewsRepository();
        repository.getReviewers(id, users).observe(this, reviewers -> {

            if(reviewers!=null && reviewers.size()!=0) {
                Reviewers reviewer = reviewers.get(0);
                content.setText(reviewer.getContent());
                switch (reviewer.getRatings()){
                    case "1":
                        getSelected(star1, star2, star3, star4, star5, !star1.isChecked(),
                                false, false, false, false);
                        break;
                    case "2":
                        getSelected(star1, star2, star3, star4, star5,
                                true, true, false, false, false);
                        break;
                    case "3":
                        getSelected(star1, star2, star3, star4, star5,
                                true, true, true, false, false);
                        break;
                    case "4":
                        getSelected(star1, star2, star3, star4, star5,
                                true, true, true, true, false);
                        break;
                    case "5":
                        getSelected(star1, star2, star3, star4, star5,
                                true, true, true, true, true);
                        break;
                }
            }

            onShowPopup(reviewers, users, layoutRating, maxLine,rate,
                    content, star1, star2, star3, star4, star5, id, repository);
        });
    }

    private void onShowPopup(ArrayList<Reviewers> reviewers, Users users, View inflatedView,
                             TextView maxLine, TextView rate, EditText content, CheckedTextView star1,
                             CheckedTextView star2, CheckedTextView star3, CheckedTextView star4,
                             CheckedTextView star5, String id, ReviewsRepository repository) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(inflatedView);

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count<=500){
                    String indicator = s.length()+"/500";
                    maxLine.setText(indicator);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        star1.setOnClickListener(v -> {
            getSelected(star1, star2, star3, star4, star5, !star1.isChecked(),
                    false, false, false, false);
        });
        star2.setOnClickListener(v -> {
            getSelected(star1, star2, star3, star4, star5,
                    true, true, false, false, false);
        });
        star3.setOnClickListener(v -> {
            getSelected(star1, star2, star3, star4, star5,
                    true, true, true, false, false);
        });
        star4.setOnClickListener(v -> {
            getSelected(star1, star2, star3, star4, star5,
                    true, true, true, true, false);
        });
        star5.setOnClickListener(v -> {
            getSelected(star1, star2, star3, star4, star5,
                    true, true, true, true, true);
        });

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();

        AlertDialog dialog = builder.create();
        dialog.show();

        rate.setOnClickListener(v->{
            int rating;
            if (star5.isChecked()) {
                rating = 5;
            } else if (star4.isChecked()) {
                rating = 4;
            } else if (star3.isChecked()) {
                rating = 3;
            } else if (star2.isChecked()) {
                rating = 2;
            } else if (star1.isChecked()) {
                rating = 1;
            } else {
                rating = 0;
            }

            Reviewers reviewer = new Reviewers(users.getUid(), format.format(date),
                    content.getText().toString(), valueOf(rating));

            if(rating !=0) {
                if(reviewers!=null && reviewers.size()!=0) {
                    repository.deleteReview(id, reviewers.get(0));
                }
                repository.updateReview(id, reviewer);
            }
            dialog.hide();
        });
    }

    private void getSelected(CheckedTextView star1, CheckedTextView star2,
                             CheckedTextView star3, CheckedTextView star4,
                             CheckedTextView star5, boolean isChecked1, boolean isChecked2,
                             boolean isChecked3, boolean isChecked4, boolean isChecked5) {
        star1.setChecked(isChecked1);
        star2.setChecked(isChecked2);
        star3.setChecked(isChecked3);
        star4.setChecked(isChecked4);
        star5.setChecked(isChecked5);
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
        ReviewersAdapter reviewersAdapter = new ReviewersAdapter(reviewers.getReviewers(), layoutBookATrip);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, HORIZONTAL, false);
        binding.layoutBookATrip.rvReviews.setLayoutManager(layoutManager);
        binding.layoutBookATrip.rvReviews.setAdapter(reviewersAdapter);

        onListener(reviewersAdapter, reviewers.getReviewers());
    }

    private void onListener(ReviewersAdapter scheduleAdapter, List<Reviewers> schedules) {
        scheduleAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(view.getId()== R.id.ivMore){
                    if(schedules.get(position).getUid().equals(user.getUid())){
                        getPopup(schedule, user);
                    }
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
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