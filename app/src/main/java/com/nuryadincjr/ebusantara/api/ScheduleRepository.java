package com.nuryadincjr.ebusantara.api;

import static java.lang.String.valueOf;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ScheduleRepository {
    private final FirebaseFirestore db;
    private final CollectionReference collection;

    public ScheduleRepository() {
        db = FirebaseFirestore.getInstance();
        collection = db.collection("schedule");
    }

    @SuppressLint("SimpleDateFormat")
    public MutableLiveData<ArrayList<ScheduleReference>> getCollectionsBuses(
            String departureCity, String arrivalCity, Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        MutableLiveData<ArrayList<ScheduleReference>> scheduleMutableLiveData = new MutableLiveData<>();
        ArrayList<ScheduleReference> scheduleReferences = new ArrayList<>();

        String date = format.format(calendar.getTime());
        collection
                .whereGreaterThanOrEqualTo("departureTime", date)
                .whereLessThanOrEqualTo("departureTime",date+"~")
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult().size()!=0) {
                for (QueryDocumentSnapshot  snapshot : task.getResult()) {
                    Schedule data = snapshot.toObject(Schedule.class);
                    db.document(data.getDeparture().getPath())
                            .get().addOnCompleteListener(departureTask -> {
                       if(departureTask.isSuccessful()){
                           Cities departureCities = departureTask.getResult().toObject(Cities.class);
                           if(departureCities != null){
                               if(departureCities.getCity().equals(departureCity)){
                                   db.document(data.getArrival().getPath())
                                           .get().addOnCompleteListener(arrivalTask -> {
                                       if(arrivalTask.isSuccessful()){
                                           Cities arrivalCities = arrivalTask.getResult().toObject(Cities.class);
                                           if(arrivalCities != null){
                                               if(arrivalCities.getCity().equals(arrivalCity)){
                                                   db.document(data.getBus().getPath())
                                                           .get().addOnCompleteListener(busTask -> {
                                                      if(busTask.isSuccessful()){
                                                          Buses buses = busTask.getResult().toObject(Buses.class);
                                                          ScheduleReference scheduleReference = new ScheduleReference();
                                                          if(buses != null){
                                                              scheduleReference.setId(data.getId());
                                                              scheduleReference.setBuses(buses);
                                                              scheduleReference.setDeparture(departureCities);
                                                              scheduleReference.setArrival(arrivalCities);
                                                              scheduleReference.setDepartureTime(data.getDepartureTime());
                                                              scheduleReference.setArrivalTime(data.getArrivalTime());

                                                              db.collection("reviews")
                                                                      .document(buses.getId()).get().addOnCompleteListener(reviewsTask -> {
                                                                  if (reviewsTask.isSuccessful()) {
                                                                      DocumentSnapshot document = reviewsTask.getResult();
                                                                      List<Map<String, Object>> reviewer = (List<Map<String, Object>>) document.get("reviewer");
                                                                      double ratings = 0;
                                                                      List<Reviewers> reviewersList = new ArrayList<>();
                                                                      if(reviewer!=null){
                                                                          for (Map<String, Object> map : reviewer) {
                                                                              Reviewers reviewers = new Reviewers(
                                                                                      valueOf(map.get("uid")),
                                                                                      valueOf(map.get("date")),
                                                                                      valueOf(map.get("content")),
                                                                                      valueOf(map.get("ratings")));

                                                                              ratings += Double.parseDouble(reviewers.getRatings());

                                                                              reviewersList.add(reviewers);
                                                                          }
                                                                          ratings /= reviewer.size();
                                                                      }

                                                                      if(valueOf(ratings).equals("NaN")) ratings = 0.0;
                                                                      String displayRating = ratings +"/5";

                                                                      ReviewersReference reviewersReference = new ReviewersReference();
                                                                      reviewersReference.setRatingsCount(displayRating);
                                                                      reviewersReference.setReviewers(reviewersList);
                                                                      scheduleReference.setReviewers(reviewersReference);

                                                                  }
                                                                  scheduleReferences.add(scheduleReference);
                                                                  scheduleMutableLiveData.postValue(scheduleReferences);
                                                              });
                                                          }
                                                      }
                                                   });
                                               }
                                           }
                                       }
                                   });
                               }
                           }
                       }
                    });
                }
            } else scheduleMutableLiveData.setValue(null);
        });
        return scheduleMutableLiveData;
    }

    public MutableLiveData<ArrayList<Cities>> getCollectionCities(String document) {
        ArrayList<Cities> Schedule = new ArrayList<>();
        final MutableLiveData<ArrayList<Cities>> ScheduleMutableLiveData = new MutableLiveData<>();

        CollectionReference collectionReference = db.collection(document);

        collectionReference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot  snapshot : task.getResult()) {
                    Cities data = snapshot.toObject(Cities.class);
                    Schedule.add(data);
                }
                ScheduleMutableLiveData.postValue(Schedule);
            }else ScheduleMutableLiveData.setValue(null);
        });
        return ScheduleMutableLiveData;
    }
}
