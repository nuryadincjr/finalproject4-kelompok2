package com.nuryadincjr.ebusantara.api;

import static com.nuryadincjr.ebusantara.util.Constant.COLLECTION_USER;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nuryadincjr.ebusantara.pojo.Users;

public class UsersRepository {
    private final CollectionReference collection;
    private final FirebaseFirestore db;

    public UsersRepository() {
        db = FirebaseFirestore.getInstance();
        collection = db.collection(COLLECTION_USER);
    }

    public Task<Void> insertUser(Users user) {
        return collection.document(user.getUid()).set(user);
    }

    public MutableLiveData<Users> getUsers(String uid) {
        MutableLiveData<Users> listMutableLiveData = new MutableLiveData<>();
        db.document("users/"+uid)
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Users users = task.getResult().toObject(Users.class);
                listMutableLiveData.setValue(users);
            } else listMutableLiveData.setValue(null);
        });
        return listMutableLiveData;
    }
}
