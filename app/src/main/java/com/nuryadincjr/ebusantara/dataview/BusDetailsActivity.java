package com.nuryadincjr.ebusantara.dataview;

import static android.view.View.VISIBLE;
import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;
import static com.nuryadincjr.ebusantara.R.drawable.ic_brand;
import static com.nuryadincjr.ebusantara.R.id.ivViewer;
import static com.nuryadincjr.ebusantara.R.layout.activity_bus_details;
import static com.nuryadincjr.ebusantara.R.layout.layout_image_viewer;
import static com.nuryadincjr.ebusantara.databinding.ActivityBusDetailsBinding.*;
import static com.nuryadincjr.ebusantara.util.Constant.getEstimatedTimes;
import static com.nuryadincjr.ebusantara.util.Constant.getPieces;
import static com.nuryadincjr.ebusantara.util.Constant.getTime;
import static com.nuryadincjr.ebusantara.util.Constant.getUsers;
import static com.nuryadincjr.ebusantara.util.Constant.toUpperCase;
import static java.lang.String.valueOf;
import static java.util.Collections.sort;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.adapters.ReviewersAdapter;
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
import com.nuryadincjr.ebusantara.util.MainViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BusDetailsActivity extends AppCompatActivity {
    private LayoutBookATripBinding layoutBookATrip;
    private ScheduleReference scheduleReference;
    private String passengers;
    private Buses buses;
    private Seats seats;
    private Users user;
    private ReviewsRepository repository;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_bus_details);

        ActivityBusDetailsBinding binding = inflate(getLayoutInflater());
        layoutBookATrip = binding.layoutBookATrip;
        setContentView(binding.getRoot());

        scheduleReference = getIntent().getParcelableExtra("schedule");
        passengers = getIntent().getStringExtra("passengers");
        Cities departureCity = scheduleReference.getDeparture();
        Cities arrivalCity = scheduleReference.getArrival();
        ReviewersReference reviewers = scheduleReference.getReviewers();
        buses = scheduleReference.getBuses();
        seats = buses.getSeats();
        repository = new ReviewsRepository();
        user = getUsers(this);

        Calendar calendar = (Calendar) getIntent().getSerializableExtra("date");
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
        layoutBookATrip.tvEstimation.setText(getEstimatedTimes(scheduleReference));
        layoutBookATrip.tvDepartureTime.setText(getTime(scheduleReference).get("departureTime"));
        layoutBookATrip.tvArrivalTime.setText(getTime(scheduleReference).get("arrivalTime"));
        binding.tvSubTotals.setText(getPieces(buses, passengers).get("displaySubTotal"));
        binding.tvTotals.setText(getPieces(buses, passengers).get("subTotal"));

        getSeats();
        getImage();
        getReviewers();
        getFacilities();

        binding.ivBackArrow.setOnClickListener(v -> onBackPressed());
        binding.btnBookNow.setOnClickListener(v -> onStartActivity(calendar));
        layoutBookATrip.llRating.setOnClickListener(v -> getPopup(new Reviewers()));
        layoutBookATrip.tvSeePicture.setOnClickListener(v-> onImageShow(buses.getImageUrl()));
    }

    @SuppressLint("InflateParams")
    public void getPopup(Reviewers reviewer) {
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

        poName.setText(scheduleReference.getBuses().getPoName());
        busNo.setText(scheduleReference.getBuses().getBusNo());

        if(reviewer.getUid()!=null){
            content.setText(reviewer.getContent());
            switch (reviewer.getRatings()){
                case "1":
                    getSelected(star1, star2, star3, star4, star5,
                            !star1.isChecked(), false, false,
                            false, false);
                    break;
                case "2":
                    getSelected(star1, star2, star3, star4, star5,
                            true, true, false,
                            false, false);
                    break;
                case "3":
                    getSelected(star1, star2, star3, star4, star5,
                            true, true, true,
                            false, false);
                    break;
                case "4":
                    getSelected(star1, star2, star3, star4, star5,
                            true, true, true,
                            true, false);
                    break;
                case "5":
                    getSelected(star1, star2, star3, star4, star5,
                            true, true, true,
                            true, true);
                    break;
            }
        }
        onShowPopup(reviewer, layoutRating, maxLine, rate,
                content, star1, star2, star3, star4, star5);
    }

    @SuppressLint("SimpleDateFormat")
    private void onShowPopup(Reviewers reviewer, View inflatedView, TextView maxLine, TextView rate,
                             EditText content, CheckedTextView star1, CheckedTextView star2,
                             CheckedTextView star3, CheckedTextView star4, CheckedTextView star5) {

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

        star1.setOnClickListener(v -> getSelected(star1, star2, star3, star4, star5,
                !star1.isChecked(), false, false, false, false));
        star2.setOnClickListener(v -> getSelected(star1, star2, star3, star4, star5,
                true, true, false, false, false));
        star3.setOnClickListener(v -> getSelected(star1, star2, star3, star4, star5,
                true, true, true, false, false));
        star4.setOnClickListener(v -> getSelected(star1, star2, star3, star4, star5,
                true, true, true, true, false));
        star5.setOnClickListener(v -> getSelected(star1, star2, star3, star4, star5,
                true, true, true, true, true));

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

            String id = scheduleReference.getBuses().getId();

            if(rating !=0){
                repository.deleteReview(id, reviewer);

                reviewer.setUid(user.getUid());
                reviewer.setDate(format.format(date));
                reviewer.setContent(content.getText().toString());
                reviewer.setRatings(valueOf(rating));
                if(reviewer.getLikes()==null || reviewer.getLikes().size()==0){
                    reviewer.setLikes(new ArrayList<>());
                }

                repository.updateReview(id, reviewer);
                getReviewers();
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
                .putExtra("schedule", scheduleReference)
                .putExtra("date", calendar)
                .putExtra("passengers", passengers));
    }

    private void getSeats() {
        if(seats!=null){
            List<Boolean> seatsA = seats.getSeatsA();
            List<Boolean> seatsB = seats.getSeatsB();
            List<Boolean> seatsC = seats.getSeatsC();
            List<Boolean> seatsD = seats.getSeatsD();

            int counter = 0;
            if(seatsA != null) counter = getCounter(seatsA, counter);
            if(seatsB != null) counter = getCounter(seatsB, counter);
            if(seatsC != null) counter = getCounter(seatsC, counter);
            if(seatsD != null) counter = getCounter(seatsD, counter);

            if(counter < Integer.parseInt(passengers)){
                passengers = String.valueOf(counter);
            }
            String displaySeats = counter + " Seat are available";
            layoutBookATrip.tvSeatAvailable.setText(displaySeats);
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
                .into(layoutBookATrip.ivBus);
    }

    private void getFacilities() {
        List<String> facilityList = buses.getFacility();
        for(String facility:facilityList){
            switch (facility){
                case "Toilet":
                    layoutBookATrip.tvToiletFacility.setVisibility(VISIBLE);
                    break;
                case "Rest stop":
                    layoutBookATrip.tvRestFacility.setVisibility(VISIBLE);
                    break;
                case "Luggage":
                    layoutBookATrip.tvLuggageFacility.setVisibility(VISIBLE);
                    break;
                case "AC":
                    layoutBookATrip.tvACFacility.setVisibility(VISIBLE);
                    break;
            }
        }
    }

    private void onListener(ReviewersAdapter scheduleAdapter, List<Reviewers> reviewersList) {
        scheduleAdapter.setItemClickListener(new ItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onClick(View view, int position) {
                if(view.getId()== R.id.ivMore) onOptionMore(view, position, reviewersList);
                else if(view.getId()== R.id.ivProfile) onImagePreview(position, reviewersList);
                else if(view.getId()== R.id.tvReaction) onReaction(position, reviewersList);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
    }

    private void onReaction(int position, List<Reviewers> schedules) {
        repository.deleteReview(buses.getId(), schedules.get(position));
        List<String> newLike = new ArrayList<>(schedules.get(position).getLikes());
        if(newLike.contains(user.getUid())){
            newLike.remove(user.getUid());
        }else newLike.add(user.getUid());

        Reviewers reviewers = schedules.get(position);
        reviewers.setLikes(newLike);
        repository.updateReview(buses.getId(), reviewers);
        getReviewers();
    }

    private void onImagePreview(int position, List<Reviewers> schedules) {
        new ViewModelProvider(this)
                .get(MainViewModel.class)
                .getUsers(schedules.get(position).getUid())
                .observe(this, users -> onImageShow(users.getPhotoUrl()));
    }

    @SuppressLint("NonConstantResourceId")
    private void onOptionMore(View view, int position, List<Reviewers> reviewersList) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.menu_more_item);
        Menu menu = popupMenu.getMenu();
        if(!reviewersList.get(position).getUid().equals(user.getUid())){
            menu.findItem(R.id.itemEdit).setVisible(false);
            menu.findItem(R.id.itemDelete).setVisible(false);
        }else menu.findItem(R.id.itemReport).setVisible(false);

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.itemEdit:
                    getPopup(reviewersList.get(position));
                    return true;
                case R.id.itemDelete:
                    new MaterialAlertDialogBuilder(this)
                            .setTitle("Delete your review?")
                            .setNeutralButton("Cancel", (dialog, which) -> { })
                            .setNegativeButton("Ok", (dialog, which) -> {
                                repository.deleteReview(buses.getId(), reviewersList.get(position));
                                getReviewers();
                                layoutBookATrip.llRating.setVisibility(View.VISIBLE);
                            }).show();

                    return true;
                case R.id.itemReport:
                    return true;
                default:
                    return false;
            }
        });
        popupMenu.show();
    }

    private void onImageShow(String uri) {
        View inflatedView = getLayoutInflater().inflate(layout_image_viewer, null);
        ImageView imageView = inflatedView.findViewById(ivViewer);
        Glide.with(this)
                .load(uri)
                .placeholder(ic_brand)
                .into(imageView);
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setView(inflatedView).show();
    }

    private void getReviewers(){
        new ReviewsRepository().getReviewers(buses).observe(this, data -> {
            List<Reviewers> reviewersList = (List<Reviewers>) data.get("reviewer");
            if(reviewersList!=null){
                sort(reviewersList, (o1, o2) -> {
                    if(o1.getUid().equals(user.getUid())) return -1;
                    else return 1;
                });
            }
            onReviewers(reviewersList);
        });
    }

    private void onReviewers(List<Reviewers> reviewers) {
        ReviewersAdapter reviewersAdapter = new ReviewersAdapter(reviewers, layoutBookATrip);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, HORIZONTAL, false);
        layoutBookATrip.rvReviews.setLayoutManager(layoutManager);
        layoutBookATrip.rvReviews.setAdapter(reviewersAdapter);

        onListener(reviewersAdapter, reviewers);
    }
}