package com.nuryadincjr.ebusantara.dataview;

import static com.nuryadincjr.ebusantara.databinding.ActivityBusChooserBinding.inflate;
import static java.lang.Double.compare;
import static java.lang.Double.parseDouble;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Collections.sort;
import static java.util.Comparator.comparingDouble;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.adapters.ScheduleAdapter;
import com.nuryadincjr.ebusantara.chooser.DatePickerActivity;
import com.nuryadincjr.ebusantara.chooser.DestinationChooserActivity;
import com.nuryadincjr.ebusantara.databinding.ActivityBusChooserBinding;
import com.nuryadincjr.ebusantara.interfaces.ItemClickListener;
import com.nuryadincjr.ebusantara.pojo.Cities;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.util.MainViewModel;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BusChooserActivity extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private ActivityBusChooserBinding binding;
    private Cities departureCity;
    private Cities arrivalCity;
    private Calendar calendar;
    private String passengers;
    private SimpleDateFormat format;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_chooser);

        binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        departureCity = getIntent().getParcelableExtra("departure_city");
        arrivalCity = getIntent().getParcelableExtra("arrival_city");
        passengers = getIntent().getStringExtra("passengers");
        calendar =  (Calendar)getIntent().getSerializableExtra("date");
        format = new SimpleDateFormat("EEE, d MMM yyyy");

        String displayPassengers = "Seat " +passengers;
        binding.tvSeats.setText(displayPassengers);
        binding.tvDeparture.setText(departureCity.getCity());
        binding.tvArrival.setText(arrivalCity.getCity());
        binding.tvDate.setText(format.format(calendar.getTime()));

        getData();

        binding.ivBackArrow.setOnClickListener(this);
        binding.tvDeparture.setOnClickListener(this);
        binding.tvArrival.setOnClickListener(this);
        binding.tvSeats.setOnClickListener(this);
        binding.tvDate.setOnClickListener(this);
        binding.layoutSlidingUp.btnSelected.setOnClickListener(this);
        binding.layoutSlidingUp.btnCancel.setOnClickListener(this);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sort,
                android.R.layout.simple_spinner_dropdown_item);
        binding.tvFilters.setAdapter(adapter);
        binding.tvFilters.setOnItemSelectedListener(this);

        binding.getRoot().setFadeOnClickListener(view ->
                binding.getRoot().setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN));

        binding.layoutSlidingUp.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.layoutSlidingUp.tvPassenger.setText(valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getData() {
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getBuses(departureCity.getCity(), arrivalCity.getCity(),
                calendar).observe(this, schedules -> {
            if(schedules!=null){
                binding.rvBuses.setVisibility(View.VISIBLE);
                binding.layoutError.linearLayout.setVisibility(View.GONE);

                boolean isLowestPiece = binding.tvFilters.getSelectedItem().toString().equals("Lowest price");
                boolean isHighestPiece = binding.tvFilters.getSelectedItem().toString().equals("Highest price");
                boolean isEstimation = binding.tvFilters.getSelectedItem().toString().equals("Estimate time");

                if(isLowestPiece){
                    sort(schedules, comparingDouble(o -> parseDouble(o.getBuses().getPrice())));
                }else if(isHighestPiece){
                    sort(schedules, (o1, o2) ->
                            compare(parseDouble(o2.getBuses().getPrice()),
                            parseDouble(o1.getBuses().getPrice())));
                }else if(isEstimation){
                    sort(schedules, comparingDouble(this::getEstimatedTimes));
                }else {
                    sort(schedules, (o1, o2) ->
                            o2.getReviewers().getRatingsCount()
                                    .compareTo(o1.getReviewers().getRatingsCount()));
                }

                String seat = binding.tvSeats.getText().toString().replace("Seat ", "");
                ScheduleAdapter scheduleAdapter = new ScheduleAdapter(schedules, Integer.parseInt(seat));
                binding.rvBuses.setLayoutManager(new LinearLayoutManager(this));
                binding.rvBuses.setAdapter(scheduleAdapter);
                onListener(scheduleAdapter, schedules);
            }else {
                binding.rvBuses.setVisibility(View.GONE);
                binding.layoutError.textView.setText("Sorry!");
                binding.layoutError.tvMassage.setText("The destination location you selected was not found");
                binding.layoutError.linearLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @SuppressLint("SimpleDateFormat, DefaultLocale")
    private int getEstimatedTimes(ScheduleReference schedule) {
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

        long minutes = TimeUnit.MILLISECONDS.toMinutes(millisTime) % 60;
        long hours = TimeUnit.MILLISECONDS.toHours(millisTime);
        String estimatedTime = format("%s%d", hours, minutes);

       return Integer.parseInt(estimatedTime);
    }

    private void onListener(ScheduleAdapter scheduleAdapter, ArrayList<ScheduleReference> schedules) {
        scheduleAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(view.getId()==R.id.btnBookNow){
                    startActivity(new Intent(getApplicationContext(),
                            BusDetailsActivity.class)
                            .putExtra("schedule", schedules.get(position))
                            .putExtra("date", calendar)
                            .putExtra("passengers", passengers));
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, DestinationChooserActivity.class);
        switch (v.getId()){
            case R.id.ivBackArrow:
                onBackPressed();
                break;
            case R.id.tvDeparture:
                startActivityForResult(intent, 1);
                break;
            case R.id.tvArrival:
                startActivityForResult(intent, 2);
                break;
            case R.id.tvDate:
                startActivityForResult(new Intent(this, DatePickerActivity.class), 3);
                break;
            case R.id.tvSeats:
                binding.slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                break;
            case R.id.btnSelected:
                String passenger = "Seat "+binding.layoutSlidingUp.tvPassenger.getText();
                binding.tvSeats.setText(passenger);
                binding.getRoot().setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                break;
            case R.id.btnCancel:
                binding.getRoot().setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                break;
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (data != null && resultCode == RESULT_OK) {
                    departureCity = data.getParcelableExtra("city");
                    binding.tvDeparture.setText(departureCity.getCity());
                }
                break;
            case 2:
                if (data != null && resultCode == RESULT_OK) {
                    arrivalCity = data.getParcelableExtra("city");
                    binding.tvArrival.setText(arrivalCity.getCity());
                }
                break;
            case 3:
                if (data != null && resultCode == RESULT_OK) {
                    calendar = (Calendar) data.getSerializableExtra("date");
                    binding.tvDate.setText(format.format(calendar.getTime()));
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        getData();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}