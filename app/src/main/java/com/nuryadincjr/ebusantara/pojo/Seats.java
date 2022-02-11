package com.nuryadincjr.ebusantara.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Seats implements Parcelable {
    private List<Boolean> seatsA;
    private List<Boolean> seatsB;
    private List<Boolean> seatsC;
    private List<Boolean> seatsD;

    public Seats() {
    }

    public Seats(List<Boolean> seatsA, List<Boolean> seatsB,
                 List<Boolean> seatsC, List<Boolean> seatsD) {
        this.seatsA = seatsA;
        this.seatsB = seatsB;
        this.seatsC = seatsC;
        this.seatsD = seatsD;
    }

    protected Seats(Parcel in) {
        seatsA = (List<Boolean>) in.readValue(getClass().getClassLoader());
        seatsB = (List<Boolean>) in.readValue(getClass().getClassLoader());
        seatsC = (List<Boolean>) in.readValue(getClass().getClassLoader());
        seatsD = (List<Boolean>) in.readValue(getClass().getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(seatsA);
        dest.writeValue(seatsB);
        dest.writeValue(seatsC);
        dest.writeValue(seatsD);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Seats> CREATOR = new Creator<Seats>() {
        @Override
        public Seats createFromParcel(Parcel in) {
            return new Seats(in);
        }

        @Override
        public Seats[] newArray(int size) {
            return new Seats[size];
        }
    };

    public List<Boolean> getSeatsA() {
        return seatsA;
    }

    public void setSeatsA(List<Boolean> seatsA) {
        this.seatsA = seatsA;
    }

    public List<Boolean> getSeatsB() {
        return seatsB;
    }

    public void setSeatsB(List<Boolean> seatsB) {
        this.seatsB = seatsB;
    }

    public List<Boolean> getSeatsC() {
        return seatsC;
    }

    public void setSeatsC(List<Boolean> seatsC) {
        this.seatsC = seatsC;
    }

    public List<Boolean> getSeatsD() {
        return seatsD;
    }

    public void setSeatsD(List<Boolean> seatsD) {
        this.seatsD = seatsD;
    }
}
