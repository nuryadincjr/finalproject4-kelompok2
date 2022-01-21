package com.nuryadincjr.ebusantara.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentReference;

public class Schedule implements Parcelable {
    private String id;
    private DocumentReference bus;
    private DocumentReference arrival;
    private DocumentReference departure;
    private String arrivalTime;
    private String departureTime;

    public Schedule() {
    }

    public Schedule(String id, DocumentReference bus, DocumentReference arrival,
                    DocumentReference departure, String arrivalTime, String departureTime) {
        this.id = id;
        this.bus = bus;
        this.arrival = arrival;
        this.departure = departure;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
    }

    protected Schedule(Parcel in) {
        id = in.readString();
        arrivalTime = in.readString();
        departureTime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(arrivalTime);
        dest.writeString(departureTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Schedule> CREATOR = new Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel in) {
            return new Schedule(in);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DocumentReference getBus() {
        return bus;
    }

    public void setBus(DocumentReference bus) {
        this.bus = bus;
    }

    public DocumentReference getArrival() {
        return arrival;
    }

    public void setArrival(DocumentReference arrival) {
        this.arrival = arrival;
    }

    public DocumentReference getDeparture() {
        return departure;
    }

    public void setDeparture(DocumentReference departure) {
        this.departure = departure;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }
}
