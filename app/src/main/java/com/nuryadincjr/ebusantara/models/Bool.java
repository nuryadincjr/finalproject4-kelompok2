package com.nuryadincjr.ebusantara.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Bool implements Parcelable {
    private boolean item;

    public Bool() {
    }

    public Bool(boolean item) {
        this.item = item;
    }

    protected Bool(Parcel in) {
        item = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (item ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Bool> CREATOR = new Creator<Bool>() {
        @Override
        public Bool createFromParcel(Parcel in) {
            return new Bool(in);
        }

        @Override
        public Bool[] newArray(int size) {
            return new Bool[size];
        }
    };

    public boolean isItem() {
        return item;
    }

    public void setItem(boolean item) {
        this.item = item;
    }
}
