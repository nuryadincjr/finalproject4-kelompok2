package com.nuryadincjr.ebusantara.payment;

import static java.lang.Double.parseDouble;
import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static java.lang.String.valueOf;
import static java.text.NumberFormat.getCurrencyInstance;
import static java.util.Collections.sort;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.ActivityDetailPaymentBinding;
import com.nuryadincjr.ebusantara.databinding.LayoutCompleteBookingBinding;
import com.nuryadincjr.ebusantara.pojo.Buses;
import com.nuryadincjr.ebusantara.pojo.Cities;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.pojo.Seats;
import com.nuryadincjr.ebusantara.pojo.Users;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DetailPaymentActivity extends AppCompatActivity {
    private ActivityDetailPaymentBinding binding;
    private ScheduleReference schedule;
    private Cities departureCity;
    private Cities arrivalCity;
    private Calendar calendar;
    private String passengers;
    private Buses buses;
    private Seats seats;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private Users users;
    private ArrayList<String> seatChooser;
    private LayoutCompleteBookingBinding view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_payment);

        binding = ActivityDetailPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        calendar =  (Calendar)getIntent().getSerializableExtra("date");
        schedule = getIntent().getParcelableExtra("schedule");
        passengers = getIntent().getStringExtra("passengers");
        seatChooser = getIntent().getStringArrayListExtra("seats");
        departureCity = schedule.getDeparture();
        arrivalCity = schedule.getArrival();
        buses = schedule.getBuses();
        seats = buses.getSeats();
        sort(seatChooser, CASE_INSENSITIVE_ORDER);

        view = binding.layoutCompleteBooking;
        view.tvSeatsNo.setText(valueOf(seatChooser));
        view.tvSeats.setText(passengers);
        view.tvPOName.setText(buses.getPoName());
        view.tvBusNo.setText(buses.getBusNo());
        view.tvDeparture.setText(departureCity.getCity());
        view.tvTerminalDeparture.setText(departureCity.getTerminal());
        view.tvDepartureTime.setText(schedule.getDepartureTime());
        view.tvArrival.setText(arrivalCity.getCity());
        view.tvTerminalArrival.setText(arrivalCity.getTerminal());
        view.tvArrivalTime.setText(schedule.getArrivalTime());

        Random rand = new Random();
        int code = rand.nextInt(999999);
        view.tvUniqueCode.setText(valueOf(code));

        getPieces();
        getUsers();
        getEstimatedTimes();
        binding.ivBackArrow.setOnClickListener(v -> onBackPressed());
        binding.btmContinue.setOnClickListener(v -> {
            ArrayList<String> seatList = new ArrayList<>(seatChooser);
            startActivity(new Intent(this,
                    PaymentActivity.class)
                    .putExtra("schedule", schedule)
                    .putExtra("date", calendar)
                    .putExtra("passengers", passengers)
                    .putStringArrayListExtra("seats", seatList)
                    .putExtra("total", view.tvTotal.getText()));
        });
    }

    private void getUsers() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.document("users/"+currentUser.getUid()).get().addOnCompleteListener(task -> {
           if(task.isComplete()){
               users = task.getResult().toObject(Users.class);
               if(users!=null){
                   view.tvName.setText(users.getName());
                   view.tvPhoneNumber.setText(users.getPhoneNumber());
               }
           }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void getEstimatedTimes()  {
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
        format = new SimpleDateFormat("EEE, d MMM yyyy");
        view.tvDepartureDate.setText(format.format(calendar.getTime()));
        
        calendar.set(Calendar.HOUR_OF_DAY, firstTime.getHours());
        calendar.set(Calendar.MINUTE,  firstTime.getMinutes());
        calendar.set(Calendar.SECOND, 0);

        calendar.add(Calendar.HOUR_OF_DAY, secondTime.getHours());
        calendar.add(Calendar.MINUTE, secondTime.getMinutes());
        
        view.tvArrivalDate.setText(format.format(calendar.getTime()));

        long millisTime = secondTime.getTime() - firstTime.getTime();
        if(firstTime.getTime() > secondTime.getTime()){
            millisTime = firstTime.getTime() - secondTime.getTime();
        }

        long minutes = TimeUnit.MILLISECONDS.toMinutes(millisTime);
        long hours = TimeUnit.MILLISECONDS.toHours(millisTime);

        String estimatedTime = hours+"h"+minutes+"m";
        view.tvEstimation.setText(estimatedTime);
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
        view.tvTickets.setText(displaySubTotal);
        view.tvTotal.setText(format.format(subTotal));
    }
}