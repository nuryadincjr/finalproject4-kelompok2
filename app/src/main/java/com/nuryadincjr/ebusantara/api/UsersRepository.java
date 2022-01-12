package com.nuryadincjr.ebusantara.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.nuryadincjr.ebusantara.models.Users;

import java.util.ArrayList;

public class UsersRepository {

    private static final String COLLECTION_USER = "users";
    private static final String TAG = "XXX";
    private final CollectionReference collectionReference;

    public UsersRepository() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        collectionReference = db.collection(COLLECTION_USER);
    }

    public Task<Void> insertUser(Users user) {
        return collectionReference.document(user.getUid()).set(user);
    }

    public Task<Void> updateUser(Users users) {
        return collectionReference.document(users.getUid()).set(users);
    }

    public MutableLiveData<ArrayList<Users>> getUserLogin(Users user) {
        ArrayList<Users> users = new ArrayList<>();
        final MutableLiveData<ArrayList<Users>> usersMutableLiveData = new MutableLiveData<>();

        collectionReference
                .whereEqualTo("phone", user.getPhoneNumber())
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                    Users data = snapshot.toObject(Users.class);
                    data.setUid(snapshot.getId());
                    users.add(data);;
                    Log.d(TAG, snapshot.getId() + " => " + snapshot.getData());
                }
                usersMutableLiveData.postValue(users);
            }else{
                usersMutableLiveData.setValue(null);
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
        return usersMutableLiveData;
    }

    public MutableLiveData<ArrayList<Users>> getUserData(String uid) {
        ArrayList<Users> users = new ArrayList<>();
        final MutableLiveData<ArrayList<Users>> usersMutableLiveData = new MutableLiveData<>();

        collectionReference
                .whereEqualTo("uid", uid)
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot  snapshot : task.getResult()) {
                    Users data = snapshot.toObject(Users.class);
                    data.setUid(snapshot.getId());
                    users.add(data);;
                    Log.d(TAG, snapshot.getId() + " => " + snapshot.getData());
                }
                usersMutableLiveData.postValue(users);
            }else{
                usersMutableLiveData.setValue(null);
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
        return usersMutableLiveData;
    }

    public Task<Void> deleteUser(String id) {
        return collectionReference.document(id).delete();
    }

    public MutableLiveData<ArrayList<Users>> getSearchUsers(String value) {
        ArrayList<Users> usersList = new ArrayList<>();
        final MutableLiveData<ArrayList<Users>> usersMutableLiveData = new MutableLiveData<>();

        collectionReference
                .whereGreaterThanOrEqualTo("name", value)
                .whereLessThanOrEqualTo("name",value+"~")
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Users users = document.toObject(Users.class);

                    usersList.add(users);
                    Log.d(TAG, document.getId() + " => " + document.getData());
                }
                usersMutableLiveData.postValue(usersList);
            }
            else{
                usersMutableLiveData.setValue(null);
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
        return usersMutableLiveData;
    }
}