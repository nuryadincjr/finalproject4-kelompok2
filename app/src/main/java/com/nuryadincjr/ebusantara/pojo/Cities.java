package com.nuryadincjr.ebusantara.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Cities implements Parcelable {
    private String id;
    private String city;
    private String terminal;

    public Cities() {
    }

    public Cities(String id, String city, String terminal) {
        this.id = id;
        this.city = city;
        this.terminal = terminal;
    }

    protected Cities(Parcel in) {
        id = in.readString();
        city = in.readString();
        terminal = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(city);
        dest.writeString(terminal);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Cities> CREATOR = new Creator<Cities>() {
        @Override
        public Cities createFromParcel(Parcel in) {
            return new Cities(in);
        }

        @Override
        public Cities[] newArray(int size) {
            return new Cities[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }
}
