package com.nuryadincjr.ebusantara.api;

import static com.google.firebase.firestore.FieldValue.arrayRemove;
import static com.google.firebase.firestore.FieldValue.arrayUnion;
import static com.nuryadincjr.ebusantara.util.Constant.COLLECTION_REVIEWS;

import static java.lang.String.valueOf;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nuryadincjr.ebusantara.pojo.Buses;
import com.nuryadincjr.ebusantara.pojo.Reviewers;
import com.nuryadincjr.ebusantara.pojo.Users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewsRepository {
    private final CollectionReference collection;

    public ReviewsRepository() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        collection = db.collection(COLLECTION_REVIEWS);
    }

    public MutableLiveData<ArrayList<Reviewers>> getReviewers(String busId, Users users) {
        ArrayList<Reviewers> reviewersArrayList = new ArrayList<>();
        MutableLiveData<ArrayList<Reviewers>> listMutableLiveData = new MutableLiveData<>();
        collection.document(busId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    List<Map<String, Object>> reviewers = (List<Map<String, Object>>) document.get("reviewer");
                    Reviewers reviewer = new Reviewers();
                    if(reviewers!=null && reviewers.size()!=0){
                        for (Map<String, Object> map : reviewers) {
                            if(users.getUid().equals(map.get("uid"))){
                                reviewer.setContent((String) map.get("content"));
                                reviewer.setDate((String) map.get("date"));
                                reviewer.setRatings((String) map.get("ratings"));
                                reviewer.setUid((String) map.get("uid"));
                                reviewersArrayList.add(reviewer);
                            }
                        }
                        listMutableLiveData.postValue(reviewersArrayList);
                    } else listMutableLiveData.setValue(null);
                }
            } else listMutableLiveData.setValue(null);
        });
        return listMutableLiveData;
    }

    public MutableLiveData<Map<String, Object>> getReviewers(Buses buses) {
        Map<String, Object> objectMap = new HashMap<>();
        MutableLiveData<Map<String, Object>> listMutableLiveData = new MutableLiveData<>();
        collection.document(buses.getId())
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                ArrayList<Map<String, Object>> reviewer = (ArrayList<Map<String, Object>>) document.get("reviewer");
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

                    objectMap.put("ratings", ratings);
                    objectMap.put("reviewer", reviewersList);
                    listMutableLiveData.postValue(objectMap);

                } else listMutableLiveData.setValue(null);
            } else listMutableLiveData.setValue(null);
        });
        return listMutableLiveData;
    }

    public void deleteReview(String busId, Reviewers reviewer){
        onDataChanged(busId, arrayRemove(reviewer));
    }

    public void updateReview(String busId, Reviewers reviewer){
        onDataChanged(busId, arrayUnion(reviewer));
    }

    private void onDataChanged(String busId, FieldValue fieldValue) {
        DocumentReference document = collection.document(busId);
        document.update("reviewer", fieldValue);
    }
}
