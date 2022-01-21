package com.nuryadincjr.ebusantara.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Buses implements Parcelable {
    private String id;
    private String busNo;
    private String poName;
    private String classType;
    private List<String> facility;
    private String imageUrl;
    private String price;
    private Seats seats;

    public Buses() {
    }

    protected Buses(Parcel in) {
        id = in.readString();
        busNo = in.readString();
        poName = in.readString();
        classType = in.readString();
        facility = in.createStringArrayList();
        imageUrl = in.readString();
        price = in.readString();
        seats = in.readParcelable(Seats.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(busNo);
        dest.writeString(poName);
        dest.writeString(classType);
        dest.writeStringList(facility);
        dest.writeString(imageUrl);
        dest.writeString(price);
        dest.writeParcelable(seats, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Buses> CREATOR = new Creator<Buses>() {
        @Override
        public Buses createFromParcel(Parcel in) {
            return new Buses(in);
        }

        @Override
        public Buses[] newArray(int size) {
            return new Buses[size];
        }
    };

    public Seats getSeats() {
        return seats;
    }

    public void setSeats(Seats seats) {
        this.seats = seats;
    }

    public Buses(String id, String busNo, String poName,
                 String classType, List<String> facility,
                 String imageUrl, String price, Seats seats) {
        this.id = id;
        this.busNo = busNo;
        this.poName = poName;
        this.classType = classType;
        this.facility = facility;
        this.imageUrl = imageUrl;
        this.price = price;
        this.seats = seats;
    }

    public Buses(String id, String busNo, String poName,
                 String classType, List<String> facility,
                 String imageUrl, String price) {
        this.id = id;
        this.busNo = busNo;
        this.poName = poName;
        this.classType = classType;
        this.facility = facility;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusNo() {
        return busNo;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    public String getPoName() {
        return poName;
    }

    public void setPoName(String poName) {
        this.poName = poName;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public List<String> getFacility() {
        return facility;
    }

    public void setFacility(List<String> facility) {
        this.facility = facility;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
