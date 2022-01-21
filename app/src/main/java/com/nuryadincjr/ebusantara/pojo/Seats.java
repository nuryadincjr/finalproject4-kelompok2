package com.nuryadincjr.ebusantara.pojo;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.util.List;

public class Seats implements Parcelable {
    private List<Boolean> A;
    private List<Boolean> B;
    private List<Boolean> C;
    private List<Boolean> D;

    public Seats() {
    }

    public Seats(List<Boolean> a, List<Boolean> b,
                 List<Boolean> c, List<Boolean> d) {
        A = a;
        B = b;
        C = c;
        D = d;
    }


    protected Seats(Parcel in) {
        A = (List<Boolean>) in.readValue(getClass().getClassLoader());
        B = (List<Boolean>) in.readValue(getClass().getClassLoader());
        C = (List<Boolean>) in.readValue(getClass().getClassLoader());
        D = (List<Boolean>) in.readValue(getClass().getClassLoader());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(A);
        dest.writeValue(B);
        dest.writeValue(C);
        dest.writeValue(D);
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

    public List<Boolean> getA() {
        return A;
    }

    public void setA(List<Boolean> a) {
        A = a;
    }

    public List<Boolean> getB() {
        return B;
    }

    public void setB(List<Boolean> b) {
        B = b;
    }

    public List<Boolean> getC() {
        return C;
    }

    public void setC(List<Boolean> c) {
        C = c;
    }

    public List<Boolean> getD() {
        return D;
    }

    public void setD(List<Boolean> d) {
        D = d;
    }
}
