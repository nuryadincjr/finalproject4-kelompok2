package com.nuryadincjr.ebusantara.dataview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.ActivitySeatChooserBinding;
import com.nuryadincjr.ebusantara.databinding.LayoutSeatsChooserBinding;
import com.nuryadincjr.ebusantara.pojo.Buses;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.pojo.Seats;
import com.nuryadincjr.ebusantara.payment.DetailPaymentActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeatChooserActivity extends AppCompatActivity
        implements View.OnClickListener {
    private ActivitySeatChooserBinding binding;
    private ScheduleReference schedule;
    private Calendar calendar;
    private String passengers;
    private Buses buses;
    private Seats seats;
    private Set<String> seatChooser;
    private List<Boolean> seatsA;
    private List<Boolean> seatsB;
    private List<Boolean> seatsC;
    private List<Boolean> seatsD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_chooser);
        binding = ActivitySeatChooserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        schedule = getIntent().getParcelableExtra("schedule");
        passengers = getIntent().getStringExtra("passengers");
        calendar =  (Calendar)getIntent().getSerializableExtra("date");
        buses = schedule.getBuses();
        seats = buses.getSeats();
        seatsA = seats.getSeatsA();
        seatsB = seats.getSeatsB();
        seatsC = seats.getSeatsC();
        seatsD = seats.getSeatsD();

        seatChooser = new HashSet<>();
        String displayStatus = "Selected: "+ seatChooser.size()+"/"+passengers;
        binding.tvStatus.setText(displayStatus);

        LayoutSeatsChooserBinding view = binding.layoutSeatChooser;
        view.ivSeatA1.setOnClickListener(this);
        view.ivSeatA2.setOnClickListener(this);
        view.ivSeatA3.setOnClickListener(this);
        view.ivSeatA4.setOnClickListener(this);
        view.ivSeatA5.setOnClickListener(this);
        view.ivSeatA6.setOnClickListener(this);
        view.ivSeatA7.setOnClickListener(this);
        view.ivSeatA8.setOnClickListener(this);
        view.ivSeatA9.setOnClickListener(this);

        view.ivSeatB1.setOnClickListener(this);
        view.ivSeatB2.setOnClickListener(this);
        view.ivSeatB3.setOnClickListener(this);
        view.ivSeatB4.setOnClickListener(this);
        view.ivSeatB5.setOnClickListener(this);
        view.ivSeatB6.setOnClickListener(this);
        view.ivSeatB7.setOnClickListener(this);
        view.ivSeatB8.setOnClickListener(this);
        view.ivSeatB9.setOnClickListener(this);
        view.ivSeatB10.setOnClickListener(this);

        view.ivSeatC1.setOnClickListener(this);
        view.ivSeatC2.setOnClickListener(this);
        view.ivSeatC3.setOnClickListener(this);
        view.ivSeatC4.setOnClickListener(this);
        view.ivSeatC5.setOnClickListener(this);
        view.ivSeatC6.setOnClickListener(this);
        view.ivSeatC7.setOnClickListener(this);
        view.ivSeatC8.setOnClickListener(this);
        view.ivSeatC9.setOnClickListener(this);
        view.ivSeatC10.setOnClickListener(this);

        view.ivSeatD1.setOnClickListener(this);
        view.ivSeatD2.setOnClickListener(this);
        view.ivSeatD3.setOnClickListener(this);
        view.ivSeatD4.setOnClickListener(this);
        view.ivSeatD5.setOnClickListener(this);
        view.ivSeatD6.setOnClickListener(this);
        view.ivSeatD7.setOnClickListener(this);
        view.ivSeatD8.setOnClickListener(this);
        view.ivSeatD9.setOnClickListener(this);
        view.ivSeatD10.setOnClickListener(this);

        getSeats();

        binding.ivBackArrow.setOnClickListener(v -> onBackPressed());
        binding.btnBookNow.setOnClickListener(v-> onStartActivity());
    }

    private void getSeats() {
        if (seatsA != null || seatsB != null || seatsC != null || seatsD != null) {
            LayoutSeatsChooserBinding view = binding.layoutSeatChooser;

            getSelected(seatsA, view.ivSeatA1, view.ivSeatA2,
                    view.ivSeatA3, view.ivSeatA4, view.ivSeatA5, view.ivSeatA6,
                    view.ivSeatA7, view.ivSeatA8, view.ivSeatA9, null);

            getSelected(seatsB, view.ivSeatB1, view.ivSeatB2,
                    view.ivSeatB3, view.ivSeatB4, view.ivSeatB5, view.ivSeatB6,
                    view.ivSeatB7, view.ivSeatB8, view.ivSeatB9, view.ivSeatB10);

            getSelected(seatsC, view.ivSeatC1, view.ivSeatC2,
                    view.ivSeatC3, view.ivSeatC4, view.ivSeatC5, view.ivSeatC6,
                    view.ivSeatC7, view.ivSeatC8, view.ivSeatC9, view.ivSeatC10);

            getSelected(seatsD, view.ivSeatD1, view.ivSeatD2,
                    view.ivSeatD3, view.ivSeatD4, view.ivSeatD5, view.ivSeatD6,
                    view.ivSeatD7, view.ivSeatD8, view.ivSeatD9, view.ivSeatD10);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void getSelected(List<Boolean> booleanList,
                             CheckedTextView seat1, CheckedTextView seat2,
                             CheckedTextView seat3, CheckedTextView seat4,
                             CheckedTextView seat5, CheckedTextView seat6,
                             CheckedTextView seat7, CheckedTextView seat8,
                             CheckedTextView seat9, CheckedTextView seat10) {

        for (int i = 0; i < booleanList.size(); i++) {
            if (!booleanList.get(i)) {
                switch (i) {
                    case 0:
                        seat1.setEnabled(false);
                        break;
                    case 1:
                        seat2.setEnabled(false);
                        break;
                    case 2:
                        seat3.setEnabled(false);
                        break;
                    case 3:
                        seat4.setEnabled(false);
                        break;
                    case 4:
                        seat5.setEnabled(false);
                        break;
                    case 5:
                        seat6.setEnabled(false);
                        break;
                    case 6:
                        seat7.setEnabled(false);
                        break;
                    case 7:
                        seat8.setEnabled(false);
                        break;
                    case 8:
                        seat9.setEnabled(false);
                        break;
                    case 9:
                        seat10.setEnabled(false);
                        break;
                }
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        LayoutSeatsChooserBinding layoutSeatChooser = binding.layoutSeatChooser;
        switch (v.getId()) {
            case R.id.ivSeatA1:
                getChooserListener(layoutSeatChooser.ivSeatA1, "A", 1);
                break;
            case R.id.ivSeatA2:
                getChooserListener(layoutSeatChooser.ivSeatA2, "A", 2);
                break;
            case R.id.ivSeatA3:
                getChooserListener(layoutSeatChooser.ivSeatA3, "A", 3);
                break;
            case R.id.ivSeatA4:
                getChooserListener(layoutSeatChooser.ivSeatA4, "A", 4);
                break;
            case R.id.ivSeatA5:
                getChooserListener(layoutSeatChooser.ivSeatA5, "A", 5);
                break;
            case R.id.ivSeatA6:
                getChooserListener(layoutSeatChooser.ivSeatA6, "A", 6);
                break;
            case R.id.ivSeatA7:
                getChooserListener(layoutSeatChooser.ivSeatA7, "A", 7);
                break;
            case R.id.ivSeatA8:
                getChooserListener(layoutSeatChooser.ivSeatA8, "A", 8);
                break;
            case R.id.ivSeatA9:
                getChooserListener(layoutSeatChooser.ivSeatA9, "A", 9);
                break;

            case R.id.ivSeatB1:
                getChooserListener(layoutSeatChooser.ivSeatB1, "B", 1);
                break;
            case R.id.ivSeatB2:
                getChooserListener(layoutSeatChooser.ivSeatB2, "B", 2);
                break;
            case R.id.ivSeatB3:
                getChooserListener(layoutSeatChooser.ivSeatB3, "B", 3);
                break;
            case R.id.ivSeatB4:
                getChooserListener(layoutSeatChooser.ivSeatB4, "B", 4);
                break;
            case R.id.ivSeatB5:
                getChooserListener(layoutSeatChooser.ivSeatB5, "B", 5);
                break;
            case R.id.ivSeatB6:
                getChooserListener(layoutSeatChooser.ivSeatB6, "B", 6);
                break;
            case R.id.ivSeatB7:
                getChooserListener(layoutSeatChooser.ivSeatB7, "B", 7);
                break;
            case R.id.ivSeatB8:
                getChooserListener(layoutSeatChooser.ivSeatB8, "B", 8);
                break;
            case R.id.ivSeatB9:
                getChooserListener(layoutSeatChooser.ivSeatB9, "B", 9);
                break;
            case R.id.ivSeatB10:
                getChooserListener(layoutSeatChooser.ivSeatB10, "B", 10);
                break;

            case R.id.ivSeatC1:
                getChooserListener(layoutSeatChooser.ivSeatC1, "C", 1);
                break;
            case R.id.ivSeatC2:
                getChooserListener(layoutSeatChooser.ivSeatC2, "C", 2);
                break;
            case R.id.ivSeatC3:
                getChooserListener(layoutSeatChooser.ivSeatC3, "C", 3);
                break;
            case R.id.ivSeatC4:
                getChooserListener(layoutSeatChooser.ivSeatC4, "C", 4);
                break;
            case R.id.ivSeatC5:
                getChooserListener(layoutSeatChooser.ivSeatC5, "C", 5);
                break;
            case R.id.ivSeatC6:
                getChooserListener(layoutSeatChooser.ivSeatC6, "C",6);
                break;
            case R.id.ivSeatC7:
                getChooserListener(layoutSeatChooser.ivSeatC7, "C", 7);
                break;
            case R.id.ivSeatC8:
                getChooserListener(layoutSeatChooser.ivSeatC8, "C", 8);
                break;
            case R.id.ivSeatC9:
                getChooserListener(layoutSeatChooser.ivSeatC9, "C", 9);
                break;
            case R.id.ivSeatC10:
                getChooserListener(layoutSeatChooser.ivSeatC10, "C", 10);
                break;

            case R.id.ivSeatD1:
                getChooserListener(layoutSeatChooser.ivSeatD1, "D", 1);
                break;
            case R.id.ivSeatD2:
                getChooserListener(layoutSeatChooser.ivSeatD2, "D", 2);
                break;
            case R.id.ivSeatD3:
                getChooserListener(layoutSeatChooser.ivSeatD3, "D", 3);
                break;
            case R.id.ivSeatD4:
                getChooserListener(layoutSeatChooser.ivSeatD4, "D", 4);
                break;
            case R.id.ivSeatD5:
                getChooserListener(layoutSeatChooser.ivSeatD5, "D", 5);
                break;
            case R.id.ivSeatD6:
                getChooserListener(layoutSeatChooser.ivSeatD6, "D", 6);
                break;
            case R.id.ivSeatD7:
                getChooserListener(layoutSeatChooser.ivSeatD7, "D", 7);
                break;
            case R.id.ivSeatD8:
                getChooserListener(layoutSeatChooser.ivSeatD8, "D",8);
                break;
            case R.id.ivSeatD9:
                getChooserListener(layoutSeatChooser.ivSeatD9, "D", 9);
                break;
            case R.id.ivSeatD10:
                getChooserListener(layoutSeatChooser.ivSeatD10, "D", 10);
                break;
        }
    }

    private void getChooserListener(CheckedTextView view, String itemX, int itemY) {
        String seatNo = itemX+itemY;
        if (seatChooser.size() < Integer.parseInt(passengers)) {
            if(view.isChecked()) seatChooser.remove(seatNo);
            else seatChooser.add(seatNo);

            view.setChecked(!view.isChecked());
            onSeatSelected(itemX, itemY, !view.isChecked());
        }else {
            view.setChecked(false);
            seatChooser.remove(seatNo);
            onSeatSelected(itemX, itemY, false);
        }

        String displayStatus = "Selected: "+ seatChooser.size()+"/"+passengers;
        binding.tvStatus.setText(displayStatus);
    }

    private void onSeatSelected(String itemX, int itemY, boolean isSelected) {
        int index = itemY-1;
        switch (itemX) {
            case "A":
                seatsA.set(index, isSelected);
                break;
            case "B":
                seatsB.set(index, isSelected);
                break;
            case "C":
                seatsC.set(index, isSelected);
                break;
            case "D":
                seatsD.set(index, isSelected);
                break;
        }
    }

    private void onStartActivity() {
        if(seatChooser.size()==Integer.parseInt(passengers)){
            ArrayList<String> seatList = new ArrayList<>(seatChooser);
            seats.setSeatsA(seatsA);
            seats.setSeatsB(seatsB);
            seats.setSeatsC(seatsC);
            seats.setSeatsD(seatsD);
            buses.setSeats(seats);
            schedule.setBuses(buses);

            startActivity(new Intent(this,
                    DetailPaymentActivity.class)
                    .putExtra("schedule", schedule)
                    .putExtra("date", calendar)
                    .putExtra("passengers", passengers)
                    .putStringArrayListExtra("seats", seatList));
        }else {
            int available = Integer.parseInt(passengers) - seatChooser.size();
            Snackbar.make(binding.getRoot(),
                    "Please choose "+available+" more seats ",
                    Snackbar.LENGTH_SHORT).show();
        }
    }
}