package com.nuryadincjr.ebusantara.api;

import static com.nuryadincjr.ebusantara.util.Constant.COLLECTION_SCHEDULE;
import static java.lang.String.valueOf;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.nuryadincjr.ebusantara.pojo.Buses;
import com.nuryadincjr.ebusantara.pojo.Cities;
import com.nuryadincjr.ebusantara.pojo.Reviewers;
import com.nuryadincjr.ebusantara.pojo.ReviewersReference;
import com.nuryadincjr.ebusantara.pojo.Schedule;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScheduleRepository {
    private final FirebaseFirestore db;
    private final CollectionReference collection;
    private ArrayList<ScheduleReference> scheduleReferenceArrayList;
    private MutableLiveData<ArrayList<ScheduleReference>> scheduleMutableLiveData;

    public ScheduleRepository() {
        db = FirebaseFirestore.getInstance();
        collection = db.collection(COLLECTION_SCHEDULE);
    }

    public MutableLiveData<Schedule> getSchedule(String reference) {
        MutableLiveData<Schedule> listMutableLiveData = new MutableLiveData<>();
        db.document(reference)
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Schedule schedule = task.getResult().toObject(Schedule.class);
                listMutableLiveData.setValue(schedule);
            } else listMutableLiveData.setValue(null);
        });
        return listMutableLiveData;
    }

    @SuppressLint("SimpleDateFormat")
    public MutableLiveData<ArrayList<ScheduleReference>> getSchedule(
            Cities departureCity, Cities arrivalCity, Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        scheduleReferenceArrayList = new ArrayList<>();
        scheduleMutableLiveData = new MutableLiveData<>();

        String date = format.format(calendar.getTime());
        collection.orderBy("departureTime")
                .whereGreaterThanOrEqualTo("departureTime", date)
                .whereLessThanOrEqualTo("departureTime",date+"~")
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult().size()!=0) {
                for (QueryDocumentSnapshot  snapshot : task.getResult()) {
                    Schedule schedule = snapshot.toObject(Schedule.class);

                    Date date1 = new Date();
                    Date date2 = new Date();
                    try {
                        date2 = formatTime.parse(schedule.getDepartureTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(date1.compareTo(date2) <= 0) {
                        getDepartureCity(departureCity.getCity(),
                                arrivalCity.getCity(),  schedule);
                    } else scheduleMutableLiveData.setValue(null);
                }
            } else scheduleMutableLiveData.setValue(null);
        });
        return scheduleMutableLiveData;
    }

    private void getDepartureCity(String departureCity, String arrivalCity, Schedule schedule) {
        new CitiesRepository().getCity(schedule.getDeparture().getPath())
                .observeForever(departureCities -> {
                    if(departureCities != null && departureCities.getCity().equals(departureCity)){
                        getArrivalCity(arrivalCity, schedule, departureCities);
                    }else {
                        scheduleMutableLiveData.postValue(null);
                    }
                });
    }

    private void getArrivalCity(String arrivalCity, Schedule schedule, Cities departureCities) {
        new CitiesRepository().getCity(schedule.getArrival().getPath())
                .observeForever(arrivalCities -> {
                    if(arrivalCities != null && arrivalCities.getCity().equals(arrivalCity)){
                        getBuses(schedule, departureCities, arrivalCities);
                    }else {
                        scheduleMutableLiveData.postValue(null);
                    }
                });
    }

    private void getBuses(Schedule schedule, Cities departureCities, Cities arrivalCities) {
        new BusesRepository().getBuses(schedule.getBus().getPath()).observeForever(buses ->{
            ScheduleReference scheduleReference = new ScheduleReference();
            if(buses != null){
                scheduleReference.setId(schedule.getId());
                scheduleReference.setBuses(buses);
                scheduleReference.setDeparture(departureCities);
                scheduleReference.setArrival(arrivalCities);
                scheduleReference.setDepartureTime(schedule.getDepartureTime());
                scheduleReference.setArrivalTime(schedule.getArrivalTime());

                getReviews(buses, scheduleReference);
            }
        });
    }

    private void getReviews(Buses buses, ScheduleReference schedule) {
        new ReviewsRepository().getReviewers(buses).observeForever(data -> {
            double ratings = (double) data.get("ratings");
            List<Reviewers> reviewersList = (List<Reviewers>) data.get("reviewer");
            if(valueOf(ratings).equals("NaN")) ratings = 0.0;
            String displayRating = ratings +"/5";

            ReviewersReference reviewersReference = new ReviewersReference();
            reviewersReference.setRatingsCount(displayRating);
            reviewersReference.setReviewers(reviewersList);
            schedule.setReviewers(reviewersReference);

            scheduleReferenceArrayList.add(schedule);
            scheduleMutableLiveData.postValue(scheduleReferenceArrayList);
        });
    }
}
