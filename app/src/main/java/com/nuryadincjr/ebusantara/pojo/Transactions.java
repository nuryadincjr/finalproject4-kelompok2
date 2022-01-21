package com.nuryadincjr.ebusantara.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Transactions implements Parcelable {
    private String id;
    private String uid;
    private String date;
    private String bookNo;
    private String status;
    private String schedule;
    private String totalPayment;
    private String paymentMethod;
    private String paymentPartner;
    private ArrayList<String> seatNo;

    public Transactions() {
    }

    public Transactions(String id, String uid, String date,
                        String bookNo, String status, String schedule,
                        String totalPayment, String paymentMethod,
                        String paymentPartner, ArrayList<String> seatNo) {
        this.id = id;
        this.uid = uid;
        this.date = date;
        this.bookNo = bookNo;
        this.status = status;
        this.schedule = schedule;
        this.totalPayment = totalPayment;
        this.paymentMethod = paymentMethod;
        this.paymentPartner = paymentPartner;
        this.seatNo = seatNo;
    }

    protected Transactions(Parcel in) {
        id = in.readString();
        uid = in.readString();
        date = in.readString();
        bookNo = in.readString();
        status = in.readString();
        schedule = in.readString();
        totalPayment = in.readString();
        paymentMethod = in.readString();
        paymentPartner = in.readString();
        seatNo = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(uid);
        dest.writeString(date);
        dest.writeString(bookNo);
        dest.writeString(status);
        dest.writeString(schedule);
        dest.writeString(totalPayment);
        dest.writeString(paymentMethod);
        dest.writeString(paymentPartner);
        dest.writeStringList(seatNo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Transactions> CREATOR = new Creator<Transactions>() {
        @Override
        public Transactions createFromParcel(Parcel in) {
            return new Transactions(in);
        }

        @Override
        public Transactions[] newArray(int size) {
            return new Transactions[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBookNo() {
        return bookNo;
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(String totalPayment) {
        this.totalPayment = totalPayment;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentPartner() {
        return paymentPartner;
    }

    public void setPaymentPartner(String paymentPartner) {
        this.paymentPartner = paymentPartner;
    }

    public ArrayList<String> getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(ArrayList<String> seatNo) {
        this.seatNo = seatNo;
    }
}
