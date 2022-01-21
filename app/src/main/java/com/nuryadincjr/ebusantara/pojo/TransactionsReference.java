package com.nuryadincjr.ebusantara.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class TransactionsReference implements Parcelable{
    private Transactions transactions;
    private ScheduleReference reference;

    public TransactionsReference() {
    }

    public TransactionsReference(Transactions transactions, ScheduleReference reference) {
        this.transactions = transactions;
        this.reference = reference;
    }

    protected TransactionsReference(Parcel in) {
        transactions = in.readParcelable(Transactions.class.getClassLoader());
        reference = in.readParcelable(ScheduleReference.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(transactions, flags);
        dest.writeParcelable(reference, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TransactionsReference> CREATOR = new Creator<TransactionsReference>() {
        @Override
        public TransactionsReference createFromParcel(Parcel in) {
            return new TransactionsReference(in);
        }

        @Override
        public TransactionsReference[] newArray(int size) {
            return new TransactionsReference[size];
        }
    };

    public Transactions getTransactions() {
        return transactions;
    }

    public void setTransactions(Transactions transactions) {
        this.transactions = transactions;
    }

    public ScheduleReference getReference() {
        return reference;
    }

    public void setReference(ScheduleReference reference) {
        this.reference = reference;
    }
}
