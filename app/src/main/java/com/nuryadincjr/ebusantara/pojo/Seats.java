package com.nuryadincjr.ebusantara.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Seats implements Parcelable {
    private boolean[] A;
    private boolean[] B;
    private boolean[] C;
    private boolean[] D;

    public Seats() {
    }

    public Seats(boolean[] a, boolean[] b, boolean[] c, boolean[] d) {
        A = a;
        B = b;
        C = c;
        D = d;
    }

    protected Seats(Parcel in) {
        A = in.createBooleanArray();
        B = in.createBooleanArray();
        C = in.createBooleanArray();
        D = in.createBooleanArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBooleanArray(A);
        dest.writeBooleanArray(B);
        dest.writeBooleanArray(C);
        dest.writeBooleanArray(D);
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

    public boolean[] getA() {
        return A;
    }

    public void setA(boolean[] a) {
        A = a;
    }

    public boolean[] getB() {
        return B;
    }

    public void setB(boolean[] b) {
        B = b;
    }

    public boolean[] getC() {
        return C;
    }

    public void setC(boolean[] c) {
        C = c;
    }

    public boolean[] getD() {
        return D;
    }

    public void setD(boolean[] d) {
        D = d;
    }
}
