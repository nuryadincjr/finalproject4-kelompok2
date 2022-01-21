package com.nuryadincjr.ebusantara.dataview;

import static com.nuryadincjr.ebusantara.databinding.ActivityBusChooserBinding.inflate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.adapters.ScheduleAdapter;
import com.nuryadincjr.ebusantara.databinding.ActivityBusChooserBinding;
import com.nuryadincjr.ebusantara.interfaces.ItemClickListener;
import com.nuryadincjr.ebusantara.pojo.Cities;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.util.MainViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class BusChooserActivity extends AppCompatActivity {
    private ActivityBusChooserBinding binding;
    private Cities departureCity;
    private Cities arrivalCity;
    private Calendar calendar;
    private String passengers;

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
        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy");

        String displayPassengers = "Seat " +passengers;
        binding.tvSeats.setText(displayPassengers);
        binding.tvDeparture.setText(departureCity.getCity());
        binding.tvArrival.setText(arrivalCity.getCity());
        binding.tvDate.setText(format.format(calendar.getTime()));

        binding.ivBackArrow.setOnClickListener(v -> onBackPressed());
        getData();
    }

    private void getData() {
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getBuses(departureCity.getCity(), arrivalCity.getCity(),
                calendar).observe(this, schedules -> {
            if(schedules!=null){
                ScheduleAdapter scheduleAdapter = new ScheduleAdapter(schedules, Integer.parseInt(passengers));
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
}