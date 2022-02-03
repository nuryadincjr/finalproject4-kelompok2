package com.nuryadincjr.ebusantara.fragment;

import static com.nuryadincjr.ebusantara.util.LocalPreference.getInstance;
import static java.lang.String.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.adapters.TicketsAdapter;
import com.nuryadincjr.ebusantara.api.ReviewsRepository;
import com.nuryadincjr.ebusantara.databinding.FragmentTicketsBinding;
import com.nuryadincjr.ebusantara.dataview.TicketDetailsActivity;
import com.nuryadincjr.ebusantara.interfaces.ItemClickListener;
import com.nuryadincjr.ebusantara.util.MainViewModel;
import com.nuryadincjr.ebusantara.pojo.Reviewers;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.pojo.Transactions;
import com.nuryadincjr.ebusantara.pojo.TransactionsReference;
import com.nuryadincjr.ebusantara.pojo.Users;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class TicketsFragment extends Fragment {
    private FragmentTicketsBinding binding;
    private Users users ;

    public TicketsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTicketsBinding.inflate(inflater, container, false);
        getUsers();

        ScheduleReference schedule = getArguments().getParcelable("schedule");
        Transactions transactions = getArguments().getParcelable("transactions");
        boolean isRating = getInstance(getContext())
                .getPreferences()
                .getBoolean("isRating", false);
        if(isRating) onShowPopup(schedule, transactions, users);

        getData(users);
        return binding.getRoot();
    }

    private void getUsers() {
        SharedPreferences preference = getInstance(getContext()).getPreferences();
        String uid = preference.getString("uid", "");
        String name = preference.getString("name", "");
        String email = preference.getString("email", "");
        String phone = preference.getString("phone", "");
        String photo = preference.getString("photo", "");
        users = new Users(uid, name, phone, email, photo);
    }

    private void getData(Users users) {
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getTransactions(users.getUid()).observe(getViewLifecycleOwner(), ticket -> {
            TicketsAdapter citiesAdapter = new TicketsAdapter(ticket);
            if(ticket != null){
                binding.rvTickets.setVisibility(View.VISIBLE);
                binding.layoutError.linearLayout.setVisibility(View.GONE);
                binding.rvTickets.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.rvTickets.setAdapter(citiesAdapter);
            }else {
                binding.rvTickets.setVisibility(View.GONE);
                binding.layoutError.linearLayout.setVisibility(View.VISIBLE);
            }

            onListener(citiesAdapter, ticket, users);
        });
    }

    @SuppressLint("InflateParams")
    public void onShowPopup(ScheduleReference schedule, Transactions transactions, Users users) {

        View inflatedView = getLayoutInflater().inflate(R.layout.layout_rating, null);
        TextView poName = inflatedView.findViewById(R.id.tvPOName);
        TextView busNo = inflatedView.findViewById(R.id.tvBusNo);
        TextView departureDate = inflatedView.findViewById(R.id.tvDepartureDate);
        TextView status = inflatedView.findViewById(R.id.tvStatus);
        TextView maxLine = inflatedView.findViewById(R.id.tvMaxLine);
        EditText content = inflatedView.findViewById(R.id.etContent);
        CheckedTextView star1 = inflatedView.findViewById(R.id.ctvStar1);
        CheckedTextView star2 = inflatedView.findViewById(R.id.ctvStar2);
        CheckedTextView star3 = inflatedView.findViewById(R.id.ctvStar3);
        CheckedTextView star4 = inflatedView.findViewById(R.id.ctvStar4);
        CheckedTextView star5 = inflatedView.findViewById(R.id.ctvStar5);

        poName.setText(schedule.getBuses().getPoName());
        busNo.setText(schedule.getBuses().getBusNo());
        departureDate.setText(schedule.getDepartureTime());
        status.setText(transactions.getStatus());

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setView(inflatedView);
        builder.setCancelable(false);

        star1.setOnClickListener(v -> getSelected(star1, star2, star3, star4, star5,
                !star1.isChecked(), false, false, false, false));
        star2.setOnClickListener(v -> getSelected(star1, star2, star3, star4, star5,
                true, true, false, false, false));
        star3.setOnClickListener(v -> getSelected(star1, star2, star3, star4, star5,
                true, true, true, false, false));
        star4.setOnClickListener(v -> getSelected(star1, star2, star3, star4, star5,
                true, true, true, true, false));
        star5.setOnClickListener(v -> getSelected(star1, star2, star3, star4, star5,
                true, true, true, true, true));

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count<=500){
                    maxLine.setText(s.length()+"/500");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        String id = schedule.getBuses().getId();
        ReviewsRepository repository = new ReviewsRepository();
        AtomicReference<Reviewers> reviewer = new AtomicReference<>(new Reviewers());
        repository.getReviewer(id, users).observe(getViewLifecycleOwner(), reviewers -> {
            if(reviewers!=null) {
                reviewer.set(reviewers.get(0));
                content.setText(reviewer.get().getContent());

                switch (reviewer.get().getRatings()){
                    case "1":
                        getSelected(star1, star2, star3, star4, star5,
                                !star1.isChecked(), false, false, false, false);
                        break;
                    case "2":
                        getSelected(star1, star2, star3, star4, star5,
                                true, true, false, false, false);
                        break;
                    case "3":
                        getSelected(star1, star2, star3, star4, star5,
                                true, true, true, false, false);
                        break;
                    case "4":
                        getSelected(star1, star2, star3, star4, star5,
                                true, true, true, true, false);
                        break;
                    case "5":
                        getSelected(star1, star2, star3, star4, star5,
                                true, true, true, true, true);
                        break;
                }
            }

            getInstance(getContext()).getEditor()
                    .putBoolean("isRating", false).apply();
        });

        builder.setPositiveButton("Rate This Trip", (dialog, which) -> {
            int rating = 0;
            if(star1.isChecked() && star2.isChecked() && star3.isChecked() && star4.isChecked() && star5.isChecked()) rating =5;
            else if(star1.isChecked() && star2.isChecked() && star3.isChecked() && star4.isChecked()) rating =4;
            else if(star1.isChecked() && star2.isChecked() && star3.isChecked()) rating =3;
            else if(star1.isChecked() && star2.isChecked()) rating =2;
            else if(star1.isChecked()) rating =1;

            int finalRating = rating;
            Reviewers postReview = new Reviewers(users.getUid(), transactions.getDate(),
                    content.getText().toString(), valueOf(finalRating));

            repository.updateReview(id, postReview);
            repository.deleteReview(id, reviewer.get());

            getInstance(getContext()).getEditor()
                        .putBoolean("isRating", false).apply();
        });

        builder.show();
    }

    private void getSelected(CheckedTextView star1, CheckedTextView star2,
                             CheckedTextView star3, CheckedTextView star4,
                             CheckedTextView star5, boolean b, boolean b2,
                             boolean b3, boolean b4, boolean b5) {
        star1.setChecked(b);
        star2.setChecked(b2);
        star3.setChecked(b3);
        star4.setChecked(b4);
        star5.setChecked(b5);
    }

    private void onListener(TicketsAdapter productsAdapter, ArrayList<TransactionsReference> ticket, Users users) {
        productsAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
               startActivity(new Intent(getContext(), TicketDetailsActivity.class)
                       .putExtra("transaction", ticket.get(position))
                       .putExtra("user", users));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
    }
}