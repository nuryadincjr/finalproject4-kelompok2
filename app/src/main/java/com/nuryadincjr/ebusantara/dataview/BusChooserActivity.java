package com.nuryadincjr.ebusantara.dataview;

import static com.nuryadincjr.ebusantara.databinding.ActivityBusChooserBinding.inflate;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.adapters.ScheduleAdapter;
import com.nuryadincjr.ebusantara.databinding.ActivityBusChooserBinding;
import com.nuryadincjr.ebusantara.models.Cities;
import com.nuryadincjr.ebusantara.models.Schedule;
import com.nuryadincjr.ebusantara.models.MainViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BusChooserActivity extends AppCompatActivity {

    private ActivityBusChooserBinding binding;
    private Cities departureCity;
    private Cities arrivalCity;
    private Calendar calendar;
    private String passengers;
    private String TAG = "xxx";
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
        calendar =  (Calendar)getIntent().getSerializableExtra("date");
        passengers = "Seat " +getIntent().getStringExtra("passengers");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy");

        binding.tvDeparture.setText(departureCity.getCity());
        binding.tvArrival.setText(arrivalCity.getCity());
        binding.tvDate.setText(format.format(calendar.getTime()));
        binding.tvSeats.setText(passengers);

        getData();
    }

    private void getData() {
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getBuses("schedule", departureCity.getCity(), arrivalCity.getCity()).observe(this, schedules -> {

            ScheduleAdapter scheduleAdapter = new ScheduleAdapter(schedules);
            binding.rvBuses.setLayoutManager(new LinearLayoutManager(this));
            binding.rvBuses.setAdapter(scheduleAdapter);




//            onListener(scheduleAdapter, schedules);
        });

//        Runnable runnable = () -> {
//            Task<QuerySnapshot> c1 = db.collection("cities")
//                    .whereEqualTo("city", departureCity.getCity()).get();
//
//            List<String> departureIdList = new ArrayList<>();
//            c1.addOnCompleteListener(task -> {
//                if(task.isSuccessful()){
//                    for (QueryDocumentSnapshot  snapshot : task.getResult()) {
//                        Cities data = snapshot.toObject(Cities.class);
//                        departureIdList.add(snapshot.getId());
//                        Log.d(TAG, snapshot.getId() + " 1=> " + snapshot.getData());
//                    }
//
//                    List<String> arrivalIdList = getStrings();
//                    List<Schedule> scheduleList = getSchedules(departureIdList, arrivalIdList);
//                }
//            });
//        };
//
//        Handler handler = new Handler(getMainLooper());
//        handler.postDelayed(runnable, 10000);
    }

    @NonNull
    private List<String> getStrings() {
        Task<QuerySnapshot> c2 = db.collection("cities")
                .whereEqualTo("city", arrivalCity.getCity()).get();

        List<String> arrivalIdList = new ArrayList<>();
        c2.addOnCompleteListener(task2 -> {
            if(task2.isSuccessful()){
                for (QueryDocumentSnapshot  snapshot : task2.getResult()) {
                    Cities data = snapshot.toObject(Cities.class);
                    arrivalIdList.add(snapshot.getId());
                    Log.d(TAG, snapshot.getId() + " 2=> " + snapshot.getData());
                }
            }
        });
        return arrivalIdList;
    }

    @NonNull
    private List<Schedule> getSchedules(List<String> departureIdList, List<String> arrivalIdList) {
        List<Schedule> scheduleList = new ArrayList<>();
        for(int i = 0; i< departureIdList.size(); i++){
            Task<QuerySnapshot> c3 = db.collection("schedule")
                    .whereEqualTo("departure", departureIdList.get(i))
                    .whereIn("arrival", arrivalIdList).get();

            c3.addOnCompleteListener(task3 -> {
                if(task3.isSuccessful()){
                    for (QueryDocumentSnapshot  snapshot : task3.getResult()) {
                        Schedule data = snapshot.toObject(Schedule.class);

                        Log.d(TAG, snapshot.getId() + " 3=> " + snapshot.getData());
                        scheduleList.add(data);
                    }
                }
            });
        }
        return scheduleList;
    }
}