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
        seatsA = seats.getA();
        seatsB = seats.getB();
        seatsC = seats.getC();
        seatsD = seats.getD();

        seatChooser = new HashSet<>();
        String displayStatus = "Selected: "+ seatChooser.size()+"/"+passengers;
        binding.tvStatus.setText(displayStatus);

        LayoutSeatsChooserBinding view = binding.layoutSeatChooser;
        view.iv1A.setOnClickListener(this);
        view.iv2A.setOnClickListener(this);
        view.iv3A.setOnClickListener(this);
        view.iv4A.setOnClickListener(this);
        view.iv5A.setOnClickListener(this);
        view.iv6A.setOnClickListener(this);
        view.iv7A.setOnClickListener(this);
        view.iv8A.setOnClickListener(this);
        view.iv9A.setOnClickListener(this);

        view.iv1B.setOnClickListener(this);
        view.iv2B.setOnClickListener(this);
        view.iv3B.setOnClickListener(this);
        view.iv4B.setOnClickListener(this);
        view.iv5B.setOnClickListener(this);
        view.iv6B.setOnClickListener(this);
        view.iv7B.setOnClickListener(this);
        view.iv8B.setOnClickListener(this);
        view.iv9B.setOnClickListener(this);
        view.iv10B.setOnClickListener(this);

        view.iv1C.setOnClickListener(this);
        view.iv2C.setOnClickListener(this);
        view.iv3C.setOnClickListener(this);
        view.iv4C.setOnClickListener(this);
        view.iv5C.setOnClickListener(this);
        view.iv6C.setOnClickListener(this);
        view.iv7C.setOnClickListener(this);
        view.iv8C.setOnClickListener(this);
        view.iv9C.setOnClickListener(this);
        view.iv10C.setOnClickListener(this);

        view.iv1D.setOnClickListener(this);
        view.iv2D.setOnClickListener(this);
        view.iv3D.setOnClickListener(this);
        view.iv4D.setOnClickListener(this);
        view.iv5D.setOnClickListener(this);
        view.iv6D.setOnClickListener(this);
        view.iv7D.setOnClickListener(this);
        view.iv8D.setOnClickListener(this);
        view.iv9D.setOnClickListener(this);
        view.iv10D.setOnClickListener(this);

        getSeats();

        binding.ivBackArrow.setOnClickListener(v -> onBackPressed());
        binding.btnBookNow.setOnClickListener(v-> onStartActivity());
    }

    private void getSeats() {
        if (seatsA != null || seatsB != null || seatsC != null || seatsD != null) {
            LayoutSeatsChooserBinding view = binding.layoutSeatChooser;

            getSelected(seatsA, view.iv1A, view.iv2A,
                    view.iv3A, view.iv4A, view.iv5A, view.iv6A,
                    view.iv7A, view.iv8A, view.iv9A, null);

            getSelected(seatsB, view.iv1B, view.iv2B,
                    view.iv3B, view.iv4B, view.iv5B, view.iv6B,
                    view.iv7B, view.iv8B, view.iv9B, view.iv10B);

            getSelected(seatsC, view.iv1C, view.iv2C,
                    view.iv3C, view.iv4C, view.iv5C, view.iv6C,
                    view.iv7C, view.iv8C, view.iv9C, view.iv10C);

            getSelected(seatsD, view.iv1D, view.iv2D,
                    view.iv3D, view.iv4D, view.iv5D, view.iv6D,
                    view.iv7D, view.iv8D, view.iv9D, view.iv10D);
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
        LayoutSeatsChooserBinding view = binding.layoutSeatChooser;
        switch (v.getId()) {
            case R.id.iv1A:
                getChooserListener(view.iv1A, "A", 1);
                break;
            case R.id.iv2A:
                getChooserListener(view.iv2A, "A", 2);
                break;
            case R.id.iv3A:
                getChooserListener(view.iv3A, "A", 3);
                break;
            case R.id.iv4A:
                getChooserListener(view.iv4A, "A", 4);
                break;
            case R.id.iv5A:
                getChooserListener(view.iv5A, "A", 5);
                break;
            case R.id.iv6A:
                getChooserListener(view.iv6A, "A", 6);
                break;
            case R.id.iv7A:
                getChooserListener(view.iv7A, "A", 7);
                break;
            case R.id.iv8A:
                getChooserListener(view.iv8A, "A", 8);
                break;
            case R.id.iv9A:
                getChooserListener(view.iv9A, "A", 9);
                break;

            case R.id.iv1B:
                getChooserListener(view.iv1B, "B", 1);
                break;
            case R.id.iv2B:
                getChooserListener(view.iv2B, "B", 2);
                break;
            case R.id.iv3B:
                getChooserListener(view.iv3B, "B", 3);
                break;
            case R.id.iv4B:
                getChooserListener(view.iv4B, "B", 4);
                break;
            case R.id.iv5B:
                getChooserListener(view.iv5B, "B", 5);
                break;
            case R.id.iv6B:
                getChooserListener(view.iv6B, "B", 6);
                break;
            case R.id.iv7B:
                getChooserListener(view.iv7B, "B", 7);
                break;
            case R.id.iv8B:
                getChooserListener(view.iv8B, "B", 8);
                break;
            case R.id.iv9B:
                getChooserListener(view.iv9B, "B", 9);
                break;
            case R.id.iv10B:
                getChooserListener(view.iv10B, "B", 10);
                break;

            case R.id.iv1C:
                getChooserListener(view.iv1C, "C", 1);
                break;
            case R.id.iv2C:
                getChooserListener(view.iv2C, "C", 2);
                break;
            case R.id.iv3C:
                getChooserListener(view.iv3C, "C", 3);
                break;
            case R.id.iv4C:
                getChooserListener(view.iv4C, "C", 4);
                break;
            case R.id.iv5C:
                getChooserListener(view.iv5C, "C", 5);
                break;
            case R.id.iv6C:
                getChooserListener(view.iv6C, "C",6);
                break;
            case R.id.iv7C:
                getChooserListener(view.iv7C, "C", 7);
                break;
            case R.id.iv8C:
                getChooserListener(view.iv8C, "C", 8);
                break;
            case R.id.iv9C:
                getChooserListener(view.iv9C, "C", 9);
                break;
            case R.id.iv10C:
                getChooserListener(view.iv10C, "C", 10);
                break;

            case R.id.iv1D:
                getChooserListener(view.iv1D, "D", 1);
                break;
            case R.id.iv2D:
                getChooserListener(view.iv2D, "D", 2);
                break;
            case R.id.iv3D:
                getChooserListener(view.iv3D, "D", 3);
                break;
            case R.id.iv4D:
                getChooserListener(view.iv4D, "D", 4);
                break;
            case R.id.iv5D:
                getChooserListener(view.iv5D, "D", 5);
                break;
            case R.id.iv6D:
                getChooserListener(view.iv6D, "D", 6);
                break;
            case R.id.iv7D:
                getChooserListener(view.iv7D, "D", 7);
                break;
            case R.id.iv8D:
                getChooserListener(view.iv8D, "D",8);
                break;
            case R.id.iv9D:
                getChooserListener(view.iv9D, "D", 9);
                break;
            case R.id.iv10D:
                getChooserListener(view.iv10D, "D", 10);
                break;
        }
    }

    private void getChooserListener(CheckedTextView view, String itemX, int itemY) {
        String seatNo = itemX+itemY;
        if (seatChooser.size() < Integer.parseInt(passengers)) {
            if(view.isChecked()) seatChooser.remove(seatNo);
            else seatChooser.add(seatNo);

            view.setChecked(!view.isChecked());
            getSeatBoolean(itemX, itemY, !view.isChecked());
        }else {
            view.setChecked(false);
            seatChooser.remove(seatNo);
            getSeatBoolean(itemX, itemY, false);
        }

        String displayStatus = "Selected: "+ seatChooser.size()+"/"+passengers;
        binding.tvStatus.setText(displayStatus);
    }

    private void getSeatBoolean(String itemX, int itemY, boolean isSelected) {
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
            seats.setA(seatsA);
            seats.setB(seatsB);
            seats.setC(seatsC);
            seats.setD(seatsD);
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