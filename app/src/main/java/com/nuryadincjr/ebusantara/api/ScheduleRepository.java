package com.nuryadincjr.ebusantara.api;

import static com.nuryadincjr.ebusantara.util.Constant.COLLECTION_SCHEDULE;
import static java.lang.String.valueOf;
import static java.util.Collections.sort;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.nuryadincjr.ebusantara.pojo.Buses;
import com.nuryadincjr.ebusantara.pojo.Cities;
import com.nuryadincjr.ebusantara.pojo.Reviewers;
import com.nuryadincjr.ebusantara.pojo.ReviewersReference;
import com.nuryadincjr.ebusantara.pojo.Schedule;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.pojo.Users;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScheduleRepository {
    private final FirebaseFirestore db;
    private final CollectionReference collection;
    private final ArrayList<ScheduleReference> scheduleReferenceArrayList;
    private final MutableLiveData<ArrayList<ScheduleReference>> scheduleMutableLiveData;

    public ScheduleRepository() {
        db = FirebaseFirestore.getInstance();
        collection = db.collection(COLLECTION_SCHEDULE);
        scheduleReferenceArrayList = new ArrayList<>();
        scheduleMutableLiveData = new MutableLiveData<>();
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
            Cities departureCity, Cities arrivalCity, Calendar calendar, Users user) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date nowDate = new Date();
        String date = format.format(calendar.getTime());

        collection.orderBy("departureTime")
                .whereGreaterThanOrEqualTo("departureTime", date)
                .whereLessThanOrEqualTo("departureTime",date+"~")
                .whereGreaterThanOrEqualTo("departureTime", formatTime.format(nowDate))
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult().size()!=0) {

                for (QueryDocumentSnapshot  snapshot : task.getResult()) {
                    Schedule schedule = snapshot.toObject(Schedule.class);
                    getReference(departureCity, arrivalCity, user, schedule);
                }
            } else scheduleMutableLiveData.setValue(null);
        });

        return scheduleMutableLiveData;
    }

    private void getReference(Cities departureCity, Cities arrivalCity, Users user, Schedule schedule) {
        Task<DocumentSnapshot> departureDocSnapTask = schedule.getDeparture().get();
        Task<DocumentSnapshot> arrivalDocSnapTask = schedule.getArrival().get();
        Task<DocumentSnapshot> busesDocSnapTask = schedule.getBus().get();
        Tasks.whenAllComplete(departureDocSnapTask, arrivalDocSnapTask, busesDocSnapTask).addOnCompleteListener(listTask -> {

            DocumentSnapshot departureSnap = (DocumentSnapshot) listTask.getResult().get(0).getResult();
            DocumentSnapshot arrivalSnap = (DocumentSnapshot) listTask.getResult().get(1).getResult();
            DocumentSnapshot busesSnap = (DocumentSnapshot) listTask.getResult().get(2).getResult();

            Cities departureCities = departureSnap.toObject(Cities.class);
            Cities arrivalCities = arrivalSnap.toObject(Cities.class);
            Buses buses = busesSnap.toObject(Buses.class);
            if(departureCity.getCity().equals(departureCities.getCity()) &&
                    arrivalCity.getCity().equals(arrivalCities.getCity())){

                new ReviewsRepository().getReviewers(buses).observeForever(data -> {
                    double ratings = (double) data.get("ratings");
                    List<Reviewers> reviewersList = (List<Reviewers>) data.get("reviewer");
                    sort(reviewersList, (o1, o2) -> {
                        if(o1.getUid().equals(user.getUid())) return -1;
                        else return 1;
                    });

                    if(valueOf(ratings).equals("NaN")) ratings = 0.0;
                    String displayRating = ratings +"/5";

                    ReviewersReference reviewersReference = new ReviewersReference(reviewersList, displayRating);
                    ScheduleReference scheduleReference = new ScheduleReference(schedule.getId(),
                            buses, departureCities, arrivalCities, schedule.getDepartureTime(),
                            schedule.getArrivalTime(), reviewersReference);

                    scheduleReferenceArrayList.add(scheduleReference);
                    scheduleMutableLiveData.postValue(scheduleReferenceArrayList);
                });
            }else scheduleMutableLiveData.setValue(null);
        });
    }
}
