package com.nuryadincjr.ebusantara.util;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.nuryadincjr.ebusantara.api.CitiesRepository;
import com.nuryadincjr.ebusantara.api.ScheduleRepository;
import com.nuryadincjr.ebusantara.api.TransactionsRepository;
import com.nuryadincjr.ebusantara.api.UsersRepository;
import com.nuryadincjr.ebusantara.pojo.Cities;
import com.nuryadincjr.ebusantara.pojo.Reviewers;
import com.nuryadincjr.ebusantara.pojo.Schedule;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.pojo.TransactionsReference;
import com.nuryadincjr.ebusantara.pojo.Users;

import java.util.ArrayList;
import java.util.Calendar;

public class MainViewModel extends AndroidViewModel {
    private final ScheduleRepository scheduleRepository;
    private final CitiesRepository citiesRepository;
    private final TransactionsRepository transactionsRepository;
    private final UsersRepository usersRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.citiesRepository = new CitiesRepository();
        this.scheduleRepository = new ScheduleRepository();
        this.transactionsRepository = new TransactionsRepository();
        this.usersRepository = new UsersRepository();
    }

    public MutableLiveData<ArrayList<Cities>> getCities() {
        return citiesRepository.getCities();
    }

    public MutableLiveData<ArrayList<Cities>> getCities(String field, String value) {
        return citiesRepository.getCities(field, value);
    }

    public MutableLiveData<ArrayList<TransactionsReference>> getTransactions(String value) {
        return transactionsRepository.getTransactions(value);
    }

    public MutableLiveData<ArrayList<ScheduleReference>> getSchedule(
            Cities departureCity, Cities arrivalCity, Calendar calendar) {
        return scheduleRepository.getSchedule(departureCity, arrivalCity, calendar);
    }

    public MutableLiveData<Users> getUsers(Reviewers reviewers) {
        return usersRepository.getUsers(reviewers);
    }
}
