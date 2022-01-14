package com.nuryadincjr.ebusantara.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Schedule implements Parcelable {
    private String id;
    private String buses;
    private String arrival;
    private String departure;
    private String arrivalTime;
    private String departureTime;

    public Schedule() {
    }

    public Schedule(String id, String buses, String arrival, String departure, String arrivalTime, String departureTime) {
        this.id = id;
        this.buses = buses;
        this.arrival = arrival;
        this.departure = departure;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
    }

    protected Schedule(Parcel in) {
        id = in.readString();
        buses = in.readString();
        arrival = in.readString();
        departure = in.readString();
        arrivalTime = in.readString();
        departureTime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(buses);
        dest.writeString(arrival);
        dest.writeString(departure);
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

    public String getBuses() {
        return buses;
    }

    public void setBuses(String buses) {
        this.buses = buses;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
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
