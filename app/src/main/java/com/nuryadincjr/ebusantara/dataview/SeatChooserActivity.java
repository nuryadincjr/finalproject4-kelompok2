package com.nuryadincjr.ebusantara.dataview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.ActivitySeatChooserBinding;
import com.nuryadincjr.ebusantara.databinding.LayoutSeatsChooserBinding;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.pojo.Seats;
import com.nuryadincjr.ebusantara.payment.DetailPaymentActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class SeatChooserActivity extends AppCompatActivity
        implements View.OnClickListener {

    private ActivitySeatChooserBinding binding;
    private ScheduleReference schedule;
    private Calendar calendar;
    private String passengers;
    private Seats seats;
    private Set<String> seatChooser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_chooser);
        binding = ActivitySeatChooserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        schedule = getIntent().getParcelableExtra("schedule");
        passengers = getIntent().getStringExtra("passengers");
        calendar =  (Calendar)getIntent().getSerializableExtra("date");
        seats = schedule.getBuses().getSeats();

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
        binding.btnBookNow.setOnClickListener(v -> {
            ArrayList<String> seatList = new ArrayList<>(seatChooser);
            startActivity(new Intent(this,
                    DetailPaymentActivity.class)
                    .putExtra("schedule", schedule)
                    .putExtra("date", calendar)
                    .putExtra("passengers", passengers)
                    .putStringArrayListExtra("seats", seatList));
        });
    }

    private void getSeats() {
        boolean[] arrayA = seats.getA();
        boolean[] arrayB = seats.getB();
        boolean[] arrayC = seats.getC();
        boolean[] arrayD = seats.getD();

        if (arrayA != null || arrayB != null || arrayC != null || arrayD != null) {
            LayoutSeatsChooserBinding view = binding.layoutSeatChooser;

            getSelected(arrayA, view.iv1A, view.iv2A,
                    view.iv3A, view.iv4A, view.iv5A, view.iv6A,
                    view.iv7A, view.iv8A, view.iv9A, null);

            getSelected(arrayB, view.iv1B, view.iv2B,
                    view.iv3B, view.iv4B, view.iv5B, view.iv6B,
                    view.iv7B, view.iv8B, view.iv9B, view.iv10B);

            getSelected(arrayC, view.iv1C, view.iv2C,
                    view.iv3C, view.iv4C, view.iv5C, view.iv6C,
                    view.iv7C, view.iv8C, view.iv9C, view.iv10C);

            getSelected(arrayD, view.iv1D, view.iv2D,
                    view.iv3D, view.iv4D, view.iv5D, view.iv6D,
                    view.iv7D, view.iv8D, view.iv9D, view.iv10D);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void getSelected(boolean[] arraySeats,
                             CheckedTextView seat1, CheckedTextView seat2,
                             CheckedTextView seat3, CheckedTextView seat4,
                             CheckedTextView seat5, CheckedTextView seat6,
                             CheckedTextView seat7, CheckedTextView seat8,
                             CheckedTextView seat9, CheckedTextView seat10) {

        for (int i = 0, aLength = arraySeats.length; i < aLength; i++) {
            boolean b = arraySeats[i];
            if (!b && seat10 != null) {
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
                getChooserListener(view.iv1A, "A1");
                break;
            case R.id.iv2A:
                getChooserListener(view.iv2A, "A2");
                break;
            case R.id.iv3A:
                getChooserListener(view.iv3A, "A3");
                break;
            case R.id.iv4A:
                getChooserListener(view.iv4A, "A4");
                break;
            case R.id.iv5A:
                getChooserListener(view.iv5A, "A5");
                break;
            case R.id.iv6A:
                getChooserListener(view.iv6A, "A6");
                break;
            case R.id.iv7A:
                getChooserListener(view.iv7A, "A7");
                break;
            case R.id.iv8A:
                getChooserListener(view.iv8A, "A8");
                break;
            case R.id.iv9A:
                getChooserListener(view.iv9A, "A9");
                break;

            case R.id.iv1B:
                getChooserListener(view.iv1B, "B1");
                break;
            case R.id.iv2B:
                getChooserListener(view.iv2B, "B2");
                break;
            case R.id.iv3B:
                getChooserListener(view.iv3B, "B3");
                break;
            case R.id.iv4B:
                getChooserListener(view.iv4B, "B4");
                break;
            case R.id.iv5B:
                getChooserListener(view.iv5B, "B5");
                break;
            case R.id.iv6B:
                getChooserListener(view.iv6B, "B6");
                break;
            case R.id.iv7B:
                getChooserListener(view.iv7B, "B7");
                break;
            case R.id.iv8B:
                getChooserListener(view.iv8B, "B8");
                break;
            case R.id.iv9B:
                getChooserListener(view.iv9B, "B9");
                break;
            case R.id.iv10B:
                getChooserListener(view.iv10B, "B10");
                break;

            case R.id.iv1C:
                getChooserListener(view.iv1C, "C1");
                break;
            case R.id.iv2C:
                getChooserListener(view.iv2C, "C2");
                break;
            case R.id.iv3C:
                getChooserListener(view.iv3C, "C3");
                break;
            case R.id.iv4C:
                getChooserListener(view.iv4C, "C4");
                break;
            case R.id.iv5C:
                getChooserListener(view.iv5C, "C5");
                break;
            case R.id.iv6C:
                getChooserListener(view.iv6C, "C6");
                break;
            case R.id.iv7C:
                getChooserListener(view.iv7C, "C7");
                break;
            case R.id.iv8C:
                getChooserListener(view.iv8C, "C8");
                break;
            case R.id.iv9C:
                getChooserListener(view.iv9C, "C9");
                break;
            case R.id.iv10C:
                getChooserListener(view.iv10C, "C10");
                break;

            case R.id.iv1D:
                getChooserListener(view.iv1D, "D1");
                break;
            case R.id.iv2D:
                getChooserListener(view.iv2D, "D2");
                break;
            case R.id.iv3D:
                getChooserListener(view.iv3D, "D3");
                break;
            case R.id.iv4D:
                getChooserListener(view.iv4D, "D4");
                break;
            case R.id.iv5D:
                getChooserListener(view.iv5D, "D5");
                break;
            case R.id.iv6D:
                getChooserListener(view.iv6D, "D6");
                break;
            case R.id.iv7D:
                getChooserListener(view.iv7D, "D7");
                break;
            case R.id.iv8D:
                getChooserListener(view.iv8D, "D8");
                break;
            case R.id.iv9D:
                getChooserListener(view.iv9D, "D9");
                break;
            case R.id.iv10D:
                getChooserListener(view.iv10D, "D10");
                break;
        }
    }

    private void getChooserListener(CheckedTextView p, String item) {
        if (seatChooser.size() < Integer.parseInt(passengers)) {
            if(p.isChecked()) seatChooser.remove(item);
            else seatChooser.add(item);
            p.setChecked(!p.isChecked());
        }else {
            p.setChecked(false);
            seatChooser.remove(item);
        }

        String displayStatus = "Selected: "+ seatChooser.size()+"/"+passengers;
        binding.tvStatus.setText(displayStatus);
    }
}