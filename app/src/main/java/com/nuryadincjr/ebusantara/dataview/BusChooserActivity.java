package com.nuryadincjr.ebusantara.dataview;

import static com.nuryadincjr.ebusantara.databinding.ActivityBusChooserBinding.inflate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.adapters.ScheduleAdapter;
import com.nuryadincjr.ebusantara.databinding.ActivityBusChooserBinding;
import com.nuryadincjr.ebusantara.interfaces.ItemClickListener;
import com.nuryadincjr.ebusantara.pojo.Cities;
import com.nuryadincjr.ebusantara.models.MainViewModel;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class BusChooserActivity extends AppCompatActivity {

    private ActivityBusChooserBinding binding;
    private Cities departureCity;
    private Cities arrivalCity;
    private Calendar calendar;
    private String passengers;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_chooser);

        binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        binding.ivBackArrow.setOnClickListener(v -> onBackPressed());

        departureCity = getIntent().getParcelableExtra("departureCity");
        arrivalCity = getIntent().getParcelableExtra("arrivalCity");
        passengers = getIntent().getStringExtra("passengers");
        calendar =  (Calendar)getIntent().getSerializableExtra("date");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy");

        String displayPassengers = "Seat " +passengers;
        binding.tvSeats.setText(displayPassengers);
        binding.tvDeparture.setText(departureCity.getCity());
        binding.tvArrival.setText(arrivalCity.getCity());
        binding.tvDate.setText(format.format(calendar.getTime()));

        getData();
    }

    private void getData() {
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getBuses("schedule", departureCity.getCity(), arrivalCity.getCity()).observe(this, schedules -> {

            ScheduleAdapter scheduleAdapter = new ScheduleAdapter(schedules, Integer.parseInt(passengers));
            binding.rvBuses.setLayoutManager(new LinearLayoutManager(this));
            binding.rvBuses.setAdapter(scheduleAdapter);

            onListener(scheduleAdapter, schedules);
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