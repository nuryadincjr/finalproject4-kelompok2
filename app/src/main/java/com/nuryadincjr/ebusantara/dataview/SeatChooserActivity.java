package com.nuryadincjr.ebusantara.dataview;

import static com.google.android.material.snackbar.Snackbar.LENGTH_SHORT;
import static com.google.android.material.snackbar.Snackbar.make;
import static com.nuryadincjr.ebusantara.R.id;
import static com.nuryadincjr.ebusantara.R.layout;
import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nuryadincjr.ebusantara.databinding.ActivitySeatChooserBinding;
import com.nuryadincjr.ebusantara.databinding.LayoutSeatsChooserBinding;
import com.nuryadincjr.ebusantara.payment.DetailPaymentActivity;
import com.nuryadincjr.ebusantara.pojo.Buses;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.pojo.Seats;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeatChooserActivity extends AppCompatActivity
        implements View.OnClickListener {
    private ActivitySeatChooserBinding binding;
    private LayoutSeatsChooserBinding layoutSeatChooser;
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
        setContentView(layout.activity_seat_chooser);
        binding = ActivitySeatChooserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        layoutSeatChooser = binding.layoutSeatChooser;
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

        layoutSeatChooser.ivSeatA1.setOnClickListener(this);
        layoutSeatChooser.ivSeatA2.setOnClickListener(this);
        layoutSeatChooser.ivSeatA3.setOnClickListener(this);
        layoutSeatChooser.ivSeatA4.setOnClickListener(this);
        layoutSeatChooser.ivSeatA5.setOnClickListener(this);
        layoutSeatChooser.ivSeatA6.setOnClickListener(this);
        layoutSeatChooser.ivSeatA7.setOnClickListener(this);
        layoutSeatChooser.ivSeatA8.setOnClickListener(this);
        layoutSeatChooser.ivSeatA9.setOnClickListener(this);

        layoutSeatChooser.ivSeatB1.setOnClickListener(this);
        layoutSeatChooser.ivSeatB2.setOnClickListener(this);
        layoutSeatChooser.ivSeatB3.setOnClickListener(this);
        layoutSeatChooser.ivSeatB4.setOnClickListener(this);
        layoutSeatChooser.ivSeatB5.setOnClickListener(this);
        layoutSeatChooser.ivSeatB6.setOnClickListener(this);
        layoutSeatChooser.ivSeatB7.setOnClickListener(this);
        layoutSeatChooser.ivSeatB8.setOnClickListener(this);
        layoutSeatChooser.ivSeatB9.setOnClickListener(this);
        layoutSeatChooser.ivSeatB10.setOnClickListener(this);

        layoutSeatChooser.ivSeatC1.setOnClickListener(this);
        layoutSeatChooser.ivSeatC2.setOnClickListener(this);
        layoutSeatChooser.ivSeatC3.setOnClickListener(this);
        layoutSeatChooser.ivSeatC4.setOnClickListener(this);
        layoutSeatChooser.ivSeatC5.setOnClickListener(this);
        layoutSeatChooser.ivSeatC6.setOnClickListener(this);
        layoutSeatChooser.ivSeatC7.setOnClickListener(this);
        layoutSeatChooser.ivSeatC8.setOnClickListener(this);
        layoutSeatChooser.ivSeatC9.setOnClickListener(this);
        layoutSeatChooser.ivSeatC10.setOnClickListener(this);

        layoutSeatChooser.ivSeatD1.setOnClickListener(this);
        layoutSeatChooser.ivSeatD2.setOnClickListener(this);
        layoutSeatChooser.ivSeatD3.setOnClickListener(this);
        layoutSeatChooser.ivSeatD4.setOnClickListener(this);
        layoutSeatChooser.ivSeatD5.setOnClickListener(this);
        layoutSeatChooser.ivSeatD6.setOnClickListener(this);
        layoutSeatChooser.ivSeatD7.setOnClickListener(this);
        layoutSeatChooser.ivSeatD8.setOnClickListener(this);
        layoutSeatChooser.ivSeatD9.setOnClickListener(this);
        layoutSeatChooser.ivSeatD10.setOnClickListener(this);

        getSeats();

        binding.ivBackArrow.setOnClickListener(v -> onBackPressed());
        binding.btnBookNow.setOnClickListener(v-> onStartActivity());
    }

    private void getSeats() {
        if (seatsA != null || seatsB != null || seatsC != null || seatsD != null) {

            getSelected(seatsA, layoutSeatChooser.ivSeatA1, layoutSeatChooser.ivSeatA2,
                    layoutSeatChooser.ivSeatA3, layoutSeatChooser.ivSeatA4,
                    layoutSeatChooser.ivSeatA5, layoutSeatChooser.ivSeatA6,
                    layoutSeatChooser.ivSeatA7, layoutSeatChooser.ivSeatA8,
                    layoutSeatChooser.ivSeatA9, null);

            getSelected(seatsB, layoutSeatChooser.ivSeatB1, layoutSeatChooser.ivSeatB2,
                    layoutSeatChooser.ivSeatB3, layoutSeatChooser.ivSeatB4,
                    layoutSeatChooser.ivSeatB5, layoutSeatChooser.ivSeatB6,
                    layoutSeatChooser.ivSeatB7, layoutSeatChooser.ivSeatB8,
                    layoutSeatChooser.ivSeatB9, layoutSeatChooser.ivSeatB10);

            getSelected(seatsC, layoutSeatChooser.ivSeatC1, layoutSeatChooser.ivSeatC2,
                    layoutSeatChooser.ivSeatC3, layoutSeatChooser.ivSeatC4,
                    layoutSeatChooser.ivSeatC5, layoutSeatChooser.ivSeatC6,
                    layoutSeatChooser.ivSeatC7, layoutSeatChooser.ivSeatC8,
                    layoutSeatChooser.ivSeatC9, layoutSeatChooser.ivSeatC10);

            getSelected(seatsD, layoutSeatChooser.ivSeatD1, layoutSeatChooser.ivSeatD2,
                    layoutSeatChooser.ivSeatD3, layoutSeatChooser.ivSeatD4,
                    layoutSeatChooser.ivSeatD5, layoutSeatChooser.ivSeatD6,
                    layoutSeatChooser.ivSeatD7, layoutSeatChooser.ivSeatD8,
                    layoutSeatChooser.ivSeatD9, layoutSeatChooser.ivSeatD10);
        }
    }

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
            case id.ivSeatA1:
                getChooserListener(layoutSeatChooser.ivSeatA1, "A", 1);
                break;
            case id.ivSeatA2:
                getChooserListener(layoutSeatChooser.ivSeatA2, "A", 2);
                break;
            case id.ivSeatA3:
                getChooserListener(layoutSeatChooser.ivSeatA3, "A", 3);
                break;
            case id.ivSeatA4:
                getChooserListener(layoutSeatChooser.ivSeatA4, "A", 4);
                break;
            case id.ivSeatA5:
                getChooserListener(layoutSeatChooser.ivSeatA5, "A", 5);
                break;
            case id.ivSeatA6:
                getChooserListener(layoutSeatChooser.ivSeatA6, "A", 6);
                break;
            case id.ivSeatA7:
                getChooserListener(layoutSeatChooser.ivSeatA7, "A", 7);
                break;
            case id.ivSeatA8:
                getChooserListener(layoutSeatChooser.ivSeatA8, "A", 8);
                break;
            case id.ivSeatA9:
                getChooserListener(layoutSeatChooser.ivSeatA9, "A", 9);
                break;

            case id.ivSeatB1:
                getChooserListener(layoutSeatChooser.ivSeatB1, "B", 1);
                break;
            case id.ivSeatB2:
                getChooserListener(layoutSeatChooser.ivSeatB2, "B", 2);
                break;
            case id.ivSeatB3:
                getChooserListener(layoutSeatChooser.ivSeatB3, "B", 3);
                break;
            case id.ivSeatB4:
                getChooserListener(layoutSeatChooser.ivSeatB4, "B", 4);
                break;
            case id.ivSeatB5:
                getChooserListener(layoutSeatChooser.ivSeatB5, "B", 5);
                break;
            case id.ivSeatB6:
                getChooserListener(layoutSeatChooser.ivSeatB6, "B", 6);
                break;
            case id.ivSeatB7:
                getChooserListener(layoutSeatChooser.ivSeatB7, "B", 7);
                break;
            case id.ivSeatB8:
                getChooserListener(layoutSeatChooser.ivSeatB8, "B", 8);
                break;
            case id.ivSeatB9:
                getChooserListener(layoutSeatChooser.ivSeatB9, "B", 9);
                break;
            case id.ivSeatB10:
                getChooserListener(layoutSeatChooser.ivSeatB10, "B", 10);
                break;

            case id.ivSeatC1:
                getChooserListener(layoutSeatChooser.ivSeatC1, "C", 1);
                break;
            case id.ivSeatC2:
                getChooserListener(layoutSeatChooser.ivSeatC2, "C", 2);
                break;
            case id.ivSeatC3:
                getChooserListener(layoutSeatChooser.ivSeatC3, "C", 3);
                break;
            case id.ivSeatC4:
                getChooserListener(layoutSeatChooser.ivSeatC4, "C", 4);
                break;
            case id.ivSeatC5:
                getChooserListener(layoutSeatChooser.ivSeatC5, "C", 5);
                break;
            case id.ivSeatC6:
                getChooserListener(layoutSeatChooser.ivSeatC6, "C",6);
                break;
            case id.ivSeatC7:
                getChooserListener(layoutSeatChooser.ivSeatC7, "C", 7);
                break;
            case id.ivSeatC8:
                getChooserListener(layoutSeatChooser.ivSeatC8, "C", 8);
                break;
            case id.ivSeatC9:
                getChooserListener(layoutSeatChooser.ivSeatC9, "C", 9);
                break;
            case id.ivSeatC10:
                getChooserListener(layoutSeatChooser.ivSeatC10, "C", 10);
                break;

            case id.ivSeatD1:
                getChooserListener(layoutSeatChooser.ivSeatD1, "D", 1);
                break;
            case id.ivSeatD2:
                getChooserListener(layoutSeatChooser.ivSeatD2, "D", 2);
                break;
            case id.ivSeatD3:
                getChooserListener(layoutSeatChooser.ivSeatD3, "D", 3);
                break;
            case id.ivSeatD4:
                getChooserListener(layoutSeatChooser.ivSeatD4, "D", 4);
                break;
            case id.ivSeatD5:
                getChooserListener(layoutSeatChooser.ivSeatD5, "D", 5);
                break;
            case id.ivSeatD6:
                getChooserListener(layoutSeatChooser.ivSeatD6, "D", 6);
                break;
            case id.ivSeatD7:
                getChooserListener(layoutSeatChooser.ivSeatD7, "D", 7);
                break;
            case id.ivSeatD8:
                getChooserListener(layoutSeatChooser.ivSeatD8, "D",8);
                break;
            case id.ivSeatD9:
                getChooserListener(layoutSeatChooser.ivSeatD9, "D", 9);
                break;
            case id.ivSeatD10:
                getChooserListener(layoutSeatChooser.ivSeatD10, "D", 10);
                break;
        }
    }

    private void getChooserListener(CheckedTextView view, String itemX, int itemY) {
        String seatNo = itemX+itemY;
        if (seatChooser.size() < parseInt(passengers)) {
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
        if(seatChooser.size() == parseInt(passengers)){
            ArrayList<String> seatList = new ArrayList<>(seatChooser);
            seats = new Seats(seatsA, seatsB, seatsC, seatsD);
            buses.setSeats(seats);
            schedule.setBuses(buses);

            startActivity(new Intent(this,
                    DetailPaymentActivity.class)
                    .putExtra("schedule", schedule)
                    .putExtra("date", calendar)
                    .putExtra("passengers", passengers)
                    .putStringArrayListExtra("seats", seatList));
        }else {
            int available = parseInt(passengers) - seatChooser.size();
            make(binding.getRoot(),
                    "Please choose "+available+" more seats ",
                    LENGTH_SHORT).show();
        }
    }
}