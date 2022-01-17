package com.nuryadincjr.ebusantara.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ReviewersReference implements Parcelable {
    private List<Reviewers> reviewers;
    private String ratingsCount;

    public ReviewersReference() {
    }

    protected ReviewersReference(Parcel in) {
        reviewers = in.createTypedArrayList(Reviewers.CREATOR);
        ratingsCount = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(reviewers);
        dest.writeString(ratingsCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReviewersReference> CREATOR = new Creator<ReviewersReference>() {
        @Override
        public ReviewersReference createFromParcel(Parcel in) {
            return new ReviewersReference(in);
        }

        @Override
        public ReviewersReference[] newArray(int size) {
            return new ReviewersReference[size];
        }
    };

    public List<Reviewers> getReviewers() {
        return reviewers;
    }

    public void setReviewers(List<Reviewers> reviewers) {
        this.reviewers = reviewers;
    }

    public String getRatingsCount() {
        return ratingsCount;
    }

    public void setRatingsCount(String ratingsCount) {
        this.ratingsCount = ratingsCount;
    }

    public ReviewersReference(List<Reviewers> reviewers, String ratingsCount) {
        this.reviewers = reviewers;
        this.ratingsCount = ratingsCount;
    }
}
