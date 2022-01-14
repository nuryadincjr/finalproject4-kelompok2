package com.nuryadincjr.ebusantara.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ScheduleReference implements Parcelable {
    private String id;
    private Buses buses;
    private Cities arrival;
    private Cities departure;
    private String arrivalTime;
    private String departureTime;

    public ScheduleReference() {
    }

    public ScheduleReference(String id, Buses buses, Cities arrival, Cities departure, String arrivalTime, String departureTime) {
        this.id = id;
        this.buses = buses;
        this.arrival = arrival;
        this.departure = departure;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
    }

    protected ScheduleReference(Parcel in) {
        id = in.readString();
        buses = in.readParcelable(Buses.class.getClassLoader());
        arrival = in.readParcelable(Cities.class.getClassLoader());
        departure = in.readParcelable(Cities.class.getClassLoader());
        arrivalTime = in.readString();
        departureTime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelable(buses, flags);
        dest.writeParcelable(arrival, flags);
        dest.writeParcelable(departure, flags);
        dest.writeString(arrivalTime);
        dest.writeString(departureTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ScheduleReference> CREATOR = new Creator<ScheduleReference>() {
        @Override
        public ScheduleReference createFromParcel(Parcel in) {
            return new ScheduleReference(in);
        }

        @Override
        public ScheduleReference[] newArray(int size) {
            return new ScheduleReference[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Buses getBuses() {
        return buses;
    }

    public void setBuses(Buses buses) {
        this.buses = buses;
    }

    public Cities getArrival() {
        return arrival;
    }

    public void setArrival(Cities arrival) {
        this.arrival = arrival;
    }

    public Cities getDeparture() {
        return departure;
    }

    public void setDeparture(Cities departure) {
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
