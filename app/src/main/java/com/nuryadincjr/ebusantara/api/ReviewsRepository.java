package com.nuryadincjr.ebusantara.api;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nuryadincjr.ebusantara.pojo.Reviewers;
import com.nuryadincjr.ebusantara.pojo.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReviewsRepository {
    private static final String COLLECTION_USER = "reviews";
    private final CollectionReference collection;

    public ReviewsRepository() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        collection = db.collection(COLLECTION_USER);
    }

    public MutableLiveData<ArrayList<Reviewers>> getReviewer(String scheduleId, Users users) {
        ArrayList<Reviewers> citiesArrayList = new ArrayList<>();
        MutableLiveData<ArrayList<Reviewers>> productsMutableLiveData = new MutableLiveData<>();
        collection.document(scheduleId).get().addOnCompleteListener(task -> {
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
                                citiesArrayList.add(reviewer);
                            }
                        }
                    }else {
                        reviewer.setContent("");
                        reviewer.setDate("");
                        reviewer.setRatings("0");
                        reviewer.setUid(users.getUid());
                        citiesArrayList.add(reviewer);
                    }
                    productsMutableLiveData.postValue(citiesArrayList);
                }
            } else productsMutableLiveData.setValue(null);
        });
        return productsMutableLiveData;
    }

    public void deleteReview(String scheduleId, Reviewers reviewer){
        DocumentReference document = collection.document(scheduleId);
        document.update("reviewer", FieldValue.arrayRemove(reviewer));
    }

    public void updateReview(String scheduleId, Reviewers reviewer){
        DocumentReference document = collection.document(scheduleId);
        document.update("reviewer", FieldValue.arrayUnion(reviewer));
    }
}
