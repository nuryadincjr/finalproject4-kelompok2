package com.nuryadincjr.ebusantara.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Users implements Parcelable {
    private String uid;
    private String name;
    private String phoneNumber;
    private String email;
    private String photoUrl;

    public Users() {
    }

    public Users(String uid, String name, String phoneNumber,
                 String email, String photoUrl) {
        this.uid = uid;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    protected Users(Parcel in) {
        uid = in.readString();
        name = in.readString();
        phoneNumber = in.readString();
        email = in.readString();
        photoUrl = in.readString();
    }

    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(name);
        dest.writeString(phoneNumber);
        dest.writeString(email);
        dest.writeString(photoUrl);
    }
}
