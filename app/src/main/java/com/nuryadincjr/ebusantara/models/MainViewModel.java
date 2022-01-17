package com.nuryadincjr.ebusantara.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.nuryadincjr.ebusantara.api.ScheduleRepository;
import com.nuryadincjr.ebusantara.pojo.Cities;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.pojo.Users;

import java.util.ArrayList;

public class MainViewModel extends AndroidViewModel {

    private final ScheduleRepository scheduleRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.scheduleRepository = new ScheduleRepository();
    }

    public MutableLiveData<ArrayList<Cities>> getCities(String collection) {
        return scheduleRepository.getCollectionCities(collection);
    }
    public MutableLiveData<ArrayList<Users>> getUsers(String collection) {
        return scheduleRepository.getUsers(collection);
    }
    public MutableLiveData<ArrayList<ScheduleReference>> getBuses(String collection, String departureCity, String arrivalCity) {
        return scheduleRepository.getCollectionsBuses(collection, departureCity, arrivalCity);
    }
}
