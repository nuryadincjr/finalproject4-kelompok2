package com.nuryadincjr.ebusantara.dataview;

import static com.nuryadincjr.ebusantara.util.Constant.*;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.ActivityTicketDetailsBinding;
import com.nuryadincjr.ebusantara.pojo.Buses;
import com.nuryadincjr.ebusantara.pojo.Cities;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.pojo.Transactions;
import com.nuryadincjr.ebusantara.pojo.TransactionsReference;
import com.nuryadincjr.ebusantara.pojo.Users;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TicketDetailsActivity extends AppCompatActivity {
    private ActivityTicketDetailsBinding binding;
    private ScheduleReference schedule;
    private Transactions transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_details);

        binding = ActivityTicketDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TransactionsReference reference = getIntent().getParcelableExtra("transaction");
        Users users = getIntent().getParcelableExtra("user");

        transactions = reference.getTransactions();
        schedule = reference.getReference();
        Buses buses = schedule.getBuses();
        Cities departureCity = schedule.getDeparture();
        Cities arrivalCity = schedule.getArrival();

        binding.tvDate.setText(transactions.getDate());
        binding.tvBookNo.setText("Book No. "+transactions.getBookNo());
        binding.tvName.setText(users.getName());
        binding.tvPhoneNumber.setText(users.getPhoneNumber());
        binding.tvSeats.setText(String.valueOf(transactions.getSeatNo().size()));
        binding.tvStatus.setText(transactions.getStatus());
        binding.tvPOName.setText(buses.getPoName());
        binding.tvBusNo.setText(buses.getBusNo());
        binding.tvDeparture.setText(departureCity.getCity());
        binding.tvTerminalDeparture.setText(departureCity.getTerminal());
        binding.tvArrival.setText(arrivalCity.getCity());
        binding.tvTerminalArrival.setText(arrivalCity.getTerminal());
        binding.tvSeatsNo.setText(transactions.getSeatNo().toString());
        binding.tvTotal.setText(transactions.getTotalPayment());
        binding.ivQrCode.setOnClickListener(this::onShowPopup);

        getTime();
        getEstimatedTimes();
        binding.ivBackArrow.setOnClickListener(v -> onBackPressed());
    }

    @SuppressLint("InflateParams")
    public void onShowPopup(View view) {
        View inflatedView = getLayoutInflater().inflate(R.layout.layout_qrcode, null);
        ImageView imageView = inflatedView.findViewById(R.id.ivQrCode);
        imageView.setImageBitmap(getQrCode(transactions.getBookNo()));
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setView(inflatedView);
        builder.show();
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

        binding.tvDepartureDate.setText(formatDate.format(departureDate));
        binding.tvArrivalDate.setText(formatDate.format(arrivalDate));

        binding.tvDepartureTime.setText(formatTime.format(departureDate));
        binding.tvArrivalTime.setText(formatTime.format(arrivalDate));
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
        binding.tvEstimation.setText(estimatedTime);
    }
}