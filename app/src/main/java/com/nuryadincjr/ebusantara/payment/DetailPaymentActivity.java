package com.nuryadincjr.ebusantara.payment;

import static com.nuryadincjr.ebusantara.databinding.ActivityDetailPaymentBinding.inflate;
import static com.nuryadincjr.ebusantara.util.LocalPreference.getInstance;
import static java.lang.Double.parseDouble;
import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static java.lang.String.valueOf;
import static java.text.NumberFormat.getCurrencyInstance;
import static java.util.Collections.sort;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.ActivityDetailPaymentBinding;
import com.nuryadincjr.ebusantara.databinding.LayoutCompleteBookingBinding;
import com.nuryadincjr.ebusantara.pojo.Buses;
import com.nuryadincjr.ebusantara.pojo.Cities;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.pojo.Transactions;
import com.nuryadincjr.ebusantara.pojo.Users;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DetailPaymentActivity extends AppCompatActivity {
    private LayoutCompleteBookingBinding bookingBinding;
    private ArrayList<String> seatChooser;
    private ScheduleReference schedule;
    private String passengers;
    private Buses buses;
    private Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_payment);

        ActivityDetailPaymentBinding binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        schedule = getIntent().getParcelableExtra("schedule");
        passengers = getIntent().getStringExtra("passengers");
        seatChooser = getIntent().getStringArrayListExtra("seats");
        Cities departureCity = schedule.getDeparture();
        Cities arrivalCity = schedule.getArrival();
        buses = schedule.getBuses();
        sort(seatChooser, CASE_INSENSITIVE_ORDER);

        bookingBinding = binding.layoutCompleteBooking;
        bookingBinding.tvSeatsNo.setText(valueOf(seatChooser));
        bookingBinding.tvSeats.setText(passengers);
        bookingBinding.tvPOName.setText(buses.getPoName());
        bookingBinding.tvBusNo.setText(buses.getBusNo());
        bookingBinding.tvDeparture.setText(departureCity.getCity());
        bookingBinding.tvTerminalDeparture.setText(departureCity.getTerminal());
        bookingBinding.tvArrival.setText(arrivalCity.getCity());
        bookingBinding.tvTerminalArrival.setText(arrivalCity.getTerminal());

        Random random = new Random();
        int firstCode = random.nextInt(999999);
        int secondCode = random.nextInt(9999);
        String uniqueCode = firstCode+"-"+secondCode;
        bookingBinding.tvUniqueCode.setText(uniqueCode);

        getPieces();
        getUsers();
        getTime();
        getEstimatedTimes();
        binding.ivBackArrow.setOnClickListener(v -> onBackPressed());
        binding.btmContinue.setOnClickListener(this::onClickContinue);
    }

    @SuppressLint("SimpleDateFormat")
    private void getTime() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatDate = new SimpleDateFormat("d MMM yyyy");

        Date departureDate = new Date();
        Date arrivalDate = new Date();
        try {
            departureDate = format.parse(schedule.getDepartureTime());
            arrivalDate = format.parse(schedule.getArrivalTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        bookingBinding.tvDepartureDate.setText(formatDate.format(departureDate));
        bookingBinding.tvArrivalDate.setText(formatDate.format(arrivalDate));

        bookingBinding.tvDepartureTime.setText(formatTime.format(departureDate));
        bookingBinding.tvArrivalTime.setText(formatTime.format(arrivalDate));
    }

    private void getUsers() {
        SharedPreferences preference = getInstance(this).getPreferences();
        String uid = preference.getString("uid", "");
        String name = preference.getString("name", "");
        String email = preference.getString("email", "");
        String phone = preference.getString("phone", "");
        String photo = preference.getString("photo", "");
        users = new Users(uid, name, phone, email, photo);

       bookingBinding.tvName.setText(users.getName());
       bookingBinding.tvPhoneNumber.setText(users.getPhoneNumber());
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

        long minutes = TimeUnit.MILLISECONDS.toMinutes(millisTime) % 60;
        long hours = TimeUnit.MILLISECONDS.toHours(millisTime);
        String estimatedTime;
        if (hours > 0) {
            estimatedTime = hours+"h"+minutes+"m";
        } else estimatedTime = minutes+"m";

        bookingBinding.tvEstimation.setText(estimatedTime);
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
        bookingBinding.tvTickets.setText(displaySubTotal);
        bookingBinding.tvTotal.setText(format.format(subTotal));
    }

    @SuppressLint("SimpleDateFormat")
    private void onClickContinue(View v) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
        Date date = new Date();
        Transactions transactions = new Transactions();
        String uniqueCode = bookingBinding.tvUniqueCode.getText().toString();

        transactions.setDate(valueOf(format.format(date.getTime())));
        transactions.setUid(users.getUid());
        transactions.setSchedule(schedule.getId());
        transactions.setSeatNo(seatChooser);
        transactions.setStatus("issued");
        transactions.setBookNo(uniqueCode);
        transactions.setTotalPayment(bookingBinding.tvTotal.getText().toString());

        startActivity(new Intent(this,
                PaymentActivity.class)
                .putExtra("transactions", transactions)
                .putExtra("schedule", schedule));
    }
}