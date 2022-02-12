package com.nuryadincjr.ebusantara.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Reviewers implements Parcelable {
    private String uid;
    private String date;
    private String content;
    private String ratings;
    private List<String> likes;

    public Reviewers() {
    }

    public Reviewers(String uid, String date, String content, String ratings, List<String> likes) {
        this.uid = uid;
        this.date = date;
        this.content = content;
        this.ratings = ratings;
        this.likes = likes;
    }

    protected Reviewers(Parcel in) {
        uid = in.readString();
        date = in.readString();
        content = in.readString();
        ratings = in.readString();
        likes = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(date);
        dest.writeString(content);
        dest.writeString(ratings);
        dest.writeStringList(likes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Reviewers> CREATOR = new Creator<Reviewers>() {
        @Override
        public Reviewers createFromParcel(Parcel in) {
            return new Reviewers(in);
        }

        @Override
        public Reviewers[] newArray(int size) {
            return new Reviewers[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }
}
